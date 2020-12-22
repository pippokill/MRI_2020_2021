/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mri2021.filtering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pierpaolo
 */
public class UserBasedIF extends CollaborativeIF {

    private int k = 20;

    private Map<String, List<Rating>> ratingsByUser = null;

    private Map<String, List<Rating>> ratingsByItem = null;

    private Map<String, Double> averageScore = null;

    private boolean cosine = false;

    public UserBasedIF(IFDataset dataset) {
        super(dataset);
        ratingsByUser = IFDatasetUtils.getRatingsByUser(dataset.getRatings());
        ratingsByItem = IFDatasetUtils.getRatingsByItem(dataset.getRatings());
        // store users average score
        averageScore = new HashMap<>();
    }

    public UserBasedIF(IFDataset dataset, boolean cosine) {
        super(dataset);
        ratingsByUser = IFDatasetUtils.getRatingsByUser(dataset.getRatings());
        ratingsByItem = IFDatasetUtils.getRatingsByItem(dataset.getRatings());
        // store users average score
        averageScore = new HashMap<>();
        this.cosine = cosine;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public boolean isCosine() {
        return cosine;
    }

    public void setCosine(boolean cosine) {
        this.cosine = cosine;
    }

    private double getAverageScore(String userid) {
        Double avg = averageScore.get(userid);
        if (avg == null) {
            avg = IFDatasetUtils.average(ratingsByUser.get(userid));
            averageScore.put(userid, avg);
        }
        return avg;
    }

    public double pearsonCorrelation(String userId1, String userId2) throws Exception {
        // find co-rated items
        List<Rating> r1 = ratingsByUser.get(userId1);
        List<Rating> r2 = ratingsByUser.get(userId2);
        if (r1 == null || r2 == null) {
            throw new Exception("No ratings");
        }
        List<String> co = IFDatasetUtils.coRatedItems(r1, r2);
        if (co.isEmpty()) {
            return -1;
        } else {
            // compute Pearson correlation
            Map<String, Integer> m1 = IFDatasetUtils.ratingsToMapByItem(r1);
            Map<String, Integer> m2 = IFDatasetUtils.ratingsToMapByItem(r2);
            double ar1 = getAverageScore(userId1);
            double ar2 = getAverageScore(userId2);
            double c = 0;
            double n1 = 0;
            double n2 = 0;
            for (String r : co) {
                c += (m1.get(r).doubleValue() - ar1) * (m2.get(r).doubleValue() - ar2);
                n1 += Math.pow(m1.get(r).doubleValue() - ar1, 2);
                n2 += Math.pow(m2.get(r).doubleValue() - ar2, 2);
            }
            if (n1 == 0 || n2 == 0) { // check denominator
                return 0;
            } else {
                return c / (Math.sqrt(n1) * Math.sqrt(n2));
            }
        }
    }

    public double cosine(String userId1, String userId2) throws Exception {
        // find co-rated items
        List<Rating> r1 = ratingsByUser.get(userId1);
        List<Rating> r2 = ratingsByUser.get(userId2);
        if (r1 == null || r2 == null) {
            throw new Exception("No ratings");
        }
        List<String> co = IFDatasetUtils.coRatedItems(r1, r2);
        if (co.isEmpty()) {
            return 0;
        } else {
            // compute Pearson correlation
            Map<String, Integer> m1 = IFDatasetUtils.ratingsToMapByItem(r1);
            Map<String, Integer> m2 = IFDatasetUtils.ratingsToMapByItem(r2);
            double c = 0;
            double n1 = 0;
            double n2 = 0;
            for (String r : co) {
                c += m1.get(r).doubleValue() * m2.get(r).doubleValue();
                n1 += Math.pow(m1.get(r).doubleValue(), 2);
                n2 += Math.pow(m2.get(r).doubleValue(), 2);
            }
            if (n1 == 0 || n2 == 0) { // check denominator
                return 0;
            } else {
                return c / (Math.sqrt(n1) * Math.sqrt(n2));
            }
        }
    }

    @Override
    public double getPrediction(User user, Item item) {
        // get users that rated the item
        List<Rating> ratings = ratingsByItem.get(item.getItemID());
        if (ratings != null) {
            List<Neighborhood> neigh = new ArrayList<>();
            for (Rating r : ratings) {
                try {
                    double sim;
                    if (cosine) {
                        sim = cosine(user.getUserId(), r.getUserId());
                    } else {
                        double c = pearsonCorrelation(user.getUserId(), r.getUserId());
                        // transform correlation in similarity
                        sim = (1 + c) / 2;
                    }
                    neigh.add(new Neighborhood(r.getUserId(), sim, r.getRating()));
                } catch (Exception ex) {
                    Logger.getLogger(UserBasedIF.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            // get k neighborhoods
            Collections.sort(neigh, Collections.reverseOrder());
            if (neigh.size() > k) {
                neigh = neigh.subList(0, k);
            }
            // predict score
            double p = getAverageScore(user.getUserId());
            double n = 0;
            double d = 0;
            for (Neighborhood nu : neigh) {
                double avg = getAverageScore(nu.getId());
                n += nu.getScore() * ((double) nu.getRating() - avg);
                d += nu.getScore();
            }
            if (d != 0) {
                return p + (n / d);
            } else {
                return p;
            }
        } else { // if the item has not ratings return the user avg rating
            return getAverageScore(user.getUserId());
        }
    }

    @Override
    public List<ItemPrediction> getPredictions(User user) {
        List<ItemPrediction> predictions = new ArrayList<>();
        // get items rated by the user and map them
        List<Rating> userRatings = ratingsByUser.get(user.getUserId());
        Map<String, Integer> ratingsToMap = IFDatasetUtils.ratingsToMapByItem(userRatings);
        for (Item item : getDataset().getItems()) {
            // for each item not rated by the user
            if (!ratingsToMap.containsKey(item.getItemID())) {
                // get prediction
                double prediction = getPrediction(user, item);
                predictions.add(new ItemPrediction(item.getItemID(), prediction));
            }
        }
        // sort predictions
        Collections.sort(predictions, Collections.reverseOrder());
        return predictions;
    }

}

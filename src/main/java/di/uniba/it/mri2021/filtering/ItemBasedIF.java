/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mri2021.filtering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pierpaolo
 */
public class ItemBasedIF extends CollaborativeIF {

    private int k = 20;

    private Map<String, List<Rating>> ratingsByUser = null;

    private Map<String, List<Rating>> ratingsByItem = null;

    public ItemBasedIF(IFDataset dataset) {
        super(dataset);
        ratingsByUser = IFDatasetUtils.getRatingsByUser(dataset.getRatings());
        ratingsByItem = IFDatasetUtils.getRatingsByItem(dataset.getRatings());
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    @Override
    public double getPrediction(User user, Item item) {
        // get items rated by the user
        List<Rating> ratings = this.ratingsByItem.get(item.getItemID());
        if (ratings == null) {
            return IFDatasetUtils.average(this.ratingsByUser.get(user.getUserId()));
        } else {
            // compute items similarity
            List<Neighborhood> neigh = new ArrayList<>();
            Map<String, Integer> m1 = IFDatasetUtils.ratingsToMapByUser(ratings);
            List<Rating> ratingsUser = this.ratingsByUser.get(user.getUserId());
            for (Rating ru : ratingsUser) {
                List<Rating> rit = this.ratingsByItem.get(ru.getItemId());
                Map<String, Integer> m2 = IFDatasetUtils.ratingsToMapByUser(rit);
                double n = 0;
                double d1 = 0;
                double d2 = 0;
                for (String idu : m1.keySet()) {
                    Integer r = m2.get(idu);
                    if (r != null) {
                        n += r.doubleValue() * m1.get(idu).doubleValue();
                    }
                    d1 += Math.pow(m1.get(idu), 2);
                }
                for (String idu : m2.keySet()) {
                    d2 += Math.pow(m2.get(idu), 2);
                }
                double sim = 0;
                if (d1 != 0 && d2 != 0) {
                    sim = n / Math.sqrt(d1) * Math.sqrt(d2);
                }
                neigh.add(new Neighborhood(ru.getItemId(), sim, ru.getRating()));
            }
            Collections.sort(neigh, Collections.reverseOrder());
            if (neigh.size() > k) {
                neigh = neigh.subList(0, k);
            }
            double p = 0;
            double d = 0;
            for (Neighborhood n : neigh) {
                p += n.getScore() * (double) n.getRating();
                d += n.getScore();
            }
            return p / d;
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

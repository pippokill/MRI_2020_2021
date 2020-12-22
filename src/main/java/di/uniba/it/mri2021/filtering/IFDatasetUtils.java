/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mri2021.filtering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author pierpaolo
 */
public class IFDatasetUtils {

    public static Map<String, Item> itemListToMap(List<Item> items) {
        Map<String, Item> map = new HashMap<>();
        for (Item i : items) {
            map.put(i.getItemID(), i);
        }
        return map;
    }

    public static Map<String, User> userListToMap(List<User> users) {
        Map<String, User> map = new HashMap<>();
        for (User u : users) {
            map.put(u.getUserId(), u);
        }
        return map;
    }

    public static Map<String, List<Rating>> getRatingsByUser(List<Rating> ratings) {
        Map<String, List<Rating>> map = new HashMap<>();
        for (Rating r : ratings) {
            List<Rating> l = map.get(r.getUserId());
            if (l == null) {
                l = new ArrayList<>();
                map.put(r.getUserId(), l);
            }
            l.add(r);
        }
        return map;
    }

    public static Map<String, List<Rating>> getRatingsByItem(List<Rating> ratings) {
        Map<String, List<Rating>> map = new HashMap<>();
        for (Rating r : ratings) {
            List<Rating> l = map.get(r.getItemId());
            if (l == null) {
                l = new ArrayList<>();
                map.put(r.getItemId(), l);
            }
            l.add(r);
        }
        return map;
    }

    public static double average(List<Rating> ratings) {
        return ratings.stream().mapToDouble(r -> r.getRating()).average().getAsDouble();
    }

    public static List<String> coRatedItems(List<Rating> ratings1, List<Rating> ratings2) {
        Set<String> ids1 = new HashSet<>();
        for (Rating r1 : ratings1) {
            ids1.add(r1.getItemId());
        }
        Set<String> ids2 = new HashSet<>();
        for (Rating r2 : ratings2) {
            ids2.add(r2.getItemId());
        }
        ids1.retainAll(ids2);
        return new ArrayList<>(ids1);
    }

    public static Map<String, Integer> ratingsToMapByItem(List<Rating> ratings) {
        Map<String, Integer> map = new HashMap<>();
        for (Rating r : ratings) {
            map.put(r.getItemId(), r.getRating());
        }
        return map;
    }
    
    public static Map<String, Integer> ratingsToMapByUser(List<Rating> ratings) {
        Map<String, Integer> map = new HashMap<>();
        for (Rating r : ratings) {
            map.put(r.getUserId(), r.getRating());
        }
        return map;
    }

}

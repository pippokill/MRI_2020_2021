/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mri2021.filtering;

/**
 *
 * @author pierpaolo
 */
public class ItemPrediction implements Comparable<ItemPrediction> {

    private String itemid;

    private double score;

    public ItemPrediction() {
    }

    public ItemPrediction(String itemid) {
        this.itemid = itemid;
    }

    public ItemPrediction(String itemid, double score) {
        this.itemid = itemid;
        this.score = score;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return itemid + "\t" + score;
    }

    @Override
    public int compareTo(ItemPrediction o) {
        return Double.compare(score, o.score);
    }

}

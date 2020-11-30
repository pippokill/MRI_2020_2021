/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mri2021.tc;

/**
 *
 * @author pierpaolo
 */
public class CategoryEntry implements Comparable<CategoryEntry> {

    private String category;

    private float score;

    public CategoryEntry(String category, float score) {
        this.category = category;
        this.score = score;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    @Override
    public int compareTo(CategoryEntry o) {
        return Float.compare(score, o.score);
    }
}

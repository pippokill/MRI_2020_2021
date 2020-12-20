/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package di.uniba.it.mri2021.filtering;

import java.util.Objects;

/**
 *
 * @author pierpaolo
 */
public class Neighborhood implements Comparable<Neighborhood> {

    private String id;

    private double score;
    
    private int rating;

    public Neighborhood() {
    }

    public Neighborhood(String id, double score) {
        this.id = id;
        this.score = score;
    }

    public Neighborhood(String id, double score, int rating) {
        this.id = id;
        this.score = score;
        this.rating = rating;
    }
    
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
    
    

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Neighborhood other = (Neighborhood) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Neighborhood{" + "id=" + id + ", score=" + score + '}';
    }

    @Override
    public int compareTo(Neighborhood o) {
        return Double.compare(score, o.score);
    }

}

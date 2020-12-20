/*
 * Copyright (C) 2020 pierpaolo
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package di.uniba.it.mri2021.filtering;

import java.util.Date;
import java.util.Objects;

/**
 * UserID::MovieID::Rating::Timestamp
 *
 * @author pierpaolo
 */
public class Rating {

    private String userId;

    private String itemId;

    private int rating;

    private Date date;

    public Rating(String userId, String itemId, int rating) {
        this.userId = userId;
        this.itemId = itemId;
        this.rating = rating;
    }

    public Rating(String userId, String itemId, int rating, Date date) {
        this.userId = userId;
        this.itemId = itemId;
        this.rating = rating;
        this.date = date;
    }

    public Rating(String userId, String itemId, int rating, long timestamp) {
        this.userId = userId;
        this.itemId = itemId;
        this.rating = rating;
        this.date = new Date(timestamp);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.userId);
        hash = 37 * hash + Objects.hashCode(this.itemId);
        return hash;
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
        final Rating other = (Rating) obj;
        if (!Objects.equals(this.userId, other.userId)) {
            return false;
        }
        if (!Objects.equals(this.itemId, other.itemId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return userId + "\t" + itemId + "\t" + rating;
    }

}

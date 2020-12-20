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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * MovieID::Title::Genres
 *
 * - Titles are identical to titles provided by the IMDB (including year of
 * release) - Genres are pipe-separated and are selected from the following
 * genres:
 *
 * Action Adventure Animation Children's Comedy Crime Documentary Drama Fantasy
 * Film-Noir Horror Musical Mystery Romance Sci-Fi Thriller War Western
 *
 * @author pierpaolo
 */
public class Movie extends Item {

    private String title;

    private Set<String> genres;

    public Movie(String title, String itemID) {
        super(itemID);
        this.title = title;
    }

    public Movie(String title, Set<String> genres, String itemID) {
        super(itemID);
        this.title = title;
        this.genres = genres;
    }

    public Movie(String itemID, String title, String[] genres) {
        super(itemID);
        this.title = title;
        this.genres = new HashSet(Arrays.asList(genres));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<String> getGenres() {
        return genres;
    }

    public void setGenres(Set<String> genres) {
        this.genres = genres;
    }

    @Override
    public String toString() {
        return getItemID() + "\t" + title + "\t" + genres;
    }

}

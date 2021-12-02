package edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "film")
public class Film
{
    @PrimaryKey(autoGenerate = true)
    private int filmId;

    // A genre that corresponds to one film
    private int filmGenreId;

    // 1 - 10 or 1-100 or 1-5
    private double rating;
    private String title;
    private String description;
    private String imageURL;

    @Ignore
    public Film(int filmId, String title, String description, String imageURL, double rating)
    {
        this.filmId = filmId;
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.imageURL = imageURL;
    }

    public Film(String title, String description, String imageURL, double rating)
    {
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.imageURL = imageURL;
    }

    public int getFilmId() {
        return filmId;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }

    public int getFilmGenreId() { return filmGenreId; }
    public void setFilmGenreId(int filmGenreId) { this.filmGenreId = filmGenreId; }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public double getRating() { return rating; }
    public String getImageURL() { return imageURL; }
}

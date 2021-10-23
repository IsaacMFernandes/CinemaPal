package edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "film")
public class Film
{
    @PrimaryKey(autoGenerate = true)
    private int filmId;

    // A genre that corresponds to one film
    private int filmGenreId;

    // 1 - 10 or 1-100 or 1-5
    private short rating;
    private String title;
    private String description;
    private String director;

    public Film(String title, String description, String director, short rating)
    {
        this.title = title;
        this.description = description;
        this.director = director;
        this.rating = rating;
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
    public String getDirector() { return director; }
    public short getRating() { return rating; }
}

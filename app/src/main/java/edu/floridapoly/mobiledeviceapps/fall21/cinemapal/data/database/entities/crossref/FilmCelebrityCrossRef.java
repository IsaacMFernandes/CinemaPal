package edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.entities.crossref;

import androidx.room.Entity;

@Entity(primaryKeys = {"celebrityId", "filmId"})
public class FilmCelebrityCrossRef
{
    private int celebrityId;
    private int filmId;

    public int getCelebrityId() {
        return celebrityId;
    }

    public void setCelebrityId(int celebrityId) {
        this.celebrityId = celebrityId;
    }

    public int getFilmId() {
        return filmId;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }
}

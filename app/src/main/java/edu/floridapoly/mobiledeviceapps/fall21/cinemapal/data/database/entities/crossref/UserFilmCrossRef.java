package edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.entities.crossref;

import androidx.room.Entity;

@Entity(primaryKeys = {"userId", "filmId"})
public class UserFilmCrossRef
{
    private int userId;
    private int filmId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFilmId() {
        return filmId;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }
}

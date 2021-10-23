package edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.maps;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.entities.Film;
import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.entities.User;
import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.entities.crossref.UserFilmCrossRef;

public class LikedFilms
{
    @Embedded
    private User user;

    @Relation(
            parentColumn = "userId",
            entityColumn = "filmId",
            associateBy = @Junction(UserFilmCrossRef.class)
    )

    private List<Film> film;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Film> getFilm() {
        return film;
    }

    public void setFilm(List<Film> film) {
        this.film = film;
    }
}

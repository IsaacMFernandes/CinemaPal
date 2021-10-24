package edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.maps;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.entities.Celebrity;
import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.entities.Film;
import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.entities.crossref.FilmCelebrityCrossRef;

public class FilmCast
{
    @Embedded
    private Film film;

    @Relation(
            parentColumn = "filmId",
            entityColumn = "celebrityId",
            associateBy = @Junction(FilmCelebrityCrossRef.class)
    )

    private List<Celebrity> cast;

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public List<Celebrity> getCast() {
        return cast;
    }

    public void setCast(List<Celebrity> cast) {
        this.cast = cast;
    }
}

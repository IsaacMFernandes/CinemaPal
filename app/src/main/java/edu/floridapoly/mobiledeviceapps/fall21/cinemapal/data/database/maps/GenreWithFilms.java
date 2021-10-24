package edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.maps;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.entities.Film;
import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.entities.Genre;

// Class to model a genre with multiple films
public class GenreWithFilms
{
    @Embedded
    private Genre genre;

    @Relation(
            parentColumn = "genreId",
            entityColumn = "filmGenreId"
    )

    private List<Film> films;

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public List<Film> getFilms() {
        return films;
    }

    public void setFilms(List<Film> films) {
        this.films = films;
    }
}

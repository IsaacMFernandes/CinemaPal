package edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.daos;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.entities.Film;
import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.maps.FilmCast;
import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.maps.GenreWithFilms;

@Dao
public interface FilmDao extends BaseDao<Film>
{
    @Query("SELECT * FROM film WHERE filmId = :filmId")
    public Film findFilmById(int filmId);

    @Query("SELECT * FROM film WHERE title LIKE :titleSearch")
    public Film findFilmByTitle(String titleSearch);

    @Transaction
    @Query("SELECT * FROM genre")
    public List<GenreWithFilms> getGenres();

    @Transaction
    @Query("SELECT * FROM film")
    public List<FilmCast> getFilmCast();
}

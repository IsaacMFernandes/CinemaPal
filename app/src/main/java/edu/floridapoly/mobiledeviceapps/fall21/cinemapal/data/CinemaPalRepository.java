package edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.CinemaPalDatabase;
import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.daos.*;
import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.entities.*;
import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.maps.FilmCast;
import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.maps.GenreWithFilms;
import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.maps.LikedFilms;

public class CinemaPalRepository
{
    // Help run background tasks for database methods
    private ExecutorService service;

    private UserDao userDao;
    private FilmDao filmDao;
    private CelebrityDao celebrityDao;

    // Live data of user's liked films
    //LiveData<List<LikedFilms>> likedFilms;

    public CinemaPalRepository(Application application)
    {
        CinemaPalDatabase database = CinemaPalDatabase.getInstance(application);
        service = Executors.newSingleThreadExecutor();

        //likedFilms = userDao.getLikedFilms();
        userDao = database.userDao();
        filmDao = database.filmDao();
        celebrityDao = database.celebrityDao();
    }

    // Return a live data list of users, each with a playlist of liked films
    //public LiveData<List<LikedFilms>> getLikedFilms()
    //{
        //return likedFilms;
    //}

    public List<Film> getFilms() { return filmDao.getFilms(); }

    // Returns a film that has the primary key provided
    public Film findFilmById(int filmId)
    {
        return filmDao.findFilmById(filmId);
    }

    // Returns a film that has a title similar to the parameter
    public Film findFilmByTitle(String titleSearch)
    {
        return filmDao.findFilmByTitle(titleSearch);
    }

    // Returns a list of genres, each with a playlist of movies (might be LiveData TODO)
    public List<GenreWithFilms> getGenres()
    {
        return filmDao.getGenres();
    }

    // Returns a list of films, each with a list of celebrities
    public List<FilmCast> getFilmCast()
    {
        return filmDao.getFilmCast();
    }

    // Insert, Update, and Delete Methods for each entity
    public void insertFilm(Film film)
    {
        service.execute(() -> filmDao.insert(film));
    }

    public void updateFilm(Film film)
    {
        service.execute(() -> filmDao.update(film));
    }

    public void deleteFilm(Film film)
    {
        service.execute(() -> filmDao.delete(film));
    }

    public void insertUser(User user)
    {
        service.execute(() -> userDao.insert(user));
    }

    public void updateUser(User user)
    {
        service.execute(() -> userDao.update(user));
    }

    public void deleteUser(User user)
    {
        service.execute(() -> userDao.delete(user));
    }

    public void insertCelebrity(Celebrity celebrity)
    {
        service.execute(() -> celebrityDao.insert(celebrity));
    }

    public void updateCelebrity(Celebrity celebrity)
    {
        service.execute(() -> celebrityDao.update(celebrity));
    }

    public void deleteCelebrity(Celebrity celebrity)
    {
        service.execute(() -> celebrityDao.delete(celebrity));
    }
}

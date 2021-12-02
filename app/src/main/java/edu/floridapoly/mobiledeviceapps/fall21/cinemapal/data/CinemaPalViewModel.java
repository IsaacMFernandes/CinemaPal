package edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.CinemaPalRepository;
import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.entities.Celebrity;
import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.entities.Film;
import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.entities.User;
import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.maps.FilmCast;
import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.maps.GenreWithFilms;
import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.maps.LikedFilms;

public class CinemaPalViewModel extends AndroidViewModel
{

    private CinemaPalRepository repository;
    //private LiveData<List<LikedFilms>> likedFilms;

    public CinemaPalViewModel(@NonNull Application application)
    {
        super(application);

        repository = new CinemaPalRepository(application);
        //likedFilms = repository.getLikedFilms();
    }

    /*public LiveData<List<LikedFilms>> getLikedFilms()
    {
        return likedFilms;
    }*/

    public Film searchFilmFromId(int id) {return repository.searchFilmFromId(id); }

    public ArrayList<Film> getDiscoverFilms ()
    {
        return repository.getDiscoverFilms();
    }

    public List<Film> getFilms()
    {
        return repository.getFilms();
    }

    // Returns a film that has the primary key provided
    public Film findFilmById(int filmId)
    {
        return repository.findFilmById(filmId);
    }

    // Returns a film that has a title similar to the parameter
    public Film findFilmByTitle(String titleSearch)
    {
        return repository.findFilmByTitle(titleSearch);
    }

    // Returns a list of genres, each with a playlist of movies (might be LiveData TODO)
    public List<GenreWithFilms> getGenres()
    {
        return repository.getGenres();
    }

    // Returns a list of films, each with a list of celebrities
    public List<FilmCast> getFilmCast()
    {
        return repository.getFilmCast();
    }

    // Insert, Update, and Delete Methods for each entity
    public void insertFilm(Film film)
    {
        repository.insertFilm(film);
    }

    public void insertFilms(Film... film) { repository.insertFilms(film);}

    public void updateFilm(Film film)
    {
        repository.updateFilm(film);
    }

    public void deleteFilm(Film film)
    {
        repository.deleteFilm(film);
    }

    public void insertUser(User user)
    {
        repository.insertUser(user);
    }

    public void updateUser(User user)
    {
        repository.updateUser(user);
    }

    public void deleteUser(User user)
    {
        repository.deleteUser(user);
    }

    public void insertCelebrity(Celebrity celebrity)
    {
        repository.insertCelebrity(celebrity);
    }

    public void updateCelebrity(Celebrity celebrity)
    {
        repository.updateCelebrity(celebrity);
    }

    public void deleteCelebrity(Celebrity celebrity)
    {
        repository.deleteCelebrity(celebrity);
    }
}

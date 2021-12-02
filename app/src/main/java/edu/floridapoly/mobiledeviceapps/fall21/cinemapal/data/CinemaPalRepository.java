package edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data;

import android.app.Application;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.MainActivity;
import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.UserUtil;
import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.CinemaPalDatabase;
import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.daos.*;
import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.entities.*;
import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.maps.FilmCast;
import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.maps.GenreWithFilms;
import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.maps.LikedFilms;

public class CinemaPalRepository
{
    private static final String API_KEY = "97b3e258357751749877448cf8d366f9";

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

    private ArrayList<Film> discoverFilms = new ArrayList<>();

    public ArrayList<Film> getDiscoverFilms()
    {
        try {
            new getDiscoverFilm().execute();
            return discoverFilms;
        } catch (Exception e) {
            Log.d("ExploreFragment", "Something bad happened when trying to run the async task for getDiscoverMovie");
        }
        return null;
    }

    private class getDiscoverFilm extends AsyncTask
    {
        @Override
        protected Object doInBackground(Object[] objects)
        {
            StringBuffer response = new StringBuffer();

            try {
                String urlString = "https://api.themoviedb.org/3/discover/movie";
                urlString += "?api_key=" + API_KEY;
                urlString += "&sort_by=popularity.desc";
                URL url = new URL(urlString);

                Log.d("ExploreFragment", "Discover URL is " + urlString);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();

                Log.d("ExploreFragment", "Response Code is " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK)
                {
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));

                    String output;

                    while ((output = in.readLine()) != null)
                        response.append(output);
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (response != null)
            {
                String responseText = response.toString();

                try {
                    JSONObject jsonResponse = new JSONObject(responseText);
                    JSONArray results = jsonResponse.getJSONArray("results");

                    Log.d("ExploreFragment", "Length of results is " + results.length());

                    for (int i = 0; i < results.length(); i++)
                    {
                        JSONObject JSONFilm = results.getJSONObject(i);

                        Log.d("ExploreFragment", "Discover Movie result json is " + JSONFilm.toString());

                        int id = JSONFilm.getInt("id");
                        String posterPath = JSONFilm.getString("poster_path");
                        String title = JSONFilm.getString("title");
                        String description = JSONFilm.getString("overview");
                        double rating = JSONFilm.getDouble("vote_average");

                        String imageURL = "https://image.tmdb.org/t/p/w500" + posterPath;

                        Film film = new Film(id, title, description, imageURL, rating);

                        Log.d("ExploreFragment", "Id is " + id);
                        Log.d("ExploreFragment", "Poster Path is " + posterPath);
                        Log.d("ExploreFragment", "Discover Image URL is " + imageURL);
                        Log.d("ExploreFragment", "Discover Movie Title is " + title);
                        Log.d("ExploreFragment", "Discover Movie Description is " + description);
                        Log.d("ExploreFragment", "Discover Movie Rating is " + rating);

                        discoverFilms.add(film);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.d("ExploreFragment", "Response is null");
            }

            return null;
        }
    }

    /*public List<Film> getLikedFilms()
    {

    }*/

    private int searchId;
    private Film filmFoundFromId;

    public Film getFilmFromId(int id)
    {
        searchId = id;
        new getFilmFromIdAsyncTask().execute();
    }

    private class getFilmFromIdAsyncTask extends AsyncTask
    {
        @Override
        protected Object doInBackground(Object[] objects) {
            StringBuffer response = new StringBuffer();

            try {
                String urlString = "https://api.themoviedb.org/3/movie";
                urlString += "/" + searchId;
                urlString += "?api_key=" + API_KEY;
                URL url = new URL(urlString);

                Log.d("ExploreFragment", "Discover URL is " + urlString);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();

                Log.d("ExploreFragment", "Response Code is " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK)
                {
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));

                    String output;

                    while ((output = in.readLine()) != null)
                        response.append(output);
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

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

    public void insertFilms(Film... film) { service.execute(() -> filmDao.insert(film)) ;}

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

package edu.floridapoly.mobiledeviceapps.fall21.cinemapal;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.entities.Film;

public class UserUtil {
    static final List<Film> localFilms = new ArrayList<>();

    public static void updateLikedFilms(Context context) {
        String apiKey = context.getString(R.string.API_KEY);

        UserUtil.getLikedFilms(context)
                .join(filmIds -> new CallbackTask<Object>() {
                    @Override
                    public void execute() {
                        for(String filmId : filmIds) {
                            new HttpGetRequest(
                                    "https://api.themoviedb.org/3/movie/{movie_id}?api_key=<<api_key>>&language=en-US"
                                            .replace("{movie_id}", filmId)
                                            .replace("<<api_key>>", apiKey)
                            )
                                    .map(responseText -> {
                                        Log.i("home", responseText);
                                        JSONObject filmJson = new JSONObject(responseText);
                                        int id = filmJson.getInt("id");
                                        String title = filmJson.getString("title");
                                        String description = filmJson.getString("overview");
                                        String posterPath = filmJson.getString("poster_path");
                                        String imageURL = "https://image.tmdb.org/t/p/w500" + posterPath;
                                        double rating = filmJson.getDouble("vote_average");

                                        return new Film(title, description, imageURL, rating);
                                    })
                                    .map(film -> {
                                        synchronized (localFilms) {
                                            for (int i = 0; i < localFilms.size(); i++) {
                                                if (localFilms.get(i).getTitle().equals(film.getTitle())) {
                                                    return null;
                                                }
                                            }
                                            localFilms.add(film);
                                        }
                                        return null;
                                    })
                                    .setOnFailureListener(e -> e.printStackTrace())
                                    .execute();
                        }
                    }
                })
                .execute();
    }

    public static String getId(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString("userId", "");
    }

    public static CallbackTask<List<String>> getFriends(Context context) {
        return getFriendsFor(getId(context));
    }
    public static CallbackTask<List<String>> getFriendsFor(String userId) {
        return new CallbackTask<List<String>>() {
            @Override
            public void execute() {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("friendships")
                        .whereArrayContains("userIds", userId)
                        .get()
                        .addOnSuccessListener(friendships -> {
                            ArrayList<String> friends = new ArrayList<>();
                            for(QueryDocumentSnapshot friendship : friendships) {
                                List<String> userIds = (List<String>)friendship.get("userIds");
                                if(userIds == null)
                                    continue;
                                int selfIdx = userIds.indexOf(userId);
                                friends.add(userIds.get(userIds.size() - 1 - selfIdx));
                            }
                            callOnSuccess(friends);
                        })
                        .addOnFailureListener(e -> onFailure.onFailure(e));
            }
        };
    }

    public static FilmListTask getLikedFilms(Context context) {
        return getLikedFilmsFor(getId(context));
    }
    public static FilmListTask getLikedFilmsFor(String userId) {
        FilmListTask task = new FilmListTask();
        task.userId = userId;
        task.filmListName = "likedFilms";

        return task;
    }
    public static FilmListTask getIgnoredFilms(Context context) {

        FilmListTask task = new FilmListTask();
        task.userId = getId(context);
        task.filmListName = "ignoredFilms";

        return task;
    }

    public static AddFilmTask addLikedFilm(Context context, String filmId) {
        AddFilmTask task = new AddFilmTask();
        task.userId = getId(context);
        task.filmId = filmId;
        task.filmListName = "likedFilms";
        return task;
    }
    public static AddFilmTask addIgnoredFilm(Context context, String filmId) {
        AddFilmTask task = new AddFilmTask();
        task.userId = getId(context);
        task.filmId = filmId;
        task.filmListName = "ignoredFilms";
        return task;
    }

    public static class FilmListTask extends CallbackTask<List<String>> {
        protected String userId;
        protected String filmListName;

        @Override
        public void execute() {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users")
                    .document(userId)
                    .get()
                    .addOnSuccessListener(document -> {
                        List<String> filmIds;
                        Object filmIdsObj = document.get(filmListName);
                        if(filmIdsObj instanceof List) {
                            filmIds = (List<String>)filmIdsObj;
                        } else {
                            filmIds = new ArrayList<String>();
                        }
                        callOnSuccess(filmIds);
                    })
                    .addOnFailureListener(e -> onFailure.onFailure(e));
        }
    }

    public static class AddFilmTask extends CallbackTask<Void> {
        protected String userId;
        protected String filmId;
        protected String filmListName;

        @Override
        public void execute() {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users")
                    .document(userId)
                    .get()
                    .addOnSuccessListener(doc -> {
                        List<String> filmIds;
                        Object filmIdsObj = doc.get(filmListName);
                        if(filmIdsObj instanceof List) {
                            filmIds = (List<String>)filmIdsObj;
                        } else {
                            filmIds = new ArrayList<String>();
                        }
                        if(!filmIds.contains(filmId)) {
                            filmIds.add(filmId);
                            db.collection("users")
                                    .document(userId)
                                    .update(filmListName, filmIds)
                                    .addOnSuccessListener((Void) -> callOnSuccess(null))
                                    .addOnFailureListener(e -> onFailure.onFailure(e));
                        } else {
                            callOnSuccess(null);
                        }
                    })
                    .addOnFailureListener(e -> onFailure.onFailure(e));
        }
    }
}

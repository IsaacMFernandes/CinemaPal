package edu.floridapoly.mobiledeviceapps.fall21.cinemapal;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class UserUtil {
    public static String getId(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString("userId", "");
    }
    public static FilmListTask getLikedFilms(Context context) {

        FilmListTask task = new FilmListTask();
        task.userId = getId(context);
        task.filmListName = "likedFilms";

        return task;
    }
    public static FilmListTask getIgnoredFilms(Context context) {

        FilmListTask task = new FilmListTask();
        task.userId = getId(context);
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
                        List<String> filmIds = (List<String>) document.get(filmListName);
                        callOnSuccess(filmIds);
                    })
                    .addOnFailureListener(e -> onFailure.onFailure(e));
        }
    }
}

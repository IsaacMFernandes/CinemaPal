package edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.daos.*;
import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.entities.*;
import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.entities.crossref.*;

@Database(entities = {Celebrity.class, Film.class, Genre.class, User.class,
        FilmCelebrityCrossRef.class, UserFilmCrossRef.class}, version = 1)
public abstract class CinemaPalDatabase extends RoomDatabase
{
    private static CinemaPalDatabase instance;

    public abstract UserDao userDao();
    public abstract FilmDao filmDao();
    public abstract CelebrityDao celebrityDao();

    public static synchronized CinemaPalDatabase getInstance(Context context)
    {
        if(instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CinemaPalDatabase.class, "cinema_pal_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    public static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>
    {
        private FilmDao filmDao;

        private PopulateDbAsyncTask(CinemaPalDatabase db)
        {
            filmDao = db.filmDao();
        }

        @Override
        protected Void doInBackground(Void...Voids)
        {
            filmDao.insert(new Film("Shangchi", "Superhero movie", "Marvel Studios", (short) 10));
            filmDao.insert(new Film("Finding Nemo", "Fish gets lost", "Idk Disney", (short) 8));
            filmDao.insert(new Film("Hallmark movie", "People fall in love", "Hallmark", (short) 4));
            return null;
        }
    }
}

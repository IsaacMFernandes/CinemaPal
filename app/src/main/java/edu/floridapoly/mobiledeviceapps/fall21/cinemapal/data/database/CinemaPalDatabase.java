package edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

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
                    // to populate database .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    /*
    Populate the database
    public static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    // Populate database here (ie. userDao.insert(new User("name"));)
                }
            });
        }
    }
    */
}

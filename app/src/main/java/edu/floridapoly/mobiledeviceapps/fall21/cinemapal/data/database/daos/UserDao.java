package edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.daos;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.entities.User;
import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.maps.LikedFilms;

@Dao
public interface UserDao extends BaseDao<User>
{
    // Queries for users


    // Retrieve list of liked films
    @Transaction
    @Query("SELECT * FROM User")
    public LiveData<List<LikedFilms>> getLikedFilms();
}

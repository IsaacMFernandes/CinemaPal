package edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.daos;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

@Dao
public interface BaseDao<T>
{
    @Insert(onConflict = REPLACE)
    public void insert(T... entity);

    @Insert(onConflict = REPLACE)
    public void insert(T entity);

    @Update
    public void update(T entity);

    @Delete
    public void delete(T entity);
}

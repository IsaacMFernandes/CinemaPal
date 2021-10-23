package edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User
{
    @PrimaryKey(autoGenerate = true)
    private int userId;

    private String name;

    public User(String name)
    {
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() { return name; }
}

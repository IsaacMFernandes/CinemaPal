package edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "celebrity")
public class Celebrity
{
    @PrimaryKey(autoGenerate = true)
    private int celebrityId;

    @ColumnInfo(name = "first_name")
    private String firstName;

    @ColumnInfo(name = "last_name")
    private String lastName;

    public Celebrity(String firstName, String lastName)
    {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getCelebrityId() { return celebrityId; }
    public void setCelebrityId(int celebrityId) { this.celebrityId = celebrityId; }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
}

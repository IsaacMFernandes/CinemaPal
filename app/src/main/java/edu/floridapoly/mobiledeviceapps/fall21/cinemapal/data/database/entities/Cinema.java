/*package edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "cinema")
public class Cinema
{
    @PrimaryKey(autoGenerate = true)
    private int cinemaId;

    // Using a String because I don't know how location information is stored yet
    private String location;
    private String name;

    public Cinema(String location, String name)
    {
        this.location = location;
        this.name = name;
    }

    public int getCinemaId() { return cinemaId; }
    public void setCinemaId(int cinemaId) { this.cinemaId = cinemaId; }

    public String getLocation() { return location; }
    public String getName() { return name; }
}
*/
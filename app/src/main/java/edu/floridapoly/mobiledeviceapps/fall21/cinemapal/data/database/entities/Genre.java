package edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "genre")
public class Genre
{
    @PrimaryKey(autoGenerate = true)
    private int genreId;

    private String tag;

    public Genre(String tag)
    {
        this.tag = tag;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public String getTag() { return tag; }
}

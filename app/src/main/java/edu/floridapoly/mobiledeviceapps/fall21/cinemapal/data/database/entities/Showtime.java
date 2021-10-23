/*package edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "showtime")
public class Showtime
{
    @PrimaryKey(autoGenerate = true)
    private int showtimeId;

    // mm:dd:yyyy
    private String date;

    // hh:mm
    private String time;

    // Stores the keys for a film and a cinema
    private int filmShowtimeId;
    private int cinemaShowtimeId;

    public Showtime(String date, String time)
    {
        this.date = date;
        this.time = time;
    }

    public int getShowtimeId() { return showtimeId; }
    public void setShowtimeId(int showtimeId) { this.showtimeId = showtimeId; }

    public String getDate() { return date; }
    public String getTime() { return time; }
    public int getFilmId() { return filmShowtimeId; }
    public int getCinemaId() { return cinemaShowtimeId; }
}
*/
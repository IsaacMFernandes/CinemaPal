package edu.floridapoly.mobiledeviceapps.fall21.cinemapal;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.CinemaPalRepository;
import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.maps.LikedFilms;

public class CinemaPalViewModel extends AndroidViewModel
{
    private CinemaPalRepository repository;
    private LiveData<List<LikedFilms>> likedFilms;

    public CinemaPalViewModel(@NonNull Application application)
    {
        super(application);

        repository = new CinemaPalRepository(application);
        likedFilms = repository.getLikedFilms();
    }

    // TODO implement all the methods contained in CinemaPalRepository
}

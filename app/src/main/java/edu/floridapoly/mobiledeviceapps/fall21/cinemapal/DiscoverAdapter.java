package edu.floridapoly.mobiledeviceapps.fall21.cinemapal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.DiscoverFilm;

public class DiscoverAdapter extends BaseAdapter
{
    private ArrayList<DiscoverFilm> discoverFilms;
    private Context context;

    public DiscoverAdapter(ArrayList<DiscoverFilm> films, Context context)
    {
        discoverFilms = films;
        this.context = context;
    }

    public void setDiscoverFilms(ArrayList<DiscoverFilm> films)
    {
        discoverFilms = films;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return discoverFilms.size();
    }

    @Override
    public Object getItem(int i) {
        return discoverFilms.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null)
        {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.discover_movie_item, viewGroup, false);
        }

        DiscoverFilm film = (DiscoverFilm) getItem(i);
        ImageView image = v.findViewById(R.id.explore_movie_img);

        Picasso.get().load(film.getImageURL()).into(image);

        return v;
    }
}

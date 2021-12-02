package edu.floridapoly.mobiledeviceapps.fall21.cinemapal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.entities.Film;

public class FilmAdapter extends ListAdapter<Film, FilmAdapter.FilmHolder>
{
    public FilmAdapter()
    {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Film> DIFF_CALLBACK = new DiffUtil.ItemCallback<Film>() {
        @Override
        public boolean areItemsTheSame(@NonNull Film oldItem, @NonNull Film newItem) {
            return oldItem.getFilmId() == newItem.getFilmId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Film oldItem, @NonNull Film newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getDirector().equals(newItem.getDirector()) &&
                    oldItem.getFilmGenreId() == newItem.getFilmGenreId() &&
                    oldItem.getRating() == newItem.getRating();
        }
    };

    @NonNull
    @Override
    public FilmHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);
        return new FilmHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull FilmHolder holder, int position)
    {
        Film film = getItem(position);
        holder.title.setText(film.getTitle());
        holder.description.setText(film.getDescription());
    }

    public Film getFilmAt(int position)
    {
        return getItem(position);
    }

    class FilmHolder extends RecyclerView.ViewHolder
    {
        private TextView title;
        private TextView description;

        public FilmHolder(@NonNull View itemView)
        {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_text_view);
            description = (TextView) itemView.findViewById(R.id.description_text_view);
            // TODO OnClickListener Implementation
        }
    }
}

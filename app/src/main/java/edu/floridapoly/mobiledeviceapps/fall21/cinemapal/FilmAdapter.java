package edu.floridapoly.mobiledeviceapps.fall21.cinemapal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
                    oldItem.getImageURL().equals(newItem.getImageURL()) &&
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
        String filmId = Integer.toString(film.getFilmId());

        holder.title.setText(film.getTitle());
        holder.description.setText(film.getDescription());

        holder.friendListIndicator.setEnabled(false);
        UserUtil.getFriends(holder.title.getContext())
                .join(friends -> new CallbackTask<Void>() {
                    @Override
                    public void execute() {
                        for(String friendId : friends) {
                            UserUtil.getLikedFilmsFor(friendId)
                                    .map(filmIds -> {
                                        if(filmIds.contains(filmId)) {
                                            holder.friendListIndicator.setEnabled(true);
                                        }
                                        return null;
                                    })
                                    .execute();

                        }
                    }
                })
                .execute();
    }

    public Film getFilmAt(int position)
    {
        return getItem(position);
    }

    class FilmHolder extends RecyclerView.ViewHolder
    {
        private TextView title;
        private TextView description;
        private Button friendListIndicator;

        public FilmHolder(@NonNull View itemView)
        {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_text_view);
            description = (TextView) itemView.findViewById(R.id.description_text_view);
            friendListIndicator = itemView.findViewById(R.id.friend_list_indicator);
            // TODO OnClickListener Implementation
        }
    }
}

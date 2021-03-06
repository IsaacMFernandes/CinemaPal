package edu.floridapoly.mobiledeviceapps.fall21.cinemapal;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daprlabs.cardstack.SwipeDeck;

import java.util.ArrayList;

import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.CinemaPalViewModel;
import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.entities.Film;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExploreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreFragment extends Fragment {

    private CinemaPalViewModel viewModel;
    private TextView titleView;
    private ImageView exploreImage;
    private ArrayList<Film> discoverFilms;
    private SwipeDeck cardDeck;

    public ExploreFragment() {
        // Required empty public constructor
    }

    public static ExploreFragment newInstance(String param1, String param2) {
        ExploreFragment fragment = new ExploreFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(CinemaPalViewModel.class);
        exploreImage = view.findViewById(R.id.explore_movie_img);
        titleView = view.findViewById(R.id.explore_movie_title);

        discoverFilms = new ArrayList<>();
        discoverFilms = viewModel.getDiscoverFilms();
        cardDeck = (SwipeDeck) view.findViewById(R.id.swipe_deck);

        final DiscoverAdapter adapter = new DiscoverAdapter(discoverFilms, getContext());
        cardDeck.setAdapter(adapter);

        cardDeck.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {
                Toast.makeText(getContext(), "Card Swiped Left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void cardSwipedRight(int position) {
                Toast.makeText(getContext(), "Card Swiped Right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void cardsDepleted() {
                Toast.makeText(getContext(), "No more cards", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void cardActionDown() {
            }

            @Override
            public void cardActionUp() {
            }
        });

        return view;
    }
}
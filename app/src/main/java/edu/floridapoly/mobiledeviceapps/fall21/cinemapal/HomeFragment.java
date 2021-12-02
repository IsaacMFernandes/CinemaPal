package edu.floridapoly.mobiledeviceapps.fall21.cinemapal;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.CinemaPalViewModel;
import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.entities.Film;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    /*
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    */

    private RecyclerView recyclerView;
    private CinemaPalViewModel viewModel;

    //private TextView title;
    //private TextView synopsis;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(/*String param1, String param2*/) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment and get the root view (Main Activity)
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(CinemaPalViewModel.class);

        // Recycler view
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        FilmAdapter filmAdapter = new FilmAdapter();
        recyclerView.setAdapter(filmAdapter);

        List<Film> films = new ArrayList<>();
        UserUtil.getLikedFilms(getContext()).setOnSuccessListener(result -> {
            for (String idString : result)
            {
                int id = Integer.getInteger(idString);
                Film film = viewModel.searchFilmFromId(id);
                films.add(film);
            }
        });
        filmAdapter.submitList(films);

        //Film film = viewModel.findFilmByTitle("Shangchi");

        //title = (TextView) view.findViewById(R.id.movie_title);
        //synopsis = (TextView) view.findViewById(R.id.movie_synopsis);

        //title.setText(film.getTitle());
        //synopsis.setText(film.getDescription());

        return view;
    }
}
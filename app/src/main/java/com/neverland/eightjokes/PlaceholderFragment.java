package com.neverland.eightjokes;

/**
 * Created by Vasilev on 18.3.2015 г..
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.neverland.eightjokes.entities.Joke;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private ListView jokesListView;
    private JokesAdapter jokesAdapter;
    private ArrayList<Joke> allJokes;
    private ProgressDialog progressDialog;

    private enum Category {
        BEST_JOKES("Best Jokes", "rate_up"),
        LAST_JOKES("Last Jokes", "createdAt"),
        ANIMALS("Animals", "createdAt"),
        Black_Humor("Black Humor", "createdAt"),
        BLONDES("Blonde", "createdAt"),
        CHUCK_NORRIS("Chuck Norris", "createdAt"),
        DARK_PEOPLE("Dark people", "createdAt"),
        DIRTY("Dirty", "createdAt"),
        FACEBOOK("Facebook", "createdAt"),
        GAYS("GAYS", "createdAt"),
        IT("IT", "createdAt"),
        LITTLE_JOHNY("Little Johny", "createdAt"),
        MEN_WOMEN("Men / Women", "createdAt"),
        SEX("Sex", "createdAt"),
        SPORT("Sport", "createdAt"),
        YO_MAMA("Yo Mama", "createdAt");

        private String name;
        private String sortColumnName;

        private Category(String name, String sortColumnName) {
            this.name = name;
            this.sortColumnName = sortColumnName;
        }

        private String getName() {
            return this.name;
        }

        private String getSortColumnName() {
            return this.sortColumnName;
        }
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {

        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PlaceholderFragment() {
        allJokes = new ArrayList<Joke>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        jokesListView = (ListView) rootView.findViewById(R.id.jokesListView);
        progressDialog = new ProgressDialog(getActivity());

        jokesListView.setVisibility(View.INVISIBLE);
        progressDialog.setMessage(getActivity().getString(R.string.loading_message));

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        jokesAdapter = new JokesAdapter(getActivity(), allJokes);
        jokesListView.setAdapter(jokesAdapter);

        getJokesByCategory();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }

    private void getJokesByCategory() {

        progressDialog.show();

        int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
        Category category = Category.values()[sectionNumber - 1];

        ParseQuery<Joke> jokeQuery = ParseQuery.getQuery(Joke.class);

        if (sectionNumber > 2) {
            jokeQuery.whereEqualTo("category_name", category.getName());
        }

        jokeQuery.orderByDescending(category.getSortColumnName());
        jokeQuery.findInBackground(new FindCallback<Joke>() {
            @Override
            public void done(List<Joke> jokes, ParseException e) {

                if (e == null) {
                    allJokes = (ArrayList<Joke>) jokes;
                    updateAdapterData();
                } else {
                    showErrorMessage(e);
                }

                jokesListView.setVisibility(View.VISIBLE);
                progressDialog.dismiss();
            }
        });
    }

    private void showErrorMessage(ParseException e) {
        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void updateAdapterData() {

        jokesAdapter.clear();

        for (Joke joke : allJokes) {
            jokesAdapter.addItem(joke);
        }

        jokesAdapter.notifyDataSetChanged();
    }
}

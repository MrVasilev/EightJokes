package com.neverland.eightjokes.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.neverland.eightjokes.R;
import com.neverland.eightjokes.Utils;
import com.neverland.eightjokes.login.WelcomeActivity;


public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private int tapCounter = 0;
    private String jokeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkForCurrentUser();

        //App-Open / Push Analytics
//        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {

        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section_best_jokes);
                break;
            case 2:
                mTitle = getString(R.string.title_section_last_jokes);
                break;
            case 3:
                mTitle = getString(R.string.title_section_animals);
                break;
            case 4:
                mTitle = getString(R.string.title_section_black_humor);
                break;
            case 5:
                mTitle = getString(R.string.title_section_blondes);
                break;
            case 6:
                mTitle = getString(R.string.title_section_chuck_norris);
                break;
            case 7:
                mTitle = getString(R.string.title_section_dark_people);
                break;
            case 8:
                mTitle = getString(R.string.title_section_dirty);
                break;
            case 9:
                mTitle = getString(R.string.title_section_facebook);
                break;
            case 10:
                mTitle = getString(R.string.title_section_gays);
                break;
            case 11:
                mTitle = getString(R.string.title_section_it);
                break;
            case 12:
                mTitle = getString(R.string.title_section_little_johny);
                break;
            case 13:
                mTitle = getString(R.string.title_section_men_women);
                break;
            case 14:
                mTitle = getString(R.string.title_section_sex);
                break;
            case 15:
                mTitle = getString(R.string.title_section_sport);
                break;
            case 16:
                mTitle = getString(R.string.title_section_yo_mama);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    /**
     * Check for currentUser and if not exists go to Welcome Screen
     */
    private void checkForCurrentUser() {

        if (!Utils.isCurrentUserExists()) {
            //Go to Welcome screen
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
        }
    }
}

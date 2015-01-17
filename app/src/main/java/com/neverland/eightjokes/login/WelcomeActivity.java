package com.neverland.eightjokes.login;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.neverland.eightjokes.R;

public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    /**
     * Handle click event on "Not now" TextView
     */
    public void onNotNowClick(View view) {

        finish();
    }
}

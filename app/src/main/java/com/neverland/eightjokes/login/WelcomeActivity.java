package com.neverland.eightjokes.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.neverland.eightjokes.R;

public class WelcomeActivity extends Activity implements View.OnClickListener {

    private Button signUpButton;
    private Button logInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        signUpButton = (Button) findViewById(R.id.signUpButton);
        logInButton = (Button) findViewById(R.id.logInButton);

        signUpButton.setOnClickListener(this);
        logInButton.setOnClickListener(this);
    }

    /**
     * Handle click event on "Not now" TextView
     */
    public void onNotNowClick(View view) {

        finish();
    }

    @Override
    public void onClick(View view) {

        String message = "";
        Intent loginIntent = new Intent(this, LoginActivity.class);

        if (view.getId() == R.id.signUpButton) {
            message = "sign_up";
        } else if (view.getId() == R.id.logInButton) {
            message = "log_in";
        }

        if (!message.equals("")) {

            loginIntent.putExtra("message", message);
            startActivity(loginIntent);
        }
    }
}

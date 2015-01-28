package com.neverland.eightjokes.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.neverland.eightjokes.Constants;
import com.neverland.eightjokes.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends ActionBarActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static final int GOOGLE_PLUS_REQUEST_CODE = 0;
    public static final int FACEBOOK_REQUEST_CODE = 1;
    //Login buttons
    private SignInButton googleButton;
    private LoginButton facebookButton;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    /* Track whether the sign-in button has been clicked so that we know to resolve
    * all issues preventing sign-in without waiting.
    */
    private boolean mSignInClicked;

    /* Store the connection result from onConnectionFailed callbacks so that we can
     * resolve them when the user clicks sign-in.
     */
    private ConnectionResult mConnectionResult;

    /* A flag indicating that a PendingIntent is in progress and prevents
   * us from starting further intents.
   */
    private boolean mIntentInProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        googleButton = (SignInButton) findViewById(R.id.googleLoginButton);
        facebookButton = (LoginButton) findViewById(R.id.facebookLoginButton);

        googleButton.setOnClickListener(this);
        facebookButton.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        String message = extras.getString("message");

        if (message.equals("log_in")) {
            Toast.makeText(this, "It should be Log in screen!", Toast.LENGTH_SHORT).show();
        } else if (message.equals("sign_up")) {
            Toast.makeText(this, "It should be Sign up screen!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.googleLoginButton:
                initGooglePlusApiClient();
                signInWithGooglePlus();
                break;

            case R.id.facebookLoginButton:
                loginWithFacebook();
                break;

            default:
                break;
        }
    }

    /**
     * Login user with Facebook
     */
    private void loginWithFacebook() {

        ParseFacebookUtils.initialize(getString(R.string.facebook_app_id));

        final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, "", "Logging in...", true);

        List<String> permissions = Arrays.asList(ParseFacebookUtils.Permissions.User.EMAIL);

        ParseFacebookUtils.logIn(permissions, this, FACEBOOK_REQUEST_CODE, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {

                if (parseUser == null) {

                    Log.d(Constants.TAG, "Uh oh. The user cancelled the Facebook login.");

                } else if (parseUser.isNew()) {

                    makeMeRequest();

                    Log.d(Constants.TAG, "User signed up and logged in through Facebook!");
                } else {

                    Log.d(Constants.TAG, "User logged in through Facebook!");
                }
            }
        });

        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_PLUS_REQUEST_CODE) {

            if (resultCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }

        } else if (requestCode == FACEBOOK_REQUEST_CODE) {

            ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
        }
    }

    /**
     * Make request to get Facebook user data.
     * Save the data of current user.
     */
    private void makeMeRequest() {

        Request request = Request.newMeRequest(ParseFacebookUtils.getSession(), new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {

                        if (user != null) {

                            ParseUser currentUser = ParseUser.getCurrentUser();

                            currentUser.setUsername(user.getName());

                            if (user.getProperty("email") != null) {

                                currentUser.setEmail((String) user.getProperty("email"));
                            }

                            if (user.getBirthday() != null) {

                                currentUser.put("birthday", user.getBirthday());
                            }

                            currentUser.saveInBackground();

                            // Now add the data to the UI elements
                            Log.d(Constants.TAG, "");

                        } else if (response.getError() != null) {
                            // handle error
                            Log.d(Constants.TAG, "Something WRONG happened when try to get Facebook User data!!!");
                        }
                    }
                }
        );

        request.executeAsync();
    }

    /**
     * Initializing Google plus api client
     */
    private void initGooglePlusApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();

        connectGooglePlusClient();
    }

    /**
     * Connect Google plus api client
     */
    private void connectGooglePlusClient() {
        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();
    }

    /**
     * Disconnect Google plus api client
     */
    private void disconnectGooglePlusClient() {
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }

    /**
     * Sign-in into Google
     */
    private void signInWithGooglePlus() {

        mSignInClicked = true;

        if (!mGoogleApiClient.isConnecting())
            resolveSignInError();
    }

    /* A helper method to resolve the current ConnectionResult error. */
    private void resolveSignInError() {

        if (mConnectionResult.hasResolution()) {

            try {

                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, GOOGLE_PLUS_REQUEST_CODE);

            } catch (IntentSender.SendIntentException e) {

                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    /**
     * Fetching user's information name, email, profile pic
     */
    private void getProfileInformation() {

        try {

            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {

                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                String password = email + personName;

                ParseUser currentUser = new ParseUser();

                currentUser.setUsername(personName);
                currentUser.setEmail(email);
                currentUser.setPassword(password);

                if (currentPerson.hasBirthday())
                    currentUser.put("birthday", currentPerson.getBirthday());

                currentUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {

                        if (e == null) {
                            Log.d(Constants.TAG, "User sign up with Google Plus account.");
                        } else {
                            Log.d(Constants.TAG, "Something WRONG when user try to sign up with Google Plus account!!!");
                        }
                    }
                });

                disconnectGooglePlusClient();

            } else {
                Log.d(Constants.TAG, "Person information is null");
            }
        } catch (Exception e) {
            Log.d(Constants.TAG, "Some WRONG happened while try to get info of GooglePlusClient\nError : " + e.getMessage());
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

        mSignInClicked = false;
        getProfileInformation();
        Log.d(Constants.TAG, "PlusUser is connected!");
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        if (!connectionResult.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = connectionResult;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }
    }
}

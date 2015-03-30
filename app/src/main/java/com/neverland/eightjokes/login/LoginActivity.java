package com.neverland.eightjokes.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.neverland.eightjokes.main.MainActivity;
import com.neverland.eightjokes.R;
import com.neverland.eightjokes.Utils;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends ActionBarActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static final int GOOGLE_PLUS_REQUEST_CODE = 0;
    public static final int FACEBOOK_REQUEST_CODE = 1;

    //Log in buttons
    private SignInButton googleButton;
    private LoginButton facebookButton;
    private Button signUpLogInButton;

    //Log in fields
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;

    private ProgressDialog loadingProgressDialog;

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
        signUpLogInButton = (Button) findViewById(R.id.signUpLogInButton);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        loadingProgressDialog = new ProgressDialog(this);

        googleButton.setOnClickListener(this);
        facebookButton.setOnClickListener(this);
        signUpLogInButton.setOnClickListener(this);

        updateUI();
    }

    /**
     * According which button is clicked "Log in" or "Sign up"
     * update fields, button and links on the screen.
     */
    private void updateUI() {

        Bundle extras = getIntent().getExtras();
        String message = extras.getString("message");

        if (message.equals(getString(R.string.log_in_button_clicked_message))) {

            nameEditText.setVisibility(View.GONE);
            signUpLogInButton.setText(R.string.log_in_button_label);

        } else if (message.equals(getString(R.string.sign_up_button_clicked_message))) {

            nameEditText.setVisibility(View.VISIBLE);
            signUpLogInButton.setText(R.string.sing_up_button_label);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.googleLoginButton:
                initGooglePlusApiClient();
                signUpWithGooglePlus();
                break;

            case R.id.facebookLoginButton:
                loginWithFacebook();
                break;

            case R.id.signUpLogInButton:
                signUpLogInWithEmail();
                break;

            default:
                break;
        }
    }

    /**
     *  Login user with Own Email address
     */
    private void signUpLogInWithEmail() {

        if (checkSignUpFields()) {

            String emailText = emailEditText.getText().toString().trim();
            final String passwordText = passwordEditText.getText().toString().trim();

            if(nameEditText.getVisibility() == View.VISIBLE){

                loadingProgressDialog.setMessage(getString(R.string.loading_sign_up_message));

                String nameText = nameEditText.getText().toString().trim();

                ParseUser currentUser = createUserAndSet(nameText, emailText, passwordText);

                currentUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {

                        if (e == null) {
                            goToMainActivity();
                            Log.d(Constants.TAG, "User sign up with  own email.");
                        } else {
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d(Constants.TAG, "Something WRONG when user try to sign up with own email!!!");
                        }

                        loadingProgressDialog.dismiss();
                    }
                });

            }else{

                loadingProgressDialog.setMessage(getString(R.string.loading_login_message));

                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("email", emailText);

                query.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> parseUsers, ParseException e) {

                        if(e == null){

                            if(parseUsers.size() > 0){

                                ParseUser user = parseUsers.get(0);
                                ParseUser.logInInBackground(user.getUsername(), passwordText, new LogInCallback() {
                                    @Override
                                    public void done(ParseUser parseUser, ParseException e) {

                                        if (e == null) {
                                            goToMainActivity();
                                            Log.d(Constants.TAG, "User login with  own email.");
                                        } else {
                                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            Log.d(Constants.TAG, "Something WRONG when user try to login with own email!!!");
                                        }
                                    }
                                });
                            }
                        }else{
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        loadingProgressDialog.dismiss();
                    }
                });
            }

            loadingProgressDialog.show();
        }
    }

    /**
     * Check fields if user decide to login/sign up with own email.
     *
     * @return
     */
    private boolean checkSignUpFields() {

        boolean isOk = true;

        if (nameEditText.getVisibility() == View.VISIBLE) {

            isOk &= Utils.checkEditTextEmptyOrLessThenThree(nameEditText);
        }

        isOk &= Utils.checkEditTextEmptyOrLessThenThree(emailEditText);
        isOk &= Utils.checkEditTextEmptyOrLessThenThree(passwordEditText);

        return isOk;
    }

    /**
     * Login user with Facebook
     */
    private void loginWithFacebook() {

        ParseFacebookUtils.initialize(getString(R.string.facebook_app_id));

        final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, "", getString(R.string.loading_login_message), true);

        List<String> permissions = Arrays.asList(ParseFacebookUtils.Permissions.User.EMAIL, ParseFacebookUtils.Permissions.User.ABOUT_ME);

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

                            ParseUser tempUser = ParseUser.getCurrentUser();

                            String name = user.getName();
                            String email = (user.getProperty("email") != null) ? (String) user.getProperty("email") : "";
                            String password = email + name;

                            ParseUser currentUser = createUserAndSet(name, email, password);

                            if (user.getProperty("birthday") != null)
                                currentUser.put("birthday", user.getProperty("birthday"));

                            currentUser.signUpInBackground(new SignUpCallback() {
                                @Override
                                public void done(ParseException e) {

                                    if (e == null) {
                                        goToMainActivity();
                                        Log.d(Constants.TAG, "User sign up with Facebook account.");
                                    } else {
                                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.d(Constants.TAG, "Something WRONG when user try to sign up with Facebook account!!!");
                                    }
                                }
                            });

                            tempUser.deleteInBackground();

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
    private void signUpWithGooglePlus() {

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

                loadingProgressDialog.setMessage(getString(R.string.loading_sign_up_message));
                loadingProgressDialog.show();

                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                String name = currentPerson.getDisplayName();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                String password = email + name;

                ParseUser currentUser = createUserAndSet(name, email, password);

                if (currentPerson.hasBirthday())
                    currentUser.put("birthday", currentPerson.getBirthday());

                currentUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {

                        if (e == null) {
                            goToMainActivity();
                            Log.d(Constants.TAG, "User sign up with Google Plus account.");
                        } else {
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d(Constants.TAG, "Something WRONG when user try to sign up with Google Plus account!!!");
                        }

                        loadingProgressDialog.dismiss();
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

    /**
     * Create ParseUser and set the third main properties
     *
     * @param name
     * @param email
     * @param password
     * @return
     */
    private ParseUser createUserAndSet(String name, String email, String password) {

        ParseUser currentUser = new ParseUser();

        currentUser.setUsername(name);
        currentUser.setEmail(email);
        currentUser.setPassword(password);
        return currentUser;
    }

    /**
     * Open the Main Activity where User can start reading, commenting and etc.
     */
    private void goToMainActivity(){

        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        finish();
    }
}

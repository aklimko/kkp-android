package pl.adamklimko.kkpandroid.view.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import pl.adamklimko.kkpandroid.R;
import pl.adamklimko.kkpandroid.exception.NoNetworkConnectedException;
import pl.adamklimko.kkpandroid.model.Token;
import pl.adamklimko.kkpandroid.model.User;
import pl.adamklimko.kkpandroid.rest.ApiClient;
import pl.adamklimko.kkpandroid.rest.UserSession;
import pl.adamklimko.kkpandroid.rest.KkpService;
import pl.adamklimko.kkpandroid.util.ProfilePictureUtil;
import pl.adamklimko.kkpandroid.util.ToastUtil;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class LoginActivity extends AppCompatActivity {

    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private KkpService kkpService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Setting SharedPreferences
//        if (UserSession.isAppJustStarted()) {
//            UserSession.initPreferences(getApplicationContext());
//        }
//
//        if (UserSession.hasToken()) {
//            UserSession.setFirstStarted(false);
//            final Intent messageActivity = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(messageActivity);
//            finish();
//            Toast.makeText(this, "Welcome " + UserSession.getUsername(), Toast.LENGTH_SHORT).show();
//            return;
//        }

        kkpService = ApiClient.createService(KkpService.class, getApplicationContext());
        // Set up the login form.
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.username);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == R.id.login || id == EditorInfo.IME_NULL) {
                attemptLogin();
                return true;
            }
            return false;
        });

        final Button mSignInButton = (Button) findViewById(R.id.username_sign_in_button);
        mSignInButton.setOnClickListener(view -> attemptLogin());

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String username = mUsernameView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            final User user = new User(username, password);
            mAuthTask = new UserLoginTask(user);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 3;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        final int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAuthTask != null) {
            mAuthTask = null;
        }
        kkpService = null;
        mLoginFormView = null;
        mUsernameView = null;
        mPasswordView = null;
        mProgressView = null;
        System.gc();
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final User user;
        private boolean noNetworkConnection = false;
        private boolean noInternetConnection = false;

        UserLoginTask(User user) {
            this.user = user;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            final Call<Token> tokenCall = kkpService.login(user);
            final Response<Token> response;
            try {
                response = tokenCall.execute();
            } catch (NoNetworkConnectedException e) {
                noNetworkConnection = true;
                return false;
            } catch (SocketTimeoutException e) {
                noInternetConnection = true;
                return false;
            } catch (IOException e) {
                return false;
            }
            if (!response.isSuccessful()) {
                return false;
            }
            if (response.code() == 200) {
                final Token token = response.body();
                if (token == null) {
                    return false;
                }
                if (token.getToken() == null) {
                    return false;
                }
                Log.i("LOGIN", "Successful login");
                saveUsername();
                UserSession.setTokenInPreferences(token);
                ProfilePictureUtil.getUserProfile(getApplicationContext());
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (noNetworkConnection) {
                ToastUtil.showToastShort("No network connection", getApplicationContext());
                return;
            }
            if (noInternetConnection) {
                ToastUtil.showToastShort("Cannot connect to a server", getApplicationContext());
                return;
            }
            if (success) {
                switchToPostLoginActivity();
            } else {
                mPasswordView.requestFocus();
                mPasswordView.setError(getString(R.string.error_incorrect_password));
            }
        }

        private void switchToPostLoginActivity() {
            informAboutSuccessfulLogin();
            startPostLoginActivity();
            closeLoginActivity();
        }

        private void informAboutSuccessfulLogin() {
            ToastUtil.showToastShort("Logged in", getApplicationContext());
        }

        private void saveUsername() {
            UserSession.setUsernameInPreferences(user.getUsername());
        }

        private void startPostLoginActivity() {
            final Intent messageActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(messageActivity);
        }

        private void closeLoginActivity() {
            finish();
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

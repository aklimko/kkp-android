package pl.adamklimko.kkpandroid.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import pl.adamklimko.kkpandroid.R;
import pl.adamklimko.kkpandroid.rest.UserSession;

public class WelcomeActivity extends AppCompatActivity {

    private static final int ANIMATION_DURATION = 1000;
    private static final int WAIT_TIME_AFTER_ANIMATION = ANIMATION_DURATION + 800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
        setContentView(R.layout.activity_welcome);

        View logo = findViewById(R.id.imageView);
        Animation logoAnimation = AnimationUtils.makeInAnimation(getApplicationContext(), true);
        logoAnimation.setDuration(ANIMATION_DURATION);

        View name = findViewById(R.id.textView);
        Animation nameAnimation = AnimationUtils.makeInAnimation(getApplicationContext(), false);
        nameAnimation.setDuration(ANIMATION_DURATION);

        logo.startAnimation(logoAnimation);
        name.startAnimation(nameAnimation);

        loadLocalData();

        new Handler().postDelayed(() -> {
            if (userLoggedIn()) {
                Intent main = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(main);
                Toast.makeText(getApplicationContext(), "Welcome " + UserSession.getUsername(), Toast.LENGTH_SHORT).show();
            } else {
                Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(login);
            }
            finish();
        }, WAIT_TIME_AFTER_ANIMATION);
    }

    private void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private void loadLocalData() {
        UserSession.initPreferences(getApplicationContext());
    }

    private boolean userLoggedIn() {
        return UserSession.hasToken();
    }
}

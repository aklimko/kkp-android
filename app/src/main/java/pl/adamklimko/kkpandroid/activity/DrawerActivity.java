package pl.adamklimko.kkpandroid.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import pl.adamklimko.kkpandroid.R;
import pl.adamklimko.kkpandroid.rest.UserSession;
import pl.adamklimko.kkpandroid.task.ProfilePictureTask;
import pl.adamklimko.kkpandroid.util.KeyboardUtil;
import pl.adamklimko.kkpandroid.util.ProfilePictureUtil;

public abstract class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FrameLayout viewStub; //This is the framelayout to keep your content view
    private NavigationView navigationView; // The new navigation view from Android Design Library. Can inflate menu resources. Easy

    private DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mDrawerToggle;
    private ImageView mProfilePicture;
    private TextView mUsername;

    public static final String UPDATE_PROFILE = "update_profile";
    private BroadcastReceiver mProfileUpdatedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateProfile();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.drawer_layout);

        viewStub = (FrameLayout) findViewById(R.id.view_stub);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setCheckedItem(0);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        final View v = navigationView.getHeaderView(0);
        mUsername = v.findViewById(R.id.header_username);
        final String fullName = UserSession.getFullName();
        if (!TextUtils.isEmpty(fullName)) {
            mUsername.setText(fullName);
        } else {
            mUsername.setText(UserSession.getUsername());
        }

        mProfilePicture = v.findViewById(R.id.header_image);
        final Bitmap profile = ProfilePictureUtil.getUserPictureFromStorage(UserSession.getUsername(), getApplicationContext());
        if (profile != null) {
            mProfilePicture.setImageBitmap(profile);
        }

        // register to receive messages
        LocalBroadcastManager.getInstance(this).registerReceiver(mProfileUpdatedReceiver,
                new IntentFilter(UPDATE_PROFILE));

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 0, 0);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                if (KeyboardUtil.isKeyboardOpen(mDrawerLayout)) {
                    KeyboardUtil.hideKeyboard(DrawerActivity.this);
                }
                super.onDrawerOpened(drawerView);
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void setContentView(int layoutResID) {
        if (viewStub != null) {
            final LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            final View stubView = inflater.inflate(layoutResID, viewStub, false);
            viewStub.addView(stubView, lp);
        }
    }

    @Override
    public void setContentView(View view) {
        if (viewStub != null) {
            final ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            viewStub.addView(view, lp);
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (viewStub != null) {
            viewStub.addView(view, params);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void unregisterReceivers() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mProfileUpdatedReceiver);
    }

    private void updateProfile() {
        updateFullName();
        updateProfilePicture();
    }

    private void updateFullName() {
        final String fullName = UserSession.getFullName();
        if (!TextUtils.isEmpty(fullName)) {
            mUsername.setText(fullName);
        } else {
            mUsername.setText(UserSession.getUsername());
        }
    }

    private void updateProfilePicture() {
        final ProfilePictureTask profilePictureTask = new ProfilePictureTask(getApplicationContext(), UserSession.getUsername());
        // Downloads and updates profile picture
        profilePictureTask.execute(UserSession.getFacebookId());
    }

    public void redrawProfilePicture() {
        final Bitmap profile = ProfilePictureUtil.getUserPictureFromStorage(UserSession.getUsername(), getApplicationContext());
        if (profile != null) {
            mProfilePicture.setImageBitmap(profile);
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceivers();
        mProfileUpdatedReceiver = null;
        viewStub = null;
        navigationView = null;
        mDrawerLayout = null;
        mDrawerToggle = null;
        mUsername = null;
        mProfilePicture = null;
        super.onDestroy();
    }

    public DrawerLayout getmDrawerLayout() {
        return mDrawerLayout;
    }

    public NavigationView getNavigationView() {
        return navigationView;
    }
}

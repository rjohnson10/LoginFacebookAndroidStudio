package com.example.jhordan.logeofacebookdevf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;

import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.entities.Profile.Properties;
import com.sromku.simple.fb.listeners.OnLikesListener;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnLogoutListener;
import com.sromku.simple.fb.listeners.OnProfileListener;

public class MyActivity extends Activity {

    protected static final String TAG ="";
    private Button mButtonLogin;
    private Button mButtonLogout;
    private TextView mTextStatus;
    private SimpleFacebook simplef;

    Permission[] permissions = new Permission[] {
            Permission.USER_PHOTOS,
            Permission.EMAIL,
            Permission.PUBLISH_ACTION
    };

    SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
            .setAppId("823915374285394")
            .setNamespace("logindevgas")
            .setPermissions(permissions)
            .build();

    Profile.Properties properties = new Profile.Properties.Builder()
            .add(Properties.ID)
            .add(Properties.FIRST_NAME)
            .add(Properties.COVER)
            .add(Properties.WORK)
            .add(Properties.EDUCATION)
            .add(Properties.PICTURE)
            .build();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);


        SimpleFacebook.setConfiguration(configuration);
        simplef = SimpleFacebook.getInstance(this);

        mButtonLogin = (Button) findViewById(R.id.entrar);
        mButtonLogout = (Button) findViewById(R.id.salir);
        mTextStatus = (TextView) findViewById(R.id.mostrar);




        setLogin();
        setLogout();
        setUIState();

    }


    @Override
    public void onResume() {
        super.onResume();
        setTitle("Regalo Perfecto");
        simplef = SimpleFacebook.getInstance(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        simplef.onActivityResult(this, requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void setLogin() {
        // Login listener
        final OnLoginListener onLoginListener = new OnLoginListener() {

            @Override
            public void onFail(String reason) {
                mTextStatus.setText(reason);
                Log.w(TAG, "Failed to login");
            }

            @Override
            public void onException(Throwable throwable) {
                mTextStatus.setText("Exception: " + throwable.getMessage());
                Log.e(TAG, "Bad thing happened", throwable);
            }

            @Override
            public void onThinking() {
                // show progress bar or something to the user while login is
                // happening
                mTextStatus.setText("Thinking...");
            }

            @Override
            public void onLogin() {
                // change the state of the button or do whatever you want
                mTextStatus.setText("Logged in");
                loggedInUIState();
            }

            @Override
            public void onNotAcceptingPermissions(Permission.Type type) {
                //				toast(String.format("You didn't accept %s permissions", type.name()));
            }
        };


        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                simplef.login(onLoginListener);


            }
        });
    }

    /**
     * Logout example
     */
    private void setLogout() {
        final OnLogoutListener onLogoutListener = new OnLogoutListener() {

            @Override
            public void onFail(String reason) {
                mTextStatus.setText(reason);
                Log.w(TAG, "Failed to login");
            }

            @Override
            public void onException(Throwable throwable) {
                mTextStatus.setText("Exception: " + throwable.getMessage());
                Log.e(TAG, "Bad thing happened", throwable);
            }



            @Override
            public void onThinking() {
                // show progress bar or something to the user while login is
                // happening
                mTextStatus.setText("Thinking...");
            }

            @Override
            public void onLogout() {
                // change the state of the button or do whatever you want
                mTextStatus.setText("Logged out");
                loggedOutUIState();
            }

        };

        mButtonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                simplef.logout(onLogoutListener);
            }
        });
    }

    private void setUIState() {
        if (simplef.isLogin()) {
            loggedInUIState();
        }
        else {
            loggedOutUIState();
        }
    }

    private void loggedInUIState() {
        mButtonLogin.setEnabled(false);
        mButtonLogout.setEnabled(true);

        mTextStatus.setText("Logged in");
        oListener();


    }

    private void loggedOutUIState() {
        mButtonLogin.setEnabled(true);
        mButtonLogout.setEnabled(false);

        mTextStatus.setText("Logged out");

    }


    public OnProfileListener oListener(){



        OnProfileListener onProfileListener = new OnProfileListener() {
            @Override

            public void onComplete(Profile profile) {
                Log.i(TAG, "Mi nombre = " + profile.getFirstName() + "Birthday" + profile.getBirthday());
                Toast.makeText(MyActivity.this, "Mi nombre: " + profile.getFirstName() + " " + "Cumpleaños: " + profile.getBirthday(), Toast.LENGTH_LONG).show();
            }

		    /*
		     * You can override other methods here:
		     * onThinking(), onFail(String reason), onException(Throwable throwable)
		     */
        };

        simplef.getProfile(onProfileListener);

        return onProfileListener;

    }




}

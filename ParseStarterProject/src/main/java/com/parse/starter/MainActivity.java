/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity {

    Boolean loginModeActive = false;

    Button b1;
    private ProgressDialog progress;
    public void redirectIfLoggedIn() {

        if (ParseUser.getCurrentUser() != null) {

            Intent intent = new Intent(getApplicationContext(), loading.class);
            startActivity(intent);

        }


    }


    public void toggleLoginMode(View view) {

        Button loginSignupButton = (Button) findViewById(R.id.loginSignupButton);

        TextView toggleLoginModeTextView = (TextView) findViewById(R.id.toggleLoginModeTextView);

        if (loginModeActive) {

            loginModeActive = false;
            loginSignupButton.setText("Sign Up");
            toggleLoginModeTextView.setText("Or, log in");


        } else {

            loginModeActive = true;
            loginSignupButton.setText("Log In");
            toggleLoginModeTextView.setText("Or, sign up");

        }

    }

  public void signupLogin(View view) {

      EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);

      EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);

      if (loginModeActive) {

          ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
              @Override
              public void done(ParseUser user, ParseException e) {

                  if (e == null) {

                      Log.i("Info", "user logged in");

                      redirectIfLoggedIn();

                  } else {

                      String message = e.getMessage();

                      if (message.toLowerCase().contains("java")) {

                          message = e.getMessage().substring(e.getMessage().indexOf(" "));

                      }

                      Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

                  }

              }
          });


      } else {

          ParseUser user = new ParseUser();

          user.setUsername(usernameEditText.getText().toString());

          user.setPassword(passwordEditText.getText().toString());

          user.signUpInBackground(new SignUpCallback() {
              @Override
              public void done(ParseException e) {

                  if (e == null) {

                      Log.i("Info", "user signed up");

                      redirectIfLoggedIn();

                  } else {

                      String message = e.getMessage();

                      if (message.toLowerCase().contains("java")) {

                          message = e.getMessage().substring(e.getMessage().indexOf(" "));

                      }

                      Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                  }

              }
          });

      }

  }


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

      //exit method
      Button btn= (Button)findViewById(R.id.Exit);
      btn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              finish();
              System.exit(0);
          }
      });


      setTitle("AK Login");

      redirectIfLoggedIn();
    
    ParseAnalytics.trackAppOpenedInBackground(getIntent());

      NotificationCompat.Builder builder =
              (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                      .setSmallIcon(R.drawable.apps)
                      .setContentTitle("Welcome To MooDf")
                      .setContentText("Continue Free chatting....")


                    ;


      Intent notificationIntent = new Intent(this, MainActivity.class);
      PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(),1,notificationIntent,0);

      builder.setContentIntent(contentIntent);

      // Add as notification
      NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
      manager.notify(0, builder.build());


  }

}
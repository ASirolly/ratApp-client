package com.example.paramount.ratappandroid;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button login;
    private Button registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = (Button) findViewById(R.id.button_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Login.class);
                startActivity(intent);
            }
        });

        registration = (Button) findViewById(R.id.button_registration);
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Registration.class);
                startActivity(intent);
            }
        });
    }

    public Button getLoginButton() {
        return login;
    }

    public Button getRegistrationButton() {
        return registration;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.button_login) {
//            System.out.println("login + asdf");
//            navigateUpTo(new Intent(this, Login.class));
//            return true;
//        } else {
//            System.out.println("registration + asdf");
//            navigateUpTo(new Intent(this, Registration.class));
//        return super.onOptionsItemSelected(item);
//        }
//    }
}

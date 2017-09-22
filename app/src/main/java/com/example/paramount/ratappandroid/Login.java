package com.example.paramount.ratappandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by joshuareno on 9/21/17.
 */

public class Login extends AppCompatActivity {

    private Button submit;
    private Button cancel;
    private String username;
    private String password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cancel = (Button) findViewById(R.id.cancelButtonLogin);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        submit = (Button) findViewById(R.id.submitButtonLogin);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = (findViewById(R.id.usernameEditTextLogin)).toString();
                password = (findViewById(R.id.passwordEditTextLogin)).toString();
                System.out.println(username);
                System.out.println(password);
            }
        });
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

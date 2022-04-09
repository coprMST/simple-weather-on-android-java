package com.example.myweather;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class auth extends AppCompatActivity {

    EditText login;
    EditText password;
    Button button;
    String[][] accounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        login=findViewById(R.id.username);
        password=findViewById(R.id.password);
        button=findViewById(R.id.login);

        login.addTextChangedListener(cityWatcher);
        password.addTextChangedListener(cityWatcher);

        accounts = new String[][]{{"77lm@mail.ru", "admin"}, {"user@gmail.com", "user"}};

        button.setEnabled(login.getText().toString().contains(".") && login.getText().toString().contains("@") && password.getText().toString().length() > 0);
    }

    private final TextWatcher cityWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            button.setEnabled(login.getText().toString().contains(".") && login.getText().toString().contains("@") && password.getText().toString().length() > 0);
        }
    };

    public void goToOpen(View view) {

        boolean log = false;
        boolean pas = false;

        for (String[] account : accounts)
        {
            if (login.getText().toString().trim().equals(account[0])) {
                log = true;

                if (password.getText().toString().trim().equals(account[1]))
                    pas = true;
            }
        }

        if (!log) {
            notEmail();
        }
        else if (!pas) {
            notPassword();
        }
        else {
            button.setEnabled(false);
            password.setText(null);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    void notPassword() {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Введен неверный пароль",
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        password.setText(null);
    }

    void notEmail() {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Введена неверная почта",
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        login.setText(null);
        password.setText(null);
    }
}
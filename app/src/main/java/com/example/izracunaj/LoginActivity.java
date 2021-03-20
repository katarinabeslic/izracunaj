package com.example.izracunaj;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.izracunaj.SQL.DatabaseBroker;
import com.example.izracunaj.SQL.Korisnik;

public class LoginActivity extends AppCompatActivity {

    private Switch tema;
    private EditText username, password;
    public static DatabaseBroker dbb;

    SharedPreferences sharedPreferences;

    private static final String MYPREFERENCES = "nightModePrefs";
    private static final String KEY_ISNIGHTMODE = "isNightMode";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        getSupportActionBar().hide();
        dbb = new DatabaseBroker(this);
        sharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);

        tema = findViewById(R.id.swTema);
        username = findViewById(R.id.etUsername);
        password = findViewById(R.id.etPassword);

        checkNightModeActivated();

        tema.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (tema.isChecked()){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    saveNightModeState(true);
                    recreate();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    saveNightModeState(false);
                    recreate();
                }
            }
        });
    }

    private void saveNightModeState(boolean b) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_ISNIGHTMODE, b);
        editor.apply();
    }

    private void checkNightModeActivated() {
        if (sharedPreferences.getBoolean(KEY_ISNIGHTMODE, false)){
            tema.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            tema.setChecked(false);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public void prijaviKorisnika(View view) {
        if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
            Toast.makeText(this, "Sva polja moraju biti popunjena!", Toast.LENGTH_SHORT).show();
            return;
        }

        Korisnik korisnik = null;

        korisnik = dbb.prijaviKorisnika(username.getText().toString(), password.getText().toString());

        if (korisnik == null) {
            Toast.makeText(this, "Korisnik sa unetim podacima ne postoji!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(LoginActivity.this, StartGameActivity.class);
        intent.putExtra("korisnik", korisnik.getUsername());
        startActivity(intent);
        finish();

        System.out.println(korisnik);
    }

    public void idiNaRegistraciju(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish(); //?
    }
}

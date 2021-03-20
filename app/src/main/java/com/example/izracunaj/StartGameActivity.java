package com.example.izracunaj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StartGameActivity extends AppCompatActivity {

    private TextView etIme;
    private String usernameKorisnika;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        getSupportActionBar().hide();
        usernameKorisnika = getIntent().getStringExtra("korisnik");

        etIme = findViewById(R.id.etIme);
        etIme.setText(usernameKorisnika);
    }

    public void playGame(View view) {
        Intent intent = new Intent(StartGameActivity.this, PlayGameActivity.class);
        intent.putExtra("ime", usernameKorisnika);
        startActivity(intent);
        finish();
    }
}

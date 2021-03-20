package com.example.izracunaj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.izracunaj.SQL.DatabaseBroker;
import com.example.izracunaj.SQL.Korisnik;

public class RegisterActivity extends AppCompatActivity {

    private EditText imePrezime, email, username, lozinka;
    private DatabaseBroker dbb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        getSupportActionBar().hide();
        dbb = new DatabaseBroker(this);

        imePrezime = findViewById(R.id.etName);
        email = findViewById(R.id.etEmail);
        username = findViewById(R.id.etUsername);
        lozinka = findViewById(R.id.etPassword);
    }

    public void idiNaLogin(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void registracijaKorisnika(View view) {
        if (imePrezime.getText().toString().isEmpty() || email.getText().toString().isEmpty() ||
                username.getText().toString().isEmpty() || lozinka.getText().toString().isEmpty()) {
            Toast.makeText(this, "Sva polja moraju biti popunjena!", Toast.LENGTH_SHORT).show();
            return;
        }

        Korisnik korisnik = new Korisnik();
        korisnik.setImePrezime(imePrezime.getText().toString());
        korisnik.setEmail(email.getText().toString());
        korisnik.setUsername(username.getText().toString());
        korisnik.setPassword(lozinka.getText().toString());

        boolean uspesno = dbb.registrujKorisnika(korisnik);

        if (uspesno){
            Toast.makeText(this, "Uspešna registracija!", Toast.LENGTH_SHORT).show();
            resetuj();
        } else {
            Toast.makeText(this, "Nespešna registracija!", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetuj() {
        imePrezime.setText("");
        email.setText("");
        username.setText("");
        lozinka.setText("");
    }
}

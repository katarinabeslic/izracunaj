package com.example.izracunaj;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.izracunaj.SQL.DatabaseBroker;
import com.example.izracunaj.SQL.Rezultat;

import java.util.List;

public class GameOverActivity extends AppCompatActivity {
    ImageView ivHighScore;
    String ime;
    TextView tvRedniBroj, tvKorisnik, tvRezultat;
    TextView rbVrednost, korisnikVrednost, rezultatVrednost;
    Button btnResetuj;
    DatabaseBroker dbb;
    TableLayout tableLayout;
    TableRow tableRow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);
        getSupportActionBar().hide();
        dbb = new DatabaseBroker(this);

        int points = getIntent().getExtras().getInt("rezultat");
        ime = getIntent().getExtras().getString("ime");

        ivHighScore = findViewById(R.id.ivHighScore);
        tvRedniBroj = findViewById(R.id.tvRedniBroj);
        tvKorisnik = findViewById(R.id.tvKorisnik);
        tvRezultat = findViewById(R.id.tvRezultat);
        tableLayout = findViewById(R.id.t1);
        tableRow = findViewById(R.id.tr);
        btnResetuj = findViewById(R.id.btnReset);

        dbb.sacuvajRezultat(ime, points);
        int pointsDB = dbb.vratiMaxRez();
        if (points > pointsDB){
            ivHighScore.setVisibility(View.VISIBLE);
            System.out.println(points + ""+ pointsDB+"");
        }
        System.out.println("punjenje tabele");
        popuniTabelu();

        btnResetuj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obrisiSveIzTabele();
            }
        });
    }

    private void obrisiSveIzTabele() {
        boolean uspesno = dbb.deleteAll();
        if (uspesno)
            Toast.makeText(this, "Rezultati uspesno resetovani", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Rezultati nisu obrisani", Toast.LENGTH_SHORT).show();
    }


    private void popuniTabelu() {
        List<Rezultat> rezultati = dbb.vratiRezultate();
        for (int i = 0; i < rezultati.size(); i++) {
            System.out.println(rezultati);
            tableRow = new TableRow(this);
            rbVrednost = new TextView(this);
            rbVrednost.setText(""+ (i+1));
            rbVrednost.setTextSize(25);
            rbVrednost.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            rbVrednost.setPadding(40,40,40,40);


            korisnikVrednost = new TextView(this);
            korisnikVrednost.setText(rezultati.get(i).getKorisnik());
            korisnikVrednost.setTextSize(25);
            korisnikVrednost.setPadding(40,40,40,40);

            rezultatVrednost = new TextView(this);
            rezultatVrednost.setText(rezultati.get(i).getRezultat()+"");
            rezultatVrednost.setTextSize(25);
            rezultatVrednost.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            rezultatVrednost.setPadding(40,40,40,40);

            tableRow.addView(rbVrednost);
            tableRow.addView(korisnikVrednost);
            tableRow.addView(rezultatVrednost);
            tableLayout.addView(tableRow);

            //srediti da sve ono bude isto kao u layout-u
        }
    }

    public void restart(View view) {
        Intent intent = new Intent(GameOverActivity.this, PlayGameActivity.class);
        intent.putExtra("ime", ime);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    public void exit(View view) {
        createNotificationChannel();
        Intent intent = new Intent(GameOverActivity.this, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(GameOverActivity.this, 0, intent,0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        long time = System.currentTimeMillis();
        long twentySeconds = 1000*20;

        alarmManager.set(AlarmManager.RTC_WAKEUP, time + twentySeconds, pendingIntent);

        Toast.makeText(this, ime + " , hvala što ste igrali!", Toast.LENGTH_SHORT).show();
        onDestroy();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "playAgainChannel";
            String description = "Channel for reminder to play again";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("playAgain", name, importance);
            channel.setDescription(description);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}

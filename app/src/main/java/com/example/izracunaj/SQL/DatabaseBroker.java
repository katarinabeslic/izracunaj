package com.example.izracunaj.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DatabaseBroker extends SQLiteOpenHelper {

    private static final String DB_NAME = "izracunajDB.db";
    private static final int DB_VERSION = 1;

    public DatabaseBroker(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //Poziva se kada se baza podataka kreira prvi put.
    //Ukoliko baza već postoji sa istim imenom ova metoda neće biti pozvana.
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL upit za kreiranje tabele.
        String sql = "CREATE TABLE korisnik ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "imePrezime TEXT, "+
                "email TEXT," +
                "username TEXT," +
                "password TEXT)";

        // Izvršavanje upita.


        String sql2 = "CREATE TABLE rezultat ( " +
                "rb INTEGER PRIMARY KEY AUTOINCREMENT," +
                "korisnik TEXT, " +
                "brBodova INTEGER)";

        db.execSQL(sql2);
        db.execSQL(sql);
    }

    //Poziva se kada je potreban upgrade baze.
    //Ova metoda će biti pozvana samo ukoliko baza podataka već postoji sa istim imenom,
    //ali verzija baze je drugačija od verzije koja već postoji.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS korisnik");
        db.execSQL("DROP TABLE IF EXISTS rezultat");

        this.onCreate(db);
    }

    public boolean registrujKorisnika(Korisnik korisnik){
        boolean uspesno = false;
        // Referenca ka bazi podataka.
        SQLiteDatabase db = this.getWritableDatabase(); //Kreiranje i/ili otvaranje baze za upisivanje i čitanje.

        // ContentValues je objekat koji nam pomaže prilikom kreiranja upita
        // za unos i izmenu podataka u tabeli.
        ContentValues values = new ContentValues();
        values.put("imePrezime", korisnik.getImePrezime());
        values.put("email", korisnik.getEmail());
        values.put("username", korisnik.getUsername());
        values.put("password", korisnik.getPassword());


        /* Upit glasi:
            INSERT INTO korisnik (imePrezime, email, username, password)
            VALUES("imeprezime","email", "username", "password")
            Ne unosi se ID jer je to polje autoincrement.
        */
        try {
            db.insert("korisnik", // tabela
                    null, // indikator da li je neka vrednost null
                    values); // Objekat tipa ContentValues

            // Zatvaranje konekcije.
            db.close();
            uspesno = true;
        } catch (Exception e){
            e.printStackTrace();
            uspesno = false;
        }
        return uspesno;
    }

    public Korisnik dajKorisnika(int id){

        // Referenca ka bazi podataka.
        SQLiteDatabase db = this.getReadableDatabase();
        // Niz naziva kolona.
        String[] COLUMNS = {"id", "imePrezime", "email", "username", "password"};

        //Metoda query izvršava upit i vraća Cursor, kojim se krećemo kroz rezultat.
        //Upit glasi: SELECT * FROM studenti WHERE id=4 (ili koji god broj da prosleđen metodi)
        Cursor cursor =
                db.query("studenti", // naziv tabele
                        COLUMNS, // imena kolona koje izvlačimo
                        " id = ?", // uslov u WHERE, svaki ? se spaja sa sledećim parametrom
                        new String[] { String.valueOf(id) }, // argumenti selekcije
                        null, // argumenti za GROUP BY
                        null, // argumenti za HAVING
                        null, // argumenti za ORDER BY
                        null); // argumenti za LIMIT

        // Cursor na početku pokazuje na zaglavlje tabele.
        if (cursor != null)
            // Pokazivač se pomera na prvi red u tabeli
            cursor.moveToFirst();

        // Iz cursora se uzimaju podaci o studentu.
        Korisnik korisnik = new Korisnik();
        korisnik.setId(cursor.getInt(0));
        korisnik.setImePrezime(cursor.getString(1));
        korisnik.setEmail(cursor.getString(2));
        korisnik.setUsername(cursor.getString(3));
        korisnik.setPassword(cursor.getString(4));

        return korisnik;
    }


    public List<Korisnik> dajSveKorisnike() {
        List<Korisnik> korisnici = new LinkedList<Korisnik>();

        // Upit koji treba da bude izvršen nad bazom.
        String query = "SELECT  * FROM korisnik";

        // Referenca ka bazi podataka.
        SQLiteDatabase db = this.getReadableDatabase();
        // Metodom rawQuery se izvršava upit nad bazom. Metoda vraća Cursor.
        Cursor cursor = db.rawQuery(query, null);

        Korisnik korisnik = null;
        //Cursor na početku pokazuje na zaglavlje tabele.
        //Metodom moveToNext() pomera se pokazivač na prvi sledeći red u tabeli.
        while (cursor.moveToNext()) {
            korisnik = new Korisnik ();
            // Formiranje studenta.
            korisnik.setId(cursor.getInt(0));
            korisnik.setImePrezime(cursor.getString(1));
            korisnik.setEmail(cursor.getString(2));
            korisnik.setUsername(cursor.getString(3));
            korisnik.setPassword(cursor.getString(4));

            // Dodavanje studenta u listu.
            korisnici.add(korisnik);
        }

        return korisnici;
    }


    public int updateKorisnik(Korisnik korisnik) {
        // Referenca ka bazi podataka.
        SQLiteDatabase db = this.getWritableDatabase();

        // ContentValues je objekat koji nam pomaže prilikom kreiranja upita
        // za unos i izmenu podataka u tabeli.
        ContentValues values = new ContentValues();
        values.put("imePrezime", korisnik.getImePrezime());
        values.put("email", korisnik.getEmail());
        values.put("username", korisnik.getUsername());
        values.put("password", korisnik.getPassword());


        /*Upit glasi:
        UPDATE studenti
        SET brojIndeksa = "brInd" AND imePrezime = "ime prezime"
        WHERE id = 5 (ili koji god broj da prosleđen metodi)
        Metoda update vraća celobrojnu vrednost koja ukazuje da li je upit uspešno izvršen.
        */
        int i = db.update("korisnik", //tabela
                values, // vrednosti kolona
                "id = ?", // uslov u WHERE
                new String[] { String.valueOf(korisnik.getId()) }); //svaki ? u whereClause se
        // spaja sa odgovarajućpm vrednošću iz niza
        db.close();

        return i;
    }

    public void deleteKorisnika(String korisnickoIme) {
        // Referenca ka bazi podataka.
        SQLiteDatabase db = this.getWritableDatabase();

        String upit = "DELETE FROM korisnik WHERE korisnickoIme ='" + korisnickoIme + "'";

        // Izvršavanja upita.
        db.execSQL(upit);

        // Zatvaranje konekcije.
        db.close();
    }

    public boolean deleteAll() {
        // Referenca ka bazi podataka.
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            String upit = "DELETE FROM rezultat";

            // Izvršavanja upita.
            db.execSQL(upit);

            //Zatvaranje konekcije.
            db.close();
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Korisnik prijaviKorisnika(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        String upit = "select * from korisnik where username = '" +username+ "' and password = '"+password+"' limit 1";
        Cursor cursor = db.rawQuery(upit, null);

        Korisnik k = null;
        while (cursor.moveToNext()){
            k = new Korisnik();
            k.setId(cursor.getInt(0));
            k.setImePrezime(cursor.getString(1));
            k.setEmail(cursor.getString(2));
            k.setUsername(cursor.getString(3));
            k.setPassword(cursor.getString(4));
        }
        return k;
    }

    public void sacuvajRezultat(String ime, int points) {
        SQLiteDatabase db = this.getWritableDatabase();

        // ContentValues je objekat koji nam pomaže prilikom kreiranja upita
        // za unos i izmenu podataka u tabeli.
        ContentValues values = new ContentValues();
        values.put("korisnik", ime);
        values.put("brBodova", points);

        db.insert("rezultat", null, values);
        db.close();
    }

    public int vratiMaxRez() {
        int rez = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        String upit = "select max(brBodova) from rezultat";
        Cursor cursor = db.rawQuery(upit, null);

        if (cursor.moveToNext()) {
            rez = cursor.getInt(0);
        }

        // Zatvaranje konekcije.
        db.close();
        return rez;
    }

    public List<Rezultat> vratiRezultate() {
        List<Rezultat> rezultati = new ArrayList<>();

        // Upit koji treba da bude izvršen nad bazom.
        String query = "SELECT  korisnik, brBodova FROM rezultat order by brBodova desc";

        // Referenca ka bazi podataka.
        SQLiteDatabase db = this.getReadableDatabase();
        // Metodom rawQuery se izvršava upit nad bazom. Metoda vraća Cursor.
        Cursor cursor = db.rawQuery(query, null);

        Rezultat rezultat = null;
        //Cursor na početku pokazuje na zaglavlje tabele.
        //Metodom moveToNext() pomera se pokazivač na prvi sledeći red u tabeli.
        while (cursor.moveToNext()) {
            rezultat = new Rezultat ();
            // Formiranje studenta.
            rezultat.setKorisnik(cursor.getString(0));
            rezultat.setRezultat(cursor.getInt(1));

            rezultati.add(rezultat);
        }
        System.out.println(rezultati);
        return rezultati;
    }
}

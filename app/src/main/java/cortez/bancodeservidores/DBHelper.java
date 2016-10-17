package cortez.bancodeservidores;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Usuario on 15/10/2016.
 */

public class DBHelper  extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context,"MyDatabase", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                "userTable " +
                "(username VARCHAR(30), password BINARY(32), id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, email VARCHAR(20),name VARCHAR(40),photo BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older users table if existed
        db.execSQL("DROP TABLE IF EXISTS tabela");
        // create fresh users table
        this.onCreate(db);
    }
    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        try{
            values.put("username", user.getUsername());
            values.put("password", user.getSenha());
            values.put("photo", user.getFoto());
            db.insert("userTable",null,values);
            db.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public List<User> searchFor(String row, String word, String table) {
        List<User> users = new LinkedList<User>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT * " + " FROM " + table + " WHERE " + row + " =?", new String[]{word});
            User user = null;
            if (cursor.moveToFirst()) {
                do {
                    user = new User();
                    user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                    user.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    user.setSenha(cursor.getString(cursor.getColumnIndex("password")));
                    user.setFoto(cursor.getBlob(cursor.getColumnIndex("photo")));
                    users.add(user);
                } while (cursor.moveToNext());
            }
            Log.d("searchForUsers()", users.toString());
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return users;
    }

    public List<User> getAllUsers(){

        List<User> users = new LinkedList<User>();

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT  * FROM userTable", null);

            // 3. go over each row, build user and add it to list
            User user = null;
            if (cursor.moveToFirst()) {
                do {
                    user = new User();
                    user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                    user.setSenha(cursor.getString(cursor.getColumnIndex("password")));
                    user.setId(cursor.getInt(cursor.getColumnIndex("id")));

                    // Add book to books
                    users.add(user);
                } while (cursor.moveToNext());
            }

            Log.d("getAllUsers()", users.toString());
            db.close();
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }

        // return users list
        return users;

    }

    public void eraseDb(){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DROP TABLE IF EXISTS tabela");
            db.execSQL("DROP TABLE IF EXISTS userTable");
            db.execSQL("CREATE TABLE IF NOT EXISTS " +
                    "userTable " +
                    "(username VARCHAR(30), password BINARY(32), id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, email VARCHAR(20),name VARCHAR(40),photo BLOB)");

            User u = new User();
            u.setUsername("victorsou");
            u.setSenha(Security.encrypt("123"));
            this.addUser(u);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}

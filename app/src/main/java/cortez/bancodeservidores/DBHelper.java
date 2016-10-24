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
                "usersTable " +
                "(uid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " first_name VARCHAR(10), last_name VARCHAR(10), username VARCHAR(30), password BINARY(32),  email VARCHAR(20), date_joined DATE,adm_status BOOLEAN, photo BLOB)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                "serviceProvidersTable " +
                "(sid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "uid INTEGER NOT NULL, " +
                "first_name VARCHAR(10), " +
                "last_name VARCHAR(10), " +
                "average DOUBLE, " +
                "date_added DATE, " +
                "FOREIGN KEY (uid) REFERENCES usersTable(uid)" +
                ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                "categoriesTable " +
                "(category VARCHAR(10), " +
                "sid INTEGER, " +
                "FOREIGN KEY (sid) REFERENCES serviceProvidersTable(sid), " +
                "PRIMARY KEY (sid,category)" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older users table if existed
        db.execSQL("DROP TABLE IF EXISTS tabela");
        // create fresh users table
        this.onCreate(db);
    }
    public void addUser(User user,String table){
        SQLiteDatabase db = this.getWritableDatabase();
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        try{
            values.put("username", user.getUsername());
            values.put("password", user.getSenha());
            values.put("photo", user.getFoto());
            db.insert(table,null,values);
            db.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void addServiceProvider(ServiceProvider sp, User u){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try{
            values.put("first_name",sp.getFirst_name());
            //passando a chave estrangeira(id do user q o adicionou)
            values.put("uid",u.getId());
            db.insert("serviceProvidersTable",null,values);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void addCategory(String cat, ServiceProvider sp){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try{
            values.put("category",cat);
            values.put("sid","1");
            db.insert("categoriesTable",null,values);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public List<User> searchFor(String row, String word, String table) {
        List<User> users = new LinkedList<User>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT * " + " FROM " + table + " WHERE " + row + " =?", new String[]{word});
                    if (cursor.moveToFirst()) {
                        do {
                            User user = new User();
                            user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                            user.setId(cursor.getInt(cursor.getColumnIndex("uid")));
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


    public LinkedList<ServiceProvider> searchForSp(String row, String word, String table) {
        LinkedList<ServiceProvider> sps = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT * " + " FROM " + table + " WHERE " + row + " =?", new String[]{word});
            if (cursor.moveToFirst()) {
                do {
                    ServiceProvider sp = new ServiceProvider();
                    sp.setFirst_name(cursor.getString(cursor.getColumnIndex("first_name")));
                    //sp.setCategory(cursor.getString(cursor.getColumnIndex("category")));
                    sps.add(sp);
                } while (cursor.moveToNext());
            }
            Log.d("searchForUsers()", sps.toString());
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return sps;
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
            db.execSQL("DROP TABLE IF EXISTS serviceProvidersTable");
            db.execSQL("DROP TABLE IF EXISTS usersTable");
            db.execSQL("DROP TABLE IF EXISTS categoriesTable");
            db.execSQL("CREATE TABLE IF NOT EXISTS " +
                    "usersTable " +
                    "(uid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " first_name VARCHAR(10), " +
                    "last_name VARCHAR(10), " +
                    "username VARCHAR(30), " +
                    "password BINARY(32), " +
                    "email VARCHAR(20), " +
                    "date_joined DATE, " +
                    "adm_status BOOLEAN, " +
                    "photo BLOB" +
                    ")");

            db.execSQL("CREATE TABLE IF NOT EXISTS " +
                    "serviceProvidersTable " +
                    "(sid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "uid INTEGER NOT NULL, " +
                    "first_name VARCHAR(10), " +
                    "last_name VARCHAR(10), " +
                    "average DOUBLE, " +
                    "date_added DATE, " +
                    "FOREIGN KEY (uid) REFERENCES usersTable(uid)" +
                    ")");
            db.execSQL("CREATE TABLE IF NOT EXISTS " +
                    "categoriesTable " +
                    "(category VARCHAR(10), " +
                    "sid INTEGER, " +
                    "FOREIGN KEY (sid) REFERENCES serviceProvidersTable(sid), " +
                    "PRIMARY KEY (sid,category)" +
                    ")");
            User u = new User();
            u.setUsername("victorsou");
            u.setSenha(Security.encrypt("123"));
            this.addUser(u,"usersTable");
            this.addCategory("Pedreiro",new ServiceProvider());
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

}

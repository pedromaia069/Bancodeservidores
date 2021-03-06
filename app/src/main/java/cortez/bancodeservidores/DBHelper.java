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
                "(" +
                "category VARCHAR(10), " +
                "sid INTEGER, " +
                "FOREIGN KEY (sid) REFERENCES serviceProvidersTable(sid), " +
                "PRIMARY KEY (sid,category)" +
                ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                "notasTable" +
                "(" +
                "nota DOUBLE, " +
                "uid INTEGER, " +
                "sid INTEGER, " +
                "FOREIGN KEY (sid) REFERENCES serviceProvidersTable(sid), " +
                "FOREIGN KEY (uid) REFERENCES usersTable(uid), " +
                "PRIMARY KEY (sid,uid)" +
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
            values.put("first_name",user.getFirst_name());
            values.put("last_name",user.getLast_name());
            values.put("email",user.getEmail());
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
        ContentValues values2 = new ContentValues();
        ContentValues values3 = new ContentValues();

        try{
            values.put("first_name",sp.getFirst_name());
            values.put("last_name",sp.getLast_name());
            //passando a chave estrangeira(id do user q o adicionou)
            values.put("uid",u.getId());
            //add and get the primary key of the line added
            long lastSpId = db.insert("serviceProvidersTable",null,values);

            // for each category of the sp,
            for(String category : sp.getCategory()) {
                values2.put("category", category);
                values2.put("sid",lastSpId);
                db.insert("categoriesTable",null,values2);
            }

            //nota
            values3.put("nota",sp.getNota());
            values3.put("uid",u.getId());
            values3.put("sid",lastSpId);
            db.insert("notasTable",null,values3);

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
                            user.setFirst_name(cursor.getString(cursor.getColumnIndex("first_name")));
                            user.setLast_name(cursor.getString(cursor.getColumnIndex("last_name")));
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

    public List<ServiceProvider> searchForSp(int sid){
        LinkedList<ServiceProvider> sps = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT usersTable.first_name AS userFirstName, usersTable.last_name AS userLastName, " +
                    "serviceProvidersTable.first_name AS first_name, serviceProvidersTable.last_name AS last_name, serviceProvidersTable.sid AS sid, " +
                    "categoriesTable.category AS category, " +
                    "ROUND(avg(notasTable.nota),1) AS avarage " +
                    "FROM serviceProvidersTable " +
                    "INNER JOIN usersTable, categoriesTable, notasTable " +
                    "ON serviceProvidersTable.uid = usersTable.uid " +
                    "AND serviceProvidersTable.sid = categoriesTable.sid " +
                    "AND serviceProvidersTable.sid = notasTable.sid " +
                    "WHERE serviceProvidersTable.sid = " + sid + " " +
                    "GROUP BY serviceProvidersTable.sid ", null);
            if (cursor.moveToFirst()) {
                do {
                    ServiceProvider sp = new ServiceProvider();
                    sp.setFirst_name(cursor.getString(cursor.getColumnIndex("first_name")));
                    sp.setLast_name(cursor.getString(cursor.getColumnIndex("last_name")));
                    sp.setUserFirst_name(cursor.getString(cursor.getColumnIndex("userFirstName")));
                    sp.setUserLast_name(cursor.getString(cursor.getColumnIndex("userLastName")));
                    sp.setSid(cursor.getInt(cursor.getColumnIndex("sid")));
                    sp.setAvarage(Double.parseDouble(cursor.getString(cursor.getColumnIndex("avarage"))));
                    LinkedList<String> categories = new LinkedList<>();
                    categories.add(cursor.getString(cursor.getColumnIndex("category")));
                    sp.setCategory(categories);
                    sps.addFirst(sp);
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


    public LinkedList<ServiceProvider> searchForSpByCategory(String chosenCategory,String order) {
        if(order.equals("Nome")){
            order = "serviceProvidersTable.first_name";
        }else{
            order = "average DESC";
        }
        LinkedList<ServiceProvider> sps = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT usersTable.first_name AS userFirstName, usersTable.last_name AS userLastName, " +
                    "serviceProvidersTable.first_name AS first_name, serviceProvidersTable.last_name AS last_name, serviceProvidersTable.sid AS sid, " +
                    "categoriesTable.category AS category, " +
                    "ROUND(avg(notasTable.nota),1) AS average " +
                    "FROM serviceProvidersTable " +
                    "INNER JOIN usersTable, categoriesTable, notasTable " +
                    "ON serviceProvidersTable.uid = usersTable.uid " +
                    "AND serviceProvidersTable.sid = categoriesTable.sid " +
                    "AND serviceProvidersTable.sid = notasTable.sid " +
                    "AND categoriesTable.category = '" + chosenCategory +"' "+
                    "GROUP BY serviceProvidersTable.sid " +
                    "ORDER BY " + order, null);
            if (cursor.moveToFirst()) {
                do {
                    ServiceProvider sp = new ServiceProvider();
                    sp.setFirst_name(cursor.getString(cursor.getColumnIndex("first_name")));
                    sp.setLast_name(cursor.getString(cursor.getColumnIndex("last_name")));
                    sp.setUserFirst_name(cursor.getString(cursor.getColumnIndex("userFirstName")));
                    sp.setUserLast_name(cursor.getString(cursor.getColumnIndex("userLastName")));
                    sp.setSid(cursor.getInt(cursor.getColumnIndex("sid")));
                    sp.setAvarage(Double.parseDouble(cursor.getString(cursor.getColumnIndex("average"))));
                    LinkedList<String> categories = new LinkedList<>();
                    categories.add(cursor.getString(cursor.getColumnIndex("category")));
                    sp.setCategory(categories);
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

    public LinkedList<ServiceProvider> getAllServiceProviders(String order){
        if(order.equals("Nome")){
            order = "first_name";
        }else{
            order = "average DESC";
        }
        LinkedList<ServiceProvider> sps = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT usersTable.first_name AS userFirstName, usersTable.last_name AS userLastName, " +
                    "serviceProvidersTable.first_name AS first_name, serviceProvidersTable.last_name AS last_name, serviceProvidersTable.sid AS sid, " +
                    "categoriesTable.category AS category, " +
                    "ROUND(avg(notasTable.nota),1) AS average " +
                    "FROM serviceProvidersTable " +
                    "INNER JOIN usersTable, categoriesTable, notasTable " +
                    "ON serviceProvidersTable.uid = usersTable.uid " +
                    "AND serviceProvidersTable.sid = categoriesTable.sid " +
                    "AND serviceProvidersTable.sid = notasTable.sid " +
                    "GROUP BY serviceProvidersTable.sid " +
                    "ORDER BY " + order, null);

            //go over each row, build serviceprovider and add it to list
            if (cursor.moveToFirst()) {
                do {
                    ServiceProvider sp = new ServiceProvider();
                    sp.setFirst_name(cursor.getString(cursor.getColumnIndex("first_name")));
                    sp.setLast_name(cursor.getString(cursor.getColumnIndex("last_name")));
                    sp.setUserFirst_name(cursor.getString(cursor.getColumnIndex("userFirstName")));
                    sp.setUserLast_name(cursor.getString(cursor.getColumnIndex("userLastName")));
                    sp.setSid(cursor.getInt(cursor.getColumnIndex("sid")));
                    sp.setAvarage(Double.parseDouble(cursor.getString(cursor.getColumnIndex("average"))));
                    LinkedList<String> categories = new LinkedList<>();
                    categories.add(cursor.getString(cursor.getColumnIndex("category")));
                    sp.setCategory(categories);
                    sps.add(sp);
                } while (cursor.moveToNext());
            }

            Log.d("getAllUsers()", sps.toString());
            db.close();
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }

        return sps;
    }

    public void eraseDb(){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DROP TABLE IF EXISTS serviceProvidersTable");
            db.execSQL("DROP TABLE IF EXISTS usersTable");
            db.execSQL("DROP TABLE IF EXISTS categoriesTable");
            db.execSQL("DROP TABLE IF EXISTS notasTable");
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
            db.execSQL("CREATE TABLE IF NOT EXISTS " +
                    "notasTable" +
                    "(" +
                    "nota DOUBLE, " +
                    "uid INTEGER, " +
                    "sid INTEGER, " +
                    "FOREIGN KEY (sid) REFERENCES serviceProvidersTable(sid), " +
                    "FOREIGN KEY (uid) REFERENCES usersTable(uid), " +
                    "PRIMARY KEY (sid,uid)" +
                    ")");



            povoarComListaExemploEUserPadrao();


            //this.addCategory()
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    private void povoarComListaExemploEUserPadrao() {
        //USUARIO PADRAO
        User u = new User();
        u.setUsername("victorsou");
        u.setSenha(Security.encrypt("123"));
        u.setFirst_name("Victor");
        u.setLast_name("Cortez");
        u.setEmail("ctovictor@gmail.com");
        this.addUser(u,"usersTable");
        u=this.searchFor("first_name",u.getFirst_name(),"usersTable").get(0);

        ServiceProvider sp = new ServiceProvider();
        sp.setFirst_name("Babalu");
        sp.setLast_name("Doparaguai");
        sp.setUserFirst_name(u.getFirst_name());
        sp.setUserLast_name(u.getLast_name());
        List<String> l = new LinkedList<>();
        l.add("Pedreiro");
        l.add("Marceneiro");
        sp.setCategory(l);
        sp.setNota(8);
        this.addServiceProvider(sp,u);

        sp.setFirst_name("Arnaldo");
        sp.setLast_name("Ruim");
        l.clear();
        l.add("Encanador");
        l.add("Pedreiro");
        sp.setCategory(l);
        sp.setNota(2);
        this.addServiceProvider(sp,u);

        sp.setFirst_name("Thomas");
        sp.setLast_name("Volt");
        l.clear();
        l.add("Eletricista");
        sp.setCategory(l);
        sp.setNota(10);
        this.addServiceProvider(sp,u);
    }

}

//lixo

//    public void addCategory(String cat, ServiceProvider sp){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        try{
//            values.put("category",cat);
//            values.put("sid","1");
//            db.insert("categoriesTable",null,values);
//        }catch(SQLException e){
//            e.printStackTrace();
//        }
//    }
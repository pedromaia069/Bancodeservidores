package cortez.bancodeservidores;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //fields
    TextView usernameTextView;
    TextView passwordTextView;
    TextView listaTextView;
    Button loginButton;

    List<User> users = new LinkedList<User>();
    DBHelper db = new DBHelper(this);

    private SharedPreferences autoLoginPref;
    private SharedPreferences.Editor autoLoginPrefEditor;
    boolean autologin;

    String encryptedPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameTextView = (TextView) findViewById(R.id.userEditText);
        passwordTextView = (TextView) findViewById(R.id.senhaEditText);
        listaTextView = (TextView) findViewById(R.id.listaTextView);
        loginButton = (Button) findViewById(R.id.loginButton);

        autoLoginPref = getSharedPreferences("autoLogin",0);
        autoLoginPrefEditor = autoLoginPref.edit();


        db.eraseDb();

        //função auto-login
        autologin = autoLoginPref.getBoolean("autoLogin",false);
        //////// set to false due developing process
        //autologin = false;
        /////// end of corrective code

        if(autologin == true){
            usernameTextView.setText(autoLoginPref.getString("username",""));
            loginButton.performClick();
        }

    }


    public void onClick(View v){
        switch (v.getId()){

            case R.id.loginButton:
                //checando se a senha confere com o usuario
                users = db.searchFor("username",usernameTextView.getText().toString(),"usersTable");
                if(users.size() == 0){
                    Toast t = Toast.makeText(this, "Usuario nao encrontado", Toast.LENGTH_LONG);
                    t.show();
                }
                else {//usuario encontrado

                    //auto filling password if auto-login enabled
                    if(autologin == true) {
                        encryptedPassword = autoLoginPref.getString("password","");
                    }
                    else{//no auto login
                        encryptedPassword = Security.encrypt(passwordTextView.getText().toString());
                    }
                    if (users.get(0).getSenha().equals(encryptedPassword)) {

                        //setando auto login
                        autoLoginPrefEditor.putBoolean("autoLogin",true);
                        autoLoginPrefEditor.putString("username",usernameTextView.getText().toString());
                        autoLoginPrefEditor.putString("password",encryptedPassword);
                        autoLoginPrefEditor.commit();

                        Intent loginIntent = new Intent(this, MainMenu.class);
                        loginIntent.putExtra("username", usernameTextView.getText().toString());
                        startActivity(loginIntent);
                    } else {
                        Toast t = Toast.makeText(this, "Usuario ou senha incorretos", Toast.LENGTH_LONG);
                        t.show();
                    }
                }
                break;

            case(R.id.showTableButton):
                List<User> users = db.getAllUsers();
                for (int i=0;i<users.size();i++) {
                    System.out.println(users.get(i).getUsername());
                }
                Toast t = Toast.makeText(this, "mostrando tabela", Toast.LENGTH_LONG);
                t.show();
                listaTextView.setText("");
                for (int i=0;i<users.size();i++) {
                    listaTextView.setText(listaTextView.getText().toString() + "\n"
                            + "usuario: " + users.get(i).getUsername() + "\n" + "senha: " + users.get(i).getSenha() + "\n");
                }
                break;
        }
    }
}


package cortez.bancodeservidores;

import android.content.Intent;
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

    List<User> users = new LinkedList<User>();
    DBHelper db = new DBHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameTextView = (TextView) findViewById(R.id.userEditText);
        passwordTextView = (TextView) findViewById(R.id.senhaEditText);
        listaTextView = (TextView) findViewById(R.id.listaTextView);

        db.eraseDb();

    }
    public void onClick(View v){
        switch (v.getId()){

            case R.id.loginButton:
                //checando se a senha confere com o usuario
                users = db.searchFor("username",usernameTextView.getText().toString(),"userTable");
                if(users.size() == 0){
                    Toast t = Toast.makeText(this, "Usuario nao encrontado", Toast.LENGTH_LONG);
                    t.show();
                }
                else {
                    if (users.get(0).getSenha().equals(Security.encrypt(passwordTextView.getText().toString()))) {
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


package cortez.bancodeservidores;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import static java.security.AccessController.getContext;

public class MainMenu extends AppCompatActivity {

    String username;
    User user;
    List<User> users = new LinkedList<User>();

    DBHelper db = new DBHelper(this);




    private static final int REQUEST_CODE_ADD_SERVICE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //pegando usuario que fez o login
        Bundle extra = getIntent().getExtras();
        username = extra.getString("username");
        users = db.searchFor("username",username,"usersTable");
        //usuario que estamos lidando foi selecionado
        user = users.get(0);



        //ADICIONAR SERVIDOR
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_menu_add));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Prestador de serviços adicionado com sucesso", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Intent addServiceIntent = new Intent(getApplicationContext(),AddServiceActivity.class);

                startActivityForResult(addServiceIntent, REQUEST_CODE_ADD_SERVICE);
            }
        });
    }

    //MENU SUPERIOR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_menu, menu);
        setTitle(user.getUsername());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_search:
                Toast t = Toast.makeText(this, "Usuario nao encrontado", Toast.LENGTH_LONG);
                t.show();
                return true;
            case R.id.action_settings:
                t = Toast.makeText(this, "Usuario nao ", Toast.LENGTH_LONG);
                t.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            //if we are here, everything processed successfully
            if(requestCode == REQUEST_CODE_ADD_SERVICE){
                //Toast t = Toast.makeText(this, "Prestador de serviços adicionado com sucesso", Toast.LENGTH_LONG);
                //t.show();

            }
        }
    }

}

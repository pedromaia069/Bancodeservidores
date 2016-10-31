package cortez.bancodeservidores;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

public class AddServiceActivity extends AppCompatActivity {

    //fields
    ServiceProvider serviceProvider;
    DBHelper db = new DBHelper(this);
    User user;
    List<User> users = new LinkedList<User>();


    private SharedPreferences autoLoginPref;
    private SharedPreferences.Editor autoLoginPrefEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        //PEGANDO USUARIO LOGADO
        autoLoginPref = getSharedPreferences("autoLogin",0);
        autoLoginPrefEditor = autoLoginPref.edit();
        users = db.searchFor("username",autoLoginPref.getString("username",""),"UsersTable");
        user = users.get(0); //user atual

        //IMPLEMENTAR VARIAVEIS DE XML

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_menu, menu);
        setTitle("");
        return true;
    }


    public void onClick(View v) {
        serviceProvider = new ServiceProvider();

        switch (v.getId()) {

            case R.id.buttonAddService:

                //IMPLEMENTAR GET NOS VIEWS E SALVAR EM SERVICEPROVIDER (ISAAC)

                serviceProvider.setFirst_name("Zé");
                serviceProvider.setLast_name("josé");
                List<String> l = new LinkedList<>();
                l.add("Pedreiro");
                serviceProvider.setCategory(l);
                serviceProvider.setUid(user.getId());
                serviceProvider.setUserFirst_name(user.getFirst_name());
                serviceProvider.setUserLast_name(user.getLast_name());
                db.addServiceProvider(serviceProvider,user);
                Intent returnServiceCreated = new Intent();
                setResult(RESULT_OK, returnServiceCreated);
                finish();
                break;

        }



    }
}

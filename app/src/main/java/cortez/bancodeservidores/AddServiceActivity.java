package cortez.bancodeservidores;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

public class AddServiceActivity extends AppCompatActivity {

    //fields
    ServiceProvider serviceProvider;
    DBHelper db = new DBHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

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

                //IMPLEMENTAR GET NOS VIEWS E SALVAR EM SERVICEPROVIDER

                serviceProvider.setFirst_name("babalu");
                db.addServiceProvider(serviceProvider);
                Intent returnServiceCreated = new Intent();
                setResult(RESULT_OK, returnServiceCreated);
                finish();
                break;

        }



    }
}

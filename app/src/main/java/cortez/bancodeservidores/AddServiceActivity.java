package cortez.bancodeservidores;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

public class AddServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_menu, menu);
        setTitle("");
        return true;
    }


    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.buttonAddService:

                break;

        }
        Intent returnServiceCreated = new Intent();
        setResult(RESULT_OK, returnServiceCreated);
        finish();


    }
}

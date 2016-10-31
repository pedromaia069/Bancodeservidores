package cortez.bancodeservidores;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class ServiceProviderProfilePage extends AppCompatActivity {

    ServiceProvider selectedSp;
    List<ServiceProvider> serviceProviders = new ArrayList<ServiceProvider>();
    ServiceProvider serviceProvider;

    DBHelper db = new DBHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_profile_page);

        Bundle extra = getIntent().getExtras();
        int sid = Integer.parseInt(extra.getString("sid"));
        serviceProviders = db.searchForSp(sid);
        serviceProvider = serviceProviders.get(0);

    }
}

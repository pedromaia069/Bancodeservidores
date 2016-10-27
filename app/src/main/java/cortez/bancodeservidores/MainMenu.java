package cortez.bancodeservidores;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.security.AccessController.getContext;

public class MainMenu extends AppCompatActivity {

    String username;
    User user;
    List<User> users = new LinkedList<User>();
    List<ServiceProvider> serviceProviders = new ArrayList<ServiceProvider>();

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
                Intent addServiceIntent = new Intent(getApplicationContext(),AddServiceActivity.class);
                addServiceIntent.putExtra("username",user.getUsername());
                startActivityForResult(addServiceIntent, REQUEST_CODE_ADD_SERVICE);
                Snackbar.make(view, "Prestador de serviços adicionado com sucesso", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
    public boolean onOptionsItemSelected(MenuItem item) { //MENU CLICADO
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.action_search:
                populateServiceProviderListView();
                enableListViewClickListener();

                return true;

            case R.id.action_settings:
                Toast t = Toast.makeText(this, "Usuario nao ", Toast.LENGTH_LONG);
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


    //cria um listview com todos os servidores cadastrados
    private void populateServiceProviderListView() {

        //CREATE LIST OF ITEMS
        serviceProviders = db.getAllServiceProviders();

        //BUILD ADAPTER
        ArrayAdapter<ServiceProvider> adapter = new MyCustomListAdapter();

        //CONFIGURE LIST VIEW
        ListView listView = (ListView) findViewById(R.id.listViewServiceProvider);

        //MAKING HEADER FOR LISTVIEW
        ViewGroup headerView = (ViewGroup) getLayoutInflater().inflate(R.layout.service_provider_list_view_header,listView,false);

        //PASSING ADAPTER IN LISTVIEW
        if(listView.getHeaderViewsCount() == 0){ //checando se header ja existe
            listView.addHeaderView(headerView);
        }
        listView.setAdapter(adapter);
    }

    private void enableListViewClickListener() {
        ListView listView = (ListView)  findViewById(R.id.listViewServiceProvider);

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View viewClicked, int position, long id) {
                        ServiceProvider clickedSp = serviceProviders.get(position);
                        Toast.makeText(getBaseContext(),"you clicked position " + position + " which is servicer " + clickedSp.getFirst_name() + ". he's awesome.",Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }


    //custom adapter for my listview layout
    private class MyCustomListAdapter extends ArrayAdapter<ServiceProvider> {
        public MyCustomListAdapter(){
            //acessing elements of base class, making same thing as it was a simple listview, and receiving the listview layout to work with
            super(getBaseContext(),R.layout.service_provider_list_view,serviceProviders);
        }


        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //if convertview is null,itemview has to be created now
            View itemView = convertView;
            //itemView é a view que usamos para popular as views do listview

            if(itemView == null){
                //we have to create a view!
                itemView = getLayoutInflater().inflate(R.layout.service_provider_list_view, parent, false);
            }

            ServiceProvider currentSp = serviceProviders.get(position);

            //fill the view
            TextView nameTextView = (TextView) itemView.findViewById(R.id.item_textViewFirstName);
            TextView notaTextView = (TextView) itemView.findViewById(R.id.item_textViewNota);
            TextView addedByTextView = (TextView) itemView.findViewById(R.id.item_TextViewAddedBy);

            nameTextView.setText(currentSp.getFirst_name() + " " + currentSp.getLast_name());
            addedByTextView.setText(currentSp.getUserFirst_name() + " " + currentSp.getUserLast_name());

            return itemView;
        }
    }
}

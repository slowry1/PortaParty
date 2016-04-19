package rodney.portaparty;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rodney on 4/3/2016.
 */
public class MainActivity extends AppCompatActivity{
    private Firebase firebase;
    private Button addItemButton;
    private EditText itemEditText;
    private ListView listView;
    private Button createPartyButton;
    private String username;
    private CheckBox checkBox;
    private CustomListViewAdapter customListViewAdapter;
    private ArrayList<HashMap<String,String>> arrayList;
    private Button goToLoginButton;
    private Button clearButton;
    private SharedPreferences sharedPreferences;
    private HashMap<String, String> data ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Firebase.setAndroidContext(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        firebase = new Firebase("https://portaparty.firebaseio.com/");
        addItemButton = (Button) findViewById(R.id.addItemButton);
        itemEditText = (EditText) findViewById(R.id.itemEditText);
        listView = (ListView) findViewById(R.id.item_list);
        createPartyButton = (Button) findViewById(R.id.createPartyButton);
        checkBox = (CheckBox) findViewById(R.id.userCheckbox);
        sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        Log.i("BACON", sharedPreferences.getAll().toString());

//SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        if(sharedPreferences.getString("username","NONE!") == "NONE!"){
            username = sharedPreferences.getString("username", "NONE!");
            Log.i("BACON", username+" Inside MA IF");
        }else{
            createPartyButton.setEnabled(false);
            addItemButton.setEnabled(true);
            username = sharedPreferences.getString("username", "NONE!");
            Log.i("BACON", username+" Inside MA ELSE");
            //arrayList = popCustomListViewAdapter();
        }

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item = String.valueOf(itemEditText.getText());
                Item itemObject = new Item(item,"");
                firebase.child("party/" + username +"/"+"item/"+item+"/").setValue(itemObject);
                arrayList = popCustomListViewAdapter();
                Toast.makeText(MainActivity.this, item+" added to the list!", Toast.LENGTH_SHORT).show();
            }
        });

        ////////////////////////////// THIS IS WHERE IT BREAKS TRYING TO FIND A CHECKBOX WHEN THEY ARE IN A LIST VIEW I THINK
/*        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "CheckBox", Toast.LENGTH_SHORT).show();

            }
        });*/
        /*checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            }
        });*/
    }
    private ArrayList<HashMap<String,String>> popCustomListViewAdapter() {
        listView = (ListView) findViewById(R.id.item_list);
        arrayList = new ArrayList<>();
        firebase.child("party/"+username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot photoSnapshot : dataSnapshot.child("item").getChildren()){
                        data = new HashMap<>();
                        data.put("item", photoSnapshot.child("item").getValue().toString());
                        data.put("member", photoSnapshot.child("member").getValue().toString());
                        arrayList.add(data);
                    Log.i("BACON", photoSnapshot.child("item").getValue().toString());
                    //Setup adapter
                    customListViewAdapter = new CustomListViewAdapter(getApplicationContext(), arrayList);
                    listView.setAdapter(customListViewAdapter);
                }
//to add to git


            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });
        return arrayList;
    }

    @Override
    protected void onResume(){
        super.onResume();
      /*  Intent intent = getIntent();
        username = intent.getStringExtra("username");
        sharedPreferences.edit().putString("username", username).apply();*/
        arrayList = popCustomListViewAdapter();
        Log.i("BACON", username+" Inside MA ONRESUME");

        //SharedPreferences sharedPreferences = getSharedPreferences("userInfo",Context.MODE_PRIVATE);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_login) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        if (id == R.id.action_logout) {
            firebase.unauth();
//SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onPrepareOptionsMenu(Menu menu)
    {
        sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        MenuItem login = menu.findItem(R.id.action_login);
        if(sharedPreferences.contains("username")) {
            login.setVisible(false);
        } else {
            login.setVisible(true);
        }
        MenuItem logout = menu.findItem(R.id.action_logout);
        if(sharedPreferences.contains("username")) {
            logout.setVisible(true);
        } else {
            logout.setVisible(false);
        }
        return true;
    }
    

}

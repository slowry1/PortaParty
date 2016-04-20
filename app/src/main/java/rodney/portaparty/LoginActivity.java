package rodney.portaparty;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class LoginActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Button loginButton;
    private Button createButton;
    private Firebase firebase;
    private EditText emailEditText;
    private EditText passwordEditText;
    private String email, uid,username;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        if(sharedPreferences.contains("username") && sharedPreferences.getString("username","NONE!") != "NONE!"){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }

        setContentView(R.layout.login_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Firebase.setAndroidContext(this);
        firebase = new Firebase("https://portaparty.firebaseio.com/");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.login);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.login_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);

        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                firebase.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        Log.i("TEST", "Success: " + authData);
                        Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                        email = (String) authData.getProviderData().get("email");
                        uid = authData.getUid();
                        firebase.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                for (DataSnapshot childSnap : dataSnapshot.getChildren()) {
                                    if (childSnap.child("email").getValue().toString().equals(email)) {
                                        username = childSnap.child("username").getValue().toString();
                                        String firstName = childSnap.child("firstName").getValue().toString();
                                        String lastName = childSnap.child("lastName").getValue().toString();
                                        String gender = childSnap.child("gender").getValue().toString();
                                        editor.putString("username", username);
                                        editor.putString("firstName", firstName);
                                        editor.putString("lastName", lastName);
                                        editor.putString("gender", gender);
                                        editor.putString("email", email);
                                        editor.putString("uid", uid);
                                        editor.commit();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("username", username);
                                        intent.putExtra("email", email);
                                        intent.putExtra("uid", uid);
                                        startActivity(intent);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        Log.e("TEST", "Failed: " + firebaseError);
                        Toast.makeText(LoginActivity.this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        createButton = (Button) findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.login);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.partiesItem) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.loginItem) {

        } else if (id == R.id.createAccountItem) {
            Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
            startActivity(intent);
        } else if (id == R.id.aboutItem) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.login);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

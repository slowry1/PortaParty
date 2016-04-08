package rodney.portaparty;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;

/**
 * Created by rodney on 4/3/2016.
 */
public class CreateAccountActivity extends AppCompatActivity {

    private Firebase firebase;
    private EditText usernameEditText;
    private RadioGroup buttonGroup;
    private RadioButton buttonSelected;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmEditText;
    private Button createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        Firebase.setAndroidContext(this);
        firebase = new Firebase("https://portaparty.firebaseio.com/");

        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        buttonGroup = (RadioGroup) findViewById(R.id.buttonGroup);
        firstNameEditText = (EditText) findViewById(R.id.firstNameEditText);
        lastNameEditText = (EditText) findViewById(R.id.lastNameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        confirmEditText = (EditText) findViewById(R.id.confirmEditText);

        createAccountButton = (Button) findViewById(R.id.createAccountButton);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String password2 = confirmEditText.getText().toString().trim();
                final String userName = usernameEditText.getText().toString().trim();
                final String firstName = firstNameEditText.getText().toString().trim();
                final String lastName = lastNameEditText.getText().toString().trim();
                ////////////////////////THIS NEEDS TO BE A RADIO BUTTON OR SOMETHING NOT SURE HOW TO GET STRING FROM CHOICE///////////////////
//                final String gender = genderRadio.getText().toString();
                ////////////////////////////////////////////////////////////////////////////////////////////////

                boolean usernameFree = checkIfNameFree(userName);

                if (!(password.equals(password2))) {
                    Toast.makeText(CreateAccountActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    if (usernameFree) {
                        firebase.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {

                            @Override
                            public void onSuccess(Map<String, Object> stringObjectMap) {
                                buttonGroup = (RadioGroup) findViewById(R.id.buttonGroup);
                                final int selectedId = buttonGroup.getCheckedRadioButtonId();
                                buttonSelected = (RadioButton) findViewById(selectedId);
                                final String gender = (String) buttonSelected.getText();
                                Toast.makeText(CreateAccountActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                String email = emailEditText.getText().toString().trim();
                                String password = passwordEditText.getText().toString().trim();
                                final String username = usernameEditText.getText().toString().trim();
                                final String firstName = firstNameEditText.getText().toString().trim();
                                final String lastName = lastNameEditText.getText().toString().trim();
                                ////////////////////////THIS NEEDS TO BE A RADIO BUTTON OR SOMETHING NOT SURE HOW TO GET STRING FROM CHOICE////////
//                                final String gender = genderRadio.getText().toString();
                                ////////////////////////////////////////////////////////////////////////////////////////////////
                                User user = new User(email, username, (String) stringObjectMap.get("uid"), firstName, lastName, gender);

                                firebase.child("user/" + user.getUsername()).setValue(user);
                                //After Register Auto Login then go to main page
                                firebase.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                                    @Override
                                    public void onAuthenticated(AuthData authData) {
                                        Log.i("TEST", "Success: " + authData);
                                        Toast.makeText(CreateAccountActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                        String email = (String) authData.getProviderData().get("email");
                                        String uid = authData.getUid();
                                        // Saves user info
                                        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putString("username", username);
                                        editor.putString("email", email);
                                        editor.putString("uid", uid);
                                        editor.putString("firstName", firstName);
                                        editor.putString("lastName", lastName);
                                        editor.putString("gender", gender);
                                        editor.apply();
                                        ////////////////////////////////THIS NEEDS TO CHANGE TO WHATEVER YOUR ACTIVITY ARE CALLED//////////////////////
                                        Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
                                        ////////////////////////////////////////
                                    /* NOT SURE IF WEE NEED THIS CANT REMEMEBR WHAT THIS IS FOR RIGHT NOW !!!!!!!!!!!!!!!!!!!
                                    intent.putExtra("email", email);
                                    intent.putExtra("uid", uid);
                                    */
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onAuthenticationError(FirebaseError firebaseError) {
                                        Log.e("TEST", "Failed: " + firebaseError);
                                        Toast.makeText(CreateAccountActivity.this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onError(FirebaseError firebaseError) {
                                Toast.makeText(CreateAccountActivity.this, "Registration Unsuccessfully", Toast.LENGTH_SHORT).show();
                                Toast.makeText(CreateAccountActivity.this, firebaseError.toString(), Toast.LENGTH_SHORT).show();
                                Log.e("TEST", "Failed to create the user. " + firebaseError);
                            }
                        });
                    }
                }// bleh
            }
        });
    }


    private Boolean checkIfNameFree(final String uName) {
        final Boolean[] uNameFree = {true};
        firebase.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(uName)) {
                    Toast.makeText(CreateAccountActivity.this, "Username Taken", Toast.LENGTH_SHORT).show();
                    uNameFree[0] = false;
                } else {
                    uNameFree[0] = true;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
        return uNameFree[0];
    }
}


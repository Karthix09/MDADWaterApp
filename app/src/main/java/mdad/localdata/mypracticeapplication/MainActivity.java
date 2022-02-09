package mdad.localdata.mypracticeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button logIn, signUp;
    EditText username, password;

    SharedPreferences preferences;

    //Variables to store retrieved user info from database
    int user_id;
    String user_username;
    String user_gender;
    int user_age;
    int user_weight;
    String user_wakeUpTime;
    String user_bedTime;
    int user_waterRequirement;
    int user_waterDrankPerc;
    String user_waterStatus;

    //Loading dialog
    final LoadingDialog loadingDialog = new LoadingDialog(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //finding buttons by Id
        logIn = findViewById(R.id.loginBtn);
        signUp = findViewById(R.id.signUpBtn);

        //Finding the editText field by Id
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        //Using Shared Preferences for persistent storage
        preferences = getSharedPreferences("UserAuthentication",MODE_PRIVATE);




        //OnClick Listener for Register button
        View.OnClickListener regListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get Input from text fields
                String userNameValue = username.getText().toString();
                String pwdValue = password.getText().toString();

                // Save values to preferences
                preferences.edit().putString("UserName", userNameValue).apply();
                preferences.edit().putString("Password", pwdValue).apply();

                //Moving to the next page
                Intent intent = new Intent(MainActivity.this, NewUser.class);
                startActivity(intent);
            }
        };


        // OnClick Listener for Login button
        View.OnClickListener loginListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get Input from text fields
                String userNameValue = username.getText().toString();
                String pwdValue = password.getText().toString();

                Log.d("Username", userNameValue);
                Log.d("Password", pwdValue);

//               Check if username and password fields are empty, else check if username and password match saved username and password in MYSQL database

                if (userNameValue.isEmpty() || pwdValue.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter your Username and Password", Toast.LENGTH_SHORT).show();
                }
                else {
                    //Creating JSON Object to send it in the POST request
                    JSONObject dataJson = new JSONObject();
                    try {
                        dataJson.put("Username",userNameValue);
                        dataJson.put("Password",pwdValue);
                    }
                    catch(JSONException e){

                    }
                    Log.d("JSON format credentials", String.valueOf(dataJson));

                    loadingDialog.startLoadingDialog();
                    loginCheckAPI(dataJson);
                }
//                    if (userNameValue.matches(userNameSaved) && pwdValue.matches(pwdSaved)){
//                        Toast.makeText(MainActivity.this, "Welcome " + userNameSaved, Toast.LENGTH_SHORT).show();
//
//                        Intent showDashboard = new Intent(MainActivity.this, Dashboard.class);
//                        startActivity(showDashboard);
//                    }
//                    else{
//                        Toast.makeText(MainActivity.this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
//                    }
//                }
            }
        };

        signUp.setOnClickListener(regListener);
        logIn.setOnClickListener(loginListener);

    }

    // Login Credential Check
    void loginCheckAPI(JSONObject dataJson) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://drinkup.atspace.cc/get_user_detailsJson.php";

        // Request a JSON RESPONSE from the provided URL
        JsonObjectRequest json_obj_req = new JsonObjectRequest(Request.Method.POST, url, dataJson, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){

                try {
                    //Get values from JSON array
                    int responseNum = response.getInt("success");

                    if (responseNum == 1) {
                        Toast.makeText(MainActivity.this, "Hey there " + dataJson.getString("Username"), Toast.LENGTH_SHORT).show();
                        Log.d("checking response", "onResponse: " + responseNum);

                        //Getting user JSON array from response
                        JSONArray userdata;
                        userdata = response.getJSONArray("user");
//                        Log.d("User JSONArray response", ""+ userdata);
                        for (int i = 0; i<userdata.length(); i++) {
                            JSONObject userObject = userdata.getJSONObject(i);
                            user_id = userObject.getInt("UserID");
                            user_username = userObject.getString("Username");
                            user_gender = userObject.getString("Gender");
                            user_age = userObject.getInt("Age");
                            user_weight= userObject.getInt("Weight");
                            user_wakeUpTime = userObject.getString("WakeUpTime");
                            user_bedTime = userObject.getString("SleepTime");
                            user_waterRequirement = userObject.getInt("WaterRequirement");
                            user_waterDrankPerc = userObject.getInt("WaterDrankPerc");
                            user_waterStatus = userObject.getString("WaterCompletion");

                        }

                        //Move to Dashboard
                        Intent nextPage = new Intent(MainActivity.this, Dashboard.class);

                        //Passing values to next Page
                        nextPage.putExtra("UserId", user_id);
                        nextPage.putExtra("Username", user_username);
                        nextPage.putExtra("Gender", user_gender);
                        nextPage.putExtra("Age", user_age);
                        nextPage.putExtra("Weight", user_weight);
                        nextPage.putExtra("WakeUpTime", user_wakeUpTime);
                        nextPage.putExtra("BedTime", user_bedTime);
                        nextPage.putExtra("WaterRequirement", user_waterRequirement);
                        nextPage.putExtra("WaterDrankPerc", user_waterDrankPerc);
                        nextPage.putExtra("WaterCompletionStatus", user_waterStatus);
                        loadingDialog.dismissDialog();
                        startActivity(nextPage);
                        finish(); // Kill activity
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Username or Password does not exit!", Toast.LENGTH_SHORT).show();
                        loadingDialog.dismissDialog();
                    }


                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                error.printStackTrace();
            }

        });

        requestQueue.add(json_obj_req);
    }
}
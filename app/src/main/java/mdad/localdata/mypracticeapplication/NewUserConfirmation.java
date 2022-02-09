package mdad.localdata.mypracticeapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArraySet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.CellSignalStrengthGsm;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewUserConfirmation extends AppCompatActivity {

    Button finalConfirmBtn;
    ListView listView;

    SharedPreferences preferences;

    //Variable to store the water requirement
    int user_waterRequirement;

    String userName;
    String password;
    String gender;
    int userAge;
    int userWeight;
    String userBedTime;
    String userWakeUpTime;
    int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_confirmation);

        finalConfirmBtn = findViewById(R.id.confirm_Button2);

        listView = findViewById(R.id.listView);

        //Getting shared preferences to display the data for registration
        preferences = getSharedPreferences("UserAuthentication",MODE_PRIVATE);

        userName = preferences.getString("UserName","UNKNOWN_VALUE");
        password = preferences.getString("Password","UNKNOWN_VALUE");
        gender = preferences.getString("Gender","UNKNOWN_VALUE");
        userAge = preferences.getInt("UserAge",0);
        userWeight = preferences.getInt("UserWeight",0);
        userBedTime = preferences.getString("UserBedTime","");
        userWakeUpTime = preferences.getString("UserWakeUpTime","");

        WaterRequirementComputation wrc = new WaterRequirementComputation();
        user_waterRequirement = wrc.waterRequirement(userWeight);

        //Pop Up dialogue to show Water Requirement
        new AlertDialog.Builder(NewUserConfirmation.this)
                .setTitle("Water Intake Goal")
                .setMessage("Your daily Water Intake Goal is " + user_waterRequirement + "ml")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.btn_star_big_on)
                .show();

        //Creating and Array to display the Data for confirmation on the listView
        ArrayList<String> arrayList = new ArrayList<>();

        //Adding the data in the list
        arrayList.add("Your Username: " + userName);
        arrayList.add("Your Password: " + password);
        arrayList.add("Your Gender: " + gender);
        arrayList.add("Your Age: " + userAge);
        arrayList.add("Your Weight: " + userWeight);
        arrayList.add("Your Bed Time: " + userBedTime);
        arrayList.add("Your WakeUp Time: " + userWakeUpTime);
        arrayList.add("Your Daily Water Intake Goal: " + user_waterRequirement + "ml");


        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);

        listView.setAdapter(arrayAdapter);


        //OnClick for button to export data to database using Volley
        View.OnClickListener confirmBtnListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Logging out all Values
                Log.d("UserName", ""+ userName);
                Log.d("Password", ""+ password);
                Log.d("Gender", ""+ gender);
                Log.d("Age", ""+ userAge);
                Log.d("Weight", ""+ userWeight);
                Log.d("BedTime", ""+ userBedTime);
                Log.d("WakeUpTime", ""+ userWakeUpTime);
                Log.d("Water Intake Goal", ""+ user_waterRequirement);

                //Creating JSON Object to send it in the POST request
                JSONObject dataJson = new JSONObject();
                try {
                    dataJson.put("Username",userName);
                    dataJson.put("Password", password);
                    dataJson.put("Gender", gender);
                    dataJson.put("Age", userAge);
                    dataJson.put("Weight", userWeight);
                    dataJson.put("WakeUpTime", userWakeUpTime);
                    dataJson.put("SleepTime", userBedTime);
                    dataJson.put("WaterRequirement", user_waterRequirement);
                }
                catch(JSONException e){

                }

                //Alert Dialog for confirmation to POST data upon Confirmation
                AlertDialog alertDialog = new AlertDialog.Builder(NewUserConfirmation.this).create();
                alertDialog.setTitle("Confirmation");
                alertDialog.setMessage("Are you sure you would like to proceed ?");

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //POST REQUEST TO SAVE IN SQL DATABASE
                        postAPIRequest(dataJson);

                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        };

        finalConfirmBtn.setOnClickListener(confirmBtnListener);

    }

    //    POST API Request
    void postAPIRequest(JSONObject dataJson) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://drinkup.atspace.cc/create_userJson.php";


        // Request a JSON RESPONSE from the provided URL
        JsonObjectRequest json_obj_req = new JsonObjectRequest(Request.Method.POST, url, dataJson, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){

                Toast.makeText(NewUserConfirmation.this, "Your Details have been saved", Toast.LENGTH_SHORT).show();

                //Intent to go to MainActivity to Sign In
                Intent newPage = new Intent(NewUserConfirmation.this, MainActivity.class);
                // set the new task and clear flags
                newPage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(newPage);
            }

        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(NewUserConfirmation.this, "ERROR", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }

        });

        requestQueue.add(json_obj_req);
    }



//    void saveLocalStorage() {
//
//        SqliteDatabaseHelper databaseHelper = new SqliteDatabaseHelper(NewUserConfirmation.this);
//
//        Boolean checkInsert = databaseHelper.insertData(id, userName,password, gender, userAge, userWeight, userWakeUpTime, userBedTime);
//
//        if (checkInsert == true){
//            Toast.makeText(NewUserConfirmation.this, "Inserted to local DB", Toast.LENGTH_SHORT).show();
//        }
//        else {
//            Toast.makeText(NewUserConfirmation.this, "FAILED-local DB", Toast.LENGTH_SHORT).show();
//        }
//
//    }
}
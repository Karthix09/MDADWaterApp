package mdad.localdata.mypracticeapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.CellSignalStrengthGsm;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

public class NewUserConfirmation extends AppCompatActivity {

    Button finalConfirmBtn;

    SharedPreferences preferences;

    String userName;
    String password;
    String gender;
    int userAge;
    int userWeight;
    String userBedTime;
    String userWakeUpTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_confirmation);

        finalConfirmBtn = findViewById(R.id.confirm_Button2);

        //Getting shared preferences to display the data for registration
        preferences = getSharedPreferences("UserAuthentication",MODE_PRIVATE);

        userName = preferences.getString("UserName","UNKNOWN_VALUE");
        password = preferences.getString("Password","UNKNOWN_VALUE");
        gender = preferences.getString("Gender","UNKNOWN_VALUE");
        userAge = preferences.getInt("UserAge",0);
        userWeight = preferences.getInt("UserWeight",0);
        userBedTime = preferences.getString("UserBedTime","");
        userWakeUpTime = preferences.getString("UserWakeUpTime","");



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

                JSONObject dataJson = new JSONObject();
                try{
                    dataJson.put("Username",userName);
                    dataJson.put("Password", password);
                    dataJson.put("Gender", gender);
                    dataJson.put("Age", userAge);
                    dataJson.put("Weight", userWeight);
                    dataJson.put("WakeUpTime", userWakeUpTime);
                    dataJson.put("SleepTime", userBedTime);

                }
                catch(JSONException e){

                };

                postAPIRequest(dataJson);

            }
        };
        finalConfirmBtn.setOnClickListener(confirmBtnListener);

    }

    //    POST API Request
    void postAPIRequest(JSONObject dataJson) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://drinkup.atspace.cc/create_userJson.php";

////         Request a STRING RESPONSE from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Make a toast success message
//                        Toast.makeText(NewUserConfirmation.this, "Your data is saved", Toast.LENGTH_SHORT).show();
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
////                textView.setText("That didn't work!");
//                Toast.makeText(NewUserConfirmation.this, "ERROR", Toast.LENGTH_SHORT).show();
//            }
//        }) {
//            @NonNull
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError { // POST Method Sending data in body
//
//                HashMap<String, String> params = new HashMap();
//                params.put("Username", userName);
//                params.put("Password", password);
//                params.put("Gender", gender);
//                params.put("Age", String.format("", userAge));
//                params.put("Weight", String.format("", userWeight));
//                params.put("WakeUpTime", userWakeUpTime);
//                params.put("SleepTime", userBedTime);
//
////                JSONObject jo = new JSONObject(params);
//
//                return params;
//
//            }
////            @Override
////            public Map<String, String> getHeaders() throws AuthFailureError {
////                Map<String, String> params = new HashMap<String, String>();
////                params.put("Content-Type", "application/json");
////                return params;
////            }
//
//        };
//
//        // Add the request to the RequestQueue.
//        queue.add(stringRequest);


        // Request a JSON RESPONSE from the provided URL
        JsonObjectRequest json_obj_req = new JsonObjectRequest(
                Request.Method.POST,url,dataJson,new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                Toast.makeText(NewUserConfirmation.this, "Your Details have been saved", Toast.LENGTH_SHORT).show();
            }

        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(NewUserConfirmation.this, "ERROR", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }



        });
        requestQueue.add(json_obj_req);

    }



}
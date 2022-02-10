package mdad.localdata.mypracticeapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView usernameTxt;
    TextView waterRequirement;
    Button resetBtn;

    //Variables to store retrieved user info from DashboardActivity
    int user_id;
    String user_username;
    int user_waterRequirement;


    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.fragment_profile,container,false);

        usernameTxt = v.findViewById(R.id.username);
        waterRequirement = v.findViewById(R.id.waterGoal);
        resetBtn = v.findViewById(R.id.resetBtn);

        //Getting values from dashboard activity
        user_id = getArguments().getInt("UserId");
        user_username = getArguments().getString("Username");
        user_waterRequirement = getArguments().getInt("WaterRequirement");

        Log.d("Profile Fragment WQ", ""+user_waterRequirement);
        usernameTxt.setText("Hey, " + user_username);
        waterRequirement.setText("Your Goal:" + user_waterRequirement + "ml");

        View.OnClickListener resetProgressListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Alert Dialog for confirmation to Reset
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setTitle("Confirmation");
                alertDialog.setMessage("Are you sure ?");

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        JSONObject idJsonData = new JSONObject();
                        try {
                            idJsonData.put("UserID", user_id);
                        }
                        catch(JSONException e) {

                        }
                        postResetAPI(idJsonData);
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

        resetBtn.setOnClickListener(resetProgressListener);

        // Inflate the layout for this fragment
        return v;
    }

    public void postResetAPI(JSONObject jsonData) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String url = "http://drinkup.atspace.cc/update_waterDrankJson.php";


        // Request a JSON RESPONSE from the provided URL
        JsonObjectRequest json_obj_req = new JsonObjectRequest(Request.Method.PUT, url, jsonData, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                Log.d("Successful", "DATA UPDATED!");
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
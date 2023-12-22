package thukral.brooms.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import thukral.brooms.Database.LocalSharedPreferences;
import thukral.brooms.R;

public class Profile extends AppCompatActivity {
    Context context;
    ProgressDialog progressDialog;
    CircleImageView prof_img;
    EditText et_name, et_mob, et_email, et_gst_no, et_password;
    Button button_update;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = Profile.this;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);

        prof_img = findViewById(R.id.prof_img);
        et_name = findViewById(R.id.et_name);
        et_mob = findViewById(R.id.et_mob);
        et_email = findViewById(R.id.et_email);
        et_gst_no = findViewById(R.id.et_gst_no);
        et_password = findViewById(R.id.et_password);
        button_update = findViewById(R.id.button_update);

        CALL_API_Info();
        progressDialog.show();

        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CALL_API_UPDATE();
                progressDialog.show();
            }
        });


    }


    private void CALL_API_Info() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thukralbroom.com/api/User/all-users.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("data_info", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        et_name.setText(jsonObject1.getString("fullname"));
                        et_mob.setText(jsonObject1.getString("phone"));
                        et_email.setText(jsonObject1.getString("email"));

                        Glide.with(context).load(jsonObject1.getString("image"))
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.profile)
                                .dontAnimate().into(prof_img);

                        //     LocalSharedPreferences.savename(context,jsonObject1.getString("fullname"));
                        //      LocalSharedPreferences.saveUser_image(context,jsonObject1.getString("image"));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", LocalSharedPreferences.getUserid(context));
                Log.d("my_param_add", params.toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);


    }

    private void CALL_API_UPDATE() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thukralbroom.com/api/User/update-user.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("data_update", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("Error").equals("1")) {
                        Toast.makeText(context, "Information Updated.", Toast.LENGTH_SHORT).show();

                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i =0 ; i<jsonArray.length();i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            finish();
                            startActivity(getIntent());

                        }
                    }
                    else {
                        Toast.makeText(context, "Try Again", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", LocalSharedPreferences.getUserid(context));
                params.put("fullname", et_name.getText().toString());
                Log.d("my_param_add", params.toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);


    }

    @Override
    public void onBackPressed() {
        Intent i1 = new Intent(context, MainActivity.class);
        i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i1);
    }
}

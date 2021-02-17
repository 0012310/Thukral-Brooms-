package thukral.brooms.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import thukral.brooms.Database.LocalSharedPreferences;
import thukral.brooms.R;

public class LoginActivity extends AppCompatActivity {
    Context context;
    public static final String PREFS = "PREFS";
    EditText et_email, et_password;
    Button btnLogin;
    String login_id, password;
    TextView signup, forget;
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = LoginActivity.this;

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);

        sp = getApplicationContext().getSharedPreferences(PREFS, MODE_PRIVATE);
        edit = sp.edit();

        signup = findViewById(R.id.signup);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.login);
        forget = findViewById(R.id.forget);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (et_email.getText().toString().isEmpty()) {
                    et_email.setError("Please Fill Email Id");
                } else if (et_password.getText().toString().isEmpty()) {
                    et_password.setError("Please Fill Password");
                } else {

                    CallApi();
                    progressDialog.show();
                }


            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ForgetActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Intent intent = getIntent();
        String id = intent.getStringExtra("EMAIL");
        et_email.setText(id);

    }

    private void CallApi() {
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thukralbroom.com/api/User/login.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                Log.d("data_login", s);

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("Error").equals("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            progressDialog.dismiss();
                            LocalSharedPreferences.saveIsLogin(LoginActivity.this, true);
                            String ID = jsonObject1.getString("id");
                            LocalSharedPreferences.saveUserid(context, ID);
                            LocalSharedPreferences.saveUser_type(context, "" + jsonObject1.getString("usertype"));
                         //   Toast.makeText(context, ""+LocalSharedPreferences.getUser_tpe(context), Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);

                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Please Check user name & password", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(LoginActivity.this, "" + volleyError, Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", et_email.getText().toString());
                params.put("password", et_password.getText().toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

}
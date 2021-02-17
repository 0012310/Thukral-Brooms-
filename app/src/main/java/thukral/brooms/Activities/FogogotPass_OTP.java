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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import thukral.brooms.R;

public class FogogotPass_OTP extends AppCompatActivity {
    TextView txtTitle;
    ImageView imgBack;
    ProgressDialog progressDialog;
    Context context;
    EditText edit_email_id, edit_code, edit_passwod;
    Button btn_sub_send_code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fogogot_pass__o_t_p);
        context = FogogotPass_OTP.this;
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);




        edit_email_id = findViewById(R.id.edit_email_id);
        edit_code = findViewById(R.id.edit_code);
        edit_passwod = findViewById(R.id.edit_passwod);

        Bundle extras = getIntent().getExtras();
        String userName;

        if (extras != null) {
            userName = extras.getString("EMAIL");
            // and get whatever type user account id is
            edit_email_id.setText(userName);
            Log.d("ata", userName);
        }

        btn_sub_send_code = findViewById(R.id.btn_sub_send_code);
        btn_sub_send_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit_code.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Please enter complete verification code ", Toast.LENGTH_SHORT).show();
                } else if (edit_passwod.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Plese enter password ", Toast.LENGTH_SHORT).show();
                } else {
                    CHANGE_PASS_API();
                    progressDialog.show();
                }

            }
        });


    }

    private void CHANGE_PASS_API() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thukralbroom.com/api/User/change-password.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                // Toast.makeText(context, "" + response, Toast.LENGTH_SHORT).show();
                Log.d("Data_res", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("ok")) {

                        Toast.makeText(context, "Your Password has been changed successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, LoginActivity.class);
                        startActivity(intent);

                    } else if (jsonObject.getString("Error").equals("5")) {
                        edit_code.setError("Invalid OTP");
                        Toast.makeText(context, "OTP invalid", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Try again.", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(context, "" + error, Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", edit_code.getText().toString());
                params.put("password", edit_passwod.getText().toString());
                Log.d("params", params.toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }
}

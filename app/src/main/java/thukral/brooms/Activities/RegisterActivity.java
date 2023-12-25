package thukral.brooms.Activities;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import thukral.brooms.Database.LocalSharedPreferences;
import thukral.brooms.R;

public class RegisterActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    Context context;
    TextView text;

    EditText et_name, et_phone, et_email, et_password, et_aadhar, et_pan, et_shop_name, et_city, et_area, et_address, et_landmark, et_pincode;
    Spinner spinner_state, spinner_zone, spinner_distributor;
    Button bt_retailer, btn_distributor;

    Button btn_distributor_sub, btn_retailer_sub;
    String STATE_ID, ZONE_ID, DIST_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = RegisterActivity.this;
        text = findViewById(R.id.text);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        FindALL();
        ALL_STATE();

        btn_distributor_sub = findViewById(R.id.btn_distributor_sub);
        btn_distributor_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (et_name.getText().toString().isEmpty()) {
                    et_name.setError("Fill Name");
                    Toast.makeText(context, "Fill Name", Toast.LENGTH_SHORT).show();

                } else if (et_phone.getText().toString().isEmpty()) {
                    et_phone.setError("Fill Phone No");
                    Toast.makeText(context, "Fill Phone No", Toast.LENGTH_SHORT).show();

                } else if (et_email.getText().toString().isEmpty()) {
                    et_email.setText("Fill Email Id");
                    Toast.makeText(context, "Fill Email Id\"", Toast.LENGTH_SHORT).show();

                } else if (et_password.getText().toString().isEmpty()) {
                    et_password.setError("Fill Password");
                    Toast.makeText(context, "Fill Password", Toast.LENGTH_SHORT).show();

                } else if (spinner_state.getSelectedItem().toString().equals("Select State")) {
                    Toast.makeText(context, "Select State first", Toast.LENGTH_SHORT).show();
                } else {
                    CALL_REG_Distributor();
                    progressDialog.show();
                }

            }
        });


        btn_retailer_sub = findViewById(R.id.btn_retailer_sub);
        btn_retailer_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (et_name.getText().toString().isEmpty()) {
                    et_name.setError("Fill Name");
                    Toast.makeText(context, "Fill Name", Toast.LENGTH_SHORT).show();

                } else if (et_phone.getText().toString().isEmpty()) {
                    et_phone.setError("Fill Phone No");
                    Toast.makeText(context, "Fill Phone No", Toast.LENGTH_SHORT).show();

                } else if (et_email.getText().toString().isEmpty()) {
                    et_email.setError("Fill Email Id");
                    Toast.makeText(context, "Fill Email Id", Toast.LENGTH_SHORT).show();

                } else if (et_password.getText().toString().isEmpty()) {
                    et_password.setError("Fill Password");
                    Toast.makeText(context, "Fill Password", Toast.LENGTH_SHORT).show();

                } else if (spinner_state.getSelectedItem().toString().equals("Select State")) {
                    Toast.makeText(context, "Select State first", Toast.LENGTH_SHORT).show();
                } else {
                    CALL_REG_Retailer();
                    progressDialog.show();
                }


            }
        });

        bt_retailer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                text.setText("Retailer");
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    spinner_state.setVisibility(View.VISIBLE);
                    spinner_zone.setVisibility(View.VISIBLE);
                    spinner_distributor.setVisibility(View.VISIBLE);
                    btn_retailer_sub.setVisibility(View.VISIBLE);
                    btn_distributor_sub.setVisibility(View.GONE);
                }
                return false;
            }
        });
        btn_distributor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                text.setText("Distributor");
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    spinner_state.setVisibility(View.VISIBLE);
                    spinner_zone.setVisibility(View.VISIBLE);
                    spinner_distributor.setVisibility(View.GONE);
                    btn_retailer_sub.setVisibility(View.GONE);
                    btn_distributor_sub.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
    }

    private void CALL_REG_Distributor() {
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thukralbroom.com/api/User/signup.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                //    Toast.makeText(RegisterActivity.this, "" + s, Toast.LENGTH_SHORT).show();
                Log.d("data_Distributor", s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("message").equals("Successfully Signup!")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            intent.putExtra("EMAIL", "" + jsonObject1.getString("email"));
                            startActivity(intent);

                        }


                    } else if (jsonObject.getString("message").equals("Email Id already exists!")) {
                        Toast.makeText(context, "Check Email Id", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(context, "Try Again", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(RegisterActivity.this, "" + volleyError, Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("fullname", et_name.getText().toString());
                params.put("phone", et_phone.getText().toString());
                params.put("email", et_email.getText().toString());
                params.put("password", et_password.getText().toString());


                params.put("state", "" + STATE_ID);
                params.put("zone", "" + ZONE_ID);
                params.put("aadharno", et_aadhar.getText().toString());
                params.put("pan", et_pan.getText().toString());
                params.put("shopname", et_shop_name.getText().toString());
                params.put("city", et_city.getText().toString());
                params.put("area", et_area.getText().toString());

                params.put("address", et_address.getText().toString());
                params.put("pan", et_pan.getText().toString());
                params.put("pincode", et_pincode.getText().toString());
                params.put("usertype", "Distributor");
                params.put("area", et_area.getText().toString());
                Log.d("Distributor_value", params.toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    private void CALL_REG_Retailer() {
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thukralbroom.com/api/User/signup.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                //   Toast.makeText(RegisterActivity.this, "" + s, Toast.LENGTH_SHORT).show();
                Log.d("data", s);

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("message").equals("Successfully Signup!")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            intent.putExtra("EMAIL", "" + jsonObject1.getString("email"));
                            startActivity(intent);

                        }


                    } else if (jsonObject.getString("message").equals("Email Id already exists!")) {
                        Toast.makeText(context, "Check Email Id", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(context, "Try Again", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(RegisterActivity.this, "" + volleyError, Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("fullname", et_name.getText().toString());
                params.put("phone", et_phone.getText().toString());
                params.put("email", et_email.getText().toString());
                params.put("password", et_password.getText().toString());


                params.put("state", "" + STATE_ID);
                params.put("zone", "" + ZONE_ID);
                params.put("distributor", "" + DIST_ID);
                params.put("aadharno", et_aadhar.getText().toString());
                params.put("pan", et_pan.getText().toString());
                params.put("shopname", et_shop_name.getText().toString());
                params.put("city", et_city.getText().toString());
                params.put("area", et_area.getText().toString());
                params.put("address", et_address.getText().toString());
                params.put("pan", et_pan.getText().toString());
                params.put("pincode", et_pincode.getText().toString());
                params.put("usertype", "Retailer");
                params.put("area", et_area.getText().toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }


    ArrayList<String> stringArrayList_state = new ArrayList<>();
    ArrayList<String> stringArrayList_state_id = new ArrayList<>();

    private void ALL_STATE() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thukralbroom.com/api/all-state.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Data_State", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        stringArrayList_state.add(jsonObject1.getString("state"));
                        stringArrayList_state_id.add(jsonObject1.getString("id"));
                    }
                    ArrayAdapter arrayAdapter = new ArrayAdapter(RegisterActivity.this, android.R.layout.simple_spinner_item, stringArrayList_state);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                    spinner_state.setAdapter(arrayAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }

    ArrayList<String> stringArrayList_zone = new ArrayList<>();
    ArrayList<String> stringArrayList_zone_id = new ArrayList<>();
    ArrayList<String> stringArrayList_zone_state_id = new ArrayList<>();

    private void ALL_ZONE(final String ID) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thukralbroom.com/api/all-zone.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.d("Data_Zone", response);
                // Toast.makeText(context, "" + response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("Error").equals("1")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            stringArrayList_zone.add(jsonObject1.getString("zone"));
                            stringArrayList_zone_id.add(jsonObject1.getString("zone_id"));
                            stringArrayList_zone_state_id.add(jsonObject1.getString("state_id"));
                        }
                        ArrayAdapter arrayAdapter = new ArrayAdapter(RegisterActivity.this, android.R.layout.simple_spinner_item, stringArrayList_zone);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                        spinner_zone.setAdapter(arrayAdapter);

                    } else if (jsonObject.getString("Error").equals("0")) {
                        Toast.makeText(context, "No Zone", Toast.LENGTH_SHORT).show();
                        stringArrayList_zone.clear();
                        spinner_zone.setAdapter(null);
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
                params.put("state_id", "" + ID);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    ArrayList<String> stringArrayList_dist = new ArrayList<>();
    ArrayList<String> stringArrayList_dist_id = new ArrayList<>();

    private void ALL_DISS(final String zone, final String state) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thukralbroom.com/api/all-distributor.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Data_Diss", response);
                progressDialog.dismiss();
                // Toast.makeText(context, "" + response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("Error").equals("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            stringArrayList_dist.add(jsonObject1.getString("distributor"));
                            stringArrayList_dist_id.add(jsonObject1.getString("id"));
                        }
                        ArrayAdapter arrayAdapter = new ArrayAdapter(RegisterActivity.this, android.R.layout.simple_spinner_item, stringArrayList_dist);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                        spinner_distributor.setAdapter(arrayAdapter);

                    } else if (jsonObject.getString("Error").equals("0")) {
                        stringArrayList_dist.clear();
                        Toast.makeText(context, "No Distributor", Toast.LENGTH_SHORT).show();
                        spinner_distributor.setAdapter(null);

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
                params.put("state_id", "" + state);
                params.put("zone_id", "" + zone);
                Log.d("data", params.toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    private void FindALL() {
        bt_retailer = findViewById(R.id.bt_retailer);
        btn_distributor = findViewById(R.id.btn_distributor);

        spinner_state = findViewById(R.id.spinner_state);
        spinner_zone = findViewById(R.id.spinner_zone);
        spinner_distributor = findViewById(R.id.spinner_distributor);


        et_name = findViewById(R.id.et_name);
        et_phone = findViewById(R.id.et_phone);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_aadhar = findViewById(R.id.et_aadhar);
        et_pan = findViewById(R.id.et_pan);
        et_shop_name = findViewById(R.id.et_shop_name);
        et_city = findViewById(R.id.et_city);
        et_area = findViewById(R.id.et_area);
        et_address = findViewById(R.id.et_address);
        et_landmark = findViewById(R.id.et_landmark);
        et_pincode = findViewById(R.id.et_pincode);


        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                //    Toast.makeText(context, "" + stringArrayList_state.get(i), Toast.LENGTH_SHORT).show();
                //  Toast.makeText(context, "" + stringArrayList_state_id.get(i), Toast.LENGTH_SHORT).show();
                ALL_ZONE(stringArrayList_state_id.get(i));
                STATE_ID = stringArrayList_state_id.get(i);
                //  Toast.makeText(context, "" + STATE_ID, Toast.LENGTH_SHORT).show();
                progressDialog.show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(context, "Hii", Toast.LENGTH_SHORT).show();
            }
        });


        spinner_zone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                Toast.makeText(context, "" + stringArrayList_zone.get(i) + stringArrayList_zone_id.get(i) + stringArrayList_zone_state_id.get(i), Toast.LENGTH_SHORT).show();
                ALL_DISS(stringArrayList_zone_id.get(i), stringArrayList_zone_state_id.get(i));
                progressDialog.show();
                ZONE_ID = stringArrayList_zone_id.get(i);
                Toast.makeText(context, "" + ZONE_ID, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
            }
        });

        spinner_distributor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                Toast.makeText(context, "" + stringArrayList_dist, Toast.LENGTH_SHORT).show();
                DIST_ID = stringArrayList_dist_id.get(i);
                Toast.makeText(context, "" + DIST_ID, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
            }
        });

    }


}
package thukral.brooms.sabActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import thukral.brooms.Activities.CartList;
import thukral.brooms.Activities.MainActivity;
import thukral.brooms.Adapters.CartAdapter;
import thukral.brooms.Database.LocalSharedPreferences;
import thukral.brooms.Interface.CartRefresh;
import thukral.brooms.Interface.Img_addWish_Onclick;
import thukral.brooms.R;
import thukral.brooms.model.modelCartList;
import thukral.brooms.subAdapter.BrushesAdapter;
import thukral.brooms.subAdapter.SubtoSubDataAdapter;
import thukral.brooms.subModel.modelAllBrushes;
import thukral.brooms.subModel.modelsubtosubList;

public class SubToSubData extends AppCompatActivity {
    Context context;
    ProgressDialog progressDialog;
    TextView txtTitle;
    ImageView imgBack;
    RecyclerView recyclerView;
    ArrayList<modelsubtosubList> modelsubtosubListArrayList = new ArrayList<>();
    SubtoSubDataAdapter subtoSubDataAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_to_sub_data);
        context = SubToSubData.this;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        txtTitle = findViewById(R.id.txtTitle);
        imgBack = findViewById(R.id.imgBack);

        recyclerView = findViewById(R.id.recyclerView);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Intent intent = getIntent();
        String name = intent.getStringExtra("Data");
        if (name.equals("1")) {
            txtTitle.setText("Plastic Handel");
            Data(name);
            progressDialog.show();
        }
        else  if (name.equals("2")) {
            txtTitle.setText("Chrom Zink Pipe");
            Data(name);
            progressDialog.show();
        }

        else  if (name.equals("3")) {
            txtTitle.setText("Computer");
            Data(name);
            progressDialog.show();
        }

        else  if (name.equals("4")) {
            txtTitle.setText("Tin Pipe");
            Data(name);
            progressDialog.show();
        }

        else  if (name.equals("5")) {
            txtTitle.setText("Fiber Non-Dust Brooms");
            Data(name);
            progressDialog.show();
        }

        else  if (name.equals("6")) {
            txtTitle.setText("Plastic Pipe");
            Data(name);
            progressDialog.show();
        }


        else  if (name.equals("7")) {
            txtTitle.setText("Patti");
            Data(name);
            progressDialog.show();
        }
        else  if (name.equals("Bamboo Stick Brooms")) {
            txtTitle.setText("Bamboo Stick Brooms");
            Data_bamboo(name);
            progressDialog.show();
        }



    }

    private void Data(final String id) {
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thukralbroom.com/api/all-brooms.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.d("Data_All", response);
            //    Toast.makeText(context, "" + response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        modelsubtosubListArrayList.add(new modelsubtosubList(jsonObject1.getString("user_id"), jsonObject1.getString("user_type"), jsonObject1.getString("id"), jsonObject1.getString("name"),
                                jsonObject1.getString("cat_id"), jsonObject1.getString("sub_id"), jsonObject1.getString("sub_to_id"), jsonObject1.getString("category_name"),
                                jsonObject1.getString("sub_name"), jsonObject1.getString("sub_to_name"), jsonObject1.getString("description"), jsonObject1.getString("color"),
                                jsonObject1.getString("unit"), jsonObject1.getString("bag"), jsonObject1.getString("qty"), jsonObject1.getString("regular_price"),
                                jsonObject1.getString("sales_price"), jsonObject1.getString("image"), jsonObject1.getString("image1"), jsonObject1.getString("date_created"),
                                jsonObject1.getString("wishlist")));


                        GridLayoutManager manager = new GridLayoutManager(context, 2);
                        recyclerView.setLayoutManager(manager);

                        recyclerView.setAdapter(new SubtoSubDataAdapter(context, modelsubtosubListArrayList, new Img_addWish_Onclick() {
                            @Override
                            public void onClick() {

                            }
                        }));


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
                Map<String, String> parms = new HashMap<>();
                parms.put("user_id", "" + LocalSharedPreferences.getUserid(context));
                parms.put("sub_to_sub_id", ""+id);
                Log.d("data_params",parms.toString());

                return parms;
            }
        };
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void Data_bamboo(final String id) {
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thukralbroom.com/api/all-sub.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.d("Data_All_bamboo", response);
                //    Toast.makeText(context, "" + response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        modelsubtosubListArrayList.add(new modelsubtosubList(jsonObject1.getString("user_id"), jsonObject1.getString("user_type"), jsonObject1.getString("id"), jsonObject1.getString("name"),
                                jsonObject1.getString("cat_id"), jsonObject1.getString("sub_id"), jsonObject1.getString("sub_to_id"), jsonObject1.getString("category_name"),
                                jsonObject1.getString("sub_name"), jsonObject1.getString("sub_to_name"), jsonObject1.getString("description"), jsonObject1.getString("color"),
                                jsonObject1.getString("unit"), jsonObject1.getString("bag"), jsonObject1.getString("qty"), jsonObject1.getString("regular_price"),
                                jsonObject1.getString("sales_price"), jsonObject1.getString("image"), jsonObject1.getString("image1"), jsonObject1.getString("date_created"),
                                jsonObject1.getString("wishlist")));


                        GridLayoutManager manager = new GridLayoutManager(context, 2);
                        recyclerView.setLayoutManager(manager);

                        recyclerView.setAdapter(new SubtoSubDataAdapter(context, modelsubtosubListArrayList, new Img_addWish_Onclick() {
                            @Override
                            public void onClick() {

                            }
                        }));


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
                Map<String, String> parms = new HashMap<>();
                parms.put("user_id", "" + LocalSharedPreferences.getUserid(context));
                parms.put("subcategory", "1");
                Log.d("data_params_bambo",parms.toString());

                return parms;
            }
        };
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void myClick() {
    }
}
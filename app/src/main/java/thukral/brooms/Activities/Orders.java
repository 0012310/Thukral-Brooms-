package thukral.brooms.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import thukral.brooms.Adapters.OrderAdapter;
import thukral.brooms.Database.LocalSharedPreferences;
import thukral.brooms.R;
import thukral.brooms.model.modelOrder;

public class Orders extends AppCompatActivity {
    Context context;
    ProgressDialog progressDialog;
    TextView txtTitle;
    ImageView imgBack;
    RecyclerView recycler_view;
    LinearLayout liner_empaty;
    ArrayList<modelOrder> modelOrderArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        context = Orders.this;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("Orders");
        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(context, MainActivity.class);
                i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i1);
            }
        });
        liner_empaty = findViewById(R.id.liner_empaty);
        recycler_view = findViewById(R.id.recycler_view);
        CALL_API_ORDERS(LocalSharedPreferences.getUserid(context));
    }

    private void CALL_API_ORDERS(String ID) {
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thukralbroom.com/api/all-orders.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //   Toast.makeText(getContext(), ""+response, Toast.LENGTH_SHORT).show();
                Log.d("order_data", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("Error").equals("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                            modelOrderArrayList.add(new modelOrder(jsonObject1.getString("order_id"), jsonObject1.getString("txn_id"), jsonObject1.getString("order_code"),
                                    jsonObject1.getString("product_id"), jsonObject1.getString("quantity"), jsonObject1.getString("user_id"),
                                    jsonObject1.getString("distributor_name"), jsonObject1.getString("distributor_phone"), jsonObject1.getString("distributor_address"),
                                    jsonObject1.getString("distributor_city"), jsonObject1.getString("distributor_zone"), jsonObject1.getString("distributor_state"),
                                    jsonObject1.getString("name"), jsonObject1.getString("category_name"), jsonObject1.getString("sub_name"),
                                    jsonObject1.getString("sales_price"), jsonObject1.getString("Image"),
                                    jsonObject1.getString("total_price"), jsonObject1.getString("deliver_days"), jsonObject1.getString("payment_status"), jsonObject1.getString("payment_mode"),
                                    jsonObject1.getString("status"), jsonObject1.getString("invoice"), jsonObject1.getString("date_created"), jsonObject1.getString("total_piece")));

                            recycler_view.setLayoutManager(new LinearLayoutManager(context));
                            recycler_view.setAdapter(new OrderAdapter(context, modelOrderArrayList));

                        }
                    } else if (jsonObject.getString("Error").equals("0")) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Three is no Order History ", Toast.LENGTH_SHORT).show();
                        liner_empaty.setVisibility(View.VISIBLE);
                    } else {
                        progressDialog.dismiss();
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
                Toast.makeText(context, "" + error, Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", "" + LocalSharedPreferences.getUserid(context));
                //   params.put("user_id","14");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
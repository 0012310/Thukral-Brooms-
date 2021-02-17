package thukral.brooms.sabActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

import me.relex.circleindicator.CircleIndicator;
import thukral.brooms.Database.LocalSharedPreferences;
import thukral.brooms.Interface.Img_addWish_Onclick;
import thukral.brooms.R;
import thukral.brooms.subAdapter.SubtoSubDataAdapter;
import thukral.brooms.subAdapter.WiperAllDataAdapter;
import thukral.brooms.subModel.modelallWiperList;
import thukral.brooms.subModel.modelsubtosubList;

public class WipersSubActivity extends AppCompatActivity {
    Context context;
    ProgressDialog progressDialog;
    TextView txtTitle;
    ImageView imgBack;
    RecyclerView recyclerView;
    ArrayList<modelallWiperList> modelsubtosubListArrayList = new ArrayList<>();
    WiperAllDataAdapter wiperAllDataAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wipers_sub);
        context = WipersSubActivity.this;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("Brooms Sub  Categories");
        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView = findViewById(R.id.recyclerView);

        final Intent intent = getIntent();
        String name = intent.getStringExtra("Data");
        if (name.equals("9")) {
            txtTitle.setText("Floor Wiper");
            Data(name);
            progressDialog.show();
        } else if (name.equals("6")) {
            txtTitle.setText("BathRoom Wiper");
            Data(name);
            progressDialog.show();

        } else if (name.equals("5")) {
            txtTitle.setText("Kitchen Wiper");
            Data(name);
            progressDialog.show();

        } else if (name.equals("4")) {
            txtTitle.setText("Glass Wiper");
            Data(name);
            progressDialog.show();
        }


    }

    private void Data(final String id) {
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thukralbroom.com/api/all-sub.php", new Response.Listener<String>() {
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

                        modelsubtosubListArrayList.add(new modelallWiperList(jsonObject1.getString("user_id"), jsonObject1.getString("user_type"), jsonObject1.getString("id"), jsonObject1.getString("name"),
                                jsonObject1.getString("cat_id"), jsonObject1.getString("sub_id"), jsonObject1.getString("category_name"),
                                jsonObject1.getString("sub_name"), jsonObject1.getString("description"), jsonObject1.getString("color"),
                                jsonObject1.getString("unit"), jsonObject1.getString("bag"), jsonObject1.getString("qty"), jsonObject1.getString("regular_price"),
                                jsonObject1.getString("sales_price"), jsonObject1.getString("image"), jsonObject1.getString("image1"), jsonObject1.getString("date_created"),
                                jsonObject1.getString("wishlist")));


                        GridLayoutManager manager = new GridLayoutManager(context, 2);
                        recyclerView.setLayoutManager(manager);

                        recyclerView.setAdapter(new WiperAllDataAdapter(context, modelsubtosubListArrayList, new Img_addWish_Onclick() {
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
                parms.put("subcategory", "" + id);
                Log.d("data_params", parms.toString());

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
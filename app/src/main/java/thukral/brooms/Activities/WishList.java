package thukral.brooms.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import thukral.brooms.Adapters.CartAdapter;
import thukral.brooms.Adapters.WishListAdapter;
import thukral.brooms.Database.LocalSharedPreferences;
import thukral.brooms.Interface.Activity_refresh_Onclick;
import thukral.brooms.Interface.CartRefresh;
import thukral.brooms.R;
import thukral.brooms.model.modelWishList;

public class WishList extends AppCompatActivity implements Activity_refresh_Onclick {
    Context context;
    ProgressDialog progressDialog;
    TextView txtTitle;
    ImageView imgBack;
    RecyclerView recycler_wishlist;
    WishListAdapter wishListAdapter;
    ArrayList<modelWishList> modelWishLists = new ArrayList<>();
    LinearLayout liner_empaty ;
    Button add_newlist ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);
        context = WishList.this;

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("Wish List");
        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recycler_wishlist = findViewById(R.id.recycler_wishlist);
        CALL_WishLIST();
        progressDialog.show();
        liner_empaty=findViewById(R.id.liner_empaty);
        add_newlist=findViewById(R.id.add_newlist);
        add_newlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(context, MainActivity.class);
                i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i1);
            }
        });
    }

    private void CALL_WishLIST() {
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thukralbroom.com/api/wishlist.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
              //  Toast.makeText(WishList.this, "" + response, Toast.LENGTH_SHORT).show();
                Log.d("wish_data", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("Error").equals("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            modelWishLists.add(new modelWishList(jsonObject1.getString("id"), jsonObject1.getString("name"),
                                    jsonObject1.getString("cat_id"), jsonObject1.getString("sub_id"), jsonObject1.getString("category_name"),
                                    jsonObject1.getString("sub_name"), jsonObject1.getString("description"), jsonObject1.getString("color"),
                                    jsonObject1.getString("unit"), jsonObject1.getString("bag"), jsonObject1.getString("qty"), jsonObject1.getString("regular_price"),
                                    jsonObject1.getString("sales_price"),  jsonObject1.getString("total_set_price"),

                                    jsonObject1.getString("Image"), jsonObject1.getString("date_created")));

                            GridLayoutManager manager = new GridLayoutManager(context, 1);
                            recycler_wishlist.setLayoutManager(manager);

                            recycler_wishlist.setAdapter(new WishListAdapter(context, modelWishLists, new CartRefresh() {
                                @Override
                                public void onClick() {
                                    CALL_WishLIST();
                                    modelWishLists.clear();
                                }
                            }));
                        }
                    } else if (jsonObject.getString("Error").equals("0")) {
                        liner_empaty.setVisibility(View.VISIBLE);
                        Toast.makeText(context, "Your Wish List is Empty ", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(WishList.this, "" + volleyError, Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", ""+LocalSharedPreferences.getUserid(context));
                Log.d("sdf", params.toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    @Override
    public void onClick() {
        liner_empaty.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        Intent i1 = new Intent(context, MainActivity.class);
        i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i1);
    }
}
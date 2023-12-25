package thukral.brooms.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import thukral.brooms.Adapters.ProductPagerAdapter;
import thukral.brooms.Database.LocalSharedPreferences;
import thukral.brooms.R;

public class TeaDetails extends AppCompatActivity {

    Context context;
    ImageView minus_quantity, plus_quantity;

    int minteger = 0;
    TextView tv_quantity;

    ViewPager view_pager;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private ArrayList<String> pagerarray_list = new ArrayList<>();
    LinearLayout liner_pager;
    String pro_id;
    TextView text_action_add_cart, text_action_buy;

    TextView tv_product_name, tv_sales_price, tv_reg_price, tv_product_price_per, tv_set , tv_des;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tea_details);
        context = TeaDetails.this;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        minus_quantity = findViewById(R.id.minus_quantity);
        plus_quantity = findViewById(R.id.plus_quantity);
        tv_quantity = (TextView) findViewById(R.id.tv_quantity);

        tv_product_name = findViewById(R.id.tv_product_name);
        tv_sales_price = findViewById(R.id.tv_sales_price);
        tv_reg_price = findViewById(R.id.tv_reg_price);
        tv_product_price_per = findViewById(R.id.tv_product_price_per);
        tv_set = findViewById(R.id.tv_set);
        tv_des=findViewById(R.id.tv_des);


        if (LocalSharedPreferences.getUser_tpe(context).equals("Retailer")) {
            minteger = 1;
            tv_quantity.setText("" + minteger);
        } else {
            minteger = 2;
            tv_quantity.setText("" + minteger);
        }

        minus_quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                minteger = minteger - 1;
                minteger = minteger > 1 ? minteger : 1;
                tv_quantity.setText("" + minteger);

            }
        });

        plus_quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minteger = minteger + 1;
                tv_quantity.setText("" + minteger);

            }
        });

        liner_pager = findViewById(R.id.liner_pager);
        liner_pager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Intent intent = getIntent();
        final String id = intent.getStringExtra("PRO_ID");

        CALL_API_PAGER(id);
        CALL_API_Pro_Details(id);
        progressDialog.show();

        text_action_buy = findViewById(R.id.text_action_buy);
        text_action_add_cart = findViewById(R.id.text_action_add_cart);
        text_action_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ADD_CART_Go_Cart(id);
                progressDialog.show();
            }
        });

        text_action_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ADD_CART(id);
                progressDialog.show();

            }
        });

    }

    private void ADD_CART(final String ID) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thukralbroom.com/api/cart-add.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("add_cart_data", response);
                progressDialog.dismiss();
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("message").equals("Products has been added to cart.")) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Item Added into Cart", Toast.LENGTH_SHORT).show();

                    } else if (jsonObject.getString("message").equals("Products quantity has been Updated to cart.")) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Item Updated into Cart", Toast.LENGTH_SHORT).show();
                    } else if (jsonObject.getString("Error").equals("2")) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "This Product already in your cart.", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Try Again.", Toast.LENGTH_SHORT).show();
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
                params.put("product_id", "" + ID);
                params.put("quantity", tv_quantity.getText().toString());
                Log.d("my_param_add", params.toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);


    }

    private void ADD_CART_Go_Cart(final String ID) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thukralbroom.com/api/cart-add.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("add_cart_data", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("message").equals("Products has been added to cart.")) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Item Added into Cart", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(TeaDetails.this, CartList.class);
                        startActivity(i);

                    } else if (jsonObject.getString("message").equals("Products quantity has been Updated to cart.")) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Item Updated into Cart", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(TeaDetails.this, CartList.class);
                        startActivity(i);
                    } else if (jsonObject.getString("Error").equals("2")) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "This Product already in your cart.", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Try Again.", Toast.LENGTH_SHORT).show();
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
                params.put("product_id", "" + ID);
                params.put("quantity", tv_quantity.getText().toString());
                Log.d("my_param_add", params.toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);


    }

    private void CALL_API_Pro_Details(final String ID) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thukralbroom.com/api/single.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.d("tea_deatails", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        tv_product_name.setText(jsonObject1.getString("category_name"));
                        tv_reg_price.setText("Rs" + " " + jsonObject1.getString("regular_price"));
                        tv_sales_price.setText("Rs" + " " + jsonObject1.getString("sales_price"));
                        tv_set.setText(jsonObject1.getString("bag") + "Bag" + " " + "(" + jsonObject1.getString("qty") + " " + "pcs" + ")");
                        tv_des.setText(jsonObject1.getString("description"));
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
                parms.put("product_id", "" + ID);
                parms.put("category_id", "2");
                parms.put("user_id", "" + LocalSharedPreferences.getUserid(context));
                Log.d("my_param", parms.toString());
                return parms;
            }
        };
        requestQueue.add(stringRequest);
    }


    private void CALL_API_PAGER(final String ID) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thukralbroom.com/api/all-images.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.d("egf", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String str = jsonObject1.getString("mult_image");
                        pagerarray_list.add(str);
                    }
                    init();
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
                parms.put("product_id", "" + ID);
                Log.d("my_param", parms.toString());
                return parms;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void init() {
        for (int i = 0; i < pagerarray_list.size(); i++)
            mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new ProductPagerAdapter(TeaDetails.this, pagerarray_list, pro_id));
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == pagerarray_list.size()) {
                    currentPage = 0;
                }
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3500, 2500);
    }
}
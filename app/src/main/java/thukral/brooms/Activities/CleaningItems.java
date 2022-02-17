package thukral.brooms.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import thukral.brooms.Adapters.CleanAdapter;
import thukral.brooms.Adapters.CleanPagerAdapter;
import thukral.brooms.Database.LocalSharedPreferences;
import thukral.brooms.Interface.Img_addWish_Onclick;
import thukral.brooms.R;
import thukral.brooms.model.modelAllClean;
import thukral.brooms.sabActivities.BrushsActivity;
import thukral.brooms.sabActivities.DusterActivity;
import thukral.brooms.sabActivities.MopActivity;
import thukral.brooms.sabActivities.PochaActivity;
import thukral.brooms.subAdapter.MopAdapter;
import thukral.brooms.subModel.modelAllMops;

public class CleaningItems extends AppCompatActivity {
    ArrayList<modelAllClean> modelAllCleanArrayList = new ArrayList<>();
    Context context;
    RecyclerView recycler_clean;
    ProgressDialog progressDialog;
    LinearLayout liner_brushes, liner_duster, liner_mop, liner_pocha;

    ViewPager view_pager;
    CircleIndicator indicator;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private ArrayList<String> pagerarray_list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaning_items);
        context = CleaningItems.this;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        recycler_clean = findViewById(R.id.recycler_clean);

        CALL_ALL_CITY();
        modelAllCleanArrayList.clear();
        CALL_API_PAGER();
        pagerarray_list.clear();

        liner_brushes=findViewById(R.id.liner_brushes);

        liner_duster=findViewById(R.id.liner_duster);
        liner_mop=findViewById(R.id.liner_mop);
        liner_pocha=findViewById(R.id.liner_pocha);

        liner_brushes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BrushsActivity.class);
                startActivity(intent);
            }
        });


        liner_duster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DusterActivity.class);
                startActivity(intent);
            }
        });

        liner_mop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MopActivity.class);
                startActivity(intent);
            }
        });
        liner_pocha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PochaActivity.class);
                startActivity(intent);
            }
        });



        
    }

    private void CALL_API_PAGER() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thukralbroom.com/api/all-image.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("egf", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String str = jsonObject1.getString("image");
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
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parms = new HashMap<>();
                parms.put("category_id", "3");
                Log.d("my_param", parms.toString());
                return parms;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void init() {
        for (int i = 0; i < pagerarray_list.size(); i++)
            mPager = findViewById(R.id.pager);
        CircleIndicator indicator = findViewById(R.id.indicator);
        mPager.setAdapter(new CleanPagerAdapter(context, pagerarray_list, "1"));

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


    private void CALL_ALL_CITY() {
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thukralbroom.com/api/all-clean.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                Log.d("Data_tea", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        modelAllCleanArrayList.add(new modelAllClean(jsonObject1.getString("user_id"), jsonObject1.getString("user_type"), jsonObject1.getString("id"), jsonObject1.getString("name"),
                                jsonObject1.getString("cat_id"), jsonObject1.getString("sub_id"), jsonObject1.getString("category_name"), jsonObject1.getString("sub_name"),
                                jsonObject1.getString("description"), jsonObject1.getString("color"), jsonObject1.getString("unit"), jsonObject1.getString("bag"),
                                jsonObject1.getString("qty"), jsonObject1.getString("regular_price"), jsonObject1.getString("sales_price"), jsonObject1.getString("image"),
                                jsonObject1.getString("date_created"), jsonObject1.getString("wishlist")));


                        GridLayoutManager manager = new GridLayoutManager(context, 2);
                        recycler_clean.setLayoutManager(manager);

                        recycler_clean.setAdapter(new CleanAdapter(context, modelAllCleanArrayList, new Img_addWish_Onclick() {
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
                return parms;
            }
        };
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}
package thukral.brooms.sabActivities;

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
import thukral.brooms.Activities.MainActivity;
import thukral.brooms.Adapters.TeaPagerAdapter;
import thukral.brooms.R;

public class BroomsSubCategory extends AppCompatActivity {
    Context context;
    ProgressDialog progressDialog;
    TextView txtTitle;
    ImageView imgBack;

    LinearLayout liner_all_grass, liner_all_coco, liner_all_bamboo, liner_all_fiber_non;

    LinearLayout plastic_handel , chromZink , computer ,tin_pipe ,fiber_nondust;
    LinearLayout  plastic_pipe  , patti , bamboo;



    ViewPager view_pager;
    CircleIndicator indicator;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private ArrayList<String> pagerarray_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brooms_sub_category);
        context = BroomsSubCategory.this;
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


        liner_all_grass = findViewById(R.id.liner_all_grass);
        liner_all_coco = findViewById(R.id.liner_all_coco);
        liner_all_bamboo = findViewById(R.id.liner_all_bamboo);
        liner_all_fiber_non = findViewById(R.id.liner_all_fiber_non);

        final Intent intent = getIntent();
        String name = intent.getStringExtra("Data");
        if (name.equals("GrassBrooms")) {
            txtTitle.setText("Grass Brooms Categroies");
            liner_all_coco.setVisibility(View.GONE);
            liner_all_grass.setVisibility(View.VISIBLE);
            liner_all_bamboo.setVisibility(View.GONE);
            liner_all_fiber_non.setVisibility(View.GONE);
        } else if (name.equals("CoCo")) {
            txtTitle.setText("Coco Stick Brooms Categroies");
            liner_all_grass.setVisibility(View.GONE);
            liner_all_coco.setVisibility(View.VISIBLE);
            liner_all_bamboo.setVisibility(View.GONE);
            liner_all_fiber_non.setVisibility(View.GONE);
        } else if (name.equals("BambooStick")) {
            txtTitle.setText("Bamboo Stick Brooms Categroies");
            liner_all_grass.setVisibility(View.GONE);
            liner_all_coco.setVisibility(View.GONE);
            liner_all_bamboo.setVisibility(View.VISIBLE);
            liner_all_fiber_non.setVisibility(View.GONE);

        } else if (name.equals("Fiber_NonDust")) {
            txtTitle.setText("Fiber Non-Dust  Brooms Categroies");
            liner_all_grass.setVisibility(View.GONE);
            liner_all_coco.setVisibility(View.GONE);
            liner_all_bamboo.setVisibility(View.GONE);
            liner_all_fiber_non.setVisibility(View.VISIBLE);

        }

        CALL_API_PAGER();
        pagerarray_list.clear();
        progressDialog.show();

        plastic_handel = findViewById(R.id.plastic_handel);
        chromZink=findViewById(R.id.chromZink);
        computer=findViewById(R.id.computer);
        tin_pipe=findViewById(R.id.tin_pipe);
        fiber_nondust=findViewById(R.id.fiber_nondust);

        plastic_pipe=findViewById(R.id.plastic_pipe);
        patti= findViewById(R.id.patti);

        bamboo=findViewById(R.id.bamboo);



        plastic_handel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_plastic_handel = new Intent(BroomsSubCategory.this, SubToSubData.class);
                intent_plastic_handel.putExtra("Data","1");
                startActivity(intent_plastic_handel);
            }
        });

        chromZink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_chromZink = new Intent(BroomsSubCategory.this, SubToSubData.class);
                intent_chromZink.putExtra("Data","2");
                startActivity(intent_chromZink);
            }
        });

        computer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_computer = new Intent(BroomsSubCategory.this, SubToSubData.class);
                intent_computer.putExtra("Data","3");
                startActivity(intent_computer);
            }
        });

        tin_pipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_tin_pipe = new Intent(BroomsSubCategory.this, SubToSubData.class);
                intent_tin_pipe.putExtra("Data","4");
                startActivity(intent_tin_pipe);
            }
        });


        fiber_nondust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_fiber_nondust = new Intent(BroomsSubCategory.this, SubToSubData.class);
                intent_fiber_nondust.putExtra("Data","5");
                startActivity(intent_fiber_nondust);
            }
        });

        plastic_pipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_plastic_pipe = new Intent(BroomsSubCategory.this, SubToSubData.class);
                intent_plastic_pipe.putExtra("Data","6");
                startActivity(intent_plastic_pipe);
            }
        });


        patti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_patti = new Intent(BroomsSubCategory.this, SubToSubData.class);
                intent_patti.putExtra("Data","7");
                startActivity(intent_patti);
            }
        });


        bamboo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_patti = new Intent(BroomsSubCategory.this, SubToSubData.class);
                intent_patti.putExtra("Data","Bamboo Stick Brooms");
                startActivity(intent_patti);
            }
        });
    }

    private void CALL_API_PAGER() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thukralbroom.com/api/all-image.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
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
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parms = new HashMap<>();
                parms.put("category_id", "2");
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
        mPager.setAdapter(new TeaPagerAdapter(context, pagerarray_list, "1"));

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
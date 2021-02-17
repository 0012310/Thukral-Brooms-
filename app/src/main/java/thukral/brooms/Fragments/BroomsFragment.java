package thukral.brooms.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
import thukral.brooms.Adapters.BroomAdapter;
import thukral.brooms.Adapters.BroomstPagerAdapter;
import thukral.brooms.Database.LocalSharedPreferences;
import thukral.brooms.Interface.Img_addWish_Onclick;
import thukral.brooms.R;
import thukral.brooms.model.modelAllBrooms;
import thukral.brooms.sabActivities.BroomsCategory;
import thukral.brooms.sabActivities.BroomsSubCategory;


public class BroomsFragment extends Fragment {
    Context context;
    ProgressDialog progressDialog;

    RecyclerView recycler_brooms;
    ArrayList<modelAllBrooms> allBroomsArrayList = new ArrayList<>();
    BroomAdapter broomAdapter;

    LinearLayout liner_grass , liner_cocobrooms , liner_babmoostick  , liner_fiber_nondust;

    ViewPager view_pager;
    CircleIndicator indicator ;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private ArrayList<String> pagerarray_list = new ArrayList<>();


    public BroomsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brooms, container, false);

        context = view.getContext();
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        recycler_brooms = view.findViewById(R.id.recycler_brooms);
        ALL_BROOMS();
        allBroomsArrayList.clear();
        CALL_API_PAGER(view);
        pagerarray_list.clear();



        liner_grass=view.findViewById(R.id.liner_grass);
        liner_grass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), BroomsSubCategory.class);
                intent.putExtra("Data","GrassBrooms");
                startActivity(intent);
            }
        });

        liner_cocobrooms=view.findViewById(R.id.liner_cocobrooms);
        liner_cocobrooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),BroomsSubCategory.class);
                intent.putExtra("Data","CoCo");
                startActivity(intent);
            }
        });

        liner_babmoostick=view.findViewById(R.id.liner_babmoostick);
        liner_babmoostick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),BroomsSubCategory.class);
                intent.putExtra("Data","BambooStick");
                startActivity(intent);
            }
        });


        liner_fiber_nondust=view.findViewById(R.id.liner_fiber_nondust);
        liner_fiber_nondust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),BroomsSubCategory.class);
                intent.putExtra("Data","Fiber_NonDust");
                startActivity(intent);
            }
        });


        return view;

    }

    private void CALL_API_PAGER(final View view) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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
                    init(view);
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
                parms.put("category_id", "1");
                Log.d("my_param", parms.toString());
                return parms;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void init(View view) {
        for (int i = 0; i < pagerarray_list.size(); i++)
            mPager = view. findViewById(R.id.pager);
        CircleIndicator indicator = view. findViewById(R.id.indicator);
        mPager.setAdapter(new BroomstPagerAdapter(getActivity(), pagerarray_list, "1"));

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

    private void ALL_BROOMS() {
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thukralbroom.com/api/all-brooms.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();
                Log.d("Broom_Data", response);
               // Toast.makeText(context, ""+response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String name = jsonObject1.getString("name");


                        allBroomsArrayList.add(new modelAllBrooms(jsonObject1.getString("user_id"), jsonObject1.getString("user_type"), jsonObject1.getString("id"), jsonObject1.getString("name"),
                                jsonObject1.getString("cat_id"), jsonObject1.getString("sub_id"), jsonObject1.getString("category_name"), jsonObject1.getString("sub_name"),
                                jsonObject1.getString("description"), jsonObject1.getString("color"), jsonObject1.getString("unit"), jsonObject1.getString("bag"),
                                jsonObject1.getString("qty"), jsonObject1.getString("regular_price"), jsonObject1.getString("sales_price"), jsonObject1.getString("image"),
                                 jsonObject1.getString("date_created"), jsonObject1.getString("wishlist")));


                        GridLayoutManager manager = new GridLayoutManager(context, 2);
                        recycler_brooms.setLayoutManager(manager);

                        recycler_brooms.setAdapter(new BroomAdapter(getContext(), allBroomsArrayList, new Img_addWish_Onclick() {
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
                parms.put("user_id", ""+LocalSharedPreferences.getUserid(context));

                return parms;
            }
        };
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new


                DefaultRetryPolicy(
                0,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


}
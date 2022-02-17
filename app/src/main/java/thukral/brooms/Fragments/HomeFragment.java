package thukral.brooms.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import thukral.brooms.Activities.BroomsActivity_Cum;
import thukral.brooms.Activities.CleanActivity_Cum;
import thukral.brooms.Activities.CleaningItems;
import thukral.brooms.Activities.TeaActivity_Cum;
import thukral.brooms.Adapters.HomePagerAdapter;
import thukral.brooms.R;
import thukral.brooms.sabActivities.BroomsCategory;
import thukral.brooms.sabActivities.BrushsActivity;
import thukral.brooms.sabActivities.DusterActivity;
import thukral.brooms.sabActivities.MopActivity;
import thukral.brooms.sabActivities.PochaActivity;
import thukral.brooms.sabActivities.WiperActivity;

public class HomeFragment extends Fragment {
    ImageView image;
    Context context;
    ProgressDialog progressDialog;

    ViewPager view_pager;
    CircleIndicator indicator;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private ArrayList<String> pagerarray_list = new ArrayList<>();

    LinearLayout linear_one, linear_two, linear_three, linear_four;
    LinearLayout  liner_broom ,liner_wiper , liner_brushes , liner_duster ,liner_mop ,liner_pocha ,liner_tea;

    public HomeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        image = view.findViewById(R.id.image);
        context = view.getContext();

        /*progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);*/
        linear_one = view.findViewById(R.id.linear_one);
        linear_two = view.findViewById(R.id.linear_two);
        linear_three = view.findViewById(R.id.linear_three);
        linear_four = view.findViewById(R.id.linear_four);

        liner_broom=view.findViewById(R.id.liner_broom);
        liner_wiper=view.findViewById(R.id.liner_wiper);
        liner_brushes=view.findViewById(R.id.liner_brushes);

        liner_duster=view.findViewById(R.id.liner_duster);
        liner_mop=view.findViewById(R.id.liner_mop);
        liner_pocha=view.findViewById(R.id.liner_pocha);
        liner_tea=view.findViewById(R.id.liner_tea);

        liner_broom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), BroomsCategory.class);
                startActivity(intent);
            }
        });

        liner_wiper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), WiperActivity.class);
                startActivity(intent);
            }
        });
        liner_brushes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), BrushsActivity.class);
                startActivity(intent);
            }
        });



        liner_duster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), DusterActivity.class);
                startActivity(intent);
            }
        });

        liner_mop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), MopActivity.class);
                startActivity(intent);
            }
        });
        liner_pocha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(getActivity().getApplication(), PochaActivity.class);
                startActivity(intent);*/

                Intent intent = new Intent(getActivity().getApplication(), CleaningItems.class);
                startActivity(intent);

            }
        });


        liner_tea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), TeaActivity_Cum.class);
                startActivity(intent);
            }
        });


        linear_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getActivity().getApplication(), BroomsActivity_Cum.class);
                startActivity(intent);


            }
        });

        linear_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), TeaActivity_Cum.class);
                startActivity(intent);
            }
        });

        linear_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), CleanActivity_Cum.class);
                startActivity(intent);

            }
        });

        linear_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Hii", Toast.LENGTH_SHORT).show();
            }
        });


        CALL_API_PAGER(view);
        pagerarray_list.clear();
        return view;
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
  /*    String backStateName = fragment.getClass().getName();
        FragmentTransaction transaction =getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();*/


    }

    private void CALL_API_PAGER(final View view) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thukralbroom.com/api/all-banner.php", new Response.Listener<String>() {
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
                parms.put("category_id", "2");
                Log.d("my_param", parms.toString());
                return parms;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void init(View view) {
        for (int i = 0; i < pagerarray_list.size(); i++)
            mPager = view.findViewById(R.id.pager);
        CircleIndicator indicator = view.findViewById(R.id.indicator);
        mPager.setAdapter(new HomePagerAdapter(getActivity(), pagerarray_list, "1"));

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
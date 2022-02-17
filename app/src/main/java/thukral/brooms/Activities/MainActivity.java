package thukral.brooms.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import thukral.brooms.AboutUs;
import thukral.brooms.Adapters.BroomAdapter;
import thukral.brooms.Database.LocalSharedPreferences;
import thukral.brooms.Fragments.CleanFragment;
import thukral.brooms.Fragments.HomeFragment;
import thukral.brooms.Fragments.TeaFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thukral.brooms.Fragments.BroomsFragment;
import thukral.brooms.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, BroomAdapter.click {
    Context context;

    TextView tv_header_name, tv_header_email;
    ImageView header_image;
    private ActionBar toolbar;

    TabLayout tabLayoutExploreActivity;
    ViewPager viewpagerExploreActivity;

    TextView txtBadge_fav, txtBadge_cart;
    ImageView fav_list, cart_list;
    RelativeLayout relative_wish ,relative_cart ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        toolbar = getSupportActionBar();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        navigationView.setNavigationItemSelectedListener(this);


        findViewById(R.id.imgDrawer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

                if (!drawer.isDrawerOpen(GravityCompat.START))
                    drawer.openDrawer(GravityCompat.START);
                else drawer.closeDrawer(GravityCompat.END);

            }
        });


        View headerview = navigationView.getHeaderView(0);
        tv_header_name = headerview.findViewById(R.id.tv_header_name);
        header_image = headerview.findViewById(R.id.header_image);


        //   tv_header_name.setText(LocalSharedPreferences.getname(this));
        //   tv_header_email = headerview.findViewById(R.id.tv_header_email);
        //  tv_header_email.setText(LocalSharedPreferences.getemail(this));


        viewpagerExploreActivity = (ViewPager) findViewById(R.id.viewpagerExploreActivity);
        tabLayoutExploreActivity = (TabLayout) findViewById(R.id.tabLayoutExploreActivity);

        setupViewPager(viewpagerExploreActivity);

        tabLayoutExploreActivity.setupWithViewPager(viewpagerExploreActivity);
        viewpagerExploreActivity.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutExploreActivity));

        txtBadge_fav = findViewById(R.id.txtBadge_fav);
        txtBadge_cart = findViewById(R.id.txtBadge_cart);

        fav_list = findViewById(R.id.fav_list);
        fav_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WishList.class);
                startActivity(intent);
            }
        });
        txtBadge_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CartList.class);
                startActivity(intent);
            }
        });

        relative_wish=findViewById(R.id.relative_wish);
        relative_cart=findViewById(R.id.relative_cart);

        relative_wish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WishList.class);
                startActivity(intent);
            }
        });
        relative_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CartList.class);
                startActivity(intent);
            }
        });


        CALLAPI_WISH_LIST_COUNT();
        CALLAPI_CART_LIST_COUNT();

        CALL_API_Info();

    }

    private void CALL_API_Info() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thukralbroom.com/api/User/all-users.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("data_info", response);
                //   progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        tv_header_name.setText(jsonObject1.getString("fullname"));
                        Glide.with(context).load(jsonObject1.getString("image")).crossFade()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.profile)
                                .dontAnimate().into(header_image);


                    //    Toast.makeText(context, "" + LocalSharedPreferences.getname(context), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //    progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", LocalSharedPreferences.getUserid(context));
                Log.d("my_param_add", params.toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);


    }

    private void setupViewPager(ViewPager viewPager) {
        viewpagerExploreActivity.setOffscreenPageLimit(1);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), " Home ");
        adapter.addFragment(new BroomsFragment(), " Brooms ");
        adapter.addFragment(new TeaFragment(), " Tea ");
        adapter.addFragment(new CleanFragment(), "Cleaning Products");
        viewPager.setAdapter(adapter);
    }


    private void CALLAPI_CART_LIST_COUNT() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thukralbroom.com/api/cart-add.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(context, "" + response, Toast.LENGTH_SHORT).show();
                Log.d("wish_count", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        txtBadge_cart.setText(jsonObject1.getString("total_cart"));

                    }

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
                Map<String, String> params = new HashMap<>();
                params.put("user_id", "" + LocalSharedPreferences.getUserid(context));
                params.put("action", "cart");
                Log.d("DDD", params.toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void CALLAPI_WISH_LIST_COUNT() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thukralbroom.com/api/wishlist.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //   Toast.makeText(context, "" + response, Toast.LENGTH_SHORT).show();
                Log.d("wish_count_list", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        txtBadge_fav.setText(jsonObject1.getString("total_wishlist"));

                    }

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
                Map<String, String> params = new HashMap<>();
                params.put("user_id", "" + LocalSharedPreferences.getUserid(context));
                params.put("action", "wishlist");
                Log.d("DDD", params.toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void myClick() {
        CALLAPI_WISH_LIST_COUNT();
        CALLAPI_CART_LIST_COUNT();

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {

            mFragmentTitleList.add(title);
            mFragmentList.add(fragment);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //    super.onBackPressed()

            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setTitle("QUIT THUKRAL BROOMS ?");
            builder1.setIcon(R.drawable.ic_bookmark_black);
            builder1.setMessage("Do you want to say goodbye so soon?");
            builder1.setCancelable(false);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                            homeIntent.addCategory(Intent.CATEGORY_HOME);
                            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                            moveTaskToBack(true);

                        }
                    });

            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            LocalSharedPreferences.saveIsLogin(this, false);
          /*  Intent i = new Intent(MainActivity.this, Login.class);
            startActivity(i);*/
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment;
        int id = item.getItemId();

        if (id == R.id.nav_cart_list) {
            Intent intent = new Intent(MainActivity.this, CartList.class);
            startActivity(intent);
        } else if (id == R.id.nav_wish_list) {
            Intent intent = new Intent(MainActivity.this, WishList.class);
            startActivity(intent);

        } else if (id == R.id.nav_cart_order) {
            Intent intent = new Intent(MainActivity.this, Orders.class);
            startActivity(intent);

        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(MainActivity.this, Profile.class);
            startActivity(intent);

        } else if (id == R.id.nav_changepass) {
            Intent intent = new Intent(MainActivity.this, ForgetActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_support) {
            Intent intent = new Intent(MainActivity.this, Support.class);
            startActivity(intent);

        } else if (id == R.id.nav_aboutUs) {
            Intent intent = new Intent(MainActivity.this, AboutUs.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {

            Toast.makeText(context, "Share", Toast.LENGTH_SHORT).show();

            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");

                String shareMessage = "\nGreetings with Thukral Brooms \n" + "Download the App Link :\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=thukral.brooms";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch (Exception e) {

            }

        } else if (id == R.id.nav_logout) {
            Toast.makeText(context, "LogOut", Toast.LENGTH_SHORT).show();
            LocalSharedPreferences.saveIsLogin(this, false);
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        String backStateName = fragment.getClass().getName();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.viewpagerExploreActivity, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}

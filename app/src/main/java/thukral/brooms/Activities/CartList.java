package thukral.brooms.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import thukral.brooms.Adapters.BroomAdapter;
import thukral.brooms.Adapters.CartAdapter;
import thukral.brooms.Database.LocalSharedPreferences;
import thukral.brooms.Database.Utility;
import thukral.brooms.Interface.Activity_refresh_Onclick;
import thukral.brooms.Interface.CartRefresh;
import thukral.brooms.R;
import thukral.brooms.model.modelCartList;

import static android.net.sip.SipErrorCode.TIME_OUT;

public class CartList extends AppCompatActivity implements Activity_refresh_Onclick {
    Context context;
    ProgressDialog progressDialog;
    TextView txtTitle;
    ImageView imgBack;
    RecyclerView recycler_cart;
    ArrayList<modelCartList> modelCartLists = new ArrayList<>();
    CartAdapter cartAdapter;
    LinearLayout liner_empaty;
    LinearLayout layout_payment;
    TextView tv_total_price;
    String Total_cart_value;
    TextView text_make_payment;
    private String userChoosenTask;
    String PRICE, EXISTING_USER;
    private static int TIME_OUT = 2500;
    Button add_newlist;
    Spinner spinner_state, spinner_zone, spinner_distributor;
    Button btnPlaceOrder;
    String dis_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);
        context = CartList.this;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("Cart List");
        layout_payment = findViewById(R.id.layout_payment);
        add_newlist = findViewById(R.id.add_newlist);
        add_newlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(context, MainActivity.class);
                i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i1);
            }
        });
        tv_total_price = findViewById(R.id.tv_total_price);
        tv_total_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog scheduleDialog;
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                AlertDialog.Builder builderAlert = new AlertDialog.Builder(context);

                final View country_alert_view = inflater.inflate(R.layout.payment_moreinfo, null);

                TextView tv_value_offall_products = country_alert_view.findViewById(R.id.tv_value_offall_products);
                TextView tv_discount = country_alert_view.findViewById(R.id.tv_discount);
                TextView tv_value_gst = country_alert_view.findViewById(R.id.tv_value_gst);
                TextView tv_shipping_charges = country_alert_view.findViewById(R.id.tv_shipping_charges);

                TextView tv_value_of_finalamount = country_alert_view.findViewById(R.id.tv_value_of_finalamount);

                tv_value_offall_products.setText(" : ₹ " + Total_cart_value);
                tv_discount.setText(" : ₹ " + "0");
                tv_value_gst.setText(" : ₹ " + "0");


                tv_value_of_finalamount.setText(" : ₹ " + Total_cart_value);

                builderAlert.setView(country_alert_view);
                builderAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builderAlert.setCancelable(true);
                scheduleDialog = builderAlert.create();
                builderAlert.show();

            }
        });

        text_make_payment = findViewById(R.id.text_make_payment);
        text_make_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //   PAYMENT_OPTIONS();
                //  MakePAY_API();
                //  progressDialog.show();

                CallDialogueForSelect();
            }
        });


        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(context, MainActivity.class);
                i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i1);
            }
        });
        recycler_cart = findViewById(R.id.recycler_cart);
        liner_empaty = findViewById(R.id.liner_empaty);
        CART_LIST();
        modelCartLists.clear();
        progressDialog.show();


    }


    private void CallDialogueForSelect() {

        AlertDialog scheduleDialog;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        AlertDialog.Builder builderAlert = new AlertDialog.Builder(context);
        selectState();
        final View country_alert_view = inflater.inflate(R.layout.selectdistiibutor, null);

        spinner_state = country_alert_view.findViewById(R.id.spinner_state);
        spinner_zone = country_alert_view.findViewById(R.id.spinner_zone);
        spinner_distributor = country_alert_view.findViewById(R.id.spinner_distributor);

        btnPlaceOrder = country_alert_view.findViewById(R.id.btnPlaceOrder);


        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CallZoneApi(stringArrayList_state_id.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_zone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                CallDistApi(stringArrayList_zone_id.get(i));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_distributor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dis_id = stringArrayList_dis_id.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinner_state.getSelectedItem().toString().equals("Select State")) {
                    Toast.makeText(context, "Select State first", Toast.LENGTH_SHORT).show();
                } else if (spinner_zone.getSelectedItem().toString().equals("Select Zone")) {
                    Toast.makeText(context, "Select Zone first", Toast.LENGTH_SHORT).show();
                } else if (spinner_distributor.getSelectedItem().toString().equals("Select Distributor")) {
                    Toast.makeText(context, "Select Distributor first", Toast.LENGTH_SHORT).show();
                } else {
                    MakePAY_API(dis_id);
                }


            }
        });

        builderAlert.setView(country_alert_view);
        builderAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });
        builderAlert.setCancelable(true);
        scheduleDialog = builderAlert.create();
        builderAlert.show();
    }


    ArrayList<String> stringArrayList_state = new ArrayList<>();
    ArrayList<String> stringArrayList_state_id = new ArrayList<>();

    ArrayList<String> stringArrayList_zone = new ArrayList<>();
    ArrayList<String> stringArrayList_zone_id = new ArrayList<>();

    ArrayList<String> stringArrayList_dis = new ArrayList<>();
    ArrayList<String> stringArrayList_dis_id = new ArrayList<>();


    private void selectState() {
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://thukralbroom.com/api/all-state.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        stringArrayList_state.add(jsonObject1.getString("state"));
                        stringArrayList_state_id.add(jsonObject1.getString("id"));
                    }
                    ArrayAdapter arrayAdapter = new ArrayAdapter(CartList.this, android.R.layout.simple_spinner_item, stringArrayList_state);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                    spinner_state.setAdapter(arrayAdapter);


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
                params.put("zone_id", LocalSharedPreferences.getUserid(context));
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void CallZoneApi(final String id) {
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thukralbroom.com/api/all-zone.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("Error").equals("1")) {
                        spinner_zone.setVisibility(View.VISIBLE);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            stringArrayList_zone.add(jsonObject1.getString("zone"));
                            stringArrayList_zone_id.add(jsonObject1.getString("zone_id"));
                        }
                        ArrayAdapter arrayAdapter = new ArrayAdapter(CartList.this, android.R.layout.simple_spinner_item, stringArrayList_zone);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                        spinner_zone.setAdapter(arrayAdapter);

                    } else {
                        progressDialog.dismiss();
                        spinner_zone.setVisibility(View.GONE);
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
                params.put("state_id", "" + id);
                Log.d("hdsag", params.toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void CallDistApi(final String id) {
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thukralbroom.com/api/all-distrib.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("Error").equals("1")) {
                        spinner_distributor.setVisibility(View.VISIBLE);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            stringArrayList_dis.add(jsonObject1.getString("distributor"));
                            stringArrayList_dis_id.add(jsonObject1.getString("id"));
                        }
                        ArrayAdapter arrayAdapter = new ArrayAdapter(CartList.this, android.R.layout.simple_spinner_item, stringArrayList_dis);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                        spinner_distributor.setAdapter(arrayAdapter);
                    } else {
                        progressDialog.dismiss();
                        spinner_distributor.setVisibility(View.GONE);
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
                params.put("zone_id", "" + id);
                Log.d("hdsag", params.toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void MakePAY_API(final String dis_id) {
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thukralbroom.com/api/orders.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                Log.d("data_pay", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("Error").equals("1")) {
                        Toast.makeText(context, "Your Order Successfully Placed ", Toast.LENGTH_LONG).show();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            Toast.makeText(context, "" + jsonObject1.getString("link"), Toast.LENGTH_SHORT).show();
                            CallDialogue(jsonObject1.getString("link"));
                        }


                       /* new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                CART_LIST();
                                progressDialog.show();
                            }
                        }, TIME_OUT);*/


                    } else if (jsonObject.getString("Error").equals("0")) {
                        Toast.makeText(context, "No Data", Toast.LENGTH_SHORT).show();
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
                params.put("distributor_id", "" + dis_id);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void CallDialogue(String link) {
        AlertDialog scheduleDialog;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        AlertDialog.Builder builderAlert = new AlertDialog.Builder(context);

        final View country_alert_view = inflater.inflate(R.layout.cod_response, null);

        WebView web_view = country_alert_view.findViewById(R.id.web_view);
        progressDialog.dismiss();
        web_view.loadUrl(link);
        Toast.makeText(context, "Your Order Successfully Placed", Toast.LENGTH_LONG).show();

        builderAlert.setView(country_alert_view);
        builderAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                Intent i = new Intent(context, CartList.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

            }
        });

        builderAlert.setCancelable(false);
        scheduleDialog = builderAlert.create();
        builderAlert.show();
    }

    private void PAYMENT_OPTIONS() {
        final CharSequence[] items = {"EXISTING ACCOUNT", "ONLINE PAYMENT", "CANCEL"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("PAYMENT OPTIONS!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(context);


                if (items[item].equals("EXISTING ACCOUNT")) {
                    userChoosenTask = "EXISTING ACCOUNT";
                    if (result) ;

                    if (EXISTING_USER.equals("Yes")) {
                        Intent i = new Intent(context, COD.class);
                        i.putExtra("Total_price", PRICE);
                        startActivity(i);

                    } else {
                        Toast.makeText(context, "You are not an Existing Account", Toast.LENGTH_SHORT).show();
                    }


                } /*else if (items[item].equals("ONLINE PAYMENT")) {
                    userChoosenTask = "ONLINE PAYMENT";
                    if (result)
                        IntentForONLINE();
                } */ else if (items[item].equals("CANCEL")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void CART_LIST() {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thukralbroom.com/api/cart-add.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("cart_list_data", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("Error").equals("1")) {


                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            tv_total_price.setText("View Details " + "₹" + " " + jsonObject1.getString("total_price"));
                            Total_cart_value = jsonObject1.getString("total_price");
                            modelCartLists.add(new modelCartList(jsonObject1.getString("product_id"), jsonObject1.getString("user_id"), jsonObject1.getString("name"),
                                    jsonObject1.getString("cat_id"), jsonObject1.getString("sub_id"), jsonObject1.getString("category_name"), jsonObject1.getString("sub_name"),
                                    jsonObject1.getString("description"), jsonObject1.getString("unit"), jsonObject1.getString("set_qty"),
                                    jsonObject1.getString("quantity"), jsonObject1.getString("total_piece"), jsonObject1.getString("price"),
                                    jsonObject1.getString("sales_price"), jsonObject1.getString("total_set_price"),
                                    jsonObject1.getString("total_price"), jsonObject1.getString("Image"), jsonObject1.getString("date_created"),
                                    jsonObject1.getString("cart_created")));

                            GridLayoutManager manager = new GridLayoutManager(context, 1);
                            recycler_cart.setLayoutManager(manager);

                            recycler_cart.setAdapter(new CartAdapter(context, modelCartLists, new CartRefresh() {
                                @Override
                                public void onClick() {
                                    CART_LIST();
                                    modelCartLists.clear();
                                }
                            }));
                            layout_payment.setVisibility(View.VISIBLE);
                        }
                    } else if (jsonObject.getString("Error").equals("0")) {
                        liner_empaty.setVisibility(View.VISIBLE);
                        layout_payment.setVisibility(View.GONE);
                        Toast.makeText(context, "Your Cart is Empty", Toast.LENGTH_SHORT).show();

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
                Log.d("my_param_add", params.toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);


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
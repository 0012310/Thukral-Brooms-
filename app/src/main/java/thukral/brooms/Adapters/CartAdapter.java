package thukral.brooms.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import thukral.brooms.Database.LocalSharedPreferences;
import thukral.brooms.Interface.CartRefresh;
import thukral.brooms.R;
import thukral.brooms.model.modelCartList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    Context context;
    ArrayList<modelCartList> modelCartLists = new ArrayList<>();
    ProgressDialog progressDialog;
    CartRefresh cartRefresh;
    String Qty;


    public CartAdapter(Context context, ArrayList<modelCartList> modelCartLists, CartRefresh cartRefresh) {
        this.context = context;
        this.modelCartLists = modelCartLists;
        this.cartRefresh = cartRefresh;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cart, parent, false);
        ViewHolder viewHolder = new ViewHolder(layoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);

        // holder.tv_set.setText(modelCartLists.get(i).getSet_qty()+" "+"Bag"+" "+"("+modelCartLists.get(i).getQuantity()+" "+"pcs"+")");

        holder.tv_set.setText("1 Bag" + " " + "(" + modelCartLists.get(i).getTotal_piece() + " " + "pcs" + ")");
        holder.tv_qnty.setText("Qty :" + " " + modelCartLists.get(i).getSet_qty() + " " + "Bag");
        holder.tv_set_qty.setText(modelCartLists.get(i).getSet_qty());
        holder.price.setText("Rs" + " " + modelCartLists.get(i).getSales_price() + " * " + modelCartLists.get(i).getQuantity() + " " + "  =  " + modelCartLists.get(i).getTotal_price());
        holder.tv_name.setText(modelCartLists.get(i).getSub_name());
        holder.tv_des.setText(modelCartLists.get(i).getDescription());

        Glide.with(context).load(modelCartLists.get(i).getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher_round)
                .dontAnimate().into(holder.image_cartlist);
        final int[] minteger = {0};


        if (LocalSharedPreferences.getUser_tpe(context).equals("Retailer")) {
            minteger[0] = modelCartLists.get(i).getSet_qty() != null && !modelCartLists.get(i).getSet_qty().isEmpty() ? Integer.parseInt(modelCartLists.get(i).getSet_qty()) : 2;
            holder.tv_set_qty.setText("" + minteger[0]);
        } else {
            minteger[0] = modelCartLists.get(i).getSet_qty() != null && !modelCartLists.get(i).getSet_qty().isEmpty() ? Integer.parseInt(modelCartLists.get(i).getSet_qty()) : 2;
            holder.tv_set_qty.setText("" + minteger[0]);
        }

        holder.minus_quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (LocalSharedPreferences.getUser_tpe(context).equals("Retailer")) {
                    minteger[0] = minteger[0] - 1;
                    minteger[0] = minteger[0] > 2 ? minteger[0] : 2;

                } else {
                    minteger[0] = minteger[0] - 1;
                    minteger[0] = minteger[0] > 1 ? minteger[0] : 1;
                }
                holder.tv_set_qty.setText("" + minteger[0]);
                UPDATE_QTY(modelCartLists.get(i).getProduct_id(), holder, minteger[0]);
                progressDialog.show();


            }
        });

        holder.plus_quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LocalSharedPreferences.getUser_tpe(context).equals("Retailer")) {
                    minteger[0] = minteger[0] + 1;
                    holder.tv_set_qty.setText("" + minteger[0]);
                } else {
                    minteger[0] = minteger[0] + 1;
                    holder.tv_set_qty.setText("" + minteger[0]);

                }
                holder.tv_set_qty.setText("" + minteger[0]);
                UPDATE_QTY(modelCartLists.get(i).getProduct_id(), holder, minteger[0]);
                progressDialog.show();

            }
        });


        holder.layout_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                REMOVE_CART_LIST(modelCartLists.get(i).getProduct_id(), holder);
                progressDialog.show();
            }
        });

        holder.layout_moveto_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MOVE_WISH_LIST(modelCartLists.get(i).getProduct_id(), holder);
                progressDialog.show();
            }
        });

    }

    private void UPDATE_QTY(final String product_id, final ViewHolder holder, final int minteger) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thukralbroom.com/api/cart-add.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                //Toast.makeText(context, "" + response, Toast.LENGTH_SHORT).show();
                Log.d("easrdgh", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String err_code = jsonObject.getString("Error");
                    if (err_code != null && !err_code.isEmpty() && err_code.equals("1")) {
                        refreshList();
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
                params.put("product_id", "" + product_id);
                params.put("quantity", String.valueOf(minteger));
                Log.d("my_param_add", params.toString());

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    private void REMOVE_CART_LIST(final String product_id, final ViewHolder holder) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thukralbroom.com/api/cart-add.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("cart_list_data", response);
                progressDialog.dismiss();


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("Error").equals("1")) {
                        Toast.makeText(context, "Cart Item has been removed", Toast.LENGTH_SHORT).show();
                        modelCartLists.remove(holder.getAdapterPosition());
                        notifyDataSetChanged();
                        refreshList();
                    } else {
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
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", LocalSharedPreferences.getUserid(context));
                params.put("product_id", "" + product_id);
                params.put("action", "remove");
                Log.d("my_param_remove", params.toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);


    }

    public void refreshList() {
        cartRefresh.onClick();
    }

    private void MOVE_WISH_LIST(final String product_id, final ViewHolder holder) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thukralbroom.com/api/wishlist.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Move_WishList_Data", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("Error").equals("1")) {
                        modelCartLists.remove(holder.getAdapterPosition());
                        notifyDataSetChanged();
                        refreshList();
                        Toast.makeText(context, "Cart Item has been Added in Wish List", Toast.LENGTH_SHORT).show();

                    } else {
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
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", LocalSharedPreferences.getUserid(context));
                params.put("product_id", "" + product_id);

                Log.d("my_param_add", params.toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);


    }

    @Override
    public int getItemCount() {
        return modelCartLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_set, tv_qnty, price, tv_set_qty, tv_name, tv_des;
        ImageView image_cartlist;
        LinearLayout layout_remove, layout_moveto_wishlist;

        ImageView minus_quantity, plus_quantity;
        ProgressDialog progressDialog;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_set = itemView.findViewById(R.id.tv_set);
            tv_qnty = itemView.findViewById(R.id.tv_qnty);
            price = itemView.findViewById(R.id.price);
            tv_set_qty = itemView.findViewById(R.id.tv_set_qty);
            minus_quantity = itemView.findViewById(R.id.minus_quantity);
            plus_quantity = itemView.findViewById(R.id.plus_quantity);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_des = itemView.findViewById(R.id.tv_des);

            image_cartlist = itemView.findViewById(R.id.image_cartlist);
            layout_remove = itemView.findViewById(R.id.layout_remove);
            layout_moveto_wishlist = itemView.findViewById(R.id.layout_moveto_wishlist);
        }
    }
}

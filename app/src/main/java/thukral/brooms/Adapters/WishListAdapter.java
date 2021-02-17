package thukral.brooms.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import thukral.brooms.Database.LocalSharedPreferences;
import thukral.brooms.Interface.CartRefresh;
import thukral.brooms.R;
import thukral.brooms.model.modelWishList;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> {
    ProgressDialog progressDialog;
    Context context;
    ArrayList<modelWishList> modelWishLists = new ArrayList<>();
    CartRefresh cartRefresh;

    public WishListAdapter(Context context, ArrayList<modelWishList> modelWishLists, CartRefresh cartRefresh) {
        this.context = context;
        this.modelWishLists = modelWishLists;
        this.cartRefresh = cartRefresh;
    }

    @NonNull
    @Override
    public WishListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_wishlist, parent, false);
        ViewHolder viewHolder = new ViewHolder(layoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final WishListAdapter.ViewHolder holder, final int i) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);

        Glide.with(context).load(modelWishLists.get(i).getImage()).crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher_round)
                .dontAnimate().into(holder.image_wish);


        holder.tv_set.setText("1 Bag" + " " + "(" + modelWishLists.get(i).getQty() + " " + "pcs" + ")");
        holder.tv_name.setText( modelWishLists.get(i).getSub_name());
      holder.tv_price.setText("Rs" + " " + "(" +modelWishLists.get(i).getSales_price() + " * " + modelWishLists.get(i).getQty() + ")" + "  =  " + modelWishLists.get(i).getTotal_set_price());
        holder.tv_des.setText(modelWishLists.get(i).getDescription());

        holder.layout_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ADD_CART_LIST(modelWishLists.get(i).getId(), holder);
                progressDialog.show();
            }
        });
        holder.layout_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                REMOVE_WISH_LIST(modelWishLists.get(i).getId(), holder);
                progressDialog.show();
            }
        });
    }


    private void REMOVE_WISH_LIST(final String product_id, final ViewHolder holder) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thukralbroom.com/api/wishlist.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("remove_wish", response);
                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("Error").equals("1")) {
                        Toast.makeText(context, "Wish Item has been removed", Toast.LENGTH_SHORT).show();
                        modelWishLists.remove(holder.getAdapterPosition());
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
                //   params.put("action", "remove");
                Log.d("my_param_remove", params.toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);


    }

    private void refreshList() {
        cartRefresh.onClick();
    }

    private void ADD_CART_LIST(final String product_id, final ViewHolder holder) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thukralbroom.com/api/cart-add.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("cart_list_data", response);
                progressDialog.dismiss();


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("Error").equals("1")) {
                        Toast.makeText(context, "Wish Item has been Added in Cart List", Toast.LENGTH_SHORT).show();
                        modelWishLists.remove(holder.getAdapterPosition());
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
                params.put("quantity", "1");
                Log.d("my_param_remove", params.toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);


    }

    @Override
    public int getItemCount() {
        return modelWishLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_wish;
        LinearLayout layout_remove, layout_add_cart;
        TextView tv_set ,tv_name ,tv_price ,tv_des;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_wish = itemView.findViewById(R.id.image_wish);
            layout_remove = itemView.findViewById(R.id.layout_remove);
            layout_add_cart = itemView.findViewById(R.id.layout_add_cart);
            tv_set=itemView.findViewById(R.id.tv_set);
            tv_name=itemView.findViewById(R.id.tv_name);
            tv_price=itemView.findViewById(R.id.tv_price);
            tv_des=itemView.findViewById(R.id.tv_des);


        }
    }
}

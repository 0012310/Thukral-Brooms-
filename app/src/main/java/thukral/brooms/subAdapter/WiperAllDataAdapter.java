package thukral.brooms.subAdapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import thukral.brooms.Activities.BroomsDetails;
import thukral.brooms.Database.LocalSharedPreferences;
import thukral.brooms.Interface.Img_addWish_Onclick;
import thukral.brooms.R;
import thukral.brooms.sabActivities.WipersSubActivity;
import thukral.brooms.subModel.modelallWiperList;
import thukral.brooms.subModel.modelsubtosubList;

public class WiperAllDataAdapter extends RecyclerView.Adapter<WiperAllDataAdapter.ViewHolder> {

    Context context;
    ArrayList<modelallWiperList>  modelsubtosubListArrayList = new ArrayList<>();
    private int flag = 0;
    private WipersSubActivity cl;

    public WiperAllDataAdapter(Context context, ArrayList<modelallWiperList> modelsubtosubListArrayList, Img_addWish_Onclick img_addWish_onclick) {

        this.context = context;
        this.modelsubtosubListArrayList = modelsubtosubListArrayList;
    }
    
    @NonNull
    @Override
    public WiperAllDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_brooms, parent, false);
        WiperAllDataAdapter.ViewHolder viewHolder = new WiperAllDataAdapter.ViewHolder(layoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final WiperAllDataAdapter.ViewHolder holder, final int i) {
        holder.name.setText(modelsubtosubListArrayList.get(i).getName());
        holder.tv_description.setText(modelsubtosubListArrayList.get(i).getDescription());
        holder.sales_price.setText("Rs" + " " + modelsubtosubListArrayList.get(i).getSales_price());
        holder.reg_price.setText("Rs" + " " + modelsubtosubListArrayList.get(i).getRegular_price());

        cl = (WipersSubActivity) context;

        Glide.with(context).load(modelsubtosubListArrayList.get(i).getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.logo_bg)
                .dontAnimate().into(holder.thumbnail);


        String data = modelsubtosubListArrayList.get(i).getWishlist();

        holder.ic_wishlist.setImageResource(data != null && data.equals("true") ? R.drawable.ic_favorite_red_24dp : R.drawable.favorite);

      holder.ic_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 0) {
                    ADD_WishList(modelsubtosubListArrayList.get(i).getId(), holder);
                    flag = 1;
               //     holder.progressDialog.show();
                } else {
                    ADD_WishList(modelsubtosubListArrayList.get(i).getId(), holder);
                    flag = 0;
                  //  holder.progressDialog.show();
                }
            }
        });


        holder.layout_item_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, BroomsDetails.class);
                intent.putExtra("PRO_ID", modelsubtosubListArrayList.get(i).getId());
                context.startActivity(intent);
            }
        });

    }

    public interface click {
        public void myClick();
    }

    private void ADD_WishList(final String ID, final ViewHolder holder) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://thukralbroom.com/api/wishlist.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, "" + response, Toast.LENGTH_SHORT).show();
                Log.d("Data_Wish_data", response);
              //  holder.progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("message").equals("Wishlist has been added successfully.")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        String adfn = jsonObject.getString("message");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            Toast.makeText(context, "Item Added in your Wish List", Toast.LENGTH_SHORT).show();
                        }
                        holder.ic_wishlist.setImageResource(R.drawable.ic_favorite_red_24dp);
                        cl.myClick();
                    } else if (jsonObject.getString("message").equals("Wishlist has been removed successfully.")) {

                        Toast.makeText(context, "Item Removed from your Wish List", Toast.LENGTH_SHORT).show();
                        holder.ic_wishlist.setImageResource(R.drawable.favorite);
                        cl.myClick();
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

                Toast.makeText(context, "" + error, Toast.LENGTH_SHORT).show();
             //   holder.progressDialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("product_id", ID);
                params.put("user_id", "" + LocalSharedPreferences.getUserid(context));
                Log.d("my_param", params.toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }



    @Override
    public int getItemCount() {
        return modelsubtosubListArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ProgressDialog progressDialog;
        ImageView thumbnail;
        TextView name, tv_description, sales_price, reg_price;
        ImageView ic_wishlist;
        LinearLayout layout_item_parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ic_wishlist = itemView.findViewById(R.id.ic_wishlist);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            name = itemView.findViewById(R.id.name);
            tv_description = itemView.findViewById(R.id.tv_description);
            sales_price = itemView.findViewById(R.id.sales_price);
            reg_price = itemView.findViewById(R.id.reg_price);
            layout_item_parent = itemView.findViewById(R.id.layout_item_parent);

        }
    }
}

package thukral.brooms.Adapters;

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
import thukral.brooms.Activities.MainActivity;
import thukral.brooms.Database.LocalSharedPreferences;
import thukral.brooms.R;
import thukral.brooms.model.modelAllBrooms;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private int flag = 0;
    Context context;
    ArrayList<modelAllBrooms> modelAllBroomsArrayList = new ArrayList<>();

    public HomeAdapter(Context context, ArrayList<modelAllBrooms> modelAllBroomsArrayList) {
        this.context = context;
        this.modelAllBroomsArrayList = modelAllBroomsArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_home, parent, false);
        ViewHolder viewHolder = new ViewHolder(layoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {

        holder.progressDialog = new ProgressDialog(context);
        holder.progressDialog.setCancelable(true);
        holder.progressDialog.setMessage("Loading");
        holder.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        holder.progressDialog.setProgress(0);

        /*holder.name.setText(modelAllBroomsArrayList.get(i).getName());
        holder.tv_description.setText(modelAllBroomsArrayList.get(i).getDescription());
        holder.sales_price.setText("Rs" + " " + modelAllBroomsArrayList.get(i).getSales_price());
        holder.reg_price.setText("Rs" + " " + modelAllBroomsArrayList.get(i).getRegular_price());

*/

        Glide.with(context).load(modelAllBroomsArrayList.get(i).getImage()).crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher_round)
                .dontAnimate().into(holder.thumbnail);

        holder.layout_item_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BroomsDetails.class);
                intent.putExtra("PRO_ID", modelAllBroomsArrayList.get(i).getId());
                context.startActivity(intent);
            }
        });


    }


    @Override
    public int getItemCount() {
        return modelAllBroomsArrayList.size();
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

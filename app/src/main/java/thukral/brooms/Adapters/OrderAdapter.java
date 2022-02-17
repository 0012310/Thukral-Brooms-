package thukral.brooms.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import thukral.brooms.R;
import thukral.brooms.model.modelOrder;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    Context context;
    ArrayList<modelOrder> modelOrderArrayList = new ArrayList<>();

    public OrderAdapter(Context context, ArrayList<modelOrder> modelOrderArrayList) {
        this.context = context;
        this.modelOrderArrayList = modelOrderArrayList;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_order_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(layoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, final int i) {
        holder.progressDialog = new ProgressDialog(context);
        holder.progressDialog.setCancelable(true);
        holder.progressDialog.setMessage("Loading");
        holder.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        holder.progressDialog.setProgress(0);

        Glide.with(context).load(modelOrderArrayList.get(i).getImage()).crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher_round)
                .dontAnimate().into(holder.image_orderd);
        holder.tv_price.setText("Rs" + " " + modelOrderArrayList.get(i).getSales_price());
        holder.tv_distributorDetails.setText("Distributor Details :" + modelOrderArrayList.get(i).getDistributor_name() +
                "," + modelOrderArrayList.get(i).getDistributor_zone() + "," + modelOrderArrayList.get(i).getDistributor_state());
        holder.tv_order_id.setText("ORDER NO :" + " " + modelOrderArrayList.get(i).getOrder_id());

        holder.prod_name.setText(modelOrderArrayList.get(i).getName());
        holder.tv_delevirychrg.setText("Delivery charge : Free");
        holder.tv_set.setText("1 Bag" + " " + "(" + modelOrderArrayList.get(i).getTotal_piece() + " " + "pcs" + ")");
        holder.tv_del_date.setText("Delivered :" + " " + modelOrderArrayList.get(i).getDate_created());

        if (modelOrderArrayList.get(i).getInvoice() != null) {


            holder.layout_download_invoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Toast.makeText(context, "Hi", Toast.LENGTH_SHORT).show();
                    Uri uri = Uri.parse(modelOrderArrayList.get(i).getInvoice()); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    Log.d("jfdshf", "" + uri);  //invoice.php?orderid=OD018
                    context.startActivity(intent);

                }
            });

        } else {
            Toast.makeText(context, "No invoice generated", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return modelOrderArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ProgressDialog progressDialog;
        ImageView image_orderd;
        TextView tv_distributorDetails, tv_price, tv_order_id, tv_invoice, prod_name, tv_delevirychrg, tv_set, tv_del_date;
        LinearLayout layout_download_invoice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_orderd = itemView.findViewById(R.id.image_orderd);
            tv_distributorDetails = itemView.findViewById(R.id.tv_distributorDetails);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_invoice = itemView.findViewById(R.id.tv_invoice);
            tv_order_id = itemView.findViewById(R.id.tv_order_id);
            layout_download_invoice = itemView.findViewById(R.id.layout_download_invoice);
            prod_name = itemView.findViewById(R.id.prod_name);
            tv_delevirychrg = itemView.findViewById(R.id.tv_delevirychrg);
            tv_set = itemView.findViewById(R.id.tv_set);
            tv_del_date = itemView.findViewById(R.id.tv_del_date);


        }
    }
}

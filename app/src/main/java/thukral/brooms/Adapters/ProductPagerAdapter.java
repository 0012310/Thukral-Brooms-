package thukral.brooms.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import thukral.brooms.R;

public class ProductPagerAdapter extends PagerAdapter {

    private ArrayList<String> images;
    private LayoutInflater inflater;
    private Context context;
    private Object pro_id;


    public ProductPagerAdapter(Context context, ArrayList<String> images, String pro_id) {
        this.pro_id = pro_id;
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View myImageLayout = inflater.inflate(R.layout.slide_product, view, false);
        ImageView myImage = (ImageView) myImageLayout.findViewById(R.id.image_product);



        Glide.with(context).load(images.get(position))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.logo_bg)
                .dontAnimate().into(myImage);



        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(context, ViewPagerActivity.class);
                intent.putExtra("PRODUCT_ID", "" + pro_id);
                context.startActivity(intent);*/
            }
        });

        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
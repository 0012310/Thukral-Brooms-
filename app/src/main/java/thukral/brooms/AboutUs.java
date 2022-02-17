package thukral.brooms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AboutUs extends AppCompatActivity {

    Context context;
    RelativeLayout relative_call;
    LinearLayout liner_privacy_policy;
    TextView txtTitle, textabout;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("About Us");
        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        textabout = findViewById(R.id.textabout);

    }
}
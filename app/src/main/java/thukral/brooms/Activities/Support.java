package thukral.brooms.Activities;

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

import thukral.brooms.R;

public class Support extends AppCompatActivity {
    Context context;
    RelativeLayout relative_call;
    LinearLayout liner_privacy_policy;
    TextView txtTitle;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("Term & Conditions");
        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        relative_call = findViewById(R.id.relative_call);
        relative_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(getContext(), "Calling", Toast.LENGTH_SHORT).show();
                String phone = "9899299237";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });
        liner_privacy_policy = findViewById(R.id.liner_privacy_policy);
        liner_privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(Support.this, Term_Conditions.class);
                startActivity(intent);*/
            }
        });

    }
}
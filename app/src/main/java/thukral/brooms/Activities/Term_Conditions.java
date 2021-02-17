package thukral.brooms.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import thukral.brooms.R;

public class Term_Conditions extends AppCompatActivity {
    WebView web_view_term, web_view_privacy, web_view_payment, web_view_return_policy;
    Context context;
    ProgressDialog progressDialog;

    TextView txtTitle;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term__conditions);
        context = Term_Conditions.this;
        txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("Term & Conditions");
        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        web_view_term = findViewById(R.id.web_view_term);
        web_view_term.loadUrl("https://www.brkonline.in/brk/api/terms.php");

        web_view_privacy = findViewById(R.id.web_view_privacy);
        web_view_privacy.loadUrl("https://www.brkonline.in/brk/api/privacy-policy.php");

        web_view_payment = findViewById(R.id.web_view_payment);
        web_view_payment.loadUrl("https://www.brkonline.in/brk/api/payments.php");

        web_view_return_policy = findViewById(R.id.web_view_return_policy);
        //  web_view_return_policy.loadUrl("https://www.brkonline.in/brk/api/returns-policy.php");


        web_view_return_policy.setFocusableInTouchMode(true);
        WebSettings settings = web_view_return_policy.getSettings();
        settings.setJavaScriptEnabled(true);

        web_view_return_policy.setWebViewClient(new WebViewClient() {
            ProgressDialog mProgress = null;

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }


            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgress = new ProgressDialog(Term_Conditions.this);
                mProgress.setTitle("Please Wait!");
                mProgress.setMessage("Loading...");
                mProgress.show();

            }

            public void onPageFinished(WebView view, String url) {
                if (mProgress.isShowing()) {
                    mProgress.hide();
                    mProgress.dismiss();
                }
            }
        });

        web_view_return_policy.loadUrl("https://www.brkonline.in/brk/api/returns-policy.php");
    }


}


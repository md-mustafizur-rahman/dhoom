package dhoombd.com;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;

public class HomeActivity extends AppCompatActivity {
    Button RefreshBtn;
    String webViewUrl = "https://dhoombd.com/";
    LinearLayout webViewPageLayout;
    ConstraintLayout noInternetPageLayout;
    SwipeRefreshLayout swipeRefreshLayout;
    WebView webView;
    ViewTreeObserver.OnScrollChangedListener myOnScrollChangedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        //Fetch item via to there id
        RefreshBtn = findViewById(R.id.refreshBtn);
        webView = findViewById(R.id.webView);
        webViewPageLayout = findViewById(R.id.webViewPage);
        noInternetPageLayout = findViewById(R.id.noInternetPage);
//        yesBtn=findViewById(R.id.yesBtn);
//        noBtn=findViewById(R.id.noBtn);


        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        webView.loadUrl(webViewUrl);
        swipeRefreshLayout = findViewById(R.id.reload);

        swipeRefreshLayout.getViewTreeObserver().addOnScrollChangedListener(myOnScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        if (webView.getScrollY() == 0) {
                            swipeRefreshLayout.setEnabled(true);
                        } else {
                            swipeRefreshLayout.setEnabled(false);
                        }
                    }
                }

        );


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkInternet();
                webView.reload();
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                checkInternet();
                super.onReceivedError(view, request, error);
            }
        });


        RefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInternet();
            }
        });


    }

    private void checkInternet() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileData = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        NetworkInfo info = manager.getActiveNetworkInfo();
//        if(info !=null)
//        {
//            if(info.isConnected())
//            {
//                webView.reload();
//                webViewPageLayout.setVisibility(View.VISIBLE);
//                noInternetPageLayout.setVisibility(View.GONE);
//
//            }
//            else
//            {
//                webView.reload();
//                webViewPageLayout.setVisibility(View.GONE);
//                noInternetPageLayout.setVisibility(View.VISIBLE);
//
//            }
//        }
//        else
//        {
//            webViewPageLayout.setVisibility(View.GONE);
//            noInternetPageLayout.setVisibility(View.VISIBLE);
//        }

        if (mobileData.isConnected() || wifi.isConnected()) {
            webView.reload();

            webViewPageLayout.setVisibility(View.VISIBLE);
            noInternetPageLayout.setVisibility(View.GONE);

        } else {
            webViewPageLayout.setVisibility(View.GONE);
            noInternetPageLayout.setVisibility(View.VISIBLE);
            webView.reload();
        }


    }

    @Override
    public void onBackPressed() {
        if (webView.isFocused() && webView.canGoBack()) {
            webView.goBack();
        } else {


            Dialog dialog = new Dialog(this);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
            dialog.setContentView(R.layout.custom_dialog_layout);

            Button yesBtn = dialog.findViewById(R.id.yesBtn);
            Button noBtn = dialog.findViewById(R.id.noBtn);
            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finishAffinity();
                }
            });
            noBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();


//          Dialog dialog = new AlertDialog.Builder(this)
//                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            finish();
//                        }
//                    })
//                    .setNegativeButton("No", null)
//                    .show();
//          dialog.setContentView(R.layout.custom_dialog_layout);

        }
    }


}
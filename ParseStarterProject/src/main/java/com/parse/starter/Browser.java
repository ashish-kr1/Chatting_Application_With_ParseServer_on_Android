package com.parse.starter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

public class Browser extends AppCompatActivity {
    WebView webView;
    ProgressBar progressBar;
    EditText editText;
    ImageButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        setTitle("Looker");

        editText=(EditText)findViewById(R.id.editText);
        button=(ImageButton)findViewById(R.id.button);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        webView=(WebView)findViewById(R.id.wbView);

        if(savedInstanceState!=null){
            webView.restoreState(savedInstanceState);
        }else{
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setBuiltInZoomControls(false);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webView.setBackgroundColor(Color.TRANSPARENT);
            webView.setWebViewClient(new ourViewClient());
            webView.setWebChromeClient(new WebChromeClient(){
                @Override
                public void onProgressChanged(WebView view,int progress){
                    progressBar.setProgress(progress);
                    if (progress<100 && progressBar.getVisibility()==ProgressBar.GONE){
                        progressBar.setVisibility(ProgressBar.VISIBLE);
                    }
                    if(progress==100){
                        progressBar.setVisibility(ProgressBar.GONE);
                    }
                }
            });

        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                webView.loadUrl("https://"+editText.getText().toString());
                editText.setText("");
            }
        });
    }
    public class ourViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view,String url){
            view.loadUrl(url);
            CookieManager.getInstance().setAcceptCookie(true);
            return true;
        }@Override
        public void onPageFinished(WebView view,String url){
            super.onPageFinished(view,url);
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu2,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.back:
                if (webView.canGoBack()){
                    webView.goBack();
                }return true;
            case R.id.forward:
                if (webView.canGoForward()){
                    webView.goForward();
                }return true;
            case R.id.reload:
                if(webView.canGoBackOrForward(0)){
                    webView.reload();
                }
            case R.id.home:
                InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

                webView.loadUrl("https://google.com");
                editText.setText("");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}

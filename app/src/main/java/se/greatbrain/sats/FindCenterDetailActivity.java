package se.greatbrain.sats;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.webkit.WebView;

/**
 * Created by aymenarbi on 30/04/15.
 */
public class FindCenterDetailActivity extends ActionBarActivity
{
    private WebView mWebView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_center_detail);

        mWebView = (WebView) findViewById(R.id.find_center_detail_web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        WebViewClientImpl webViewClient = new WebViewClientImpl(this);
        mWebView.setWebViewClient(webViewClient);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String centerUrl = extras.getString("centerUrl");
            mWebView.loadUrl(centerUrl);
        }

    }
}

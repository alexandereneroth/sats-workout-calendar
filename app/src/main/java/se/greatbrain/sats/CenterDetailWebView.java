package se.greatbrain.sats;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CenterDetailWebView extends WebViewClient
{
    public CenterDetailWebView() {}

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url)
    {
        return false;
    }
}

package se.greatbrain.sats;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CenterDetailWebView extends WebViewClient
{
    private final Activity activity;

    public CenterDetailWebView(Activity activity)
    {
        this.activity = activity;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url)
    {
        if(url.startsWith("mailto:"))
        {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
            activity.startActivity(intent);
        }
        else if(url.startsWith("tel:"))
        {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
            activity.startActivity(intent);
        }
        else
        {
            return false;
        }

        return true;
    }
}

package se.greatbrain.sats.ui.center;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CenterWebView extends WebViewClient
{
    private final Activity activity;

    public CenterWebView(Activity activity)
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

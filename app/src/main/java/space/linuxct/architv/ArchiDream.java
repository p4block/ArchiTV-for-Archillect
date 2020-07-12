package space.linuxct.architv;

import android.service.dreams.DreamService;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ArchiDream extends DreamService {
    private WebView backgroundView;
    private String upstreamURL = "https://archillect.com/tv";

    @Override
    public void onDreamingStarted() {
        super.onDreamingStarted();
        setContentView(R.layout.activity_main);

        backgroundView = findViewById(R.id.backgroundview);

        backgroundView.setWebViewClient(new WebViewClient(){});

        WebSettings webSettings = backgroundView.getSettings();
        webSettings.setJavaScriptEnabled(false);
        backgroundView.loadUrl(upstreamURL);
    }

    @Override
    public void onDreamingStopped(){
        backgroundView.destroy();
        backgroundView = null;
    }
}

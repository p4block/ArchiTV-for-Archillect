package space.linuxct.architv;

import android.app.Activity;
import android.os.Bundle;
import android.os.PowerManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    private WebView mWebView;
    private String upstreamURL = "https://archillect.com/tv";
    private PowerManager.WakeLock wakeLock;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = findViewById(R.id.backgroundview);

        mWebView.setWebViewClient(new WebViewClient(){});

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "ArchiTV:wakelock");

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl(upstreamURL);
    }

    @Override
    public void onResume() {
        if(wakeLock != null){
            if(!wakeLock.isHeld()){
                wakeLock.acquire();
            }
        }
        mWebView.resumeTimers();
        mWebView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        if(wakeLock != null){
            if(wakeLock.isHeld()){
                wakeLock.release();
            }
        }
        mWebView.onPause();
        mWebView.pauseTimers();
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        if(wakeLock != null){
            if(wakeLock.isHeld()){
                wakeLock.release();
            }
        }
        mWebView.destroy();
        mWebView = null;
        super.onDestroy();
    }
}

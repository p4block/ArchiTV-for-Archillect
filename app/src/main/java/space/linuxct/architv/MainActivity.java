package space.linuxct.architv;

import android.app.Activity;
import android.os.Bundle;
import android.os.PowerManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

    public WebView backgroundView;

    String url;

    private Runnable mRunnable;

    public static boolean donette = false;

    PowerManager.WakeLock wakeLock;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backgroundView = findViewById(R.id.backgroundview);

        backgroundView.setWebViewClient(new WebViewClient(){});

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Archillect:wakelock");
        wakeLock.acquire();

        url = "https://archillect.com/tv";
        backgroundView.loadUrl(url);

        mRunnable = new Runnable() {
            @Override
            public void run() {
                if (!donette) {
                    donette = true;
                    backgroundView.postDelayed(this, 7000);
                    WebSettings webSettings = backgroundView.getSettings();
                    webSettings.setJavaScriptEnabled(true);
                    backgroundView.loadUrl(url);

                }
            }
        };
        backgroundView.postDelayed(mRunnable,7000);
    }


    @Override
    public void onResume() {
        if(wakeLock != null){
            if(!wakeLock.isHeld()){
                wakeLock.acquire();
            }
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if(wakeLock != null){
            if(wakeLock.isHeld()){
                wakeLock.release();
            }
        }
        super.onPause();
    }


    @Override
    protected void onDestroy(){
        if(wakeLock != null){
            if(wakeLock.isHeld()){
                wakeLock.release();
            }
        }
        super.onDestroy();
    }


}

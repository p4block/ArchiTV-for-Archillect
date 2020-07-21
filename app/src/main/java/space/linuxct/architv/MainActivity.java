package space.linuxct.architv;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaMetadata;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.os.PowerManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    WebView mWebView;
    private String upstreamURL = BuildConfig.SERVER_URL;
    private PowerManager.WakeLock wakeLock;
    public MediaSession mSession;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handleMediaSession();
        handleAudioFocus();
        handleWebView();


        if (!mSession.isActive()) {
            mSession.setActive(true);
        }

    }

    private void handleWebView(){
        mWebView = findViewById(R.id.backgroundview);
        mWebView.setWebViewClient(new WebViewClient(){});
        mWebView.setOnTouchListener(null);
        mWebView.setEnabled(false);

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "ArchiTV:wakelock");

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setNeedInitialFocus(false);

        webSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl(upstreamURL);
    }

    private void handleMediaSession(){
        mSession = new MediaSession(this, "ArchiTV");
        mSession.setCallback(new MediaSessionCallback(this));
        mSession.setFlags(MediaSession.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);
        PlaybackState state = new PlaybackState.Builder()
                .setActions(PlaybackState.ACTION_PAUSE)
                .setState(PlaybackState.STATE_PLAYING, 0, 1.0f)
                .build();
        mSession.setPlaybackState(state);
        MediaMetadata.Builder metadataBuilder = new MediaMetadata.Builder();
        metadataBuilder.putString(MediaMetadata.METADATA_KEY_TITLE, getString(R.string.title));
        metadataBuilder.putString(MediaMetadata.METADATA_KEY_ARTIST, getString(R.string.desc));
        mSession.setMetadata(metadataBuilder.build());
    }

    private void handleAudioFocus(){
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        AudioManager.OnAudioFocusChangeListener changedListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                    mSession.setActive(true);
                }
                else if (focusChange == AudioManager.AUDIOFOCUS_REQUEST_FAILED) {
                    mSession.setActive(false);
                }
            }
        };
        audioManager.requestAudioFocus(changedListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
    }

    @Override
    public void onResume() {
        if(wakeLock != null){
            if(!wakeLock.isHeld()){
                wakeLock.acquire();
            }
        }
        if (!mSession.isActive()) {
            mSession.setActive(true);
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
        if (mSession.isActive()) {
            mSession.setActive(false);
        }
        super.onDestroy();
    }
}

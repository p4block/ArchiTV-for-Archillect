package space.linuxct.architv;

import android.media.session.MediaSession;

public class MediaSessionCallback extends MediaSession.Callback {
    private MainActivity mMainActivity;

    public MediaSessionCallback(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    @Override
    public void onPause(){
        mMainActivity.onDestroy();
    }

    @Override
    public void onStop(){
        mMainActivity.onDestroy();
    }
}

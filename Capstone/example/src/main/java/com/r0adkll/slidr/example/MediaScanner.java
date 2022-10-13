package com.r0adkll.slidr.example;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;


public class MediaScanner {
    private Context mContext;
    private String mPath;
    private MediaScannerConnection mMediaScanner;
    private MediaScannerConnection.MediaScannerConnectionClient mMediaScannerClient;
    public static MediaScanner newInstance(Context context) {
        return new MediaScanner(context);
    }
    private MediaScanner(Context context) {
        mContext = context;
    }
    public void mediaScanning(final String path) {
        if (mMediaScanner == null) {
            mMediaScannerClient = new MediaScannerConnection.MediaScannerConnectionClient() {
                @Override
                public void onMediaScannerConnected() {
                    mMediaScanner.scanFile(mPath, null);
                }
                @Override
                public void onScanCompleted(String path, Uri uri) {
                    mMediaScanner.disconnect();
                }
            };
            mMediaScanner = new MediaScannerConnection(mContext, mMediaScannerClient);
        }
        mPath = path;
        mMediaScanner.connect();
    }
}

package com.example.playground.cache;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamEncoder implements Encoder<InputStream> {
    private static final String TAG = "StreamEncoder";
    private static final int DEFAULT_BUFFER_SIZE_BYTES = 64 * 1024;

    @Override
    public boolean encode(@NonNull InputStream data, @NonNull File file) {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE_BYTES];
        boolean success = false;
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            int read;
            while ((read = data.read(buffer)) != -1) {
                os.write(buffer, 0, read);
            }
            os.close();
            success = true;
        } catch (IOException e) {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "Failed to encode data onto the OutputStream", e);
            }
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    // Do nothing.
                }
            }
        }
        return success;
    }
}

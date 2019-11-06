/*
 * Copyright (c) 2013. Bump Technologies Inc. All Rights Reserved.
 */

package com.example.playground.cache;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

/**
 * The default DiskCache implementation. There must be no more than one active instance for a given
 * directory at a time.
 *
 * @see #get(java.io.File, long)
 */
public class DefaultDiskCache implements DiskCache {
    private static final String TAG = "DefaultDiskCache";

    private static final int APP_VERSION = 1;
    private static final int VALUE_COUNT = 1;
    private static DefaultDiskCache wrapper;

    private final File directory;
    private final long maxSize;
    private final DiskCacheWriteLocker writeLocker = new DiskCacheWriteLocker();
    private DiskLruCache diskLruCache;

    /**
     * Get a DiskCache in the given directory and size. If a disk cache has already been created with
     * a different directory and/or size, it will be returned instead and the new arguments will be
     * ignored.
     *
     * @param directory The directory for the disk cache
     * @param maxSize The max size for the disk cache
     * @return The new disk cache with the given arguments, or the current cache if one already exists
     * @deprecated Use {@link #create(File, long)} to create a new cache with the specified arguments.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public static synchronized DiskCache get(File directory, long maxSize) {
        // TODO calling twice with different arguments makes it return the cache for the same
        // directory, it's public!
        if (wrapper == null) {
            wrapper = new DefaultDiskCache(directory, maxSize);
        }
        return wrapper;
    }

    /**
     * Create a new DiskCache in the given directory with a specified max size.
     *
     * @param directory The directory for the disk cache
     * @param maxSize The max size for the disk cache
     * @return The new disk cache with the given arguments
     */
    @SuppressWarnings("deprecation")
    public static DiskCache create(File directory, long maxSize) {
        return new DefaultDiskCache(directory, maxSize);
    }

    /** @deprecated Do not extend this class. */
    @Deprecated
    // Deprecated public API.
    @SuppressWarnings({"WeakerAccess", "DeprecatedIsStillUsed"})
    protected DefaultDiskCache(File directory, long maxSize) {
        this.directory = directory;
        this.maxSize = maxSize;
    }

    private synchronized DiskLruCache getDiskCache() throws IOException {
        if (diskLruCache == null) {
            diskLruCache = DiskLruCache.open(directory, APP_VERSION, VALUE_COUNT, maxSize);
        }
        return diskLruCache;
    }

    @Override
    public File get(@NotNull String key) {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "Get: Obtained: " + key + " for for Key: " + key);
        }
        File result = null;
        try {
            // It is possible that the there will be a put in between these two gets. If so that shouldn't
            // be a problem because we will always put the same value at the same key so our input streams
            // will still represent the same data.
            final DiskLruCache.Value value = getDiskCache().get(key);
            if (value != null) {
                result = value.getFile(0);
            }
        } catch (IOException e) {
            if (Log.isLoggable(TAG, Log.WARN)) {
                Log.w(TAG, "Unable to get from disk cache", e);
            }
        }
        return result;
    }

    @Override
    public void put(@NotNull String key, @NotNull Writer writer) {
        // We want to make sure that puts block so that data is available when put completes. We may
        // actually not write any data if we find that data is written by the time we acquire the lock.
        writeLocker.acquire(key);
        try {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "Put: Obtained: " + key + " for for Key: " + key);
            }
            try {
                // We assume we only need to put once, so if data was written while we were trying to get
                // the lock, we can simply abort.
                DiskLruCache diskCache = getDiskCache();
                DiskLruCache.Value current = diskCache.get(key);
                if (current != null) {
                    return;
                }

                DiskLruCache.Editor editor = diskCache.edit(key);
                if (editor == null) {
                    throw new IllegalStateException("Had two simultaneous puts for: " + key);
                }
                try {
                    File file = editor.getFile(0);
                    if (writer.write(file)) {
                        editor.commit();
                    }
                } finally {
                    editor.abortUnlessCommitted();
                }
            } catch (IOException e) {
                if (Log.isLoggable(TAG, Log.WARN)) {
                    Log.w(TAG, "Unable to put to disk cache", e);
                }
            }
        } finally {
            writeLocker.release(key);
        }
    }

    @Override
    public void delete(@NotNull String key) {
        try {
            getDiskCache().remove(key);
        } catch (IOException e) {
            if (Log.isLoggable(TAG, Log.WARN)) {
                Log.w(TAG, "Unable to delete from disk cache", e);
            }
        }
    }

    @Override
    public synchronized void clear() {
        try {
            getDiskCache().delete();
        } catch (IOException e) {
            if (Log.isLoggable(TAG, Log.WARN)) {
                Log.w(TAG, "Unable to clear disk cache or disk cache cleared externally", e);
            }
        } finally {
            // Delete can close the cache but still throw. If we don't null out the disk cache here, every
            // subsequent request will try to act on a closed disk cache and fail. By nulling out the disk
            // cache we at least allow for attempts to open the cache in the future. See #2465.
            resetDiskCache();
        }
    }

    private synchronized void resetDiskCache() {
        diskLruCache = null;
    }
}

package com.example.playground.cache

import java.io.File

/** An interface for writing to and reading from a disk cache. */
interface DiskCache {

    /** An interface to actually write data to a key in the disk cache.  */
    interface Writer {
        /**
         * Writes data to the file and returns true if the write was successful and should be committed,
         * and false if the write should be aborted.
         *
         * @param file The File the Writer should write to.
         */
        fun write(file: File): Boolean
    }

    /**
     * Get the cache for the value at the given key.
     *
     *
     * Note - This is potentially dangerous, someone may write a new value to the file at any point
     * in time and we won't know about it.
     *
     * @param key The key in the cache.
     * @return An InputStream representing the data at key at the time get is called.
     */
    fun get(key: String): File?

    /**
     * Write to a key in the cache. [Writer] is used so that the cache implementation can
     * perform actions after the write finishes, like commit (via atomic file rename).
     *
     * @param key The key to write to.
     * @param writer An interface that will write data given an OutputStream for the key.
     */
    fun put(key: String, writer: Writer)


    /**
     * Remove the key and value from the cache.
     *
     * @param key The key to remove.
     */
    // Public API.
    fun delete(key: String)

    /** Clear the cache.  */
    fun clear()
}
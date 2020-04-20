package com.example.imageloader;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.CipherSuite;
import okhttp3.Handshake;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;

/**
 * Prints TLS Version and Cipher Suite for SSL Calls through OkHttp3
 */
public final class SSLHandshakeInterceptor implements okhttp3.Interceptor {

    private static final String TAG = "Glide-OkHttp3-SSL";

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request request = chain.request();
        final String host = request.url().host();
        final Response response = chain.proceed(chain.request());
        printTlsAndCipherSuiteInfo(response, host);
        return response;
    }

    private void printTlsAndCipherSuiteInfo(Response response, String host) {
        if (response != null && host != null) {
            Handshake handshake = response.handshake();
            if (handshake != null) {
                final CipherSuite cipherSuite = handshake.cipherSuite();
                final TlsVersion tlsVersion = handshake.tlsVersion();

                Log.v(TAG, "Host: " + host + ", TLS: " + tlsVersion + ", CipherSuite: " + cipherSuite);
            }
        }
    }
}
package com.lasantamadre.sastoken;

import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class SharedAccessSignature {

    private static final String TAG = "SharedAccessToken";

    private final String endpoint;
    private final String sharedAccessKey;
    private final String sharedAccessKeyName;

    // Microsoft has a weird way to share business logic between her environment.
    // IoT Hub uses a key with Base64 and Service Bus not.
    // You can check the SAS Policy.
    private final boolean isIoTHub;

    private String sas;

    private SharedAccessSignature(@NonNull Builder builder) {
        this.endpoint = builder.endpoint;
        this.sharedAccessKey = builder.sharedAccessKey;
        this.sharedAccessKeyName = builder.sharedAccessKeyName;
        this.isIoTHub = builder.isIoTHub;
    }

    public String get() {
        if (sas == "" || sas == null)
            return sas = getSas();

        return sas;
    }

    private String getSas() {
        long epoch = System.currentTimeMillis() / 1000L;
        int hour = 60 * 60;
        String expiry = Long.toString(epoch + hour);

        String sasToken = null;

        try {
            String stringToSign = URLEncoder.encode(this.endpoint, StandardCharsets.UTF_8.name()) + "\n" + expiry;

            sasToken = String.format("SharedAccessSignature sr=%s&sig=%s&se=%s&skn=%s",
                    URLEncoder.encode(this.endpoint, StandardCharsets.UTF_8.name()),
                    URLEncoder.encode(encode(this.sharedAccessKey, stringToSign), StandardCharsets.UTF_8.name()),
                    expiry,
                    this.sharedAccessKeyName);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sasToken;
    }

    private String encode(String key, String data) throws Exception {
        Mac sha = Mac.getInstance("HMACSHA256");
        byte[] lKey = this.isIoTHub ? Base64.decode(key.getBytes(StandardCharsets.UTF_8.name()), Base64.NO_WRAP) : key.getBytes(StandardCharsets.UTF_8.name());
        SecretKeySpec secretKey = new SecretKeySpec(lKey, sha.getAlgorithm());
        sha.init(secretKey);

        byte[] hash = sha.doFinal(data.getBytes(StandardCharsets.UTF_8.name()));

        return Base64.encodeToString(hash, Base64.NO_WRAP);
    }

    // region: Inner Class
    public static final class Builder {
        private String sharedAccessKeyName;
        private String sharedAccessKey;
        private String endpoint;
        private boolean isIoTHub;

        public Builder setSharedAccessKeyName(String sharedAccessKeyName) {
            this.sharedAccessKeyName = sharedAccessKeyName;
            return this;
        }

        public Builder setSharedAccessKey(String sharedAccessKey) {
            this.sharedAccessKey = sharedAccessKey;
            return this;
        }

        public Builder setEndpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public Builder setIoTHub(boolean ioTHub) {
            this.isIoTHub = ioTHub;
            return this;
        }

        public SharedAccessSignature build() {
            return new SharedAccessSignature(this);
        }
    }

    // Inner Class
}
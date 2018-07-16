package com.lasantamadre.sastoken;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class SASTest {
    final String ENDPOINT = "myendpoint.servicebus.windows.net";
    final String SHARED_ACCESS_KEY = "MySharedKey";
    final String SHARED_ACCESS_KEY_NAME = "MySharedKeyName";

    @Test
    public void sasTokenIsNotNull() {
        SharedAccessSignature sas = new SharedAccessSignature.Builder()
                .setEndpoint(ENDPOINT)
                .setSharedAccessKey(SHARED_ACCESS_KEY)
                .setIoTHub(false)
                .setSharedAccessKeyName(SHARED_ACCESS_KEY_NAME)
                .build();

        assertNotNull(sas.get());
    }
}

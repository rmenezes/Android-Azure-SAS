# Android Azure-SAS

Simple and small library to handle Azure SAS (Shared Access Signature).

Tested on Azure Service Bus, Azure Event Hub and Azure IoTHub.

## How to use:

```
final String ENDPOINT = "myendpoint.servicebus.windows.net";
final String SHARED_ACCESS_KEY = "MySharedKey";
final String SHARED_ACCESS_KEY_NAME = "MySharedKeyName";

SharedAccessSignature sas = new SharedAccessSignature.Builder()
                .setEndpoint(ENDPOINT)
                .setSharedAccessKey(SHARED_ACCESS_KEY)
                // Set true if you are using IoT Hub. Default is always false.
                .setIoTHub(false)
                .setSharedAccessKeyName(SHARED_ACCESS_KEY_NAME)
                .build();
                
String sasToken = sas.get();

```

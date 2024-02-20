## Example for a local bind service

This example will show how to use a local bind service that is not visible for other applications.

[Create your service](https://github.com/mkett/example-bind-service/blob/main/app/src/main/java/com/example/service/example/RandomNumberService.kt) (File->New->Service). Because 
our service is only running on our process we don't need to handle IPC (Inter-Process Communication). We return our inner class directly as IBinder in *onBind* function. 

```
class RandomNumberService : Service() {

    private val binder = LocalBinder()

    ...

    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods.
        fun getService(): RandomNumberService = this@RandomNumberService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

}
```

Take care your service is not visible for other applications (exported=„false“) 
on [manifext.xml](https://github.com/mkett/example-bind-service/blob/main/app/src/main/AndroidManifest.xml):

```
<application>
    ...
    <service
        android:name=".RandomNumberService"
        android:enabled="true"
        android:exported=„false“ />
</application>
```

Android creates connections to services asyncronously. That's the reason we need to bind to our service through a *ServiceConnection* object. We receive our service 
through the override function *onServiceConnected* on a success connention. At this moment we know we have a successful connection to the service and we save 
our service reference.

```
private val connection = object : ServiceConnection {

    override fun onServiceConnected(className: ComponentName, service: IBinder) {
        val binder = service as RandomNumberService.LocalBinder
        numberService = binder.getService()
        serviceBounded = true
    }

    override fun onServiceDisconnected(arg0: ComponentName) {
        serviceBounded = false
    }
}

```

[Bind now to the service](https://github.com/mkett/example-bind-service/blob/main/app/src/main/java/com/example/service/example/MainActivity.kt) via your *ServiceConnection* object 
and use your service.

```
Intent(this, RandomNumberService::class.java).also { intent ->
    bindService(intent, connection, Context.BIND_AUTO_CREATE)
}
```

If you are interested to use a service in other applications you can checkout the project [android-client-server-bind-service-example](https://github.com/mkett/android-client-server-bind-service-example/tree/main)

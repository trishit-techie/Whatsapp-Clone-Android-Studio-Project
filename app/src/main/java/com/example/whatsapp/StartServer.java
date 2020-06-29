package com.example.whatsapp;

import com.parse.Parse;
import com.parse.ParseObject;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.widget.EditText;

import com.parse.ParseQuery;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;

import java.net.URI;
import java.net.URISyntaxException;


public class StartServer extends Application {

    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("UUzrtSPEbEmEmnSUTlVA8tkG8zM39gmwJQ7NMC9l")
                .clientKey("TRg1LPeejrBcvxfQGy5Ng6xzc1ffyDbUQ64ep0sM")
                .server("https://parseapi.back4app.com")
                .build()
        );

       /* ParseObject object = new ParseObject("exampleObject");
        object.put("name", "ritam");
        object.put("regNo", "19BCE1141");
        object.saveInBackground();

        */

    }
}
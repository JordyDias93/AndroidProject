/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.AndroidProject;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

import java.util.Date;


public class StarterApplication extends Application {

  public Date DernierMessageDate;

  @Override
  public void onCreate() {
    super.onCreate();
    // Enable Local Datastore.
    Parse.enableLocalDatastore(this);

    // Add your initialization code here
    Parse.initialize(this);
    ParseUser.enableRevocableSessionInBackground();
    //ParseUser.enableRevocableSessionInBackground();
    //Penser à rajouter le data user si déjà présent

    //ParseUser.enableAutomaticUser();
    //ParseACL defaultACL = new ParseACL();
    // Optionally enable public read access.
    // defaultACL.setPublicReadAccess(true);
    // ParseACL.setDefaultACL(defaultACL, true);
  }
}

/*
 * Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.ar.core.codelabs.hellogeospatial.helpers

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/** Helper to ask camera permission.  */
object GeoPermissionsHelper {
  private val PERMISSIONS = arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION)

  /** Check to see we have the necessary permissions for this app.  */
  fun hasGeoPermissions(activity: Activity): Boolean {
    return PERMISSIONS.all {
      ContextCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_GRANTED
    }
  }

  /** Check to see we have the necessary permissions for this app, and ask for them if we don't.  */
  fun requestPermissions(activity: Activity?) {
    ActivityCompat.requestPermissions(
      activity!!, PERMISSIONS, 0)
  }

  /** Check to see if we need to show the rationale for this permission.  */
  fun shouldShowRequestPermissionRationale(activity: Activity): Boolean {
    return PERMISSIONS.any {  ActivityCompat.shouldShowRequestPermissionRationale(activity, it) }
  }

  /** Launch Application Setting to grant permission.  */
  fun launchPermissionSettings(activity: Activity) {
    val intent = Intent()
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    intent.data = Uri.fromParts("package", activity.packageName, null)
    activity.startActivity(intent)
  }
}
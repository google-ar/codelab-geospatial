/*
 * Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.ar.core.codelabs.hellogeospatial.helpers

import android.opengl.GLSurfaceView
import android.view.View
import android.widget.TextView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.ar.core.Earth
import com.google.ar.core.GeospatialPose
import com.google.ar.core.codelabs.hellogeospatial.HelloGeoActivity
import com.google.ar.core.codelabs.hellogeospatial.R
import com.google.ar.core.examples.java.common.helpers.SnackbarHelper

/** Contains UI elements for Hello Geo. */
class HelloGeoView(val activity: HelloGeoActivity) : DefaultLifecycleObserver {
  val root = View.inflate(activity, R.layout.activity_main, null)
  val surfaceView = root.findViewById<GLSurfaceView>(R.id.surfaceview)

  val session
    get() = activity.arCoreSessionHelper.session

  val snackbarHelper = SnackbarHelper()

  var mapView: MapView? = null
  val mapTouchWrapper = root.findViewById<MapTouchWrapper>(R.id.map_wrapper).apply {
    setup { screenLocation ->
      val latLng: LatLng =
        mapView?.googleMap?.projection?.fromScreenLocation(screenLocation) ?: return@setup
      activity.renderer.onMapClick(latLng)
    }
  }
  val mapFragment =
    (activity.supportFragmentManager.findFragmentById(R.id.map)!! as SupportMapFragment).also {
      it.getMapAsync { googleMap -> mapView = MapView(activity, googleMap) }
    }

  val statusText = root.findViewById<TextView>(R.id.statusText)
  fun updateStatusText(earth: Earth, cameraGeospatialPose: GeospatialPose?) {
    activity.runOnUiThread {
      val poseText = if (cameraGeospatialPose == null) "" else
        activity.getString(R.string.geospatial_pose,
                           cameraGeospatialPose.latitude,
                           cameraGeospatialPose.longitude,
                           cameraGeospatialPose.horizontalAccuracy,
                           cameraGeospatialPose.altitude,
                           cameraGeospatialPose.verticalAccuracy,
                           cameraGeospatialPose.heading,
                           cameraGeospatialPose.headingAccuracy)
      statusText.text = activity.resources.getString(R.string.earth_state,
                                                     earth.earthState.toString(),
                                                     earth.trackingState.toString(),
                                                     poseText)
    }
  }

  override fun onResume(owner: LifecycleOwner) {
    surfaceView.onResume()
  }

  override fun onPause(owner: LifecycleOwner) {
    surfaceView.onPause()
  }
}

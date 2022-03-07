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

import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.FrameLayout
import kotlin.math.sqrt

/**
 * Class used to get finer granularity when tapping on GoogleMap views.
 * This wrapper view will intercept the touch events for a tap.
 */
class MapTouchWrapper : FrameLayout {
  private var touchSlop = 0
  private var down: Point? = null
  private var listener: ((Point) -> Unit)? = null

  constructor(context: Context) : super(context) {
    setup(context)
  }

  constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
    setup(context)
  }

  private fun setup(context: Context) {
    val vc = ViewConfiguration.get(context)
    touchSlop = vc.scaledTouchSlop
  }

  fun setup(listener: ((Point) -> Unit)?) {
    this.listener = listener
  }

  private fun distance(p1: Point, p2: Point): Double {
    val xDiff = (p1.x - p2.x).toDouble()
    val yDiff = (p1.y - p2.y).toDouble()
    return sqrt(xDiff * xDiff + yDiff * yDiff)
  }

  override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
    if (listener == null) {
      return false
    }
    val x = event.x.toInt()
    val y = event.y.toInt()
    val tapped = Point(x, y)
    when (event.action) {
      MotionEvent.ACTION_DOWN -> down = tapped
      MotionEvent.ACTION_MOVE -> if (down != null && distance(down!!, tapped) >= touchSlop) {
        down = null
      }
      MotionEvent.ACTION_UP -> if (down != null && distance(down!!, tapped) < touchSlop) {
        listener?.invoke(tapped)
        return true
      }
      else -> {
      }
    }
    return false
  }
}
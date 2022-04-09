package com.brunoponte.everythinglisboa.helpers

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import androidx.core.content.ContextCompat
import kotlin.math.roundToInt

class Util {

    companion object {

        fun hasPermissions(context: Context, permission: String)
                = hasPermissions(context, listOf(permission))

        fun hasPermissions(context: Context, permissions: List<String>) : Boolean {
            permissions.forEach { permission ->
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }

            return true
        }

        fun bitmapResize(bitmapIn: Bitmap, scale: Float): Bitmap {
            return Bitmap.createScaledBitmap(bitmapIn,
                (bitmapIn.width * scale).roundToInt(),
                (bitmapIn.height * scale).roundToInt(), false)
        }

        fun getBitmapFromDrawable(context: Context, drawableId: Int, rotationAngle: Double? = 0.0): Bitmap {
            var drawable = ContextCompat.getDrawable(context, drawableId)

            val bitmap = Bitmap.createBitmap(
                drawable!!.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )

            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)


            val matrix = Matrix()
            matrix.postRotate(-(rotationAngle?.toFloat() ?: 0f))

            return Bitmap.createBitmap(
                bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
            )
        }
    }

}
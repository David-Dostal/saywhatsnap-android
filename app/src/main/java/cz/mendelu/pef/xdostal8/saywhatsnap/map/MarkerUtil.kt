package cz.mendelu.pef.xdostal8.saywhatsnap.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect


class MarkerUtil {

    companion object {
        private fun createSimpleLetterIcon(letter: String, bgColor: Int): Bitmap {
            val width = 100 // width of the icon
            val height = 150 // height of the icon including the pointy part
            val circleRadius = width / 2f
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)

            // Paint for the background
            val paint = Paint().apply {
                color = bgColor
                style = Paint.Style.FILL
                isAntiAlias = true
            }

            // Draw the circle part of the marker
            canvas.drawCircle(width / 2f, circleRadius, circleRadius, paint)

            // Draw the pointy part of the marker
            val path = Path().apply {
                moveTo(width / 2f, height.toFloat()) // Bottom point of the triangle
                lineTo(width.toFloat(), circleRadius) // Bottom right of the circle
                lineTo(0f, circleRadius) // Bottom left of the circle
                close()
            }
            canvas.drawPath(path, paint)

            // Draw the letter
            paint.apply {
                color = Color.WHITE // Text color
                textSize = 50f // Text size
                textAlign = Paint.Align.CENTER
            }
            val textBounds = Rect()
            paint.getTextBounds(letter, 0, letter.length, textBounds)
            val x = width / 2f
            val y = circleRadius + (textBounds.height() / 2f) - textBounds.bottom
            canvas.drawText(letter, x, y, paint)

            return bitmap
        }

        fun createBitmapMarker(context: Context, letter: String, color: Int): Bitmap {
            return createSimpleLetterIcon(letter, color)
        }
    }
}

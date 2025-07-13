package cz.mendelu.pef.xdostal8.saywhatsnap.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import cz.mendelu.pef.xdostal8.saywhatsnap.model.database.TranslationEntity

class CustomMapRenderer(
    private val context: Context,
    map: GoogleMap,
    manager: ClusterManager<TranslationEntity>
) : DefaultClusterRenderer<TranslationEntity>(context, map, manager) {

    private val icons: MutableMap<String, Bitmap> = mutableMapOf() // Changed to String keys

    override fun onBeforeClusterItemRendered(
        item: TranslationEntity,
        markerOptions: MarkerOptions
    ) {
        super.onBeforeClusterItemRendered(item, markerOptions)
        markerOptions.title(item.title)
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getIcon(place = item)))
    }

    override fun shouldRenderAsCluster(cluster: Cluster<TranslationEntity>): Boolean {
        return cluster.size > 2
    }

    private fun getIcon(place: TranslationEntity): Bitmap {
        val letter = place.category.firstOrNull()?.toString()?.uppercase() ?: "Unknown"
        val categoryColor = CategoryColor.from(letter[0])

        val icon = icons.getOrPut(letter) {
            MarkerUtil.createBitmapMarker(context, letter, categoryColor.color)
        }

        val text = place.name
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = 50f // Increase text size
            textAlign = Paint.Align.CENTER
        }

        val textHeight = (paint.descent() - paint.ascent()).toInt()
        val combinedBitmap = Bitmap.createBitmap(
            icon.width,
            icon.height + textHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(combinedBitmap)

        // Draw the icon
        canvas.drawBitmap(icon, 0f, 0f, null)

        // Draw the text below the icon
        val x = icon.width / 2f
        val y = icon.height + (-paint.ascent())
        canvas.drawText(text, x, y, paint)

        return combinedBitmap
    }
}
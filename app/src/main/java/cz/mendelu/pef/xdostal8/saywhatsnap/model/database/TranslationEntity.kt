package cz.mendelu.pef.xdostal8.saywhatsnap.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import java.io.Serializable

@Entity(tableName = "translations")
data class TranslationEntity(

    @ColumnInfo(name = "originalString") var originalString: String,

    @ColumnInfo(name = "originalLanguage") var originalLanguage: String,

    @ColumnInfo(name = "translatedString") var translatedString: String,

    @ColumnInfo(name = "translatedLanguage") var translatedLanguage: String,

    @ColumnInfo(name = "date") var date: Long,


    @ColumnInfo(name = "image") var image: String,


    @ColumnInfo(name = "name") var name: String,

    @ColumnInfo(name = "latitude")
    var latitude: Double,

    @ColumnInfo(name = "longitude")
    var longitude: Double,

    @ColumnInfo(name = "category")
    var category: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "visible")
    var visible: Boolean,


    ) : Serializable, ClusterItem {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null

    override fun getPosition(): LatLng {
        return LatLng(latitude, longitude)
    }

    override fun getTitle(): String {
        return name
    }

    override fun getSnippet(): String {
        return category
    }

    override fun getZIndex(): Float {
        return 0.0f
    }

}
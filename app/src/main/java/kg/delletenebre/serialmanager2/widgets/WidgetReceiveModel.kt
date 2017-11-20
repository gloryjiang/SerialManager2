package kg.delletenebre.serialmanager2.widgets

import android.support.annotation.Keep
import io.realm.RealmObject

@Keep
open class WidgetReceiveModel : RealmObject() {
    var id: Int = 0
    var key: String = ""
    var value: String = ""
    var textColor: String = ""
    var textSize: Int = 14
    var backgroundColor: String = "#88000000"
    var backgroundImage: String = ""
    var layoutAlignId: Int = 1
    var textAlignId: Int = 1
}
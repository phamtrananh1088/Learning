package jp.co.toukei.log.trustar.db.result.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import jp.co.toukei.log.lib.util.RequestBodyJson
import org.json.JSONObject

@Entity(
        tableName = "failed_data"
)
class FailedData(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") @JvmField val id: Int,
        @ColumnInfo(name = "error_message") @JvmField val errMessage: String,
        @ColumnInfo(name = "contents") @JvmField val contents: JSONObject,
        @ColumnInfo(name = "type") @JvmField val type: Int,
        @ColumnInfo(name = "datetime") @JvmField val date: Long
) {
    sealed class Content(
            @JvmField val request: RequestBodyJson,
            @JvmField val type: Int
    ) {
        class Bin(request: RequestBodyJson) : Content(request, 1)
        class Notice(request: RequestBodyJson) : Content(request, 2)
        class Fuel(request: RequestBodyJson) : Content(request, 3)
        class Geo(request: RequestBodyJson) : Content(request, 4)
        class Incidental(request: RequestBodyJson) : Content(request, 5)
        class Collection(request: RequestBodyJson) : Content(request, 6)
    }

    constructor(cause: Exception, content: Content) : this(
            0,
            cause.message ?: cause.toString(),
            content.request.jsonBody(),
            content.type,
            System.currentTimeMillis()
    )
}

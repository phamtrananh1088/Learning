package jp.co.toukei.log.trustar.db.image

import android.util.Base64
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import jp.co.toukei.log.lib.byteArray
import jp.co.toukei.log.lib.encodeToBase64String
import jp.co.toukei.log.trustar.rest.post.PicPost
import jp.co.toukei.log.trustar.user.UserInfo
import org.json.JSONObject
import java.util.*

@Entity(tableName = "image_sending")
class ImageSending(
        @ColumnInfo(name = "pic_id") @PrimaryKey @JvmField val picId: String,
        @ColumnInfo(name = "company_cd") @JvmField val companyCd: String,
        @ColumnInfo(name = "user_id") @JvmField val userId: String,
        @ColumnInfo(name = "pic_raw", typeAffinity = ColumnInfo.BLOB) @JvmField val picRaw: ByteArray,
        @ColumnInfo(name = "content") @JvmField val content: JSONObject
) {
    constructor(userInfo: UserInfo, picRaw: ByteArray, content: PicPost.Content) : this(
            UUID.randomUUID()
                    .byteArray()
                    .encodeToBase64String(Base64.NO_PADDING or Base64.NO_WRAP or Base64.URL_SAFE),
            userInfo.companyCd,
            userInfo.userId,
            picRaw,
            content.toJson()
    )
}

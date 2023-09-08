package jp.co.toukei.log.trustar.rest.api

import io.reactivex.rxjava3.core.Single
import jp.co.toukei.log.trustar.db.chat.entity.ChatMessagePending
import jp.co.toukei.log.trustar.rest.annotation.InjectHttpApi
import jp.co.toukei.log.trustar.rest.annotation.Type
import jp.co.toukei.log.trustar.rest.model.FileKey
import jp.co.toukei.log.trustar.rest.model.FileKey2
import jp.co.toukei.log.trustar.rest.model.MessageItem
import jp.co.toukei.log.trustar.rest.model.TalkRoom
import jp.co.toukei.log.trustar.rest.model.TalkUser
import jp.co.toukei.log.trustar.rest.post.ChatUserSettings
import jp.co.toukei.log.trustar.rest.post.DefaultPostContent
import jp.co.toukei.log.trustar.rest.post.MessageItemRows
import jp.co.toukei.log.trustar.rest.post.RoomAddMember
import jp.co.toukei.log.trustar.rest.post.RoomCreate
import jp.co.toukei.log.trustar.rest.response.StateResult
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import retrofit2.http.Streaming

interface ChatApi {

    @GET("Talk/GetData?isSelectAll=false")
    @InjectHttpApi(Type.MsgApiKeyAndTokenHeader, Type.CompanyCdQuery)
    fun getData(): Single<List<TalkRoom>>

    @InjectHttpApi(Type.MsgApiKeyAndTokenHeader, Type.CompanyCdQuery)
    @GET("Talk/GetTalkRooms")
    fun getTalkRooms(): Single<List<TalkRoom>>

    @InjectHttpApi(Type.MsgApiKeyAndTokenHeader, Type.CompanyCdQuery)
    @GET("Talk/GetHistories")
    fun getHistories(): Single<List<TalkUser>>

    @InjectHttpApi(Type.MsgApiKeyAndTokenHeader, Type.CompanyCdQuery)
    @GET("Account/GetAllUsers")
    fun getAllUsers(@Query("includeInvalid") includeInvalid: Boolean): Single<List<TalkUser>>

    @InjectHttpApi(Type.MsgApiKeyAndTokenHeader)
    @POST("Talk/AddTalkRoom")
    fun addRoom(@Body body: RoomCreate): Single<StateResult>

    @InjectHttpApi(Type.MsgApiKeyAndTokenHeader)
    @POST("Talk/AddMember")
    fun addMember(@Body body: RoomAddMember): Single<StateResult>

    @InjectHttpApi(Type.MsgApiKeyAndTokenHeader)
    @POST("Talk/SendMessage")
    fun sendMessage(@Body body: ChatMessagePending): Single<StateResult>

    @InjectHttpApi(Type.MsgApiKeyAndTokenHeader, Type.CompanyCdQuery)
    @GET("Talk/GetPrevious")
    fun getMessagePrevious(
        @Query("talkRoomId") roomId: String,
        @Query("messageId") messageId: String,
        @Query("pageSize") pageSize: Int,
    ): Single<List<MessageItem>>

    @InjectHttpApi(Type.MsgApiKeyAndTokenHeader, Type.CompanyCdQuery)
    @GET("Talk/GetNext")
    fun getMessageNext(
        @Query("talkRoomId") roomId: String,
        @Query("messageId") messageId: String,
        @Query("pageSize") pageSize: Int,
    ): Single<List<MessageItem>>

    @InjectHttpApi(Type.MsgApiKeyAndTokenHeader, Type.CompanyCdQuery)
    @GET("Talk/GetMessageItems")
    fun getMessageItems(
        @Query("talkRoomId") roomId: String,
        @Query("pageSize") pageSize: Int,
    ): Single<List<MessageItem>>

    @InjectHttpApi(Type.MsgApiKeyAndTokenHeader)
    @POST("Talk/UpdateUserSetting")
    fun updateUserSetting(@Body body: ChatUserSettings): Single<StateResult>

    @InjectHttpApi(Type.MsgApiKeyAndTokenHeader)
    @POST("Talk/UpdateIsRead")
    fun updateRead(@Body body: MessageItemRows): Single<StateResult>

    @InjectHttpApi(Type.MsgApiKeyAndTokenHeader)
    @POST("Talk/UpdateIsDeleted")
    fun updateDelete(@Body body: MessageItemRows): Single<StateResult>

    @InjectHttpApi(Type.MsgApiKeyAndTokenHeader, Type.CompanyCdQuery)
    @POST("Talk/RegisterFirebaseToken")
    fun registerFirebaseToken(@Body body: DefaultPostContent): Single<StateResult>

    @InjectHttpApi(Type.MsgApiKeyAndTokenHeader)
    @POST("Talk/DeleteMessage")
    fun deleteMessage(@Body body: DefaultPostContent): Single<StateResult>

    @InjectHttpApi(Type.MsgApiKeyAndTokenHeader, Type.CompanyCdHeader)
    @GET("File/Download")
    @Streaming
    fun download(@Query("fileKey") fileKey: String): Single<ResponseBody>

    @InjectHttpApi(Type.MsgApiKeyAndTokenHeader, Type.CompanyCdHeader)
    @POST("File/Upload")
    @Multipart
    fun uploadFile(
        @Part buffer: MultipartBody.Part,
    ): Single<FileKey2>

    @InjectHttpApi(Type.MsgApiKeyAndTokenHeader, Type.CompanyCdHeader)
    @POST("Image/Upload")
    @Multipart
    fun uploadImage(
        @Part buffer: MultipartBody.Part,
    ): Single<FileKey>

}

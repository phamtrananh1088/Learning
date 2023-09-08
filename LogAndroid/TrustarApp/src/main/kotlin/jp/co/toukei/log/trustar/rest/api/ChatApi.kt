package jp.co.toukei.log.trustar.rest.api

import io.reactivex.rxjava3.core.Single
import jp.co.toukei.log.trustar.db.chat.entity.ChatMessagePending
import jp.co.toukei.log.trustar.rest.annotation.InjectHttpApi
import jp.co.toukei.log.trustar.rest.annotation.Type
import jp.co.toukei.log.trustar.rest.model.MessageItem
import jp.co.toukei.log.trustar.rest.model.TalkRoom
import jp.co.toukei.log.trustar.rest.model.TalkUser
import jp.co.toukei.log.trustar.rest.post.ChatUserSettings
import jp.co.toukei.log.trustar.rest.post.DefaultPostContent
import jp.co.toukei.log.trustar.rest.post.MessageItemRows
import jp.co.toukei.log.trustar.rest.post.RoomAddMember
import jp.co.toukei.log.trustar.rest.post.RoomCreate
import jp.co.toukei.log.trustar.rest.response.StateResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ChatApi {

    @GET("Talk/GetData?isSelectAll=false")
    @InjectHttpApi(Type.ApiKeyAndTokenHeader, Type.CompanyCdQuery)
    fun getData(): Single<List<TalkRoom>>

    @InjectHttpApi(Type.ApiKeyAndTokenHeader, Type.CompanyCdQuery)
    @GET("Talk/GetTalkRooms")
    fun getTalkRooms(): Single<List<TalkRoom>>

    @InjectHttpApi(Type.ApiKeyAndTokenHeader, Type.CompanyCdQuery)
    @GET("Talk/GetHistories")
    fun getHistories(): Single<List<TalkUser>>

    @InjectHttpApi(Type.ApiKeyAndTokenHeader, Type.CompanyCdQuery)
    @GET("Account/GetAllUsers")
    fun getAllUsers(@Query("includeInvalid") includeInvalid: Boolean): Single<List<TalkUser>>

    @InjectHttpApi(Type.ApiKeyAndTokenHeader)
    @POST("Talk/AddTalkRoom")
    fun addRoom(@Body body: RoomCreate): Single<StateResult>

    @InjectHttpApi(Type.ApiKeyAndTokenHeader)
    @POST("Talk/AddMember")
    fun addMember(@Body body: RoomAddMember): Single<StateResult>

    @InjectHttpApi(Type.ApiKeyAndTokenHeader)
    @POST("Talk/SendMessage")
    fun sendMessage(@Body body: ChatMessagePending): Single<StateResult>

    @InjectHttpApi(Type.ApiKeyAndTokenHeader, Type.CompanyCdQuery)
    @GET("Talk/GetPrevious")
    fun getMessagePrevious(
        @Query("talkRoomId") roomId: String,
        @Query("messageId") messageId: String,
        @Query("pageSize") pageSize: Int,
    ): Single<List<MessageItem>>

    @InjectHttpApi(Type.ApiKeyAndTokenHeader, Type.CompanyCdQuery)
    @GET("Talk/GetNext")
    fun getMessageNext(
        @Query("talkRoomId") roomId: String,
        @Query("messageId") messageId: String,
        @Query("pageSize") pageSize: Int,
    ): Single<List<MessageItem>>

    @InjectHttpApi(Type.ApiKeyAndTokenHeader, Type.CompanyCdQuery)
    @GET("Talk/GetMessageItems")
    fun getMessageItems(
        @Query("talkRoomId") roomId: String,
        @Query("pageSize") pageSize: Int,
    ): Single<List<MessageItem>>

    @InjectHttpApi(Type.ApiKeyAndTokenHeader)
    @POST("Talk/UpdateUserSetting")
    fun updateUserSetting(@Body body: ChatUserSettings): Single<StateResult>

    @InjectHttpApi(Type.ApiKeyAndTokenHeader)
    @POST("Talk/UpdateIsRead")
    fun updateRead(@Body body: MessageItemRows): Single<StateResult>

    @InjectHttpApi(Type.ApiKeyAndTokenHeader)
    @POST("Talk/UpdateIsDeleted")
    fun updateDelete(@Body body: MessageItemRows): Single<StateResult>

    @InjectHttpApi(Type.ApiKeyAndTokenHeader, Type.CompanyCdQuery)
    @POST("Talk/RegisterFirebaseToken")
    fun registerFirebaseToken(@Body body: DefaultPostContent): Single<StateResult>

    @InjectHttpApi(Type.ApiKeyAndTokenHeader)
    @POST("Talk/DeleteMessage")
    fun deleteMessage(@Body body: DefaultPostContent): Single<StateResult>
}

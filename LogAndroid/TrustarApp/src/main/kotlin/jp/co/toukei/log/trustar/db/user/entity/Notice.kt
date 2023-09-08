package jp.co.toukei.log.trustar.db.user.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import annotation.Find
import annotation.Para
import jp.co.toukei.log.lib.room.AbstractSync
import jp.co.toukei.log.lib.util.CompareItem
import jp.co.toukei.log.trustar.Config
import jp.co.toukei.log.trustar.dateFromStringT

/**
 *
 * Notice
 *
 * ${package_name}
 * ${file_name}
 * ${project_name}
 *
 * @since  : 1.0 : 2019/05/23
 * @author : M.Watanabe
 *
 * @property recordId レコードID
 * @property grpRecordId グループレコードID
 * @property noticeCd 通知先コード
 * @property eventRank 通知区分  1：重要、2：通常
 * @property publicationDateFrom 掲載開始日時
 * @property publicationDateTo 掲載終了日時
 * @property noticeTitle タイトル
 * @property noticeText 本文
 *
 * Copyright (c) Toukei Computer Co., Ltd.<br>
 *
 */

@Entity(tableName = "notice", primaryKeys = ["record_id"])
class Notice(
        @ColumnInfo(name = "record_id") @JvmField val recordId: String,
        @ColumnInfo(name = "grpRecord_id") @JvmField val grpRecordId: String,
        @ColumnInfo(name = "notice_cd") @JvmField val noticeCd: String?,
        @ColumnInfo(name = "event_rank") @JvmField val eventRank: Int,
        @ColumnInfo(name = "publication_date_from") @JvmField val publicationDateFrom: Long?,
        @ColumnInfo(name = "publication_date_to") @JvmField val publicationDateTo: Long?,
        @ColumnInfo(name = "notice_title") @JvmField val noticeTitle: String?,
        @ColumnInfo(name = "notice_text") @JvmField val noticeText: String?,
        @ColumnInfo(name = "unread_flag") @JvmField val unreadFlag: Boolean,
        @ColumnInfo(name = "read_datetime") @JvmField val readDatetime: Long?
) : CompareItem<Notice>, AbstractSync() {

    @Find
    constructor(
            @Para("recordId") recordId: String,
            @Para("grpRecordId") grpRecordId: String,
            @Para("noticeCd") noticeCd: String,
            @Para("eventRank") eventRank: Int,
            @Para("publicationDateFrom") publicationDateFrom: String?,
            @Para("publicationDateTo") publicationDateTo: String?,
            @Para("noticeTitle") noticeTitle: String?,
            @Para("noticeText") noticeText: String?,
            @Para("unreadFlag") unreadFlag: Int?,
            @Para("readDatetime") readDateTime: Long?
    ) : this(
            recordId,
            grpRecordId,
            noticeCd,
            eventRank,
            publicationDateFrom.dateFromStringT()?.time,
            publicationDateTo.dateFromStringT()?.time,
            noticeTitle,
            noticeText,
            unreadFlag != 0,
            readDateTime
    )

    @Ignore
    @JvmField
    val showDialogFlag: Boolean = noticeTitle != null &&
            unreadFlag &&
            eventRank == 1 &&
            noticeTitle.contains("{${Config.androidId}}")
    @Ignore
    @JvmField
    val dataSendFlag: Boolean = showDialogFlag &&
            noticeText != null &&
            noticeText.contains("{LOCAL_DATA}")
    @Ignore
    @JvmField
    val dataDelFlag: Boolean = showDialogFlag &&
            noticeText != null &&
            noticeText.contains("{INITIALIZE}")

    override fun sameItem(other: Notice): Boolean {
        return recordId == other.recordId
    }
}

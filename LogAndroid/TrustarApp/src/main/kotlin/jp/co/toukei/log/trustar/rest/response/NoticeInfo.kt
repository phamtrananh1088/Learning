package jp.co.toukei.log.trustar.rest.response

import annotation.Keep
import annotation.Para
import jp.co.toukei.log.trustar.db.user.entity.Notice

class NoticeInfo @Keep constructor(
        @Para("notices") @JvmField val notices: Array<Notice>?
)

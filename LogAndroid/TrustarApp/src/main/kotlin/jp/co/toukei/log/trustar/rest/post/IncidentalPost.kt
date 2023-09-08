package jp.co.toukei.log.trustar.rest.post

import jp.co.toukei.log.lib.jsonArr
import jp.co.toukei.log.lib.jsonArrNotNull
import jp.co.toukei.log.lib.util.RequestBodyJson
import jp.co.toukei.log.trustar.db.result.entity.CommonIncidentalHeaderResult
import jp.co.toukei.log.trustar.db.result.entity.CommonIncidentalTimeResult
import jp.co.toukei.log.trustar.db.result.entity.CommonUserSync
import jp.co.toukei.log.trustar.user.ClientInfo
import org.json.JSONObject
import third.jsonObj

class IncidentalPost(
        private val clientInfo: ClientInfo,
        @JvmField val headerList: List<CommonIncidentalHeaderResult>,
        @JvmField val timeList: List<CommonIncidentalTimeResult>
) : RequestBodyJson {

    private class R {
        @JvmField
        val header: MutableList<CommonIncidentalHeaderResult> = mutableListOf()
        @JvmField
        val time: MutableList<CommonIncidentalTimeResult> = mutableListOf()
    }

    private fun MutableMap<String, MutableMap<String, R>>.r(it: CommonUserSync): R {
        return getOrPut(it.companyCd) { mutableMapOf() }.getOrPut(it.userId, ::R)
    }

    override fun jsonBody(): JSONObject {
        val resultList: List<JSONObject> = run {
            mutableMapOf<String, MutableMap<String, R>>().apply {
                headerList.forEach { r(it).header += it }
                timeList.forEach { r(it).time += it }
            }.flatMap { (company, l) ->
                l.map { (user, r) ->
                    val map = r.time.groupByTo(LinkedHashMap()) { it.headerUUID }
                    val removedIncidental = mutableSetOf<String>()
                    val addedIncidental = mutableListOf<JSONObject>()
                    val editedIncidental = mutableListOf<JSONObject>()
                    val removedTime = mutableSetOf<JSONObject>()
                    val addedTime = mutableSetOf<JSONObject>()
                    val editedTime = mutableSetOf<JSONObject>()
                    val hg = r.header.groupBy { it.sheetNo == null }
                    hg[true]?.forEach { h ->
                        val times = map.remove(h.uuid)
                        if (!h.deleted) {
                            addedIncidental += jsonObj {
                                "guid" v h.uuid
                                "allocationNo" v h.allocationNo
                                "allocationRowNo" v h.allocationRowNo
                                "shipperCd" v h.shipperCd
                                "workCd" v h.workList.jsonArr()
                                "picId" v h.picId
                                "time" v times?.jsonArrNotNull { t ->
                                    if (t.deleted) null
                                    else jsonObj {
                                        "guid" v t.uuid
                                        "cls" v t.type
                                        "beginTime" v t.beginDatetime
                                        "endTime" v t.endDatetime
                                    }
                                }
                            }
                        }
                    }
                    hg[false]?.forEach { h ->
                        h.sheetNo?.let { s ->
                            if (h.deleted) {
                                map.remove(h.uuid)
                                removedIncidental += s
                            } else {
                                editedIncidental += jsonObj {
                                    "sheetNo" v s
                                    "shipperCd" v h.shipperCd
                                    "workCd" v h.workList.jsonArr()
                                    "picId" v h.picId
                                }
                            }
                        }
                    }
                    map.values.asSequence()
                            .flatten()
                            .forEach { t ->
                                val s = t.sheetNo
                                val n = t.sheetRowNo
                                if (s != null && n != null) {
                                    if (t.deleted) {
                                        removedTime += jsonObj {
                                            "sheetNo" v s
                                            "sheetRowNo" v n
                                        }
                                    } else {
                                        editedTime += jsonObj {
                                            "sheetNo" v s
                                            "sheetRowNo" v n
                                            "beginTime" v t.beginDatetime
                                            "endTime" v t.endDatetime
                                        }
                                    }
                                } else {
                                    addedTime += jsonObj {
                                        "guid" v t.uuid
                                        "sheetNo" v s
                                        "cls" v t.type
                                        "beginTime" v t.beginDatetime
                                        "endTime" v t.endDatetime
                                    }
                                }
                            }
                    jsonObj {
                        "companyCd" v company
                        "userId" v user
                        "removeIncidental" v removedIncidental.jsonArr()
                        "editIncidental" v editedIncidental.jsonArr()
                        "addIncidental" v addedIncidental.jsonArr()
                        "editTime" v editedTime.jsonArr()
                        "removeTime" v removedTime.jsonArr()
                        "addTime" v addedTime.jsonArr()
                    }
                }
            }
        }

        return jsonObj {
            "incidentalResults" v resultList.jsonArr()
            "clientInfo" v clientInfo.jsonBody()
        }
    }
}

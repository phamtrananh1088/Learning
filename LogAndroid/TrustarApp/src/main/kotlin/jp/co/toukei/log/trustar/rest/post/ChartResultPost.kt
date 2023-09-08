package jp.co.toukei.log.trustar.rest.post

import jp.co.toukei.log.lib.jsonArrIndexed
import jp.co.toukei.log.lib.util.RequestBodyJson
import jp.co.toukei.log.trustar.db.allocationNo
import jp.co.toukei.log.trustar.db.allocationRowNo
import jp.co.toukei.log.trustar.db.result.entity.CommonDeliveryChart
import jp.co.toukei.log.trustar.toInt
import jp.co.toukei.log.trustar.user.ClientInfo
import jp.co.toukei.log.trustar.user.UserInfo
import org.json.JSONArray
import org.json.JSONObject
import third.jsonObj

class ChartResultPost(
    @JvmField val chart: CommonDeliveryChart,
    private val userInfo: UserInfo,
    private val clientInfo: ClientInfo,
    private val imagesOverride: List<Pair<String, JSONObject?>>
) : RequestBodyJson {

    override fun jsonBody(): JSONObject {
        return jsonObj {
            "userId" v userInfo.userId
            "clientInfo" v clientInfo.jsonBody()

            "chart" v jsonObj {
                "chartHeaders" v JSONArray().put( // wtf??
                    jsonObj(chart.extra) {
                        val allocationRow = chart.lastAllocationRow
                        val info = chart.info

                        "companyCd" v chart.companyCd
                        "allocationNo" v allocationRow?.allocationNo
                        "allocationRowNo" v allocationRow?.allocationRowNo
                        "chartCd" v chart.chartCd
                        "placeCd" v chart.placeCd
                        "chartNm" v info.dest
                        "chartAddr1" v info.addr1
                        "chartAddr2" v info.addr2
                        "chartTel" v info.tel
                        "chargeNm" v info.carrier
                        "chargeTel" v info.carrierTel
                    }
                )
                "chartMemos" v chart.memos.jsonArrIndexed { index, memo ->
                    jsonObj(memo.extra) {
                        "chartCd" v chart.chartCd
                        "title" v memo.label
                        "note" v memo.note
                        "highLightFlag" v memo.highlight.toInt()
                        "seqNo" v index + 1
                    }
                }
                "chartFiles" v imagesOverride.jsonArrIndexed { index, (key, extra) ->
                    jsonObj(extra) {
                        "chartCd" v chart.chartCd
                        "fileId" v key
                        "seqNo" v index + 1
                    }
                }
            }
        }
    }
}

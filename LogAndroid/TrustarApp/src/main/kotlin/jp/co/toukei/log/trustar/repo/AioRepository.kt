package jp.co.toukei.log.trustar.repo

import jp.co.toukei.log.trustar.user.LoggedUser

@Deprecated("")
class AioRepository(user: LoggedUser) {
    @JvmField
    val binHeaderRepo = BinHeaderRepo(user.userDB)
    @JvmField
    val binDetailRepo = BinDetailRepo(user.userDB)
    @JvmField
    val workRepo = WorkRepo(user.userDB)
    @JvmField
    val fuelRepo = FuelRepo(user.userDB)
    @JvmField
    val kyuyuRepo = KyuyuRepo(user)
    @JvmField
    val incidentalRepo = IncidentalRepo(user)
}

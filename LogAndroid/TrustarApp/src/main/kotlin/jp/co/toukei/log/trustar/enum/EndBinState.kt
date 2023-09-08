package jp.co.toukei.log.trustar.enum

@JvmInline
value class EndBinState(val state: Int) {

    companion object {
        val Loading = EndBinState(1)
        val Error = EndBinState(2)
    }
}

package jp.co.toukei.log.trustar.rest.response

class LoginException(
        @JvmField val messageCode: String
) : Exception(messageCode)

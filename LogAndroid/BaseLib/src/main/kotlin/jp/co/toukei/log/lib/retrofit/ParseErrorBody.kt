package jp.co.toukei.log.lib.retrofit

import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class ParseErrorBody(vararg val type: KClass<out Exception>)

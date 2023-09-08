package jp.co.toukei.log.trustar.rest.annotation


@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class InjectHttpApi(
    vararg val type: Type,
)

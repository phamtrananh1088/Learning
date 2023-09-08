package annotation

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSNode

class KspProcessor : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return P(environment)
    }

    private class P(environment: SymbolProcessorEnvironment) : SymbolProcessor {
        private val logger = environment.logger

        private val keepName = Keep::class.qualifiedName!!
        private val findName = Find::class.qualifiedName!!
        private val paraName = Para::class.qualifiedName!!
        private val extraName = JsonObjectExtra::class.qualifiedName!!

        private fun KSAnnotation.qualifiedName(): String? {
            return annotationType.resolve().declaration.qualifiedName?.asString()
        }

        private fun KSAnnotated.hasAnnotation(vararg name: String): Boolean {
            return annotations.any { a ->
                val n = a.qualifiedName()
                name.any { it == n }
            }
        }

        override fun process(resolver: Resolver): List<KSAnnotated> {
            sequenceOf(paraName, extraName)
                .flatMap { resolver.getSymbolsWithAnnotation(it) }
                .groupBy(KSAnnotated::parent)
                .forEach { (p, _) ->
                    if (p is KSFunctionDeclaration) {
                        if (!p.hasAnnotation(keepName, findName)) {
                            loggerErr(p)
                        }
                        p.parameters.forEach { arg ->
                            if (!arg.hasAnnotation(paraName, extraName)) {
                                loggerErr(arg)
                            }
                        }
                    } else {
                        logger.error("how?")
                    }
                }
            return emptyList()
        }

        private fun loggerErr(e: KSNode) {
            logger.error("muda muda muda!!!", e)
        }

    }
}

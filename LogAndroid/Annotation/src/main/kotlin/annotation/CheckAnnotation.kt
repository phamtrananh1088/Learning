package annotation

import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

class CheckAnnotation : AbstractProcessor() {

    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        val p = annotations.firstOrNull {
            it.qualifiedName.contentEquals(Para::class.java.canonicalName)
        } ?: return false
        p.let { roundEnv.getElementsAnnotatedWith(Para::class.java) }
                .asSequence()
                .map { it.enclosingElement }
                .distinct()
                .filter { it.kind == ElementKind.CONSTRUCTOR }
                .forEach {
                    if (it.getAnnotation(Keep::class.java) == null && it.getAnnotation(Find::class.java) == null) {
                        processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, "muda muda muda!!!", it)
                    }
                }
        return true
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        return setOf(Para::class.java.canonicalName)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }
}

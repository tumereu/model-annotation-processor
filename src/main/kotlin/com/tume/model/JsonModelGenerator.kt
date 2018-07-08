package com.tume.model

import com.google.auto.service.AutoService
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
class JsonModelGenerator : AbstractProcessor() {
    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
        roundEnv?.getElementsAnnotatedWith(GenerateModels::class.java)
                ?.forEach {
                    val className = it.simpleName.toString()
                    val classPackage = processingEnv.elementUtils.getPackageOf(it).toString()
                    val properties = it.enclosedElements.filter {
                        it.kind == ElementKind.METHOD && it.simpleName.matches(Regex("^(get|is).*$"))
                    }

                    generateClass(className, classPackage, properties)
                }

        return true
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return setOf(GenerateModels::class.java.name).toMutableSet()
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latest()
    }

    private fun generateClass(name: String, pkg: String, properties: List<Element>) {
        println("Processing $name with $pkg and a list of props as ${properties.joinToString(",")}")
    }
}
package com.juliohaga.kelp.core.scanner

import io.github.classgraph.ClassGraph
import kotlin.reflect.KClass

/** Generic classpath scanner. Knows nothing about commands, listeners, or
 *  components — it just finds classes annotated with a given annotation, or
 *  classes implementing a given interface. */
class ClassScanner(private val classLoader: ClassLoader, private val basePackage: String) {

    fun findAnnotatedWith(annotation: KClass<out Annotation>): List<KClass<*>> {
        ClassGraph()
            .overrideClassLoaders(classLoader)
            .enableAnnotationInfo()
            .acceptPackages(basePackage)
            .scan().use { scanResult ->
                return scanResult.getClassesWithAnnotation(annotation.java)
                    .loadClasses()
                    .map { it.kotlin }
            }
    }

    /** Finds every class implementing [interfaceClass] (e.g. Listener).
     *  Generic on purpose — kelp-core has no Bukkit dependency, so callers
     *  in kelp-paper pass the interface they care about (org.bukkit.event.Listener)
     *  without this module needing to know it exists. */
    fun findImplementing(interfaceClass: KClass<*>): List<KClass<*>> {
        ClassGraph()
            .overrideClassLoaders(classLoader)
            .enableClassInfo()
            .acceptPackages(basePackage)
            .scan().use { scanResult ->
                return scanResult.getClassesImplementing(interfaceClass.java)
                    .loadClasses()
                    .map { it.kotlin }
            }
    }
}
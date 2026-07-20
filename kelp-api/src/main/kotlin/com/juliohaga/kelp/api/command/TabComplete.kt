package com.juliohaga.kelp.api.command

/**
 * Marks a method as the tab completer for the @SubCommand of the same path,
 * within the same class.
 *
 * Expected signature:
 *
 * (TabCompleteContext) -> List<String>
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class TabComplete(val path: String)
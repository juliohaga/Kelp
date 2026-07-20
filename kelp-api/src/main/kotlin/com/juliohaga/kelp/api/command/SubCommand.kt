package com.juliohaga.kelp.api.command

/**
 * Marca uma função como um subcomando.
 *
 * path = "" -> comando raiz (ex: /gm)
 * path = "list" -> subcomando (ex: /gm list)
 *
 * Assim como @Command, flags (PlayerOnly, ItemInHand, Cooldown, etc.)
 * não são parâmetro daqui — são annotations empilhadas na função:
 *
 * @SubCommand("toggle")
 * @PlayerOnly
 * @Cooldown(30)
 * fun toggle() { ... }
 *
 * As flags da classe (@Command) são somadas às da função na hora do dispatch.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class SubCommand(
    val path: String,
    val permission: String = ""
)
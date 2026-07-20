package com.juliohaga.kelp.api.command

/**
 * Marca uma classe como um comando Kelp.
 *
 * Restrições de sender (PLAYER_ONLY, CONSOLE_ONLY, etc.) e outras regras
 * NÃO ficam aqui como parâmetro — elas são declaradas como annotations
 * empilhadas na própria classe, ex:
 *
 * @Command("fly", "kelp.fly")
 * @PlayerOnly
 * class FlyCommand : KelpCommand() { ... }
 *
 * As flags da classe são herdadas por todos os @SubCommand, que podem
 * adicionar flags extras (nunca remover as da classe).
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Command(
    val name: String,
    val permission: String = "",
    val aliases: Array<String> = []
)
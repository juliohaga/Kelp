package com.juliohaga.kelp.api.requirement.types

import com.juliohaga.kelp.api.requirement.Requirement

/**
 * Restringe a execução a senders do tipo Player.
 * Aplicável em classe ou função — tanto de @Command/@SubCommand quanto
 * de um futuro event handler. Quando na classe, é herdada por todos os
 * elementos internos (subcomandos, métodos de handler, etc.), regra essa
 * resolvida pelo scanner no kelp-core.
 *
 * A verificação real de "o que é um Player" acontece no RequirementHandler
 * correspondente, implementado no kelp-paper (é lá que existe a API do Bukkit).
 */
@Requirement
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class PlayerOnly
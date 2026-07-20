package com.juliohaga.kelp.api.requirement

/**
 * Contrato implementado por cada handler de requirement.
 * A: o tipo da annotation que esse handler resolve (ex: PlayerOnly, Cooldown).
 *
 * Para criar um requirement novo:
 *   1. Criar a annotation aqui no kelp-api, marcada com @Requirement
 *   2. Implementar RequirementHandler pra ela no kelp-core (ou kelp-paper,
 *      se depender de API de plataforma)
 *   3. Registrar no RequirementRegistry
 * Nenhum arquivo existente precisa ser tocado.
 */
interface RequirementHandler<A : Annotation> {
    fun check(annotation: A, context: RequirementContext): RequirementResult
}
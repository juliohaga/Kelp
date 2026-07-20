package com.juliohaga.kelp.api.requirement

/**
 * Meta-annotation aplicada em outras annotations (PlayerOnly, Cooldown, etc.)
 * pra marcá-las como requirements.
 *
 * O scanner (kelp-core) varre as annotations presentes num elemento
 * (classe ou função, de comando ou de event handler) e filtra apenas
 * as que possuem @Requirement, delegando cada uma pro seu
 * RequirementHandler correspondente. Isso permite criar um requirement
 * novo sem tocar em nenhum código de scan/dispatch existente.
 */
@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Requirement
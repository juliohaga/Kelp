package com.juliohaga.kelp.core.command

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class ParsedSubCommand(
    val path: String,
    val permission: String,
    val requirements: List<Annotation>,
    val owner: KClass<*>,
    val function: KFunction<*>
)
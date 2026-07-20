package com.juliohaga.kelp.core.command

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class ParsedTabComplete(
    val path: String,
    val owner: KClass<*>,
    val function: KFunction<*>
)
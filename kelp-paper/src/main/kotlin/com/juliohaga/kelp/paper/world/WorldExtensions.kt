package com.juliohaga.kelp.paper.world

import org.bukkit.NamespacedKey

fun NamespacedKey.format(): String {

    val namespace = namespace
        .replaceFirstChar { it.uppercase() }

    val value = value()
        .replace("_", " ")
        .replaceFirstChar { it.uppercase() }

    return "$namespace ($value)"
}
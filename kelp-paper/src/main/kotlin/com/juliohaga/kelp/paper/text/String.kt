package com.juliohaga.kelp.paper.text

fun String.toCapitalize(): String =
    lowercase()
        .replaceFirstChar { it.titlecase() }
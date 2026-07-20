package com.juliohaga.kelp.api.command

/**
 * Small helper for parsing --flag value pairs out of a raw args array.
 * Flags can appear anywhere in the array, in any order — callers don't need
 * to know their position, just their name. Framework-level utility (no
 * Bukkit/Paper/Adventure dependency), reusable by any @SubCommand.
 */
object CommandFlags {

    fun string(args: Array<String>, flag: String): String? {
        val idx = args.indexOf(flag)
        if (idx == -1 || idx + 1 >= args.size) return null
        return args[idx + 1]
    }

    fun boolean(args: Array<String>, flag: String, default: Boolean): Boolean {
        val value = string(args, flag) ?: return default
        return value.lowercase().toBooleanStrictOrNull() ?: default
    }

    fun long(args: Array<String>, flag: String): Long? =
        string(args, flag)?.toLongOrNull()

    /** From a list of known flag names, returns the ones not yet present in
     *  [args] — used to suggest remaining flags in a @TabComplete. */
    fun unused(args: Array<String>, flags: List<String>): List<String> =
        flags.filter { it !in args }
}
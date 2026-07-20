package com.juliohaga.kelp.api

/**
 * Marker for a Sender that represents a player (as opposed to console,
 * command block, etc). Platform modules implement this on their player
 * sender wrapper.
 *
 * Exists so requirements like PlayerOnly can be resolved in kelp-core
 * without depending on any platform-specific Player type.
 */
interface PlayerSender : Sender
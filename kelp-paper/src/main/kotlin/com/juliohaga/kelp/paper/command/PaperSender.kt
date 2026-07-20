package com.juliohaga.kelp.paper.command

import com.juliohaga.kelp.api.Sender
import com.juliohaga.kelp.api.text.Text
import com.juliohaga.kelp.paper.text.TextRenderer
import org.bukkit.command.CommandSender

/** Wraps any Bukkit CommandSender (player, console, command block, etc)
 *  into the platform-independent Sender contract from kelp-api. */
open class PaperSender(val bukkit: CommandSender) : Sender {

    override fun sendMessage(message: String) {
        bukkit.sendMessage(message)
    }

    override fun sendMessage(text: Text) {
        bukkit.sendMessage(TextRenderer.render(text))
    }
}
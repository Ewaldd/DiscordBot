package pl.fcraft.event.bot

import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.OnlineStatus
import net.dv8tion.jda.core.entities.Game
import pl.fcraft.event.bot.command.ChannelCommand
import pl.fcraft.event.bot.command.HelpCommand
import pl.fcraft.event.bot.command.UserCommand
import pl.fcraft.event.bot.command.util.CommandManager
import pl.fcraft.event.bot.config.EventBotConfig
import pl.fcraft.event.bot.listener.JoinListener
import pl.fcraft.event.bot.listener.MessageListener
import pl.fcraft.event.bot.util.EventBotException

class EventBot(val config: EventBotConfig) {

    lateinit var jda: JDA
        private set

    var commandManager = CommandManager()

    var running = false
        private set

    fun start() {
        if (running)
            throw EventBotException("Bot is already running!")
        jda = JDABuilder(AccountType.BOT)
                .setToken(config.token)
                .addEventListener(JoinListener(this))
                .addEventListener(MessageListener(this))
                .setAudioEnabled(false)
                .setAutoReconnect(true)
                .setStatus(OnlineStatus.ONLINE)
                .setGame(Game.playing("EwaldBot v 1.0"))
                .setAudioEnabled(false)
                .buildBlocking()
        commandManager.add(HelpCommand(this))
        commandManager.add(ChannelCommand(this))
        commandManager.add(UserCommand(this))
        running = true
    }

    fun stop() {
        if (!running)
            throw EventBotException("Bot is not running!")
        running = false
        jda.shutdown()
    }

}
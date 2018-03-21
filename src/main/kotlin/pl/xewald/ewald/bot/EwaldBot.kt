package pl.xewald.ewald.bot

import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.OnlineStatus
import net.dv8tion.jda.core.entities.Game
import pl.xewald.ewald.bot.command.*
import pl.xewald.ewald.bot.command.util.CommandManager
import pl.xewald.ewald.bot.config.EwaldBotConfig
import pl.xewald.ewald.bot.listener.*
import pl.xewald.ewald.bot.util.EventBotException

class EwaldBot(val config: EwaldBotConfig) {

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
                .setGame(Game.playing("Wpisz !help | v 1.0"))
                .setAudioEnabled(false)
                .buildBlocking()
        commandManager.add(HelpCommand(this))
        commandManager.add(ChannelCommand(this))
        commandManager.add(UserCommand(this))
        commandManager.add(BotCommand(this))
        commandManager.add(SurveyCommand(this))
        running = true
    }

    fun stop() {
        if (!running)
            throw EventBotException("Bot is not running!")
        running = false
        jda.shutdown()
    }

}
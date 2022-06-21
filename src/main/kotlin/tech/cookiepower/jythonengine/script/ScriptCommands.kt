package tech.cookiepower.jythonengine.script

import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.module.lang.sendLang

@CommandHeader(name = "//script", description = "jython script manager")
object ScriptCommands {
    @CommandBody
    val main = mainCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            sender.sendLang("script-help")
        }
    }

    @CommandBody
    val help = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            sender.sendLang("script-help")
        }
    }

    @CommandBody
    val reload = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            sender.sendLang("script-reloading")
            ScriptManager.reloadAll()
            sender.sendLang("script-reloaded")
        }
    }
}
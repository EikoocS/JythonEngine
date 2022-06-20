package tech.cookiepower.jythonengine.console

import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.module.lang.sendLang
import tech.cookiepower.jythonengine.util.uniqueId

@CommandHeader("//", description = "Jython console main command")
object ConsolesModeCommands{
    @CommandBody
    val main = mainCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            val setting = Consoles.getSettings(sender.uniqueId)
            if (setting.inConsoleMode()) {
                setting.disableConsoleMode()
                sender.sendLang("console-exit")
            } else {
                setting.enableConsoleMode()
                sender.sendLang("console-enter")
            }
        }
    }
}
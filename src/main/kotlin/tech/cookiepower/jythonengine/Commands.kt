package tech.cookiepower.jythonengine

import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.module.lang.sendLang
import tech.cookiepower.jythonengine.console.Consoles
import tech.cookiepower.jythonengine.event.PlayerEnterConsolesModeEvent
import tech.cookiepower.jythonengine.event.PlayerLeaveConsolesModeEvent
import tech.cookiepower.jythonengine.util.uniqueId

@CommandHeader("//", description = "Jython console main command")
object Commands{
    @CommandBody
    val main = mainCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            val setting = Consoles.getSettings(sender.uniqueId)
            if (setting.inConsoleMode()) {
                val event = PlayerLeaveConsolesModeEvent(sender.cast(),setting.getInterpreterOrNull())
                event.call()
                if (event.isCancelled) { return@execute }

                setting.disableConsoleMode()
                sender.sendLang("jython-console-exit")
            } else {
                val event = PlayerEnterConsolesModeEvent(sender.cast(),setting.getInterpreterOrNull())
                event.call()
                if(event.isCancelled){ return@execute }

                setting.enableConsoleMode()
                sender.sendLang("jython-console-enter")
            }
        }
    }
}
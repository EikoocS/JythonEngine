package tech.cookiepower.jythonengine.console

import org.python.util.PythonInterpreter
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submit
import taboolib.module.lang.sendLang
import tech.cookiepower.jythonengine.io.ProxyCommandSenderWriter
import tech.cookiepower.jythonengine.util.uniqueId

@CommandHeader("//consoles", description = "Jython console")
object ConsolesCommands {
    @CommandBody
    val main = mainCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            sender.sendLang("console-help")
        }
    }

    @CommandBody
    val create = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            val setting = Consoles.getSettings(sender.uniqueId)
            if(setting.hasInterpreter()) {
                sender.sendLang("console-create-already-exists")
                return@execute
            }

            sender.sendLang("console-creating")
            submit(async = true) {
                val interpreter = PythonInterpreter()
                interpreter.setErr(ProxyCommandSenderWriter(sender,"console-error"))
                interpreter.setOut(ProxyCommandSenderWriter(sender,"console-output"))
                setting.setInterpreter(interpreter)
                sender.sendLang("console-create")
            }
        }
    }

    @CommandBody
    val close = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            val setting = Consoles.getSettings(sender.uniqueId)
            if (!setting.hasInterpreter()) {
                sender.sendLang("console-close-not-exists")
                return@execute
            }

            setting.removeInterpreterAndClose()
            sender.sendLang("console-interpreter-close")
        }
    }

    @CommandBody
    val clean = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            val setting = Consoles.getSettings(sender.uniqueId)
            val interpreter = setting.getInterpreter()

            interpreter.cleanup()
            sender.sendLang("console-interpreter-clean")
        }
    }

    @CommandBody
    val sync = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            val setting = Consoles.getSettings(sender.uniqueId)
            if(setting.isSync()){
                setting.disableSync()
                sender.sendLang("console-sync-off")
            }else{
                setting.enableSync()
                sender.sendLang("console-sync-on")
            }
        }
    }

    @CommandBody
    val output = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            val setting = Consoles.getSettings(sender.uniqueId)
            val interpreter = setting.getInterpreter()

            interpreter.setErr(ProxyCommandSenderWriter(sender,"console-error"))
            interpreter.setOut(ProxyCommandSenderWriter(sender,"console-output"))
            sender.sendLang("console-re-get-output")
        }
    }

    @CommandBody
    val help = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            sender.sendLang("console-help")
        }
    }
}
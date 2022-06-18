package tech.cookiepower.jythonengine.console

import org.python.util.PythonInterpreter
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submit
import taboolib.module.lang.sendLang
import tech.cookiepower.jythonengine.event.PlayerCleanInterpreterEvent
import tech.cookiepower.jythonengine.event.PlayerCloseInterpreterEvent
import tech.cookiepower.jythonengine.event.PlayerCreateInterpreterEvent
import tech.cookiepower.jythonengine.event.PlayerReGetOutputEvent
import tech.cookiepower.jythonengine.io.ProxyCommandSenderWriter
import tech.cookiepower.jythonengine.util.uniqueId

@CommandHeader("//consoles", description = "Jython console")
object ConsolesCommands {
    @CommandBody
    val create = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            val event = PlayerCreateInterpreterEvent(sender.cast())
            event.call()
            if (event.isCancelled) { return@execute }
            sender.sendLang("jython-console-creating")
            submit(async = true) {
                val interpreter = PythonInterpreter()
                interpreter.setErr(ProxyCommandSenderWriter(sender,"jython-console-error"))
                interpreter.setOut(ProxyCommandSenderWriter(sender,"jython-console-output"))
                Consoles.setInterpreter(sender.uniqueId, interpreter)
                sender.sendLang("jython-console-create")
            }
        }
    }

    @CommandBody
    val close = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            val interpreter = Consoles.getInterpreter(sender.uniqueId)
            val event = PlayerCloseInterpreterEvent(sender.cast(), interpreter)
            event.call()
            if (event.isCancelled) { return@execute }
            interpreter.close()
            Consoles.removeInterpreter(sender.uniqueId)
            sender.sendLang("jython-console-interpreter-close")
        }
    }

    @CommandBody
    val clean = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            val interpreter = Consoles.getInterpreter(sender.uniqueId)
            val event = PlayerCleanInterpreterEvent(sender.cast(), interpreter)
            event.call()
            if (event.isCancelled) { return@execute }
            interpreter.cleanup()
            sender.sendLang("jython-console-interpreter-clean")
        }
    }

    @CommandBody
    val synch = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            val uuid = sender.uniqueId
            if(Consoles.inSynch(uuid)){
                Consoles.unsetSynch(uuid)
                sender.sendLang("jython-console-synch-off")
            }else{
                Consoles.setSynch(uuid)
                sender.sendLang("jython-console-synch-on")
            }
        }
    }

    @CommandBody
    val get_output = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            val interpreter = Consoles.getInterpreter(sender.uniqueId)
            val event = PlayerReGetOutputEvent(sender.cast(), interpreter)
            event.call()
            if (event.isCancelled) { return@execute }
            interpreter.setErr(ProxyCommandSenderWriter(sender,"jython-console-error"))
            interpreter.setOut(ProxyCommandSenderWriter(sender,"jython-console-output"))
            sender.sendLang("jython-console-re-get-output")
        }
    }
}
package tech.cookiepower.jythonengine.script

import org.python.core.PyException
import org.python.util.PythonInterpreter
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.console
import taboolib.common.platform.function.submit
import taboolib.common5.Demand
import taboolib.module.lang.sendLang
import tech.cookiepower.jythonengine.console.Consoles
import tech.cookiepower.jythonengine.event.PlayerRunScriptEvent
import tech.cookiepower.jythonengine.io.EmptyWriter
import tech.cookiepower.jythonengine.io.ProxyCommandSenderWriter
import tech.cookiepower.jythonengine.util.uniqueId
import java.io.FileInputStream

@CommandHeader(name = "//script", description = "jython script manager")
object ScriptCommands {
    @CommandBody
    val main = mainCommand {
        execute<ProxyCommandSender> { _, _, _ ->
            // TODO 命令简介
        }
    }

    @CommandBody
    val list = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            val scripts = ScriptManager.getScripts()
            sender.sendLang("jython-script-list-header")
            scripts.forEach {
                sender.sendLang("jython-script-list-unit",it.path,it.name?:"",it.author?:"",it.version?:"",it.description?:"")
            }
            sender.sendLang("jython-script-list-footer",scripts.size)
        }
    }

    @CommandBody
    val reload = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            ScriptManager.reloadScripts()
            sender.sendLang("jython-script-reload")
        }
    }

    @CommandBody
    val run = subCommand {
        execute<ProxyCommandSender> { sender, _, args ->
            val demand = Demand(args)
            val path = demand.get(0)
            if (path==null){
                sender.sendLang("jython-script-run-path-null")
                return@execute
            }
            val script = if (path.startsWith("#")){
                ScriptManager.getScript(path.substring(1))
            }else{
                ScriptManager.getScriptByPath(path)
            }
            if (script==null){
                sender.sendLang("jython-script-run-not-found",path)
                return@execute
            }
            val interpreterLazy :Lazy<PythonInterpreter?> = when(val interType = demand.get("on")?.uppercase()){
                null,"NEW" -> {
                    lazy { PythonInterpreter() }
                }
                "THIS" -> {
                    lazy {
                        Consoles.getInterpreterOrNull(sender.uniqueId) ?: null.also {
                            sender.sendLang("jython-console-not-initialized")
                        }
                    }
                }
                else -> {
                    sender.sendLang("jython-run-type-not-found",interType)
                    lazy { null }
                }
            }
            val output = when(val outputType = demand.get("out")?.uppercase()){
                null,"THIS" -> {
                    ProxyCommandSenderWriter(sender,"jython-script-run-output") to ProxyCommandSenderWriter(sender,"jython-script-run-error")
                }
                "CONSOLE" -> {
                    ProxyCommandSenderWriter(console(),"jython-script-run-output") to ProxyCommandSenderWriter(console(), "jython-script-run-error")
                }
                "IGNORE" -> {
                    EmptyWriter() to EmptyWriter()
                }
                else -> {
                    sender.sendLang("jython-script-run-output-type-not-found",outputType)
                    return@execute
                }
            }
            val async = demand.get("async")?.toBoolean() ?: false

            sender.sendLang("jython-script-run-start",script.path)
            submit(async = true) {
                try {
                    val interpreter = interpreterLazy.value ?: return@submit
                    val event = PlayerRunScriptEvent(sender.cast(), interpreter, script)
                    event.call()
                    if (event.isCancelled) { return@submit }
                    interpreter.setErr(output.second)
                    interpreter.setOut(output.first)
                    if (!async){
                        interpreter.execfile(FileInputStream(script.file))
                        sender.sendLang("jython-script-run-success")
                    }else{
                        submit {
                            interpreter.execfile(FileInputStream(script.file))
                            sender.sendLang("jython-script-run-success")
                        }
                    }
                }catch (e: PyException){
                    sender.sendLang("jython-script-run-error",e.message?:e.toString())
                }
            }
        }
    }
}
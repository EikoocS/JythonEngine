package tech.cookiepower.jythonengine.frame

import taboolib.common.io.getClasses
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.getDataFolder
import taboolib.common.platform.function.info
import taboolib.common.platform.function.submit
import taboolib.module.lang.sendLang
import tech.cookiepower.jythonengine.JythonEnginePlugin.config
import java.io.File

@CommandHeader("//frame")
object FrameCommand {
    private val base = File("./")
    @CommandBody
    // TODO 无法获取服务端API
    val reload = subCommand {
        execute<ProxyCommandSender> { sender, _, _ ->
            sender.sendLang("jython-frame-reload-start")
            submit(async = true) {
                val libs = File(getDataFolder(),"libs")
                getAllClass().forEach {
                    try {
                        FrameBuilder.build(PythonClass(it),libs)
                    }catch (_:Throwable){ }
                }
                sender.sendLang("jython-frame-reload-success")
            }
        }
    }

    private fun getAllClass(): Set<Class<*>>{
        return base.walk().filter { it.extension == "jar" }.filter {file ->
            config.getStringList("frame.ignore-path").none { it.toRegex().matches(file.relativeTo(base).toString()) }
        }.map {
            it.toURI().toURL().getClasses()
        }.flatten().toSet()
    }
}
package tech.cookiepower.jythonengine.script.modifier

import org.python.util.PythonInterpreter
import taboolib.common.platform.event.SubscribeEvent
import tech.cookiepower.jythonengine.event.ScriptExecuteEvent
import tech.cookiepower.jythonengine.event.ScriptLoadEvent
import tech.cookiepower.jythonengine.event.ScriptUnloadEvent

object StandaloneModifier : Modifier() {
    private val path2Interpreter = mutableMapOf<String, PythonInterpreter>()

    @SubscribeEvent(ignoreCancelled = true)
    fun onLoad(event: ScriptLoadEvent){
        if (event.script.standalone){
            path2Interpreter[event.script.path] = PythonInterpreter()
        }
    }

    @SubscribeEvent(ignoreCancelled = true)
    fun onLoad(event: ScriptUnloadEvent){
        if (event.script.standalone){
            val path = event.script.path
            path2Interpreter[path].also { path2Interpreter.remove(path) }?.close()
        }
    }

    @SubscribeEvent(ignoreCancelled = true)
    fun onExecute(event: ScriptExecuteEvent){
        val script = event.script
        if (script.standalone){
            val path = script.path
            event.interpreter = path2Interpreter[script.path] ?: PythonInterpreter().also { path2Interpreter[script.path] = it }
            if (script.end_clean){ event.after { _, _ -> path2Interpreter[path]?.cleanup() } }
        }
    }
}
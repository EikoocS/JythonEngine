package tech.cookiepower.jythonengine.script.modifier

import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submit
import tech.cookiepower.jythonengine.event.ScriptExecuteEvent
import tech.cookiepower.jythonengine.event.ScriptLoadEvent

object AsyncModifier : Modifier(){
    @SubscribeEvent(ignoreCancelled = true)
    fun onLoad(event: ScriptLoadEvent){
        val script = event.script
        if (script.async && script.sync){
            event.isCancelled = true
            throw IllegalArgumentException("Script cannot be async and sync at the same time")
        }
    }
    @SubscribeEvent(ignoreCancelled = true)
    fun onExecute(event: ScriptExecuteEvent){
        if (event.script.async){
            event.execute{ script, pythonInterpreter ->
                submit(async = true) {
                    pythonInterpreter.execfile(script.getInputStream())
                }
            }
        }else if (event.script.sync){
            event.execute{ script, pythonInterpreter ->
                submit(async = false) {
                    pythonInterpreter.execfile(script.getInputStream())
                }
            }
        }
    }
}
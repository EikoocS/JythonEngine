package tech.cookiepower.jythonengine.script.trigger

import taboolib.common.platform.event.SubscribeEvent
import tech.cookiepower.jythonengine.event.ScriptExecuteEvent
import tech.cookiepower.jythonengine.event.ScriptLoadEvent
import tech.cookiepower.jythonengine.event.ScriptUnloadEvent
import tech.cookiepower.jythonengine.script.Script

object StatusTrigger : Trigger<Unit>() {
    override val TRIGGER_IDENTIFIER: String = "STATUS_TRIGGER"

    @SubscribeEvent(ignoreCancelled = true)
    fun onScriptLoad(event: ScriptLoadEvent){
        if(event.script.autorun){
            onSubscribe(event.script)
        }
    }

    @SubscribeEvent(ignoreCancelled = true)
    fun onScriptUnload(event: ScriptUnloadEvent){
        if(event.script.release){
            onUnsubscribe(event.script)
        }
    }

    override fun onSubscribe(script: Script) {
        val event = ScriptExecuteEvent(script,defaultInterpreter)
        event.argument("__STATUS__" to "LOAD")
        defaultHandlerAndPost(event)
    }

    override fun onUnsubscribe(script: Script): Boolean {
        val event = ScriptExecuteEvent(script,defaultInterpreter)
        event.argument("__STATUS__" to "UNLOAD")
        defaultHandlerAndPost(event)
        return true
    }

    override fun getAll() {}
}
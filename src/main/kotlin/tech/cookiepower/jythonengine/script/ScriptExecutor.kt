package tech.cookiepower.jythonengine.script

import tech.cookiepower.jythonengine.event.ScriptExecuteEvent

object ScriptExecutor {
    fun post(event: ScriptExecuteEvent){
        if (event.isCancelled) return

        val script = event.script
        val interpreter = event.interpreter
        event.argumentHandler(interpreter)
        event.prepareHandler(script, interpreter)
        event.execute()
        event.afterHandler(script, interpreter)
    }
}
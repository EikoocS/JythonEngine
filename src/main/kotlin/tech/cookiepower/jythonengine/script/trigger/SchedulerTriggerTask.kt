package tech.cookiepower.jythonengine.script.trigger

import taboolib.common.platform.function.submit
import taboolib.common.platform.service.PlatformExecutor
import tech.cookiepower.jythonengine.event.ScriptExecuteEvent
import tech.cookiepower.jythonengine.script.Script
import tech.cookiepower.jythonengine.script.namespacedKey

class SchedulerTriggerTask(
    val script: Script
) {
    var task : PlatformExecutor.PlatformTask? = null
    fun start(){
        if (task != null) throw IllegalStateException("Task already started")
        task = submit(
            period=script.period?:0,
            delay = script.delay?:0,
            commit = script.namespacedKey?.toString()?:script.path,
            async = true
        ) {
            val exeEvent = ScriptExecuteEvent(script, SchedulerTrigger.defaultInterpreter)
            SchedulerTrigger.defaultHandlerAndPost(exeEvent)
        }
    }
    fun stop(){
        task?.cancel()
    }
}
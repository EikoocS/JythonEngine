package tech.cookiepower.jythonengine.script.trigger

import org.python.util.PythonInterpreter
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.info
import tech.cookiepower.jythonengine.event.ScriptLoadEvent
import tech.cookiepower.jythonengine.event.ScriptUnloadEvent
import tech.cookiepower.jythonengine.script.Script

object SchedulerTrigger : Trigger<List<SchedulerTriggerTask>>(){
    override val TRIGGER_IDENTIFIER: String = "SCHEDULER_TRIGGER"
    private val tasks = mutableListOf<SchedulerTriggerTask>()
    public override val defaultInterpreter = PythonInterpreter()

    @SubscribeEvent(ignoreCancelled = true)
    fun onScriptLoad(event: ScriptLoadEvent){
        info("####################################################################################")
        info("event subscriber: ScriptLoadEvent")
        info("####################################################################################")
        if(event.script.isSchedulerScript){
            onSubscribe(event.script)
        }
    }

    @SubscribeEvent(ignoreCancelled = true)
    fun onScriptUnload(event: ScriptUnloadEvent){
        if(event.script.isSchedulerScript){
            onUnsubscribe(event.script)
        }
    }

    override fun onSubscribe(script: Script) {
        val task = SchedulerTriggerTask(script)
        tasks.add(task)
        task.start()
    }

    override fun onUnsubscribe(script: Script): Boolean {
        val task = tasks.find { it.script == script }
        task?.stop()
        tasks.remove(task)
        return task!=null
    }

    override fun getAll(): List<SchedulerTriggerTask> = tasks
}
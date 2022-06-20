package tech.cookiepower.jythonengine.script.trigger

import taboolib.common.platform.event.SubscribeEvent
import tech.cookiepower.jythonengine.annotation.ReflexCall
import tech.cookiepower.jythonengine.event.ScriptLoadEvent
import tech.cookiepower.jythonengine.event.ScriptUnloadEvent
import tech.cookiepower.jythonengine.script.Script

@ReflexCall
object EventTrigger : Trigger<Map<String, MutableList<Script>>>() {
    override val TRIGGER_IDENTIFIER: String = "EVENT_TRIGGER"
    /**
     * @key event class name
     * @value scripts
     * */
    private val event2Scripts = mutableMapOf<String, MutableList<Script>>()

    @SubscribeEvent(ignoreCancelled = true)
    fun onScriptLoad(event: ScriptLoadEvent){
        if(event.script.isEventScript){
            onSubscribe(event.script)
        }
    }

    @SubscribeEvent(ignoreCancelled = true)
    fun onScriptUnload(event: ScriptUnloadEvent){
        if(event.script.isEventScript){
            onUnsubscribe(event.script)
        }
    }

//    @SubscribeEvent
//    fun onEventCall(event: Event){
//        event.javaClass.name.let {
//            event2Scripts[it]?.forEach { script ->
//                val exeEvent = ScriptExecuteEvent(script,defaultInterpreter)
//                exeEvent.argument("__EVENT__" to event)
//                defaultHandlerAndPost(exeEvent)
//            }
//        }
//    }

//    @SubscribeEvent(bind = "org.bukkit.event.Event")
//    fun onEventCall(ope: OptionalEvent){
//        val event = ope.get<Event>()
//        event.javaClass.name.let {
//            event2Scripts[it]?.forEach { script ->
//                val exeEvent = ScriptExecuteEvent(script,defaultInterpreter)
//                exeEvent.argument("__EVENT__" to event)
//                defaultHandlerAndPost(exeEvent)
//            }
//        }
//    }

    override fun onSubscribe(script: Script) {
        val events = script.events
        events.forEach {event ->
            event2Scripts[event]?: mutableListOf<Script>().also { event2Scripts[event] = it }.add(script)
        }
    }

    override fun onUnsubscribe(script: Script): Boolean {
        val events = script.events
        events.forEach {event ->
            event2Scripts[event]?.remove(script)
            if(event2Scripts[event]?.isEmpty() == true){ event2Scripts.remove(event) }
        }
        return true
    }

    override fun getAll(): Map<String, MutableList<Script>> = event2Scripts
}
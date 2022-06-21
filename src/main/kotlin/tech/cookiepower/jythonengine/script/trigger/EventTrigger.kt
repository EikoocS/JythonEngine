package tech.cookiepower.jythonengine.script.trigger

import org.bukkit.Bukkit
import org.bukkit.event.Event
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.Platform
import taboolib.common.platform.PlatformSide
import taboolib.common.platform.event.SubscribeEvent
import taboolib.platform.BukkitPlugin
import tech.cookiepower.jythonengine.annotation.ReflexCall
import tech.cookiepower.jythonengine.event.ScriptExecuteEvent
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

    @SubscribeEvent
    fun onScriptUnload(event: ScriptUnloadEvent){
        if(event.script.isEventScript){
            onUnsubscribe(event.script)
        }
    }

    @Awake(LifeCycle.ACTIVE)
    @PlatformSide([Platform.BUKKIT])
    fun registerListener(){
        Bukkit.getPluginManager().registerEvents(EventListener(), BukkitPlugin.getInstance())
    }

    fun onEventCall(event: Event){
        val name = event.javaClass.name
        event2Scripts[name]?.forEach { script ->
            val exeEvent = ScriptExecuteEvent(script,defaultInterpreter)
            exeEvent.argument("__EVENT__" to event)
            defaultHandlerAndPost(exeEvent)
        }
    }

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
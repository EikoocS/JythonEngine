package tech.cookiepower.jythonengine.script.trigger

import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.RegisteredListener
import taboolib.common.platform.function.submit
import taboolib.platform.BukkitPlugin

class EventListener : Listener {
    private val handlers = ArrayList<HandlerList>()
    private val listener = RegisteredListener( this,
        { _, event -> EventTrigger.onEventCall(event) },
        EventPriority.NORMAL,
        BukkitPlugin.getInstance(),
        false
    )
    private var lastSize = 0
    val task = submit(period = 20, async = true) {
        if (lastSize != HandlerList.getHandlerLists().size){
            HandlerList.getHandlerLists().filter {
                it !in handlers
            }.forEach {
                it.register(listener)
                handlers.add(it)
            }
            lastSize = HandlerList.getHandlerLists().size
        }
    }
}
package tech.cookiepower.jythonengine.console

import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.platform.util.sendLang
import tech.cookiepower.jythonengine.annotation.ReflexCall

@ReflexCall
object ConsolesHandler {

    @SubscribeEvent(ignoreCancelled = true, priority = EventPriority.LOWEST)
    fun commandFilter(event: AsyncPlayerChatEvent){
        val sender = event.player
        val uniqueId = sender.uniqueId
        val code = event.message
        if(Consoles.getSettings(uniqueId).inConsoleMode() && !code.startsWith("/")){
            event.isCancelled = true
            if(!Consoles.getSettings(uniqueId).hasInterpreter()){
                sender.sendLang("console-not-initialized")
                return
            }
            Consoles.runRickCode(sender, code)
        }
    }

    @SubscribeEvent
    fun onPlayerQuit(event: PlayerQuitEvent){
        Consoles.removeSettings(event.player.uniqueId)
    }
}
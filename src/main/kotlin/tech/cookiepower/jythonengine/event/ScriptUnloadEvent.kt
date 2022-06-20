package tech.cookiepower.jythonengine.event

import taboolib.platform.type.BukkitProxyEvent
import tech.cookiepower.jythonengine.script.Script

class ScriptUnloadEvent(val script: Script): BukkitProxyEvent(){
    override val allowCancelled: Boolean = false
}
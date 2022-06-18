package tech.cookiepower.jythonengine.event

import org.bukkit.entity.Player
import org.python.util.PythonInterpreter
import taboolib.platform.type.BukkitProxyEvent
import tech.cookiepower.jythonengine.script.Script

open class ScriptExecutionEvent(val script: Script) : BukkitProxyEvent(){ override val allowCancelled: Boolean = true }

class PlayerRunScriptEvent(val player: Player,val interpreter: PythonInterpreter,script: Script) : ScriptExecutionEvent(script){ override val allowCancelled: Boolean = true }
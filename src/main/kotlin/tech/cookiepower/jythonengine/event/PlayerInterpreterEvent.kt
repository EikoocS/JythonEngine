package tech.cookiepower.jythonengine.event

import org.bukkit.entity.Player
import org.python.util.PythonInterpreter
import taboolib.platform.type.BukkitProxyEvent

class PlayerCreateInterpreterEvent(val player: Player) : BukkitProxyEvent(){ override val allowCancelled: Boolean = true }

class PlayerCloseInterpreterEvent(val player: Player,val interpreter: PythonInterpreter) : BukkitProxyEvent(){ override val allowCancelled: Boolean = true }

class PlayerCleanInterpreterEvent(val player: Player,val interpreter: PythonInterpreter) : BukkitProxyEvent(){ override val allowCancelled: Boolean = true }

class PlayerEnterConsolesModeEvent(val player: Player,val interpreter: PythonInterpreter?) : BukkitProxyEvent(){ override val allowCancelled: Boolean = true }

class PlayerLeaveConsolesModeEvent(val player: Player,val interpreter: PythonInterpreter?) : BukkitProxyEvent(){ override val allowCancelled: Boolean = true }

class PlayerExecuteCodeEvent(val player: Player,val interpreter: PythonInterpreter,val code: String) : BukkitProxyEvent(){ override val allowCancelled: Boolean = true }

class PlayerReGetOutputEvent(val player: Player, val interpreter: PythonInterpreter) : BukkitProxyEvent(){ override val allowCancelled: Boolean = true }
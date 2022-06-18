package tech.cookiepower.jythonengine.console

import org.bukkit.entity.Player
import org.python.core.PyException
import org.python.core.PyObject
import org.python.util.PythonInterpreter
import taboolib.common.platform.function.submit
import taboolib.platform.util.sendLang
import tech.cookiepower.jythonengine.JythonEnginePlugin.config
import tech.cookiepower.jythonengine.event.PlayerExecuteCodeEvent
import java.util.*

object Consoles {
    private val consolesSet = LinkedList<UUID>()
    private val uuid2Interpreter = HashMap<UUID, PythonInterpreter>()
    private val uuid2Buffer = HashMap<UUID, StringBuilder>()
    private val uuid2synch = LinkedHashSet<UUID>()

    fun inConsoles(uniqueId: UUID): Boolean = consolesSet.contains(uniqueId)
    fun enterConsole(uniqueId: UUID) = consolesSet.add(uniqueId)
    fun exitConsole(uniqueId: UUID) = consolesSet.remove(uniqueId)

    fun inSynch(uniqueId: UUID): Boolean = uuid2synch.contains(uniqueId)
    fun setSynch(uniqueId: UUID) = uuid2synch.add(uniqueId)
    fun unsetSynch(uniqueId: UUID) = uuid2synch.remove(uniqueId)
    private fun getBuffer(uniqueId: UUID): StringBuilder = uuid2Buffer[uniqueId] ?: StringBuilder().also { uuid2Buffer[uniqueId] = it }

    fun runRickCode(sender: Player, codeSingle: String){
        val uniqueId = sender.uniqueId
        var code = formatIndent(config["console.indent-key"].firstChar(),codeSingle)
        val buffer = getBuffer(uniqueId)
        val needBuffer =codeSingle.startsWith(config["console.indent-key"].toString())||codeSingle.endsWith(":")
        val isEmptyExecute = codeSingle == config["console.indent-key"]
        val interpreter = uuid2Interpreter[uniqueId] ?: return

        if (needBuffer&&!isEmptyExecute) {
            buffer.append("\n").append(code)
            sender.sendLang("jython-console-executing-buffer", code)
            return
        }else{
            code = buffer.toString() + code
        }

        val event = PlayerExecuteCodeEvent(sender,interpreter,code)
        event.call()
        if(event.isCancelled){ return }
        buffer.setLength(0)

        submit(async = !inSynch(uniqueId)) {
            try {
                if(code.startsWith("$")){
                    sender.sendLang("jython-console-executing-with-value", code.substring(1))
                    val result = eval(uniqueId,code.substring(1))
                    sender.sendLang("jython-console-result", result)
                }else{
                    if (!isEmptyExecute)
                        sender.sendLang("jython-console-executing", code)
                    exec(uniqueId,code)
                }
            }catch (e: Throwable){
                sender.sendLang("jython-console-error",e.message?:e.toString())
            }
        }
    }

    /**
     * @throws PyException if the code is not valid
     * */
    fun exec(uniqueId: UUID, code: String){
        val interpreter = uuid2Interpreter[uniqueId]?: throw IllegalArgumentException("Interpreter not found for uuid $uniqueId")
        interpreter.exec(code)
    }
    /**
     * @throws PyException if the code is not valid
     * */
    fun eval(uniqueId: UUID,code: String): PyObject{
        val interpreter = uuid2Interpreter[uniqueId]?: throw IllegalArgumentException("Interpreter not found for uuid $uniqueId")
        return interpreter.eval(code)
    }

    fun setInterpreter(uniqueId: UUID, engine: PythonInterpreter) { uuid2Interpreter[uniqueId] = engine}
    fun removeInterpreter(uniqueId: UUID) = uuid2Interpreter.remove(uniqueId)
    fun getInterpreter(uniqueId: UUID): PythonInterpreter = uuid2Interpreter[uniqueId] ?: throw IllegalArgumentException("Interpreter not found for uuid $uniqueId")
    fun getInterpreterOrNull(uniqueId: UUID): PythonInterpreter? = uuid2Interpreter[uniqueId]
    fun haveInterpreter(uniqueId: UUID): Boolean = uuid2Interpreter.containsKey(uniqueId)

    // TODO 糟糕的结构 应该改成一个类
    fun unregisterUUID(uniqueId: UUID) {
        consolesSet.remove(uniqueId)
        uuid2Interpreter[uniqueId]?.close()
        uuid2Interpreter.remove(uniqueId)
        uuid2Buffer.remove(uniqueId)
        uuid2synch.remove(uniqueId)
    }
}
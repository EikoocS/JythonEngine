package tech.cookiepower.jythonengine.console

import org.bukkit.entity.Player
import org.python.core.PyException
import org.python.core.PyObject
import taboolib.common.platform.function.submit
import taboolib.platform.util.sendLang
import tech.cookiepower.jythonengine.JythonEnginePlugin.config
import tech.cookiepower.jythonengine.event.PlayerExecuteCodeEvent
import tech.cookiepower.jythonengine.util.string
import java.util.*

object Consoles {
    private val uuid2Settings = HashMap<UUID, ConsoleSetting>()
    private val indentKey : Char
        get() = (config.getString("console.indent-key") ?: ".").toString().first()
    private val indentValue : String
        get() = config.string("console.indent-value") ?: "    "

    fun runRickCode(sender: Player, codeRaw: String){
        val uniqueId = sender.uniqueId
        val setting = getSettings(uniqueId)
        var code = formatIndent(indentKey, indentValue, codeRaw)
        val interpreter = setting.getInterpreterOrNull() ?: return
        val needBuffer =codeRaw.startsWith(indentKey)||codeRaw.endsWith(":")
        val isEmptyExecute = codeRaw == indentKey.toString()

        if (needBuffer&&!isEmptyExecute) {
            setting.appendCodeToBuffer(code)
            sender.sendLang("jython-console-executing-buffer", code)
            return
        }else{
            code = setting.buildAndClearBuffer(code)
        }

        val event = PlayerExecuteCodeEvent(sender,interpreter,code)
        event.call()
        if(event.isCancelled){ return }

        submit(async = !getSettings(uniqueId).isSynch()) {
            try {
                if(code.startsWith("$")){
                    sender.sendLang("jython-console-executing-with-value", code.substring(1))
                    val result = eval(uniqueId,code.substring(1))
                    sender.sendLang("jython-console-result", result)
                }else{
                    if (!isEmptyExecute) sender.sendLang("jython-console-executing", code)
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
        val interpreter = getSettings(uniqueId).getInterpreterOrNull() ?: throw IllegalArgumentException("Interpreter not found for uuid $uniqueId")
        interpreter.exec(code)
    }
    /**
     * @throws PyException if the code is not valid
     * */
    fun eval(uniqueId: UUID,code: String): PyObject{
        val interpreter = getSettings(uniqueId).getInterpreterOrNull() ?: throw IllegalArgumentException("Interpreter not found for uuid $uniqueId")
        return interpreter.eval(code)
    }


    fun formatIndent(indentKey: Char,indentValue: String, value: String):String{
        val sb = StringBuilder()
        var amount = 0
        for (c in value){
            if(c==indentKey){
                sb.append(indentValue)
                amount++
            }else{
                sb.append(value.substring(amount))
                break
            }
        }
        return sb.toString()
    }

    fun getSettings(uniqueId: UUID): ConsoleSetting = uuid2Settings[uniqueId] ?: ConsoleSetting().also { uuid2Settings[uniqueId] = it }
    fun removeSettings(uniqueId: UUID) { uuid2Settings.remove(uniqueId) }
}
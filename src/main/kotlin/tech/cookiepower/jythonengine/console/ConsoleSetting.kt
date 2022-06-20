package tech.cookiepower.jythonengine.console

import org.python.util.PythonInterpreter

class ConsoleSetting {
    private var consoleMode: Boolean = false
    private var interpreter : PythonInterpreter? = null
    private var buffer : StringBuilder = StringBuilder()
    private var sync : Boolean = false

    fun inConsoleMode() : Boolean = consoleMode
    fun setConsoleMode(mode : Boolean) { consoleMode = mode }
    fun enableConsoleMode() { consoleMode = true }
    fun disableConsoleMode() { consoleMode = false }

    fun getInterpreter() : PythonInterpreter = interpreter ?: throw IllegalStateException("Interpreter is not set")
    fun getInterpreterOrNull() : PythonInterpreter? = interpreter
    fun setInterpreter(interpreter : PythonInterpreter) { this.interpreter = interpreter }
    fun removeInterpreter() { interpreter = null }
    fun removeInterpreterAndClose() {
        interpreter?.close()
        interpreter = null
    }
    fun hasInterpreter() : Boolean = interpreter != null

    fun isSync() : Boolean = sync
    fun setSync(sync : Boolean) { this.sync = sync }
    fun enableSync() { sync = true }
    fun disableSync() { sync = false }

    fun getBuffer() : StringBuilder = buffer
    fun appendCodeToBuffer(str : String) { buffer.append(str).append("\n") }
    fun buildAndClearBuffer(str: String = "") : String {
        val result = buffer.append(str).toString()
        buffer.setLength(0)
        return result
    }
    fun clearBuffer() { buffer.setLength(0) }
}
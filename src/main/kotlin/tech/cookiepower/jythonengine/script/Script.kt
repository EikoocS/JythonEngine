package tech.cookiepower.jythonengine.script

import org.python.core.PyCode
import org.python.util.PythonInterpreter
import java.io.File
import java.io.FileReader

class Script(
    val file: File,
) {
    private val data = HashMap<String, String?>()
    val path : String
        get() = file.absolutePath.removePrefix(rootDir.absolutePath)
    var code: PyCode? = null
    fun setData(key: String) = data.put(key, null)
    fun setData(key: String, value: String?) = data.put(key, value)
    fun getData(key: String): String? = data[key]
    fun hasData(key: String): Boolean = data.containsKey(key)

    fun compile(interpreter: PythonInterpreter): PyCode?{
        code = interpreter.compile(FileReader(file),path)
        return code
    }
    companion object{
        val rootDir = File(".")
    }
}

val Script.name: String?
    get() = getData("name")
val Script.author: String?
    get() = getData("author")
val Script.version: String?
    get() = getData("version")
val Script.description: String?
    get() = getData("description")


package tech.cookiepower.jythonengine.script

import tech.cookiepower.jythonengine.script.ScriptManager.rootDir
import java.io.File
import java.io.InputStream

class Script(
    private val file: File,
){
    val data = HashMap<String, String?>()
    val path : String
        get() = file.relativeTo(rootDir).path

    init {
        file.readLines().forEach { line->
            if (dataRegex.find(line) != null) {
                val (key, value) = dataRegex.find(line)!!.destructured
                this[key] = value
            }
        }
    }

    operator fun get(key: String): String? = data[key]
    operator fun set(key: String, value: String?) = data.put(key, value)
    operator fun contains(key: String): Boolean = data.containsKey(key)
    operator fun plusAssign(pair: Pair<String, String?>) { data[pair.first] = pair.second }
    operator fun plusAssign(key: String) { data[key] = null }

    fun getInputStream(): InputStream = file.inputStream()

    companion object{
        private val dataRegex = Regex("^#\\s*@(\\S*)\\s*(.*)$")
    }
}


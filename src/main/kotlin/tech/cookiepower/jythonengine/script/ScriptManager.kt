package tech.cookiepower.jythonengine.script

import org.python.util.PythonInterpreter
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.submit

object ScriptManager {
    private val scripts = mutableListOf<Script>()
    private val dataRegex = Regex("^#\\s*@(\\S*)\\s*(.*)$")

    private val interpreter = PythonInterpreter()

    fun getScripts(): List<Script> = scripts
    fun getScript(name: String): Script? = scripts.find { it.name == name }
    fun getScriptByPath(path: String): Script? = scripts.find { it.file.path == path }

    @Awake(LifeCycle.ENABLE)
    fun reloadScripts() {
        submit(async = true) {
            scripts.clear()
            Script.rootDir.walk().filter {
                it.extension == "jy"
            }.forEach {file ->
                val script = Script(file)
                file.readLines().forEach { line->
                    if (dataRegex.find(line) != null) {
                        val (key, value) = dataRegex.find(line)!!.destructured
                        script.setData(key,value)
                    }
                }
                script.compile(interpreter)
                scripts.add(script)
            }
        }
    }
}
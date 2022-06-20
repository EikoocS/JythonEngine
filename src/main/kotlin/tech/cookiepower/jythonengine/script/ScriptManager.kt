package tech.cookiepower.jythonengine.script

import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.info
import tech.cookiepower.jythonengine.event.ScriptLoadEvent
import tech.cookiepower.jythonengine.event.ScriptUnloadEvent
import java.io.File

object ScriptManager {
    val rootDir = File("./scripts")
    private val scripts = mutableListOf<Script>()
    private val namespacedKeys = mutableListOf<NamespacedKey>()

    fun getScript(path: String): Script? = scripts.find { it.path == path }
    fun getScript(key: NamespacedKey): Script? = scripts.find { it.namespacedKey == key }

    private fun load(script: Script){
        val namespacedKey = script.namespacedKey
        if (namespacedKey != null && namespacedKey in namespacedKeys){
            throw IllegalArgumentException("Can't load ${script.path} Script with namespaced key $namespacedKey " +
                    "already loaded by ${getScript(namespacedKey)?.path}")
        }
        val event = ScriptLoadEvent(script)
        if(event.call()){ return }
        scripts.add(script)
    }

    private fun unload(script: Script){
        ScriptUnloadEvent(script).call()
        namespacedKeys.remove(script.namespacedKey)
        scripts.remove(script)
    }

    private fun unRemoveUnload(script: Script){
        ScriptUnloadEvent(script).call()
        namespacedKeys.remove(script.namespacedKey)
    }

    fun loadScripts(){
        rootDir.walk().filter {
            it.isFile && it.extension == "jy"
        }.forEach {
            load(Script(it))
        }
    }

    fun unloadScripts(){
        scripts
    }

    @Awake(LifeCycle.ENABLE)
    fun reloadAllScripts(){
        unloadScripts()
        loadScripts()
    }
}
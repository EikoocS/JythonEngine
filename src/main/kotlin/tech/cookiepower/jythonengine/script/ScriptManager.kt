package tech.cookiepower.jythonengine.script

import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
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

    fun loadByDir(dir: File = rootDir){
        dir.walk().filter {
            it.isFile && it.extension == "jy"
        }.forEach {
            load(Script(it))
        }
    }

    fun unloadAll(){
        scripts.forEach {
            namespacedKeys.remove(it.namespacedKey)
            ScriptUnloadEvent(it).call()
            unload(it)
        }
        scripts.clear()
    }

    @Awake(LifeCycle.ENABLE)
    fun reloadAll(){
        unloadAll()
        loadByDir()
    }
}
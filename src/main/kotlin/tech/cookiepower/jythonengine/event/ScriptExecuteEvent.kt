package tech.cookiepower.jythonengine.event

import org.python.util.PythonInterpreter
import taboolib.platform.type.BukkitProxyEvent
import tech.cookiepower.jythonengine.script.Script

class ScriptExecuteEvent(val script: Script,var interpreter: PythonInterpreter): BukkitProxyEvent() {
    private val prepareList = mutableListOf<(Script,PythonInterpreter)->Unit>()
    private val afterList = mutableListOf<(Script,PythonInterpreter)->Unit>()
    private val argumentList = mutableListOf<Pair<String,Any?>>()
    private var execute : (Script,PythonInterpreter)->Unit = {script,interpreter-> interpreter.execfile(script.getInputStream())}

    fun prepare(func: (Script,PythonInterpreter)->Unit) = prepareList.add(func)
    fun after(func: (Script,PythonInterpreter)->Unit) = afterList.add(func)
    fun argument(arg: Pair<String,Any?>) = argumentList.add(arg)
    fun execute(func: (Script,PythonInterpreter)->Unit) { execute = func }

    fun prepareHandler(script: Script,interpreter: PythonInterpreter) = prepareList.forEach { it(script,interpreter) }
    fun afterHandler(script: Script,interpreter: PythonInterpreter) = afterList.forEach { it(script,interpreter) }
    fun argumentHandler(interpreter: PythonInterpreter) = argumentList.forEach { interpreter.set(it.first,it.second) }
    fun execute() = execute(script,interpreter)
}
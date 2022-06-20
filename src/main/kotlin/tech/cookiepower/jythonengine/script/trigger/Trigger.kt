package tech.cookiepower.jythonengine.script.trigger

import org.python.util.PythonInterpreter
import tech.cookiepower.jythonengine.event.ScriptExecuteEvent
import tech.cookiepower.jythonengine.script.Script
import tech.cookiepower.jythonengine.script.ScriptExecutor

abstract class Trigger<T> {
    /**
     * 标识这个触发器
     * @suppress 应该被 object 继承
     * */
    @Suppress("PropertyName")
    abstract val TRIGGER_IDENTIFIER: String
    /**
     * 默认解释器
     * */
    protected open val defaultInterpreter = PythonInterpreter()
    /**
     * 将标识符加入解释器以供脚本参考
     * */
    fun setIdent(interpreter: PythonInterpreter){
        interpreter.set("__TRIGGER__",TRIGGER_IDENTIFIER)
    }
    /**
     *
     */
    fun defaultHandlerAndPost(event: ScriptExecuteEvent){
        event.prepare { _, pythonInterpreter -> setIdent(pythonInterpreter) }
        event.after { _, _ ->  StatusTrigger.defaultInterpreter.cleanup()}
        event.call()
        ScriptExecutor.post(event)
    }
    /**
     * 将脚本注册到触发器
     * @param script 脚本
     * @throws IllegalArgumentException 如果脚本不适合这个触发器
     */
    abstract fun onSubscribe(script: Script)
    /**
     *从触发器中删除脚本
     * @param script 脚本
     * @return 如果删除成功，返回true，否则返回false
     * */
    abstract fun onUnsubscribe(script: Script) : Boolean
    /**
     * 列出触发器已注册的内容
     * @return 触发器已注册的内容
     * */
    abstract fun getAll(): T
}
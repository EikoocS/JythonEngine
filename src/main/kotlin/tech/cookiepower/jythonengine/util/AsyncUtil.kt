package tech.cookiepower.jythonengine.util

import taboolib.common.platform.function.submit

class CallbackAble<V>(async: Boolean,block: () -> V?) {
    var result = false
    var value : V? = null
    var callback : ((V?) -> Unit?)? = null
    var callbackAsync : Boolean? = null
    var lock : Unit = Unit
    init {
        submit(async) {
            value = block()
            result = true
        }
    }
    fun onCallback(value: V?){
        synchronized(lock){
            if(callback != null) {
                this.value = value
                result = true
            }else{
                submit(async = callbackAsync!!) {
                    callback!!(value)
                }
            }
        }
    }
    fun setCallback(callbackAsync:Boolean, callback: (V?) -> Unit?) {
        synchronized(lock){
            if(result){
                submit(async = callbackAsync) {
                    callback(value)
                }
            }else{
                this.callback = callback
                this.callbackAsync = callbackAsync
            }
        }
    }
}
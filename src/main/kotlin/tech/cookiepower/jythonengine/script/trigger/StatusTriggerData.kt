package tech.cookiepower.jythonengine.script.trigger

import tech.cookiepower.jythonengine.script.Script

/**
 * 状态调用脚本设置
 * */
val Script.autorun: Boolean
    get() = "autorun" in this
val Script.release : Boolean
    get() = "release" in this
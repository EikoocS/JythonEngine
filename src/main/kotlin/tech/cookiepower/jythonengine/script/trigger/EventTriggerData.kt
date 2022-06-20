package tech.cookiepower.jythonengine.script.trigger

import tech.cookiepower.jythonengine.script.Script

/**
 * Event 脚本设置
 * */
val Script.isEventScript: Boolean
    get() = "subscribe" in this
val Script.events: List<String>
    get() = this["events"]?.split(",")?.map { it.trim() } ?: emptyList()
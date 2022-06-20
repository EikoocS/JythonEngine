package tech.cookiepower.jythonengine.script.trigger

import tech.cookiepower.jythonengine.script.Script

/**
 * Scheduler 脚本设置
 * */
val Script.isSchedulerScript: Boolean
    get() = "scheduler" in this
val Script.period: Long?
    get() = this["period"]?.toLong()
val Script.delay: Long?
    get() = this["delay"]?.toLong()
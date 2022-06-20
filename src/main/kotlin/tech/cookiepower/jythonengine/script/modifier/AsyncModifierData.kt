package tech.cookiepower.jythonengine.script.modifier

import tech.cookiepower.jythonengine.script.Script

val Script.sync: Boolean
    get() = (this["sync"]?:"false").toBoolean()
val Script.async: Boolean
    get() = (this["sync"]?:"false").toBoolean()
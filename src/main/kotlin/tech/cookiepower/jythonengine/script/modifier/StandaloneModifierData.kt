package tech.cookiepower.jythonengine.script.modifier

import tech.cookiepower.jythonengine.script.Script

val Script.standalone: Boolean
    get() = "standalone" in this
val Script.end_clean: Boolean
    get() = "end_clean" in this
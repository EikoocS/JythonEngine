package tech.cookiepower.jythonengine.event

import taboolib.platform.type.BukkitProxyEvent
import tech.cookiepower.jythonengine.script.Script


class ScriptLoadEvent(val script: Script): BukkitProxyEvent()
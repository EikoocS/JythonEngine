package tech.cookiepower.jythonengine.io

import taboolib.common.platform.ProxyCommandSender
import taboolib.module.lang.sendLang
import java.io.Writer

class ProxyCommandSenderWriter(val sender: ProxyCommandSender, val node :String) : Writer() {
    private val buffer = StringBuilder()
    override fun write(cbuf: CharArray, off: Int, len: Int) {
        buffer.append(cbuf, off, len)
    }

    override fun flush() {
        sender.sendLang(node,buffer.toString())
        buffer.setLength(0)
    }

    override fun close() {
        sender.sendLang("jython.io.close")
    }
}
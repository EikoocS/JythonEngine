package tech.cookiepower.jythonengine.io

import java.io.Writer

class EmptyWriter : Writer() {
    var closed = false
        private set
    override fun write(cbuf: CharArray, off: Int, len: Int) {
        if (closed) throw IllegalStateException("Writer is closed")
    }

    override fun close() {
        closed = true
    }

    override fun flush() {
        if (closed) throw IllegalStateException("Writer is closed")
    }
}
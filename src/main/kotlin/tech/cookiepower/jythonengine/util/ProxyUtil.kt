package tech.cookiepower.jythonengine.util

import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.ProxyPlayer
import java.util.UUID

// TODO: 可能不安全的实现 需要改进
val ProxyCommandSender.uniqueId: UUID
    get() = if (this is ProxyPlayer) {
        this.uniqueId
    } else {
        UUID.fromString("00000000-0000-0000-0000-000000000000")
    }



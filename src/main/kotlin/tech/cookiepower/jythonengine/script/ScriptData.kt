@file:SuppressWarnings("unused")
package tech.cookiepower.jythonengine.script

/**
 * 全局脚本设置
 * */
val Script.namespacedKey: NamespacedKey?
    get() = this["namespace"]?.let { NamespacedKey(it) }
val Script.author: String?
    get() = this["author"]
val Script.version: String?
    get() = this["version"]
val Script.description: String?
    get() = this["description"]

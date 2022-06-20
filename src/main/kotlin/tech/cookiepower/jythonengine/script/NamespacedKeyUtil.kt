package tech.cookiepower.jythonengine.script

fun NamespacedKey(namespacedKey: String):NamespacedKey{
    val split = namespacedKey.split(":")
    if (split.size != 2) { throw IllegalArgumentException("Invalid namespaced key: $namespacedKey") }
    return NamespacedKey(split[0],split[1])
}
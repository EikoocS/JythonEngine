package tech.cookiepower.jythonengine.util

import taboolib.library.configuration.ConfigurationSection
import taboolib.module.configuration.util.getMap

fun ConfigurationSection.string(key: String): String? = getString(key)
fun ConfigurationSection.int(key: String): Int = getInt(key)
fun ConfigurationSection.long(key: String): Long = getLong(key)
fun ConfigurationSection.double(key: String): Double = getDouble(key)
fun ConfigurationSection.boolean(key: String): Boolean = getBoolean(key)
fun ConfigurationSection.stringList(key: String): List<String> = getStringList(key)
fun ConfigurationSection.map(key: String): Map<String, Any> = getMap(key)
fun ConfigurationSection.section(key: String): ConfigurationSection? = getConfigurationSection(key)

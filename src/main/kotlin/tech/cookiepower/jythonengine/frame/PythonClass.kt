package tech.cookiepower.jythonengine.frame

import java.lang.reflect.Field
import java.lang.reflect.Method

class PythonClass(
    clazz: Class<*>
){
    val packagePath: String
    val name: String
    val field: List<Field>
    val methods: List<Method>
    val imports = mutableSetOf<Class<*>>()
    val extensions = mutableSetOf<Class<*>>()

    init {
        packagePath = clazz.`package`.name
        name = clazz.simpleName
        field = clazz.declaredFields.toList()
        methods = clazz.declaredMethods.toList()

        clazz.interfaces.also { imports.addAll(it); extensions.addAll(it) }
        clazz.superclass?.also { imports.add(it); extensions.add(it) }
        methods.map {
            //it.parameterTypes.forEach { c -> imports.add(c) }
            //it.returnType.also { c -> imports.add(c) }
        }
    }
}
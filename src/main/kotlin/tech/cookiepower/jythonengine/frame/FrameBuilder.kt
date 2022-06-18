package tech.cookiepower.jythonengine.frame

import java.io.File
import java.io.FileWriter
import java.lang.reflect.Modifier

object FrameBuilder {
    fun build(clazz: PythonClass,root: File){
        if (clazz.name.isBlank()){ return }

        val outputFile = getPathFile(clazz, root)
        val writer = FileWriter(outputFile)

        clazz.imports.forEach{
            writer.write("from ${it.`package`.name} import ${it.simpleName}\n")
        }
        writer.write("class ${clazz.name}(${clazz.extensions.joinToString(", ") { it.simpleName }}):\n")
        clazz.methods.forEach { method ->
            if (method.name.contains("$")){ return@forEach }

            if(Modifier.isStatic(method.modifiers)){
                writer.write("    def ${method.name}(${method.parameters.joinToString(", ") { it.name }}):\n")
            }else{
                writer.write("    def ${method.name}(self, ${method.parameters.joinToString(", ") { it.name }}):\n")
            }
            writer.write("        pass\n")
        }
        writer.flush()
        writer.close()
    }

    fun getPathFile(clazz: PythonClass,root: File):File{
        val packPath = clazz.packagePath.replace(".","/")
        val pack = File(root,packPath).also {
            it.mkdirs()
        }
        val clazzPath = clazz.name+".py"
        return File(pack,clazzPath).also {
            it.createNewFile()
        }
    }
}
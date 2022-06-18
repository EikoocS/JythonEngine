package tech.cookiepower.jythonengine.console

import tech.cookiepower.jythonengine.JythonEnginePlugin

fun Any?.firstChar(): Char = (this?.toString()?.get(0) ?: ' ')

fun formatIndent(asIndent: Char, value: String):String{
    val sb = StringBuilder()
    var amount = 0
    for (c in value){
        if(c==asIndent){
            sb.append(JythonEnginePlugin.config["console.indent-value"])
            amount++
        }else{
            sb.append(value.substring(amount))
            break
        }
    }
    return sb.toString()
}
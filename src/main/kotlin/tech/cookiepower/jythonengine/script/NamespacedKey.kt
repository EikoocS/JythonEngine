package tech.cookiepower.jythonengine.script

data class NamespacedKey(
    val namespace: String,
    val key: String
){
    init {
        if(namespace.isBlank()){ throw IllegalArgumentException("Namespace cannot be empty") }
        if(key.isBlank()){ throw IllegalArgumentException("Key cannot be empty") }
    }

    override fun toString(): String {
        return "$namespace:$key"
    }

    override fun equals(other: Any?): Boolean {
        return when(other){
            is NamespacedKey -> this.toString() == other.toString()
            is String -> this.toString() == other
            else -> false
        }
    }

    override fun hashCode(): Int {
        var result = namespace.hashCode()
        result = 31 * result + key.hashCode()
        return result
    }
}
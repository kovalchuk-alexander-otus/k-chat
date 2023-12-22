package data

/**
 * Сообщения
 */
data class Message(
    val author: User, // автор сообщения
    var text: String, // текст сообщения
    var date: Long, // дата
    val onlyOne: Boolean? = false // признак того, что сообщение после прочтения удаляется из чата
)

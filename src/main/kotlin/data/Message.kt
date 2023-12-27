package data

/**
 * Сообщения
 *
 */
data class Message(
    val author: User, // автор сообщения
    var text: String, // текст сообщения
    var date: Long? = System.currentTimeMillis(), // дата
    val onlyOne: Boolean? = false, // признак того, что сообщение после прочтения удаляется из чата
    var isReading: Boolean = false // признак того, что сообщение прочитано
) : Printed {
    constructor() : this(User(), "")

    val noMessage: String = "Нет сообщений"
    override fun getInfo(): String {
        return text
    }

    override fun getEmptyValue(): String {
        return noMessage
    }
}

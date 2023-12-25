package data

/**
 * Сообщения
 *
 * TODO: тут как раз сделал реализацию методов, заявленных в интерфейсе Printed .. но раскрутить цепочку до использования не удалось
 *  причины описал в другом TODO
 */
data class Message(
    val author: User, // автор сообщения
    var text: String, // текст сообщения
    var date: Long? = System.currentTimeMillis(), // дата
    val onlyOne: Boolean? = false, // признак того, что сообщение после прочтения удаляется из чата
    var isReading: Boolean = false // признак того, что сообщение прочитано
):Printed {
    val noMessage: String = "нет сообщений"
    override fun getTextToScreen(): String {
        return text
    }
    override fun getEmptyValue(): String {
        return noMessage
    }
}

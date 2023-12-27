package data

/**
 * Чат — это общение с одним человеком, так называемые direct messages.
 *  В каждом чате есть сообщения от 1 до нескольких (см. раздел ниже).
 *  В каждом чате есть прочитанные и непрочитанные сообщения.
 */
data class Chat(
    val user: User, // собеседник
    val messages: MutableList<Message>,  // список сообщений
) : Printed {
    constructor() : this(User(), mutableListOf())

    val noMessage: String = "Нет чатов"
    override fun getInfo(): String {
        return user?.nickName ?: ""
    }

    override fun getEmptyValue(): String {
        return noMessage
    }
}

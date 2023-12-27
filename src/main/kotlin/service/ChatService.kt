package service

import data.Chat
import data.Message
import data.Printed
import data.User
import java.util.HashMap
import java.util.function.Consumer

/**
 * Сервис обмена сообщениями
 *
 */

object ChatService {
    private lateinit var owner: User
    val chats: MutableMap<User, Chat> = HashMap() // список чатов конкретного Пользователя

    fun getOwner(): User {
        return owner
    }

    /**
     * Инициализация сервиса
     */
    fun init(user: User) {
        owner = user
    }

    /**
     * Создание нового чата
     */
    private fun addChat(user: User): Chat? {
        chats[user] = Chat(user, mutableListOf())
        return chats[user]
    }

    /**
     * Удаление чата
     */
    fun delChat(user: User) {
        chats.remove(user)
    }

    /**
     * Просмотр списка имеющихся чатов
     */
    fun getChats(): List<Chat> {
        return chats.map<User, Chat, Chat> { (u, c) -> c }.sortedBy { c -> c.user?.nickName }.toList()
    }

    /**
     * Число чатов с непрочитанными сообщениями
     */
    fun getUnreadChatsCount(): Int {
        return chats.count { c -> c.value.messages?.any { m -> m.isReading == false } ?: false }
    }

    /**
     * Число непрочитанных сообщений в чате
     */
    fun getUnreadMessagesCount(user: User): Int {
        return chats[user]?.messages?.count { m -> !m.isReading } ?: 0
    }

    /**
     * Создать сообщение
     * Чат создаётся, когда пользователю отправляется первое сообщение.
     */
    fun addMessage(user: User, text: String, onlyOne: Boolean? = false, author: User? = owner): Message? {
        if (!chats.contains(user)) addChat(user)
        chats[user]?.messages?.add(Message(author ?: owner, text, System.currentTimeMillis(), onlyOne))
        return chats[user]?.messages?.last()
    }

    /**
     * Редактировать сообщение
     */
    fun editMessage(user: User, message: Message?, text: String) {
        chats.get(user)?.messages?.filter { m -> m == message }?.forEach { m ->
            m.date = System.currentTimeMillis()
            m.text = text
        }
    }

    /**
     * Удалить сообщение
     */
    fun delMessage(user: User, message: Message?) {
        chats.get(user)?.messages?.remove(message)
    }

    /**
     * Сообщение прочитано (не перегружаем функцию информацией о чате - чисто работаем с Сообщением)
     */
    fun readMessage(user: User, message: Message?): Message? {
        return message?.also {
            it.isReading = true
            if (it.onlyOne == true) delMessage(user, it)
        }
    }

    /**
     * Просмотр последних сообщений из чата
     */
    fun getMessage(user: User): List<Message> {
        return chats[user]?.messages?.onEach { m -> readMessage(user, m) }?.sortedBy { m -> m.date } ?: emptyList()
    }

    /**
     * Читаем несколько сообщений
     */
    fun getMessage(user: User, count: Int): List<Message> {
        return chats[user]?.messages?.take(count)?.onEach { m -> readMessage(user, m) }?.sortedBy { m -> m.date }
            ?: emptyList()
    }

    /**
     * Получить список непрочитанных сообщений
     */
    fun getLastMessage(user: User): List<Message> {
        return chats[user]?.messages?.takeLast(getUnreadMessagesCount(user))?.onEach { m -> readMessage(user, m) }
            ?.sortedBy { m -> m.date } ?: emptyList()
    }

    /**
     * Печать сообщений на экран
     */
    fun <T : Printed> printListWithNewLines(list: List<T>, type: Class<T>) {
        val obj: T = type.newInstance()
        if (list.isEmpty()) {
            println(obj.getEmptyValue())
            /*when (type) {
                Message::class.java -> println("Нет сообщений")
                Chat::class.java -> println("Нет чатов")
                else -> println("Список пуст")
            }*/
        } else {
            list.forEach { println(it.getInfo()) }
        }
    }

    /**
     * ONLY FOR TEST
     */
    fun clear() {
        chats.clear()
    }
}
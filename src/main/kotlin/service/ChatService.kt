package service

import data.Chat
import data.Message
import data.User
import java.util.HashMap

/**
 * Сервис обмена сообщениями
 *
 * TODO: описать тесты
 * TODO: настроить ci
 *
 * TODO: уточниться по требованиям - не следует ли описать какую-то свою функцию
 */

object ChatService {
    private lateinit var owner: User
    private val chats: MutableMap<User, Chat> = HashMap() // список чатов конкретного Пользователя

    /**
     * Инициализация сервиса
     */
    fun init(user: User) {
        owner = user
    }

    /**
     * Создание нового чата
     */
    fun addChat(user: User): Chat? {
        chats.put(user, Chat(mutableListOf()))
        return chats.get(user)
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
    fun showChats() {
        chats.forEach(::println)
    }

    /**
     * Число чатов с непрочитанными сообщениями
     */
    fun getUnreadChatsCount(): Int {
        return chats.filter { c -> c.value.messages.filter { m -> m.isReading == false }.isNotEmpty() }.count()
    }

    /**
     * Число непрочитанных сообщений в чате
     */
    fun getUnreadMessagesCount(user: User): Int {
        return chats.get(user)?.messages?.filter { m -> m.isReading == false }?.count() ?: 0
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
    fun readMessage(message: Message?) {
        message?.isReading = true
    }

    /**
     * Просмотр сообщений из чата
     */
    fun showMessage(user: User) {
        chats.get(user)?.messages?.forEach(::println)
    }

    /**
     * Читаем несколько сообщений
     */
    fun showMessage(user: User, count: Int) {
        var i = 0
        chats.get(user)?.messages?.forEach { m ->
            if (++i > count) return
            readMessage(m)
            println(m)
        }
    }

    /**
     * Получить список непрочитанных сообщений
     */
    fun showLastMessage(user: User) {
        val result = chats.get(user)?.messages?.filter { m -> m.isReading == false } ?: emptyList()
        if (result.isEmpty()) println("нет сообщений") else result.forEach(::println)
    }
}
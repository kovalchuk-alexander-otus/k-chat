package service

import data.Chat
import data.Message
import data.User
import java.util.HashMap

/**
 * Сервис обмена сообщениями
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
     * Создать сообщение
     */
    fun addMessage(user: User, text: String, onlyOne: Boolean? = false): Message? {
        chats[user]?.messages?.add(Message(owner, text, System.currentTimeMillis(), onlyOne))
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
     * Просмотр сообщений из чата
     */
    fun showMessage(user: User) {
        chats.get(user)?.messages?.forEach(::println)
    }
}
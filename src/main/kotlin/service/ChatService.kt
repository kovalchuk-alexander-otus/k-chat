package service

import data.Chat
import data.Message
import data.User
import java.util.HashMap

/**
 * Сервис обмена сообщениями
 *
 * TODO: Возможности для пользователя:
 *  - Видеть, сколько чатов не прочитано (например, service.getUnreadChatsCount). В каждом из таких чатов есть хотя бы одно непрочитанное сообщение.
 *  - Получить список чатов (например, service.getChats).
 *  - Получить список последних сообщений из чатов (можно в виде списка строк). Если сообщений в чате нет (все были удалены), то пишется «нет сообщений».
 *  - Получить список сообщений из чата, указав:
 *     - ID собеседника;
 *     - количество сообщений. После того как вызвана эта функция, все отданные сообщения автоматически считаются прочитанными.
 *  - Создать новое сообщение.
 *  - Удалить сообщение.
 *  - Создать чат. Чат создаётся, когда пользователю отправляется первое сообщение.
 *  - Удалить чат, т. е. целиком удалить всю переписку.
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
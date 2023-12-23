package service

import data.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ChatServiceTest {
    // говорилка
    private val watsApp = ChatService

    // пользаки
    private lateinit var owner: User
    private lateinit var user1: User
    private lateinit var user2: User

    @BeforeEach
    fun initTest() {
        // пользаки
        owner = User("Коваленко Иван", "koba", "koba@mail.ru")
        user1 = User("Коваленко Наталья", "koban", "koban@mail.ru")
        user2 = User("Изумрудный Павел", "pahan", "izi-pahan@mail.ru")
        // сервис
        watsApp.clear()
        watsApp.init(owner)
    }

    /**
     * Установка говорилки
     */
    @Test
    fun setupServiceTest() {
        val ownerTest = User("Кружкин Петр", "kruPa", "kruggg@mail.ru")
        watsApp.init(ownerTest)
        assertEquals(ownerTest, watsApp.getOwner())
    }

    /**
     * Добавление сообщения в чат
     *   (первое сообщение в чате - инициирует создание чата)
     */
    @Test
    fun addMessageTest() {
        watsApp.addMessage(user1, "test", false)

        assert(watsApp.chats.containsKey(user1))
    }

    /**
     * Удаление чата
     */
    @Test
    fun delChatTest() {
        watsApp.addChat(user1)
        watsApp.delChat(user1)

        assert(!watsApp.chats.containsKey(user1))
    }

    /**
     * Число чатов с непрочитанными сообщениями
     */
    @Test
    fun getUnreadChatsCountTest() {
        watsApp.addChat(user1)
        val m1 = watsApp.addMessage(user2, "message")

        assertEquals(1, watsApp.getUnreadChatsCount())

        val m2 = watsApp.addMessage(user1, "message")
        val m3 = watsApp.addMessage(user1, "message", false, owner)

        assertEquals(2, watsApp.getUnreadChatsCount())

        watsApp.readMessage(m1)

        assertEquals(1, watsApp.getUnreadChatsCount())

        watsApp.readMessage(m2)

        assertEquals(1, watsApp.getUnreadChatsCount())
    }

    /**
     * Число непрочитанных сообщений в чате
     */
    @Test
    fun getUnreadMessagesCountTest() {
        val m1 = watsApp.addMessage(user1, "message")
        val m2 = watsApp.addMessage(user1, "message", true)
        val m3 = watsApp.addMessage(user1, "message")

        assertEquals(3, watsApp.getUnreadMessagesCount(user1))

        watsApp.readMessage(m2)

        assertEquals(2, watsApp.getUnreadMessagesCount(user1))

        assertEquals(0, watsApp.getUnreadMessagesCount(owner))

        watsApp.delMessage(user1, m1)
        watsApp.delMessage(user1, m3)

        assertEquals(0, watsApp.getUnreadMessagesCount(user1))
    }
}
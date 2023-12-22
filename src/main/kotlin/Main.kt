import data.User
import service.ChatService

fun main() {
    println("test")
    val t = System.currentTimeMillis()

    // пользаки
    val owner = User("Коваленко Иван", "koba", "koba@mail.ru")
    val user1 = User("Коваленко Наталья", "koban", "koban@mail.ru")
    val user2 = User("Изумрудный Павел", "pahan", "izi-pahan@mail.ru")

    // владелец ставит себе вацапу-гравицапу
    val watsApp = ChatService
    watsApp.init(owner)

    // владелец заводит чаты
    val c1 = watsApp.addChat(user1)
    val c2 = watsApp.addChat(user2)
    println(c1)
    println(c2)
    println()

    // манипуляции с чатами
    watsApp.showChats()
    watsApp.delChat(user2)
    println()
    watsApp.showChats()

    // поговорим
    val m1 = watsApp.addMessage(user1, "привет")
    val m2 = watsApp.addMessage(user1, "как дела")
    println(".. " + watsApp.getUnreadMessagesCount(user1))
    val m3 = watsApp.addMessage(user1, "чо по чем")
    println(".. " + watsApp.getUnreadMessagesCount(user1))
    val m300 = watsApp.addMessage(user1, "а чувычка пунс", author = user1)
    println(watsApp.getUnreadChatsCount())

    val m4 = watsApp.addMessage(user2, "привет!")
    println(watsApp.getUnreadChatsCount())
    println(".. " + watsApp.getUnreadMessagesCount(user2))

    println(m1)
    println(m2)
    println(m3)
    println(m4)

    watsApp.showMessage(user1)
    println()
    watsApp.editMessage(user1, m3, "что делаешь")
    watsApp.showMessage(user1)
    println("..до удаления")
    watsApp.editMessage(user1, m1, "доброе утро")
    watsApp.showMessage(user1)
    println("..после удаления")
    watsApp.delMessage(user1, m2)
    watsApp.showMessage(user1)
    println()
    println(watsApp.getUnreadMessagesCount(user1))
    watsApp.showLastMessage(user1)

    watsApp.readMessage(m300)
    println("..show after reading")
    println(m300)

    println("..read to message")
    watsApp.showMessage(user1, 2)

    watsApp.delMessage(user1,m1)
    watsApp.delMessage(user1,m3)
    println("..show after del : showMessage")
    watsApp.showMessage(user1)

    println("..show after del : showLastMessage")
    watsApp.showLastMessage(user1)

    //watsApp.showLastMessage(user1)
    //watsApp.showLastMessage(user2)
    //watsApp.showLastMessage(user1)

}

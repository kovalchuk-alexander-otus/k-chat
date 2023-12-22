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
    val m3 = watsApp.addMessage(user1, "чо по чем")

    val m4 = watsApp.addMessage(user2, "привет")

    println(m1)
    println(m2)
    println(m3)
    println(m4)

    watsApp.showMessage(user1)
    println()
    watsApp.editMessage(user1, m3, "что делаешь")
    watsApp.showMessage(user1)
    println()
    watsApp.editMessage(user1, m1, "доброе утро")
    watsApp.showMessage(user1)
    println()
    watsApp.delMessage(user1, m2)
    watsApp.showMessage(user1)

}

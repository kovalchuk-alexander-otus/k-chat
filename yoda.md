
> I've been training Jedi for eight hundred years. And I keep quiet about who I train.

# Master Yoda's Rules of Life
[:bowtie:](https://github.com/ikatyang/emoji-cheat-sheet/blob/master/README.md)

## _SPR (Single responsibility principle)_
Принцип единой ответственности

Согласно заданию нам нужно получить список чатов, список последних сообщений из чатов, список сообщений из чата, указав параметры. Функции `showLastMessage`, `showMessage`, `showChats` у вас находят требуемое и выводят на экран, таким образом осуществляют сразу 2 действия. Хотелось бы разделить ответственность так, чтобы функция только находило требуемое и возвращала в качестве результата (что удовлетворяло бы условию задания), а вторая выводила на экран эти данные. Таким образом у вас получится соблюсти принцип единственной ответственности.

## _forEach : специфические альтернативы_
В функции `showMessage` хотелось бы отказаться от `forEach`, попробуйте рассмотреть возможность использования `take` (`takeLast`), `onEach`, `also`.

## _private : функции, которые не хотим дать пользователям_
Так как чаты должны создаваться автоматически при отправке первого сообщения, функцию `addChat` было бы неплохо сделать приватной.

## _count : фильтрация внутри_
Некоторые функции можно немного оптимизировать:
В этом варианте используется `count` с предикатом. Этот метод прямо подсчитывает количество элементов, удовлетворяющих условию, не создавая при этом отдельного списка.

```kotlin
fun getUnreadMessagesCount(user: User): Int {
    return chats.get(user)?.messages?.count { m -> m.isReading == false } ?: 0
}
```

## _any : проверка наличия_

В этом варианте используется `count` с предикатом для чатов и `any` для проверки наличия хотя бы одного непрочитанного сообщения в каждом чате. Этот подход избегает создания дополнительных коллекций и немедленно возвращает результат, как только находит подходящий элемент.

```kotlin
fun getUnreadChatsCount(): Int {
    return chats.count { c -> c.value.messages.any { m -> m.isReading == false } }
}
```

<details>
<summary>student</summary>

…сомневаюсь только в том, как реализовал единую ответственность  
…а именно, хотел сделать так, чтобы функция печати на экран была стандартной - для этого на вход ей следует передавать список строк (не важно, чаты это или сообщения)

и именно из-за этого, showChats, showMessage реализовал т.о., что они не возвращают список объектов, а возвращают список строк

но кажется это не верное решение - лучше все же возвращать список объектов

второй момент … у меня уже была функция readMessage()
насколько корректно внутри этого метода реализовать println()
…ну т.е. внутри этого метода реализована логика по простановки признака, что месага прочитана и удаление месаги, если она помечена признаком, что можно прочитать только один раз
…вроде логично внутри и печать сделать, не выделяя для этого отдельно printToScreen()

…хотя возможно - здесь решение аргументированно можно принять только при реальной разработке - в текущих же требованиях можно поступить и так и так…?
</details>

## _generics_
Касательно первого момента, здесь хорошо бы подошло использование дженериков.
Пример реализации:
```kotlin
fun <T> printListWithNewLines(list: List<T>) {
    list.forEach { item -> // так как нам не нужен список на выходе (функция ничего не возвращает), здесь подойдёт именно forEach, так как он не возвращает (не создаёт) список
        println(item)
    }
}
```
## _SPR (пример №2)_
Касательно второго момента, лучше всё же разделить. Например, метод возвращает сообщение, попутно удаляя его из чата. Что делать с этим сообщением дальше (передать спецслужбам или вывести на экран) будет решаться уже в контексте приложения (для вывода на экран можно воспользоваться дженерик функцией).
Пример реализации:
```kotlin
fun readMessage(user: User, message: Message?): Message? {
    return message?.also {
        it.isReading = true
        if (it.onlyOne) {
            delMessage(user, it)
        }
    }
}
```
## _get* : когда этот префикс уместен в названии функций_
На что стоит обратить внимание: если метод возвращает элемент, или список элементов, то его желательно переименовать с show на `get`.


## _companion object_

Небольшое замечание: обычно для констант, которые не изменяются и являются общими для всех экземпляров класса, лучше использовать companion object. Это позволяет избежать дублирования этого значения в каждом экземпляре Message.

<details>
<summary>student</summary>

завис на пункте с генериком  
текущая реализация имеет геттеры, возвращающие списки Строк с данными о имеющихся чатах и сообщениях  
этот факт меня несколько озадачил  
я кинулся переделывать геттеры, чтобы они возвращали список Чатов и Сообщений (а не Строк)  
далее, я решил что было бы красиво сделать интерфейс, реализацию которого выполнить в классах Чат и Сообщение  
но споткнулся на методе, возвращающем непрочитанные сообщения, а если таких нет, то возвращается “нет сообщений” …  
получалось, что:
- либо оставить, возвращать список Строк;
- либо значение “нет сообщений” поместить также в объект Сообщение;
- либо значение “нет сообщений” …“нет чатов” хранить также в data class (например, сделать под них геттер).

ну т.е. примерно так

```kotlin
interface Printed {
    fun getTextToScreen(): String
    fun getEmptyValue(): String
}
```
и реализация
```kotlin
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
```

для первого варианта - не получалось создать одну функцию и под список наследников Printed и под список Строк
```kotlin
fun <T: Printed> printListWithNewLines(list: List<T>) {
    list.forEach { item -> println(item.getTextToScreen()) }
}
```
для второго варианта … видилось костылем - создание объекта Message(User(), “нет сообщений”)
```kotlin
fun getLastMessage(user: User): List<String> {
    return chats[user]?.messages?.takeLast(getUnreadMessagesCount(user))?.onEach { m -> readMessage(user, m) }
    ?.sortedBy { m -> m.date }?.map { m -> m.text }?.toList() ?: listOf(Message(User(), noMessage))
}
```
для третьего варианта не понял, как в случае пустого списка обратиться к статическому методу объекта
```kotlin
fun <T> printListWithNewLines(list: List<T>) {
    // ну, т.е. если список пустой, то надо обратиться к T::getEmptyValue()
    list.forEach { item -> println(item) }
}
```

</details>

## _KISS : keep it simple, stupid_
keep it short and simple :octocat:  

Принцип KISS подсказывает, что важно стремиться к простоте в решениях. В контексте вашей задачи, это может означать выбор подхода, который наиболее прост и понятен, даже если это означает некоторые компромиссы по гибкости. Например, вместо создания сложных иерархий интерфейсов или классов, может быть проще использовать общие функции с явной логикой обработки для разных типов данных.

Возможные реализации:

## _Class\<T> : use in parameters function_
Более производительный вариант:
```kotlin
fun <T> printListWithMessage(list: List<T>, type: Class<T>) {
    if (list.isEmpty()) {
        when (type) {
            Message::class.java -> println("Нет сообщений")
            Chat::class.java -> println("Нет чатов")
            else -> println("Список пуст")
        }
    } else {
        list.forEach { println(it) }
    }
}
```
## _inline, \<reified T> & T::class_
Более простой в использовании вариант:
```kotlin
inline fun <reified T> printListWithMessage(list: List<T>) {
    if (list.isEmpty()) {
        when (T::class) {
            Message::class -> println("Нет сообщений")
            Chat::class -> println("Нет чатов")
            else -> println("Список пуст")
        }
    } else {
        list.forEach { println(it) }
    }
}
```
<details>
<summary>student</summary>

нашел в github дифф коммитов, возможно им удобнее смотреть дельту с предыдущей версией
[diff](https://github.com/kovalchuk-alexander-otus/k-chat/compare/914ab6a3458ed67b3972780726cdc4aaa75df8c9..27befe640faa943ee1e375a04ab5654b59b12f4c?diff=split&w=)

заморочился, чтобы уйти от перебора известных классов в методе
получилось, но пришлось в округе наколотить чуть больше кода
```kotlin
fun <T : Printed> printListWithNewLines(list: List<T>, type: Class<T>) {
    if (list.isEmpty()) {
        val obj: T = type.newInstance()
        println(obj.getEmptyValue())
    } else {
        list.forEach { println(it.getInfo()) }
    }
}
```
</details>

## _companion object_
Небольшое замечание: обычно для констант, которые не изменяются и являются общими для всех экземпляров класса, лучше использовать `companion object`. Это позволяет избежать дублирования этого значения в каждом экземпляре `Message`.

```kotlin
data class Message(
    val author: User, // автор сообщения
    var text: String, // текст сообщения
    var date: Long? = System.currentTimeMillis(), // дата
    val onlyOne: Boolean? = false, // признак того, что сообщение после прочтения удаляется из чата
    var isReading: Boolean = false // признак того, что сообщение прочитано
) : Printed {

    constructor() : this(User(), "")

    override fun getInfo(): String {
        return text
    }

    override fun getEmptyValue(): String {
        return NO_MESSAGE
    }

    companion object {
        const val NO_MESSAGE = "Нет сообщений"
    }
}
```

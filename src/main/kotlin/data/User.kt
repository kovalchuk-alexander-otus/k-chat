package data

/**
 * Пользователь
 */
data class User(
    val fullName: String, // полное имя пользователя
    val nickName: String, // ник пользователя
    val email: String // электронная почта
) {
    constructor() : this("", "", "")
}

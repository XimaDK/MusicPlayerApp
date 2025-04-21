package kadyshev.dmitry.ui_tracks_core

import android.content.Context
import android.widget.Toast

fun showToast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
}

fun mapErrorToMessage(e: Throwable): String {
    return when (e) {
        is java.net.UnknownHostException -> "Нет подключения к интернету"
        is java.net.SocketTimeoutException -> "Превышено время ожидания ответа от сервера"
        else -> e.message ?: "Неизвестная ошибка"
    }
}
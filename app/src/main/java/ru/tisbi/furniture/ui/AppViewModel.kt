package ru.tisbi.furniture.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.tisbi.furniture.data.AppData
import ru.tisbi.furniture.data.AppDatabase
import ru.tisbi.furniture.data.RefKind
import ru.tisbi.furniture.data.Repository
import ru.tisbi.furniture.data.UserError

enum class Role { Admin, Manager }

/** Экраны, открываемые поверх вкладок (стек навигации). */
sealed interface Route {
    data class ProductEdit(val id: Long?) : Route
    data object OrderNew : Route
    data class OrderDetail(val id: Long) : Route
    data object SupplyNew : Route
    data class SupplyDetail(val id: Long) : Route
    data class RefList(val kind: RefKind) : Route
    data object Suppliers : Route
    data object Reports : Route
}

class AppViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = Repository(AppDatabase.create(app).dao())

    var role by mutableStateOf<Role?>(null)
        private set
    var tab by mutableIntStateOf(0)
    val stack = mutableStateListOf<Route>()
    var message by mutableStateOf<String?>(null)
    var data by mutableStateOf(AppData())
        private set

    init {
        viewModelScope.launch { data = withContext(Dispatchers.IO) { repo.load() } }
    }

    fun login(r: Role) { role = r; tab = 0; stack.clear() }
    fun logout() { role = null; stack.clear() }
    fun push(r: Route) { stack.add(r) }
    fun pop() { if (stack.isNotEmpty()) stack.removeAt(stack.lastIndex) }

    /** Операция записи: выполняется в фоне, затем обновляется снимок данных. */
    fun exec(okMessage: String? = null, onDone: () -> Unit = {}, block: Repository.() -> Unit) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    repo.block()
                    Result.success<AppData>(repo.load())
                } catch (e: UserError) {
                    Result.failure<AppData>(e)
                } catch (e: Exception) {
                    Result.failure<AppData>(UserError("Ошибка базы данных: ${e.message}"))
                }
            }
            result.onSuccess {
                data = it
                okMessage?.let { m -> message = m }
                onDone()
            }.onFailure { message = it.message }
        }
    }

    /** Запрос на чтение (детали заказа, отчёты). */
    fun <T> load(block: Repository.() -> T, onResult: (T) -> Unit) {
        viewModelScope.launch { onResult(withContext(Dispatchers.IO) { repo.block() }) }
    }
}

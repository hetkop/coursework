@file:OptIn(ExperimentalMaterial3Api::class)

package ru.tisbi.furniture.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ru.tisbi.furniture.data.Ref
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

// ---------- форматирование и проверки ----------

fun money(v: Double): String {
    val pattern = if (v % 1.0 == 0.0) "%,.0f" else "%,.2f"
    return String.format(Locale("ru", "RU"), pattern, v) + " ₽"
}

fun fmtDate(iso: String): String = try {
    LocalDate.parse(iso).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
} catch (e: Exception) {
    iso
}

val NAME_REGEX = Regex("^[А-Яа-яЁё]+([ -][А-Яа-яЁё]+)*$")
val PHONE_REGEX = Regex("^\\+7 \\(\\d{3}\\) \\d{3}-\\d{2}-\\d{2}$")
const val REQUIRED = "Заполните все обязательные поля"

/** Приводит имя к виду «Иванов», «Петров-Водкин». */
fun normName(s: String) = s.trim().lowercase().split("-").joinToString("-") { part ->
    part.split(" ").joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
}

/** Маска телефона +7 (XXX) XXX-XX-XX: пользователь вводит только цифры. */
fun formatPhone(raw: String): String {
    var d = raw.filter { it.isDigit() }
    if (raw.startsWith("+7")) d = d.drop(1)
    else if (d.length == 11 && (d[0] == '7' || d[0] == '8')) d = d.drop(1)
    d = d.take(10)
    if (d.isEmpty()) return ""
    val sb = StringBuilder("+7 (").append(d.take(3))
    if (d.length > 3) sb.append(") ").append(d.substring(3, minOf(6, d.length)))
    if (d.length > 6) sb.append("-").append(d.substring(6, minOf(8, d.length)))
    if (d.length > 8) sb.append("-").append(d.substring(8))
    return sb.toString()
}

// ---------- базовые элементы ----------

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(14.dp)
    val colors = CardDefaults.cardColors(containerColor = color)
    val elevation = CardDefaults.cardElevation(1.dp)
    if (onClick != null) {
        Card(onClick = onClick, modifier = modifier.fillMaxWidth(), shape = shape, colors = colors, elevation = elevation) {
            Column(Modifier.padding(14.dp), content = content)
        }
    } else {
        Card(modifier = modifier.fillMaxWidth(), shape = shape, colors = colors, elevation = elevation) {
            Column(Modifier.padding(14.dp), content = content)
        }
    }
}

@Composable
fun EmptyState(text: String = "Записи не найдены") {
    Box(Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
        Text(text, color = Muted, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun SectionTitle(text: String) =
    Text(text, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)

@Composable
fun StatusChip(name: String) {
    val (bg, fg) = when (name) {
        "В ожидании" -> Color(0xFFFFF3CD) to Color(0xFF7A5C00)
        "В обработке" -> Color(0xFFD0E4F7) to Color(0xFF1B3A5C)
        "Выполнен" -> Color(0xFFD4EDDA) to Color(0xFF1E6B34)
        "Доставлен" -> Color(0xFFCDEFE9) to Color(0xFF0B6258)
        "Отменён" -> Color(0xFFFFCCCC) to Color(0xFF9B2C20)
        else -> Color(0xFFE8EEF4) to Color(0xFF333333)
    }
    Surface(color = bg, shape = RoundedCornerShape(50)) {
        Text(name, color = fg, style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
    }
}

@Composable
fun StockText(inStock: Int) {
    val (text, color) = when {
        inStock <= 0 -> "Нет в наличии" to Danger
        inStock < 5 -> "Осталось $inStock шт." to Color(0xFFB26A00)
        else -> "В наличии: $inStock шт." to Muted
    }
    Text(text, color = color, style = MaterialTheme.typography.labelLarge)
}

@Composable
fun BoxScope.AddFab(text: String, onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        text = { Text(text) },
        icon = { Icon(Icons.Default.Add, null) },
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = Color.White,
        modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
    )
}

// ---------- поля ввода ----------

@Composable
fun Field(
    value: String, onChange: (String) -> Unit, label: String,
    modifier: Modifier = Modifier.fillMaxWidth(), error: String? = null,
    keyboard: KeyboardType = KeyboardType.Text, minLines: Int = 1, maxLen: Int = 200
) {
    OutlinedTextField(
        value = value, onValueChange = { onChange(it.take(maxLen)) },
        label = { Text(label) }, isError = error != null,
        supportingText = if (error != null) { { Text(error) } } else null,
        keyboardOptions = KeyboardOptions(keyboardType = keyboard),
        minLines = minLines, singleLine = minLines == 1, modifier = modifier
    )
}

@Composable
fun PhoneField(value: String, onChange: (String) -> Unit, error: Boolean, label: String = "Телефон") {
    OutlinedTextField(
        value = value, onValueChange = { onChange(formatPhone(it)) },
        label = { Text(label) }, placeholder = { Text("+7 (XXX) XXX-XX-XX") },
        isError = error, singleLine = true,
        supportingText = if (error) { { Text("Введите номер телефона в формате +7 (XXX) XXX-XX-XX") } } else null,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun SearchField(value: String, onChange: (String) -> Unit, hint: String, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = value, onValueChange = { onChange(it.take(100)) },
        modifier = modifier.fillMaxWidth(), singleLine = true,
        placeholder = { Text(hint) },
        leadingIcon = { Icon(Icons.Default.Search, null) },
        trailingIcon = {
            if (value.isNotEmpty()) IconButton(onClick = { onChange("") }) { Icon(Icons.Default.Close, "Очистить") }
        },
        shape = RoundedCornerShape(28.dp),
        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White)
    )
}

/** Выпадающий список внутри формы. */
@Composable
fun DropdownField(
    label: String, options: List<Ref>, selectedId: Long?, onSelect: (Long) -> Unit,
    modifier: Modifier = Modifier, error: String? = null
) {
    var open by remember { mutableStateOf(false) }
    Box(modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = options.firstOrNull { it.id == selectedId }?.name ?: "",
            onValueChange = {}, readOnly = true, label = { Text(label) },
            trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) },
            isError = error != null,
            supportingText = if (error != null) { { Text(error) } } else null,
            modifier = Modifier.fillMaxWidth()
        )
        Box(Modifier.matchParentSize().clickable { open = true })
        DropdownMenu(expanded = open, onDismissRequest = { open = false }) {
            options.forEach {
                DropdownMenuItem(text = { Text(it.name) }, onClick = { onSelect(it.id); open = false })
            }
        }
    }
}

/** Чип-фильтр со списком значений. */
@Composable
fun DropdownFilter(label: String, options: List<Ref>, selectedId: Long, onSelect: (Long) -> Unit) {
    var open by remember { mutableStateOf(false) }
    val selected = options.firstOrNull { it.id == selectedId }
    Box {
        FilterChip(
            selected = selected != null, onClick = { open = true },
            label = { Text(selected?.name ?: label) },
            trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) }
        )
        DropdownMenu(expanded = open, onDismissRequest = { open = false }) {
            DropdownMenuItem(text = { Text("Все") }, onClick = { onSelect(0L); open = false })
            options.forEach {
                DropdownMenuItem(text = { Text(it.name) }, onClick = { onSelect(it.id); open = false })
            }
        }
    }
}

/** Поле даты: хранит ISO-строку (yyyy-MM-dd), показывает ДД.ММ.ГГГГ, выбор через календарь. */
@Composable
fun DateField(label: String, iso: String, onChange: (String) -> Unit, modifier: Modifier = Modifier) {
    var open by remember { mutableStateOf(false) }
    Box(modifier) {
        OutlinedTextField(
            value = fmtDate(iso), onValueChange = {}, readOnly = true, label = { Text(label) },
            trailingIcon = { Icon(Icons.Default.DateRange, null) }, modifier = Modifier.fillMaxWidth()
        )
        Box(Modifier.matchParentSize().clickable { open = true })
    }
    if (open) {
        val state = rememberDatePickerState(
            initialSelectedDateMillis = LocalDate.parse(iso).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { open = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let {
                        onChange(Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDate().toString())
                    }
                    open = false
                }) { Text("ОК") }
            },
            dismissButton = { TextButton(onClick = { open = false }) { Text("Отмена") } }
        ) { DatePicker(state = state) }
    }
}

// ---------- диалоги ----------

@Composable
fun ConfirmDialog(text: String, confirmLabel: String = "Удалить", onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss, containerColor = Color.White,
        title = { Text("Подтверждение") }, text = { Text(text) },
        confirmButton = { TextButton(onClick = onConfirm) { Text(confirmLabel, color = Danger) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Отмена") } }
    )
}

@Composable
fun FormDialog(
    title: String, onDismiss: () -> Unit, onSave: () -> Unit, onDelete: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss, containerColor = Color.White,
        title = { Text(title) },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(6.dp), content = content)
        },
        confirmButton = { Button(onClick = onSave) { Text("Сохранить") } },
        dismissButton = {
            Row {
                if (onDelete != null) TextButton(onClick = onDelete) { Text("Удалить", color = Danger) }
                TextButton(onClick = onDismiss) { Text("Отмена") }
            }
        }
    )
}

// ---------- шаблон списочного экрана ----------

/** Поиск + фильтры + список карточек + кнопка добавления. */
@Composable
fun <T> ListScreen(
    query: String, onQuery: (String) -> Unit, hint: String,
    list: List<T>, key: (T) -> Any, fabText: String, onFab: () -> Unit,
    filters: @Composable RowScope.() -> Unit = {},
    row: @Composable (T) -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            SearchField(query, onQuery, hint, Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp))
            Row(
                Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp), content = filters
            )
            if (list.isEmpty()) EmptyState() else LazyColumn(
                Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(list, key = key) { row(it) }
            }
        }
        AddFab(fabText, onFab)
    }
}

package ru.tisbi.furniture.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.tisbi.furniture.data.Customer
import ru.tisbi.furniture.data.Ref
import ru.tisbi.furniture.data.RefKind
import ru.tisbi.furniture.data.Supplier

// ===================== Клиенты =====================

@Composable
fun CustomersScreen(vm: AppViewModel) {
    var q by rememberSaveable { mutableStateOf("") }
    var edit by remember { mutableStateOf<Customer?>(null) }
    var creating by remember { mutableStateOf(false) }

    val list = remember(vm.data.customers, q) {
        val t = q.trim()
        val digits = t.filter { it.isDigit() }
        vm.data.customers.filter {
            t.isEmpty() || it.lastName.contains(t, true) || it.firstName.contains(t, true) ||
                (digits.isNotEmpty() && it.phone.filter { ch -> ch.isDigit() }.contains(digits))
        }
    }

    ListScreen(q, { q = it }, "Поиск по фамилии или телефону", list, { it.id }, "Клиент", { creating = true }) { c ->
        AppCard(onClick = { edit = c }) {
            Text(c.fullName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(c.phone, style = MaterialTheme.typography.bodyMedium)
            Text(c.address, style = MaterialTheme.typography.bodySmall, color = Muted)
        }
    }
    if (creating || edit != null) CustomerDialog(vm, edit) { creating = false; edit = null }
}

@Composable
private fun CustomerDialog(vm: AppViewModel, c: Customer?, onClose: () -> Unit) {
    var first by remember { mutableStateOf(c?.firstName ?: "") }
    var last by remember { mutableStateOf(c?.lastName ?: "") }
    var phone by remember { mutableStateOf(c?.phone ?: "") }
    var addr by remember { mutableStateOf(c?.address ?: "") }
    var tried by remember { mutableStateOf(false) }
    var confirm by remember { mutableStateOf(false) }

    fun nameErr(s: String): String? = when {
        s.isBlank() -> if (tried) REQUIRED else null
        !NAME_REGEX.matches(s.trim()) -> "Поле может содержать только буквы"
        else -> null
    }
    fun valid() = first.isNotBlank() && last.isNotBlank() && NAME_REGEX.matches(first.trim()) &&
        NAME_REGEX.matches(last.trim()) && PHONE_REGEX.matches(phone) && addr.isNotBlank()

    FormDialog(
        title = if (c == null) "Новый клиент" else "Клиент",
        onDismiss = onClose,
        onSave = {
            tried = true
            if (valid()) {
                val item = Customer(c?.id ?: 0L, normName(first), normName(last), phone, addr.trim())
                vm.exec(if (c == null) "Клиент успешно добавлен" else "Данные успешно обновлены", onClose) { saveCustomer(item) }
            }
        },
        onDelete = if (c != null) ({ confirm = true }) else null
    ) {
        Field(last, { last = it }, "Фамилия *", error = nameErr(last), maxLen = 50)
        Field(first, { first = it }, "Имя *", error = nameErr(first), maxLen = 50)
        PhoneField(phone, { phone = it }, tried && !PHONE_REGEX.matches(phone))
        Field(addr, { addr = it }, "Адрес доставки *", error = if (tried && addr.isBlank()) REQUIRED else null, minLines = 2, maxLen = 300)
    }

    if (confirm && c != null) {
        ConfirmDialog(
            "Вы уверены, что хотите удалить клиента? Это действие необратимо.",
            onConfirm = {
                confirm = false; onClose()
                vm.exec("Клиент удалён") { deleteCustomer(c.id) }
            },
            onDismiss = { confirm = false }
        )
    }
}

// ===================== Поставщики =====================

@Composable
fun SuppliersScreen(vm: AppViewModel) {
    var q by rememberSaveable { mutableStateOf("") }
    var edit by remember { mutableStateOf<Supplier?>(null) }
    var creating by remember { mutableStateOf(false) }
    val list = remember(vm.data.suppliers, q) {
        vm.data.suppliers.filter { q.isBlank() || it.company.contains(q.trim(), true) || it.contact.contains(q.trim(), true) }
    }
    ListScreen(q, { q = it }, "Поиск поставщика", list, { it.id }, "Поставщик", { creating = true }) { s ->
        AppCard(onClick = { edit = s }) {
            Text(s.company, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(s.contact, style = MaterialTheme.typography.bodyMedium)
            Text(s.phone, style = MaterialTheme.typography.bodySmall, color = Muted)
        }
    }
    if (creating || edit != null) SupplierDialog(vm, edit) { creating = false; edit = null }
}

@Composable
private fun SupplierDialog(vm: AppViewModel, s: Supplier?, onClose: () -> Unit) {
    var company by remember { mutableStateOf(s?.company ?: "") }
    var contact by remember { mutableStateOf(s?.contact ?: "") }
    var phone by remember { mutableStateOf(s?.phone ?: "") }
    var tried by remember { mutableStateOf(false) }
    var confirm by remember { mutableStateOf(false) }

    FormDialog(
        title = if (s == null) "Новый поставщик" else "Поставщик",
        onDismiss = onClose,
        onSave = {
            tried = true
            if (company.isNotBlank() && contact.isNotBlank() && PHONE_REGEX.matches(phone)) {
                val item = Supplier(s?.id ?: 0L, company.trim(), contact.trim(), phone)
                vm.exec(if (s == null) "Поставщик успешно добавлен" else "Данные успешно обновлены", onClose) { saveSupplier(item) }
            }
        },
        onDelete = if (s != null) ({ confirm = true }) else null
    ) {
        Field(company, { company = it }, "Наименование организации *", error = if (tried && company.isBlank()) REQUIRED else null)
        Field(contact, { contact = it }, "Контактное лицо *", error = if (tried && contact.isBlank()) REQUIRED else null, maxLen = 100)
        PhoneField(phone, { phone = it }, tried && !PHONE_REGEX.matches(phone))
    }

    if (confirm && s != null) {
        ConfirmDialog(
            "Вы уверены, что хотите удалить поставщика?",
            onConfirm = {
                confirm = false; onClose()
                vm.exec("Поставщик удалён") { deleteSupplier(s.id) }
            },
            onDismiss = { confirm = false }
        )
    }
}

// ===================== Справочники =====================

@Composable
fun RefListScreen(vm: AppViewModel, kind: RefKind) {
    val all = when (kind) {
        RefKind.Category -> vm.data.categories
        RefKind.Material -> vm.data.materials
        RefKind.Style -> vm.data.styles
        RefKind.Status -> vm.data.statuses
    }
    var q by rememberSaveable { mutableStateOf("") }
    var edit by remember { mutableStateOf<Ref?>(null) }
    var creating by remember { mutableStateOf(false) }
    val list = remember(all, q) { all.filter { q.isBlank() || it.name.contains(q.trim(), true) } }

    ListScreen(q, { q = it }, "Поиск по наименованию", list, { it.id }, "Добавить", { creating = true }) { r ->
        AppCard(onClick = { edit = r }) { Text(r.name, style = MaterialTheme.typography.titleMedium) }
    }
    if (creating || edit != null) RefDialog(vm, kind, edit) { creating = false; edit = null }
}

@Composable
private fun RefDialog(vm: AppViewModel, kind: RefKind, r: Ref?, onClose: () -> Unit) {
    var name by remember { mutableStateOf(r?.name ?: "") }
    var tried by remember { mutableStateOf(false) }
    var confirm by remember { mutableStateOf(false) }

    FormDialog(
        title = if (r == null) "Новая запись" else "Редактирование",
        onDismiss = onClose,
        onSave = {
            tried = true
            if (name.isNotBlank()) {
                vm.exec(if (r == null) "Запись успешно добавлена" else "Данные успешно обновлены", onClose) {
                    saveRef(kind, r?.id ?: 0L, name.trim())
                }
            }
        },
        onDelete = if (r != null) ({ confirm = true }) else null
    ) {
        Field(name, { name = it }, "Наименование *", error = if (tried && name.isBlank()) REQUIRED else null, maxLen = 100)
    }

    if (confirm && r != null) {
        ConfirmDialog(
            "Вы уверены, что хотите удалить запись? Это действие необратимо.",
            onConfirm = {
                confirm = false; onClose()
                vm.exec("Запись удалена") { deleteRef(kind, r.id) }
            },
            onDismiss = { confirm = false }
        )
    }
}

// ===================== Раздел «Ещё» =====================

@Composable
fun MoreScreen(vm: AppViewModel) {
    val entries = listOf<Triple<ImageVector, String, Route>>(
        Triple(Icons.Default.LocalShipping, "Поставщики", Route.Suppliers),
        Triple(Icons.Default.Category, "Категории мебели", Route.RefList(RefKind.Category)),
        Triple(Icons.Default.Texture, "Материалы", Route.RefList(RefKind.Material)),
        Triple(Icons.Default.Palette, "Стили", Route.RefList(RefKind.Style)),
        Triple(Icons.Default.Flag, "Статусы заказов", Route.RefList(RefKind.Status)),
        Triple(Icons.Default.Assessment, "Отчёты и аналитика", Route.Reports)
    )
    LazyColumn(
        Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(entries) { (icon, title, route) ->
            AppCard(onClick = { vm.push(route) }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(icon, null, tint = MaterialTheme.colorScheme.secondary)
                    Spacer(Modifier.width(14.dp))
                    Text(title, Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
                    Icon(Icons.Default.KeyboardArrowRight, null, tint = Muted)
                }
            }
        }
    }
}

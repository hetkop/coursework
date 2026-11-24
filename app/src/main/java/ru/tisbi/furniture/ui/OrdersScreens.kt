package ru.tisbi.furniture.ui

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.tisbi.furniture.data.fullName
import ru.tisbi.furniture.data.sum
import ru.tisbi.furniture.data.OrderLine
import ru.tisbi.furniture.data.OrderRow
import ru.tisbi.furniture.data.Product
import ru.tisbi.furniture.data.Ref
import java.time.LocalDate

// ===================== Список заказов =====================

@Composable
fun OrdersScreen(vm: AppViewModel) {
    var q by rememberSaveable { mutableStateOf("") }
    var st by rememberSaveable { mutableStateOf(0L) }
    val list = remember(vm.data.orders, q, st) {
        val t = q.trim()
        vm.data.orders.filter {
            (t.isEmpty() || it.customer.contains(t, true) || it.id.toString() == t) &&
                (st == 0L || it.statusId == st)
        }
    }
    ListScreen(
        query = q, onQuery = { q = it }, hint = "Клиент или № заказа",
        list = list, key = { it.id }, fabText = "Заказ",
        onFab = { vm.push(Route.OrderNew) },
        filters = { DropdownFilter("Статус", vm.data.statuses, st) { st = it } }
    ) { o -> OrderCard(o) { vm.push(Route.OrderDetail(o.id)) } }
}

@Composable
private fun OrderCard(o: OrderRow, onClick: () -> Unit) {
    AppCard(onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Заказ №${o.id}", Modifier.weight(1f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            StatusChip(o.status)
        }
        Text("${o.customer} · ${fmtDate(o.date)}", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(6.dp))
        Row {
            Text("${o.qty} шт.", Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = Muted)
            Text(money(o.total), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary)
        }
    }
}

// ===================== Новый заказ =====================

private data class CartLine(val productId: Long, val qty: Int)

@Composable
fun OrderNewScreen(vm: AppViewModel) {
    val d = vm.data
    var customer by remember { mutableStateOf(0L) }
    var date by remember { mutableStateOf(LocalDate.now().toString()) }
    var status by remember {
        mutableStateOf(d.statuses.firstOrNull { it.name == "В ожидании" }?.id ?: d.statuses.firstOrNull()?.id ?: 0L)
    }
    var cart by remember { mutableStateOf(listOf<CartLine>()) }
    var picker by remember { mutableStateOf(false) }
    var tried by remember { mutableStateOf(false) }

    val byId = remember(d.products) { d.products.associateBy { it.id } }
    val customerOptions = remember(d.customers) { d.customers.map { Ref(it.id, it.fullName) } }
    val total = cart.sumOf { (byId[it.productId]?.price ?: 0.0) * it.qty }

    Column(Modifier.fillMaxSize()) {
        LazyColumn(
            Modifier.weight(1f), contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                DropdownField("Покупатель *", customerOptions, customer.takeIf { it != 0L }, { customer = it },
                    error = if (tried && customer == 0L) "Выберите покупателя" else null)
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    DateField("Дата", date, { date = it }, Modifier.weight(1f))
                    DropdownField("Статус", d.statuses, status, { status = it }, Modifier.weight(1f))
                }
            }
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.weight(1f)) { SectionTitle("Состав заказа") }
                    OutlinedButton(onClick = { picker = true }) {
                        Icon(Icons.Default.Add, null); Spacer(Modifier.width(6.dp)); Text("Добавить товар")
                    }
                }
            }
            if (tried && cart.isEmpty()) item { Text("Добавьте хотя бы один товар", color = Danger) }

            items(cart, key = { it.productId }) { line ->
                val p = byId[line.productId]
                if (p != null) AppCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(p.name, Modifier.weight(1f), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                        IconButton(onClick = { cart = cart.filter { it.productId != line.productId } }) {
                            Icon(Icons.Default.Delete, "Убрать", tint = Danger)
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("${money(p.price)} × ${line.qty} = ${money(p.price * line.qty)}",
                            Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
                        IconButton(
                            onClick = { cart = cart.map { if (it.productId == line.productId) it.copy(qty = it.qty - 1) else it } },
                            enabled = line.qty > 1
                        ) { Icon(Icons.Default.Remove, "Меньше") }
                        Text("${line.qty}", fontWeight = FontWeight.Bold)
                        IconButton(
                            onClick = { cart = cart.map { if (it.productId == line.productId) it.copy(qty = it.qty + 1) else it } },
                            enabled = line.qty < p.inStock
                        ) { Icon(Icons.Default.Add, "Больше") }
                    }
                    Text("На складе: ${p.inStock} шт.", style = MaterialTheme.typography.bodySmall, color = Muted)
                }
            }
        }
        Surface(shadowElevation = 8.dp, color = Color.White) {
            Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Итого", style = MaterialTheme.typography.bodySmall, color = Muted)
                    Text(money(total), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = {
                        tried = true
                        if (customer != 0L && cart.isNotEmpty()) {
                            val items = cart.map { it.productId to it.qty }
                            vm.exec("Заказ успешно создан", vm::pop) { createOrder(customer, status, date, items) }
                        }
                    },
                    modifier = Modifier.height(52.dp)
                ) { Text("Оформить заказ") }
            }
        }
    }

    if (picker) {
        ProductPickerDialog(
            products = d.products.filter { it.inStock > 0 },
            onPick = { p ->
                val existing = cart.firstOrNull { it.productId == p.id }
                cart = if (existing == null) cart + CartLine(p.id, 1)
                else cart.map { if (it.productId == p.id) it.copy(qty = minOf(it.qty + 1, p.inStock)) else it }
                picker = false
            },
            onDismiss = { picker = false }
        )
    }
}

@Composable
private fun ProductPickerDialog(products: List<Product>, onPick: (Product) -> Unit, onDismiss: () -> Unit) {
    var q by remember { mutableStateOf("") }
    val list = remember(products, q) { products.filter { q.isBlank() || it.name.contains(q.trim(), true) } }
    AlertDialog(
        onDismissRequest = onDismiss, containerColor = Color.White,
        title = { Text("Выбор товара") },
        text = {
            Column {
                SearchField(q, { q = it }, "Поиск товара")
                Spacer(Modifier.height(8.dp))
                if (list.isEmpty()) Text("Записи не найдены", color = Muted)
                LazyColumn(Modifier.heightIn(max = 360.dp)) {
                    items(list, key = { it.id }) { p ->
                        Column(Modifier.fillMaxWidth().clickable { onPick(p) }.padding(vertical = 10.dp)) {
                            Text(p.name, fontWeight = FontWeight.SemiBold)
                            Text("${money(p.price)} · на складе ${p.inStock} шт.", style = MaterialTheme.typography.bodySmall, color = Muted)
                        }
                        HorizontalDivider()
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Закрыть") } }
    )
}

// ===================== Просмотр заказа =====================

@Composable
fun OrderDetailScreen(vm: AppViewModel, id: Long) {
    val order = vm.data.orders.firstOrNull { it.id == id } ?: return
    val customer = vm.data.customers.firstOrNull { it.id == order.customerId }
    var lines by remember { mutableStateOf(listOf<OrderLine>()) }
    var statusDialog by remember { mutableStateOf(false) }
    var confirm by remember { mutableStateOf(false) }

    LaunchedEffect(id, vm.data) { vm.load({ orderLines(id) }) { lines = it } }

    LazyColumn(
        Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            AppCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Заказ №${order.id}", Modifier.weight(1f), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    StatusChip(order.status)
                }
                Spacer(Modifier.height(6.dp))
                Text("Дата: ${fmtDate(order.date)}", style = MaterialTheme.typography.bodyMedium)
                Text("Покупатель: ${order.customer}", style = MaterialTheme.typography.bodyMedium)
                if (customer != null) {
                    Text(customer.phone, style = MaterialTheme.typography.bodyMedium)
                    Text(customer.address, style = MaterialTheme.typography.bodySmall, color = Muted)
                }
            }
        }
        item { SectionTitle("Состав заказа") }
        items(lines, key = { it.productId }) { l ->
            AppCard {
                Text(l.product, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Row {
                    Text("${money(l.price)} × ${l.qty}", Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, color = Muted)
                    Text(money(l.sum), fontWeight = FontWeight.Bold)
                }
            }
        }
        item {
            AppCard(color = MaterialTheme.colorScheme.primaryContainer) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Итого", Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
                    Text(money(order.total), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary)
                }
            }
        }
        item {
            Button(onClick = { statusDialog = true }, modifier = Modifier.fillMaxWidth().height(52.dp)) { Text("Изменить статус") }
        }
        item {
            OutlinedButton(
                onClick = { confirm = true }, modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Danger)
            ) { Text("Удалить заказ") }
        }
    }

    if (statusDialog) {
        var selected by remember { mutableStateOf(order.statusId) }
        AlertDialog(
            onDismissRequest = { statusDialog = false }, containerColor = Color.White,
            title = { Text("Статус заказа") },
            text = {
                Column {
                    vm.data.statuses.forEach { s ->
                        Row(Modifier.fillMaxWidth().clickable { selected = s.id }, verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = selected == s.id, onClick = { selected = s.id })
                            Text(s.name)
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    statusDialog = false
                    vm.exec("Статус заказа успешно обновлён") { setOrderStatus(id, selected) }
                }) { Text("Сохранить") }
            },
            dismissButton = { TextButton(onClick = { statusDialog = false }) { Text("Отмена") } }
        )
    }
    if (confirm) {
        ConfirmDialog(
            "Вы уверены, что хотите удалить заказ и все его позиции? Товары вернутся на склад.",
            onConfirm = {
                confirm = false
                vm.exec("Заказ удалён", vm::pop) { deleteOrder(id) }
            },
            onDismiss = { confirm = false }
        )
    }
}

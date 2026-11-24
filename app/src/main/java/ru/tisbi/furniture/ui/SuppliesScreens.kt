package ru.tisbi.furniture.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ru.tisbi.furniture.data.Ref
import ru.tisbi.furniture.data.SupplyLine
import java.time.LocalDate

// ===================== Список поставок =====================

@Composable
fun SuppliesScreen(vm: AppViewModel) {
    var q by rememberSaveable { mutableStateOf("") }
    val list = remember(vm.data.supplies, q) {
        val t = q.trim()
        vm.data.supplies.filter { t.isEmpty() || fmtDate(it.date).contains(t) || it.id.toString() == t }
    }
    ListScreen(q, { q = it }, "Дата или № поставки", list, { it.id }, "Поставка", { vm.push(Route.SupplyNew) }) { s ->
        AppCard(onClick = { vm.push(Route.SupplyDetail(s.id)) }) {
            Text("Поставка №${s.id}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(fmtDate(s.date), style = MaterialTheme.typography.bodyMedium)
            Text("Позиций: ${s.positions} · всего ${s.qty} шт.", style = MaterialTheme.typography.bodySmall, color = Muted)
        }
    }
}

// ===================== Новая поставка =====================

@Composable
fun SupplyNewScreen(vm: AppViewModel) {
    val d = vm.data
    var date by remember { mutableStateOf(LocalDate.now().toString()) }
    var lines by remember { mutableStateOf(listOf<SupplyLine>()) }
    var adding by remember { mutableStateOf(false) }
    var tried by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize()) {
        LazyColumn(
            Modifier.weight(1f), contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item { DateField("Дата поставки", date, { date = it }) }
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.weight(1f)) { SectionTitle("Позиции поставки") }
                    OutlinedButton(onClick = { adding = true }) {
                        Icon(Icons.Default.Add, null); Spacer(Modifier.width(6.dp)); Text("Добавить")
                    }
                }
            }
            if (tried && lines.isEmpty()) item { Text("Добавьте хотя бы одну позицию", color = Danger) }
            itemsIndexed(lines) { index, l ->
                AppCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(l.product, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                            Text("${l.supplier} · ${l.qty} шт.", style = MaterialTheme.typography.bodySmall, color = Muted)
                        }
                        IconButton(onClick = { lines = lines.filterIndexed { i, _ -> i != index } }) {
                            Icon(Icons.Default.Delete, "Убрать", tint = Danger)
                        }
                    }
                }
            }
        }
        Surface(shadowElevation = 8.dp, color = Color.White) {
            Button(
                onClick = {
                    tried = true
                    if (lines.isNotEmpty()) {
                        vm.exec("Поставка зарегистрирована. Остатки на складе обновлены", vm::pop) { createSupply(date, lines) }
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(16.dp).height(52.dp)
            ) { Text("Зарегистрировать поставку") }
        }
    }

    if (adding) {
        AddSupplyLineDialog(
            products = d.products.map { Ref(it.id, it.name) },
            suppliers = d.suppliers.map { Ref(it.id, it.company) },
            onAdd = { lines = lines + it; adding = false },
            onDismiss = { adding = false }
        )
    }
}

@Composable
private fun AddSupplyLineDialog(
    products: List<Ref>, suppliers: List<Ref>, onAdd: (SupplyLine) -> Unit, onDismiss: () -> Unit
) {
    var product by remember { mutableStateOf(0L) }
    var supplier by remember { mutableStateOf(0L) }
    var qty by remember { mutableStateOf("") }
    var tried by remember { mutableStateOf(false) }
    val qtyVal = qty.toIntOrNull()

    FormDialog(
        title = "Позиция поставки", onDismiss = onDismiss,
        onSave = {
            tried = true
            if (product != 0L && supplier != 0L && qtyVal != null && qtyVal > 0) {
                onAdd(SupplyLine(product, products.first { it.id == product }.name,
                    supplier, suppliers.first { it.id == supplier }.name, qtyVal))
            }
        }
    ) {
        DropdownField("Товар *", products, product.takeIf { it != 0L }, { product = it },
            error = if (tried && product == 0L) REQUIRED else null)
        DropdownField("Поставщик *", suppliers, supplier.takeIf { it != 0L }, { supplier = it },
            error = if (tried && supplier == 0L) REQUIRED else null)
        Field(qty, { qty = it }, "Количество, шт. *", keyboard = KeyboardType.Number,
            error = if (tried && (qtyVal == null || qtyVal <= 0)) "Введите корректное количество" else null, maxLen = 6)
    }
}

// ===================== Просмотр поставки =====================

@Composable
fun SupplyDetailScreen(vm: AppViewModel, id: Long) {
    val supply = vm.data.supplies.firstOrNull { it.id == id } ?: return
    var lines by remember { mutableStateOf(listOf<SupplyLine>()) }
    var confirm by remember { mutableStateOf(false) }

    LaunchedEffect(id, vm.data) { vm.load({ supplyLines(id) }) { lines = it } }

    LazyColumn(
        Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            AppCard {
                Text("Поставка №${supply.id}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("Дата: ${fmtDate(supply.date)}", style = MaterialTheme.typography.bodyMedium)
                Text("Всего принято: ${supply.qty} шт.", style = MaterialTheme.typography.bodySmall, color = Muted)
            }
        }
        item { SectionTitle("Состав поставки") }
        items(lines) { l ->
            AppCard {
                Text(l.product, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Row {
                    Text(l.supplier, Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = Muted)
                    Text("${l.qty} шт.", fontWeight = FontWeight.Bold)
                }
            }
        }
        item {
            OutlinedButton(
                onClick = { confirm = true }, modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Danger)
            ) { Text("Удалить поставку") }
        }
    }

    if (confirm) {
        ConfirmDialog(
            "Вы уверены, что хотите удалить поставку и все её позиции? Остатки на складе будут уменьшены.",
            onConfirm = {
                confirm = false
                vm.exec("Поставка удалена", vm::pop) { deleteSupply(id) }
            },
            onDismiss = { confirm = false }
        )
    }
}

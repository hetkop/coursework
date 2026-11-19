package ru.tisbi.furniture.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ru.tisbi.furniture.data.OrderRow
import ru.tisbi.furniture.data.PopularRow
import java.time.LocalDate

@Composable
fun ReportsScreen(vm: AppViewModel) {
    var tab by remember { mutableStateOf(0) }
    Column(Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = tab, containerColor = Color.White, contentColor = MaterialTheme.colorScheme.primary) {
            listOf("Заказы", "Популярные", "Остатки").forEachIndexed { i, t ->
                Tab(selected = tab == i, onClick = { tab = i }, text = { Text(t) })
            }
        }
        when (tab) {
            0 -> OrdersReport(vm)
            1 -> PopularReport(vm)
            else -> StockReport(vm)
        }
    }
}

/** Заказы за выбранный период. */
@Composable
private fun OrdersReport(vm: AppViewModel) {
    var from by remember { mutableStateOf(LocalDate.now().withDayOfYear(1).toString()) }
    var to by remember { mutableStateOf(LocalDate.now().toString()) }
    var rows by remember { mutableStateOf<List<OrderRow>?>(null) }

    fun generate() {
        if (from > to) {
            vm.message = "Дата начала периода не может быть позже даты окончания"
        } else {
            vm.load({ orders(from, to) }) { rows = it }
        }
    }
    LaunchedEffect(vm.data) { generate() }

    Column(Modifier.fillMaxSize()) {
        Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            DateField("С", from, { from = it }, Modifier.weight(1f))
            DateField("По", to, { to = it }, Modifier.weight(1f))
        }
        Button(onClick = { generate() }, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(48.dp)) {
            Text("Сформировать")
        }
        val r = rows
        if (r == null) {
            Spacer(Modifier.height(1.dp))
        } else if (r.isEmpty()) {
            EmptyState("За указанный период заказов не найдено")
        } else {
            AppCard(color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.padding(16.dp)) {
                Row {
                    Text("Заказов: ${r.size}", Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                    Text(money(r.sumOf { it.total }), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }
            LazyColumn(
                Modifier.fillMaxSize(), contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(r, key = { it.id }) { o ->
                    AppCard {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("${fmtDate(o.date)} · №${o.id}", Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                            StatusChip(o.status)
                        }
                        Text(o.customer, style = MaterialTheme.typography.bodyMedium)
                        Row {
                            Text("${o.qty} шт.", Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = Muted)
                            Text(money(o.total), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

/** Рейтинг товаров по суммарному количеству в заказах. */
@Composable
private fun PopularReport(vm: AppViewModel) {
    var rows by remember { mutableStateOf<List<PopularRow>?>(null) }
    LaunchedEffect(vm.data) { vm.load({ popularProducts() }) { rows = it } }

    val r = rows
    when {
        r == null -> Spacer(Modifier.height(1.dp))
        r.isEmpty() -> EmptyState("Недостаточно данных для формирования отчёта")
        else -> LazyColumn(
            Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(r) { i, p ->
                AppCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(shape = CircleShape, color = if (i < 3) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant) {
                            Text("${i + 1}", Modifier.padding(horizontal = 12.dp, vertical = 6.dp), fontWeight = FontWeight.Bold,
                                color = if (i < 3) Color.White else MaterialTheme.colorScheme.onSurface)
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(p.product, fontWeight = FontWeight.SemiBold)
                            Text(p.category, style = MaterialTheme.typography.bodySmall, color = Muted)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("${p.qty} шт.", fontWeight = FontWeight.Bold)
                            Text(money(p.revenue), style = MaterialTheme.typography.bodySmall, color = Muted)
                        }
                    }
                }
            }
        }
    }
}

/** Остатки на складе: позиции ниже порога подсвечиваются. */
@Composable
private fun StockReport(vm: AppViewModel) {
    var threshold by remember { mutableStateOf("5") }
    val limit = threshold.toIntOrNull() ?: 0
    val list = remember(vm.data.products) { vm.data.products.sortedBy { it.inStock } }
    val low = list.count { it.inStock < limit }

    Column(Modifier.fillMaxSize()) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Field(threshold, { threshold = it }, "Минимальный остаток, шт.", Modifier.weight(1f), keyboard = KeyboardType.Number, maxLen = 4)
            Text("Ниже порога: $low", fontWeight = FontWeight.SemiBold, color = if (low > 0) Danger else Muted)
        }
        LazyColumn(
            Modifier.fillMaxSize(), contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(list, key = { it.id }) { p ->
                AppCard(color = if (p.inStock < limit) LowStockBg else Color.White) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(p.name, fontWeight = FontWeight.SemiBold)
                            Text("${p.categoryName} · ${p.materialName} · ${p.styleName}", style = MaterialTheme.typography.bodySmall, color = Muted)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("${p.inStock} шт.", fontWeight = FontWeight.Bold)
                            Text(money(p.price), style = MaterialTheme.typography.bodySmall, color = Muted)
                        }
                    }
                }
            }
        }
    }
}

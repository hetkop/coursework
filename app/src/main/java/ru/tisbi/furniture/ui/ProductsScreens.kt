package ru.tisbi.furniture.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ru.tisbi.furniture.data.Product

@Composable
fun ProductsScreen(vm: AppViewModel) {
    val d = vm.data
    var q by rememberSaveable { mutableStateOf("") }
    var cat by rememberSaveable { mutableStateOf(0L) }
    var mat by rememberSaveable { mutableStateOf(0L) }
    var sty by rememberSaveable { mutableStateOf(0L) }

    val list = remember(d.products, q, cat, mat, sty) {
        val t = q.trim()
        d.products.filter {
            (t.isEmpty() || it.name.contains(t, true) || it.description.contains(t, true)) &&
                (cat == 0L || it.categoryId == cat) &&
                (mat == 0L || it.materialId == mat) &&
                (sty == 0L || it.styleId == sty)
        }
    }

    ListScreen(
        query = q, onQuery = { q = it }, hint = "Поиск товара",
        list = list, key = { it.id }, fabText = "Товар",
        onFab = { vm.push(Route.ProductEdit(null)) },
        filters = {
            DropdownFilter("Категория", d.categories, cat) { cat = it }
            DropdownFilter("Материал", d.materials, mat) { mat = it }
            DropdownFilter("Стиль", d.styles, sty) { sty = it }
        }
    ) { p -> ProductCard(p) { vm.push(Route.ProductEdit(p.id)) } }
}

@Composable
private fun ProductCard(p: Product, onClick: () -> Unit) {
    AppCard(onClick = onClick) {
        Row(verticalAlignment = Alignment.Top) {
            Column(Modifier.weight(1f)) {
                Text(p.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text("${p.categoryName} · ${p.materialName} · ${p.styleName}",
                    style = MaterialTheme.typography.bodySmall, color = Muted)
                Spacer(Modifier.height(6.dp))
                StockText(p.inStock)
            }
            Spacer(Modifier.width(12.dp))
            Text(money(p.price), style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun ProductEditScreen(vm: AppViewModel, id: Long?) {
    val d = vm.data
    val existing = remember(id) { d.products.firstOrNull { it.id == id } }

    var name by rememberSaveable { mutableStateOf(existing?.name ?: "") }
    var desc by rememberSaveable { mutableStateOf(existing?.description ?: "") }
    var price by rememberSaveable {
        mutableStateOf(existing?.price?.let { if (it % 1.0 == 0.0) it.toLong().toString() else it.toString() } ?: "")
    }
    var stock by rememberSaveable { mutableStateOf(existing?.inStock?.toString() ?: "") }
    var cat by rememberSaveable { mutableStateOf(existing?.categoryId ?: 0L) }
    var mat by rememberSaveable { mutableStateOf(existing?.materialId ?: 0L) }
    var sty by rememberSaveable { mutableStateOf(existing?.styleId ?: 0L) }
    var tried by rememberSaveable { mutableStateOf(false) }
    var confirm by remember { mutableStateOf(false) }

    val priceVal = price.replace(',', '.').toDoubleOrNull()
    val stockVal = stock.toIntOrNull()

    val nameErr = if (tried && name.isBlank()) REQUIRED else null
    val priceErr = if (!tried) null else when {
        price.isBlank() -> REQUIRED
        priceVal == null -> "Введите корректное числовое значение"
        priceVal <= 0 -> "Цена должна быть больше нуля"
        else -> null
    }
    val stockErr = if (!tried) null else when {
        stock.isBlank() -> REQUIRED
        stockVal == null -> "Введите целое число"
        stockVal < 0 -> "Количество не может быть отрицательным"
        else -> null
    }
    val catErr = if (tried && cat == 0L) REQUIRED else null
    val matErr = if (tried && mat == 0L) REQUIRED else null
    val styErr = if (tried && sty == 0L) REQUIRED else null

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Field(name, { name = it }, "Наименование *", error = nameErr, maxLen = 200)
        Field(desc, { desc = it }, "Описание", minLines = 2, maxLen = 500)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Field(price, { price = it }, "Цена, ₽ *", Modifier.weight(1f), priceErr, KeyboardType.Decimal)
            Field(stock, { stock = it }, "На складе, шт. *", Modifier.weight(1f), stockErr, KeyboardType.Number)
        }
        DropdownField("Категория *", d.categories, cat.takeIf { it != 0L }, { cat = it }, error = catErr)
        DropdownField("Материал *", d.materials, mat.takeIf { it != 0L }, { mat = it }, error = matErr)
        DropdownField("Стиль *", d.styles, sty.takeIf { it != 0L }, { sty = it }, error = styErr)

        Spacer(Modifier.height(6.dp))
        Button(
            onClick = {
                tried = true
                if (name.isNotBlank() && priceVal != null && priceVal > 0 && stockVal != null && stockVal >= 0 &&
                    cat != 0L && mat != 0L && sty != 0L
                ) {
                    val p = Product(id ?: 0L, name.trim(), desc.trim(), priceVal, stockVal, cat, mat, sty)
                    vm.exec(if (id == null) "Товар успешно добавлен" else "Данные успешно обновлены", vm::pop) { saveProduct(p) }
                }
            },
            modifier = Modifier.fillMaxWidth().height(52.dp)
        ) { Text("Сохранить") }

        if (id != null) {
            OutlinedButton(
                onClick = { confirm = true },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Danger)
            ) { Text("Удалить товар") }
        }
    }

    if (confirm && id != null) {
        ConfirmDialog(
            "Вы уверены, что хотите удалить товар? Это действие необратимо.",
            onConfirm = {
                confirm = false
                vm.exec("Товар удалён", vm::pop) { deleteProduct(id) }
            },
            onDismiss = { confirm = false }
        )
    }
}

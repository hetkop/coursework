package ru.tisbi.furniture.ui

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

private val TabTitles = listOf("Товары", "Заказы", "Клиенты", "Поставки", "Ещё")
private val TabIcons = listOf(Icons.Default.Chair, Icons.Default.ShoppingCart, Icons.Default.People,
    Icons.Default.LocalShipping, Icons.Default.MoreHoriz)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRoot(vm: AppViewModel = viewModel()) {
    val context = LocalContext.current
    LaunchedEffect(vm.message) {
        vm.message?.let { Toast.makeText(context, it, Toast.LENGTH_LONG).show(); vm.message = null }
    }

    val role = vm.role
    val route = vm.stack.lastOrNull()
    BackHandler(enabled = route != null) { vm.pop() }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            if (role != null) {
                TopAppBar(
                    title = { Text(titleFor(role, route, vm.tab)) },
                    navigationIcon = {
                        if (route != null) IconButton(onClick = { vm.pop() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                        }
                    },
                    actions = {
                        if (route == null) IconButton(onClick = { vm.logout() }) {
                            Icon(Icons.AutoMirrored.Filled.ExitToApp, "Сменить роль")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = Color.White, navigationIconContentColor = Color.White,
                        actionIconContentColor = Color.White
                    )
                )
            }
        },
        bottomBar = {
            if (role == Role.Admin && route == null) {
                NavigationBar(containerColor = Color.White) {
                    TabTitles.forEachIndexed { i, title ->
                        NavigationBarItem(
                            selected = vm.tab == i, onClick = { vm.tab = i },
                            icon = { Icon(TabIcons[i], null) }, label = { Text(title) },
                            colors = NavigationBarItemDefaults.colors(indicatorColor = MaterialTheme.colorScheme.primaryContainer)
                        )
                    }
                }
            }
        }
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            when {
                role == null -> LoginScreen(vm)
                route != null -> RouteScreen(vm, route)
                role == Role.Admin -> when (vm.tab) {
                    0 -> ProductsScreen(vm)
                    1 -> OrdersScreen(vm)
                    2 -> CustomersScreen(vm)
                    3 -> SuppliesScreen(vm)
                    else -> MoreScreen(vm)
                }
                else -> ReportsScreen(vm)
            }
        }
    }
}

private fun titleFor(role: Role, route: Route?, tab: Int): String = when (route) {
    null -> if (role == Role.Manager) "Отчёты" else TabTitles[tab]
    is Route.ProductEdit -> if (route.id == null) "Новый товар" else "Товар"
    Route.OrderNew -> "Новый заказ"
    is Route.OrderDetail -> "Заказ №${route.id}"
    Route.SupplyNew -> "Новая поставка"
    is Route.SupplyDetail -> "Поставка №${route.id}"
    is Route.RefList -> route.kind.title
    Route.Suppliers -> "Поставщики"
    Route.Reports -> "Отчёты и аналитика"
}

@Composable
private fun RouteScreen(vm: AppViewModel, route: Route) {
    when (route) {
        is Route.ProductEdit -> ProductEditScreen(vm, route.id)
        Route.OrderNew -> OrderNewScreen(vm)
        is Route.OrderDetail -> OrderDetailScreen(vm, route.id)
        Route.SupplyNew -> SupplyNewScreen(vm)
        is Route.SupplyDetail -> SupplyDetailScreen(vm, route.id)
        is Route.RefList -> RefListScreen(vm, route.kind)
        Route.Suppliers -> SuppliersScreen(vm)
        Route.Reports -> ReportsScreen(vm)
    }
}

@Composable
private fun LoginScreen(vm: AppViewModel) {
    Column(
        Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primary).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.Chair, null, tint = Color.White, modifier = Modifier.size(72.dp))
        Spacer(Modifier.height(12.dp))
        Text("Мебельный магазин", color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Информационная система", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(40.dp))
        Text("Войти как", color = Color.White.copy(alpha = 0.8f), modifier = Modifier.align(Alignment.Start))
        Spacer(Modifier.height(8.dp))
        RoleCard(Icons.Default.Person, "Администратор", "Каталог, клиенты, заказы, поставки и отчёты") { vm.login(Role.Admin) }
        Spacer(Modifier.height(12.dp))
        RoleCard(Icons.Default.Assessment, "Руководитель", "Просмотр отчётов и складских остатков") { vm.login(Role.Manager) }
    }
}

@Composable
private fun RoleCard(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        onClick = onClick, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(36.dp))
            Spacer(Modifier.width(16.dp))
            Column {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Muted)
            }
        }
    }
}

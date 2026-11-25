package com.morsetranslatorpro.app.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.morsetranslatorpro.app.ui.navigation.Destination
import com.morsetranslatorpro.app.ui.screens.AboutScreen
import com.morsetranslatorpro.app.ui.screens.AudioDecodeScreen
import com.morsetranslatorpro.app.ui.screens.AudioScreen
import com.morsetranslatorpro.app.ui.screens.CameraDecodeScreen
import com.morsetranslatorpro.app.ui.screens.DecodeScreen
import com.morsetranslatorpro.app.ui.screens.EncodeScreen
import com.morsetranslatorpro.app.ui.screens.FlashlightScreen
import com.morsetranslatorpro.app.ui.screens.HistoryScreen
import com.morsetranslatorpro.app.ui.screens.HomeScreen
import com.morsetranslatorpro.app.ui.screens.SettingsScreen
import com.morsetranslatorpro.app.ui.viewmodel.DecoderViewModel
import com.morsetranslatorpro.app.ui.viewmodel.HistoryViewModel
import com.morsetranslatorpro.app.ui.viewmodel.SettingsViewModel
import com.morsetranslatorpro.app.ui.viewmodel.TranslatorViewModel
import com.morsetranslatorpro.app.ui.viewmodel.TransmitViewModel
import com.morsetranslatorpro.app.ui.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MorseApp(factory: ViewModelFactory) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = Destination.fromRoute(backStackEntry?.destination?.route)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    "Morse Translator Pro",
                    modifier = Modifier.padding(16.dp),
                    style = androidx.compose.material3.MaterialTheme.typography.titleLarge
                )
                Destination.entries.filter { it.inDrawer }.forEach { dest ->
                    NavigationDrawerItem(
                        icon = { Icon(dest.icon, contentDescription = null) },
                        label = { Text(dest.title) },
                        selected = dest == currentDestination,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(dest.route) {
                                launchSingleTop = true
                                popUpTo(Destination.HOME.route)
                            }
                        },
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(currentDestination.title) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Open navigation menu")
                        }
                    }
                )
            }
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = Destination.HOME.route,
                modifier = Modifier.fillMaxSize().padding(padding)
            ) {
                composable(Destination.HOME.route) {
                    HomeScreen(onNavigate = { navController.navigate(it.route) })
                }
                composable(Destination.ENCODE.route) {
                    val vm: TranslatorViewModel = viewModel(factory = factory)
                    EncodeScreen(vm, onNavigate = { navController.navigate(it.route) })
                }
                composable(Destination.DECODE.route) {
                    val vm: TranslatorViewModel = viewModel(factory = factory)
                    DecodeScreen(vm, onNavigate = { navController.navigate(it.route) })
                }
                composable(Destination.FLASHLIGHT.route) {
                    val vm: TransmitViewModel = viewModel(factory = factory)
                    FlashlightScreen(vm)
                }
                composable(Destination.AUDIO.route) {
                    val vm: TransmitViewModel = viewModel(factory = factory)
                    AudioScreen(vm)
                }
                composable(Destination.CAMERA_DECODE.route) {
                    val vm: DecoderViewModel = viewModel(factory = factory)
                    CameraDecodeScreen(vm)
                }
                composable(Destination.AUDIO_DECODE.route) {
                    val vm: DecoderViewModel = viewModel(factory = factory)
                    AudioDecodeScreen(vm)
                }
                composable(Destination.HISTORY.route) {
                    val vm: HistoryViewModel = viewModel(factory = factory)
                    HistoryScreen(vm, onNavigate = { navController.navigate(it.route) })
                }
                composable(Destination.SETTINGS.route) {
                    val vm: SettingsViewModel = viewModel(factory = factory)
                    SettingsScreen(vm)
                }
                composable(Destination.ABOUT.route) {
                    AboutScreen()
                }
            }
        }
    }
}

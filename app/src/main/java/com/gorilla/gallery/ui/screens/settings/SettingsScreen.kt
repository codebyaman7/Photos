package com.gorilla.gallery.ui.screens.settings

import androidx.compose.runtime.setValue

import androidx.compose.runtime.getValue

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gorilla.gallery.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import com.gorilla.gallery.data.settings.AppSettings
import com.gorilla.gallery.data.settings.DateGranularity
import com.gorilla.gallery.ui.AppViewModel
import com.gorilla.gallery.ui.components.GlassAlertDialog
import com.gorilla.gallery.ui.components.GlassSegmentedControl
import com.gorilla.gallery.ui.components.ScreenTitle
import com.gorilla.gallery.ui.components.GlassSettingsGroup
import com.gorilla.gallery.ui.components.GlassSettingsItem
import com.gorilla.gallery.ui.theme.BlurIntensity
import com.gorilla.gallery.ui.theme.LocalAppColors

@Composable
fun SettingsScreen(
    app: AppViewModel,
    contentPadding: PaddingValues,
    vm: SettingsViewModel = viewModel(factory = SettingsViewModel.Factory),
) {
    val settings by app.settings.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }
    val searchLower = searchQuery.lowercase()
    var showSurfaceOpacityDialog by remember { mutableStateOf(false) }
    
    val context = LocalContext.current
    val density = androidx.compose.ui.platform.LocalDensity.current
    var headerHeight by remember { mutableIntStateOf(0) }
    val headerHeightDp = with(density) { headerHeight.toDp() }

    val themeItem = rememberThemeSettingsItem(settings = settings, vm = vm)

    Box(Modifier.fillMaxSize().background(LocalAppColors.current.bgBase)) {
        LazyColumn(
            contentPadding = PaddingValues(top = headerHeightDp, bottom = contentPadding.calculateBottomPadding() + 32.dp),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // --- Appearance Settings ---
            val appearanceItems = buildList {
                if ("theme appearance".contains(searchLower)) {
                    add(themeItem)
                }
                if ("player slider style seekbar".contains(searchLower)) {
                    add(
                        GlassSettingsItem(
                            title = "Player Slider Style",
                            icon = Icons.Rounded.LinearScale,
                            bottomContent = {
                                Box(Modifier.padding(top = 12.dp)) {
                                    com.gorilla.gallery.ui.components.GlassSegmentedControl(
                                        options = com.gorilla.gallery.data.settings.PlayerSliderStyle.entries.map { it to it.label },
                                        selected = settings.playerSliderStyle,
                                        onSelect = { vm.setPlayerSliderStyle(it) }
                                    )
                                }
                            }
                        )
                    )
                }
                if ("liquid glass intensity".contains(searchLower)) {
                    add(
                        GlassSettingsItem(
                            title = "Liquid Glass Intensity",
                            icon = Icons.Rounded.BlurLinear,
                            bottomContent = {
                                Box(Modifier.padding(top = 12.dp)) {
                                    GlassSegmentedControl(
                                        options = listOf(BlurIntensity.LOW to "Low", BlurIntensity.MEDIUM to "Default", BlurIntensity.HIGH to "High"),
                                        selected = settings.liquidGlassIntensity,
                                        onSelect = { vm.setLiquidGlassIntensity(it) }
                                    )
                                }
                            }
                        )
                    )
                }
                if ("surface opacity".contains(searchLower)) {
                    add(
                        GlassSettingsItem(
                            title = "Surface Opacity",
                            subtitle = "Adjust the opacity of the liquid glass surface",
                            icon = Icons.Rounded.Opacity,
                            onClick = { showSurfaceOpacityDialog = true }
                        )
                    )
                }
                if ("grid columns".contains(searchLower)) {
                    add(
                        GlassSettingsItem(
                            title = "Grid Columns",
                            icon = Icons.Rounded.GridView,
                            bottomContent = {
                                Box(Modifier.padding(top = 12.dp)) {
                                    GlassSegmentedControl(
                                        options = listOf(2 to "2", 3 to "3", 4 to "4", 5 to "5", 6 to "6"),
                                        selected = settings.gridColumns,
                                        onSelect = { vm.setGridColumns(it) }
                                    )
                                }
                            }
                        )
                    )
                }
                if ("group photos by date granularity".contains(searchLower)) {
                    add(
                        GlassSettingsItem(
                            title = "Group Photos By",
                            icon = Icons.Rounded.DateRange,
                            bottomContent = {
                                Box(Modifier.padding(top = 12.dp)) {
                                    GlassSegmentedControl(
                                        options = DateGranularity.entries.map { it to it.label },
                                        selected = settings.dateGranularity,
                                        onSelect = { vm.setDateGranularity(it) }
                                    )
                                }
                            }
                        )
                    )
                }
                if ("bottom bar style navigation split full".contains(searchLower)) {
                    add(
                        GlassSettingsItem(
                            title = "Bottom Bar Style",
                            icon = Icons.Rounded.ViewStream,
                            bottomContent = {
                                Box(Modifier.padding(top = 12.dp)) {
                                    GlassSegmentedControl(
                                        options = com.gorilla.gallery.data.settings.BottomBarStyle.entries.map { it to it.label },
                                        selected = settings.bottomBarStyle,
                                        onSelect = { vm.setBottomBarStyle(it) }
                                    )
                                }
                            }
                        )
                    )
                }
                if ("high quality thumbnails".contains(searchLower)) {
                    add(
                        GlassSettingsItem(
                            title = "High Quality Thumbnails",
                            subtitle = "Use higher resolution for grid thumbnails",
                            icon = Icons.Rounded.Image,
                            trailingContent = {
                                androidx.compose.material3.Switch(
                                    checked = settings.highQualityThumbnails,
                                    onCheckedChange = { vm.setHighQualityThumbnails(it) }
                                )
                            }
                        )
                    )
                }
            }

            if (appearanceItems.isNotEmpty()) {
                item {
                    GlassSettingsGroup(
                        title = "Appearance",
                        items = appearanceItems,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            // --- General Settings ---
            item {
                GeneralSection(settings, vm, searchLower, Modifier.padding(horizontal = 16.dp))
            }

            // --- Privacy Settings ---
            item {
                PrivacySection(settings, vm, searchLower, Modifier.padding(horizontal = 16.dp))
            }
        }
        
        Column(
            Modifier
                .fillMaxWidth()
                .background(com.gorilla.gallery.ui.theme.DesignTokens.BgBase)
                .onSizeChanged { headerHeight = it.height }
        ) {
            ScreenTitle("Settings")
            
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search settings...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = "Search"
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Rounded.Clear,
                                contentDescription = "Clear"
                            )
                        }
                    }
                },
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp, top = 8.dp)
                    .border(1.dp, LocalAppColors.current.borderGlass, RoundedCornerShape(24.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = LocalAppColors.current.bgSurface.copy(alpha = 0.5f),
                    unfocusedContainerColor = LocalAppColors.current.bgSurface.copy(alpha = 0.5f),
                    focusedIndicatorColor = com.gorilla.gallery.ui.theme.LocalDynamicColors.current.accent,
                    unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent
                )
            )
        }

        if (showSurfaceOpacityDialog) {
            var tempValue by remember { mutableFloatStateOf(settings.surfaceOpacity) }
            com.gorilla.gallery.ui.components.GlassCustomDialog(
                onDismissRequest = { showSurfaceOpacityDialog = false }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(28.dp)
                ) {
                    Text(
                        text = "Surface Opacity",
                        style = MaterialTheme.typography.titleMedium,
                        color = com.gorilla.gallery.ui.theme.DesignTokens.TextPrimary,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "%.2f".format(tempValue),
                        style = MaterialTheme.typography.bodyLarge,
                        color = com.gorilla.gallery.ui.theme.DesignTokens.TextPrimary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Slider(
                        value = tempValue,
                        onValueChange = { tempValue = it },
                        valueRange = 0f..1f,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        com.gorilla.gallery.ui.components.DialogPillButton(
                            text = "Reset",
                            onClick = { tempValue = 0.4f }
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        com.gorilla.gallery.ui.components.DialogPillButton(
                            text = "Cancel",
                            onClick = { showSurfaceOpacityDialog = false }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        com.gorilla.gallery.ui.components.DialogPillButton(
                            text = "OK",
                            onClick = { vm.setSurfaceOpacity(tempValue); showSurfaceOpacityDialog = false },
                            isPrimary = true
                        )
                    }
                }
            }
        }
    }
}
@Composable
private fun GeneralSection(settings: AppSettings, vm: SettingsViewModel, searchLower: String, modifier: Modifier = Modifier) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val items = buildList {
        if ("loop videos".contains(searchLower)) {
            add(
                GlassSettingsItem(
                    title = "Loop Videos",
                    subtitle = "Automatically replay videos when they end",
                    icon = Icons.Rounded.Loop,
                    trailingContent = {
                        androidx.compose.material3.Switch(
                            checked = settings.loopVideos,
                            onCheckedChange = { vm.setLoopVideos(it) }
                        )
                    }
                )
            )
        }
        if ("hide warnings skip confirmation toast messages".contains(searchLower)) {
            add(
                GlassSettingsItem(
                    title = "Hide Warnings & Toasts",
                    subtitle = "Skip confirmation dialogs and hide toast messages for photo actions",
                    icon = Icons.Rounded.NotificationsOff,
                    trailingContent = {
                        androidx.compose.material3.Switch(
                            checked = settings.skipConfirmationsAndToasts,
                            onCheckedChange = { 
                                vm.setSkipConfirmationsAndToasts(it)
                                if (it && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                                    if (!android.os.Environment.isExternalStorageManager()) {
                                        context.startActivity(android.content.Intent(android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, android.net.Uri.parse("package:${context.packageName}")))
                                    }
                                }
                            }
                        )
                    }
                )
            )
        }
        if ("move to recycle bin default trash delete".contains(searchLower)) {
            add(
                GlassSettingsItem(
                    title = "Move to Recycle Bin",
                    subtitle = "Move deleted items to trash instead of permanent deletion",
                    icon = Icons.Rounded.Delete,
                    trailingContent = {
                        androidx.compose.material3.Switch(
                            checked = settings.moveToTrashByDefaultForDelete,
                            onCheckedChange = { vm.setMoveToTrashByDefaultForDelete(it) }
                        )
                    }
                )
            )
        }

        if ("background mode picture in picture pip".contains(searchLower)) {
            add(
                GlassSettingsItem(
                    title = "Picture-in-Picture",
                    subtitle = "Continue playing videos in a mini window when leaving the app",
                    icon = Icons.Rounded.PictureInPicture,
                    trailingContent = {
                        androidx.compose.material3.Switch(
                            checked = settings.enablePipMode,
                            onCheckedChange = { vm.setEnablePipMode(it) }
                        )
                    }
                )
            )
        }

        if ("scroll bar fast scrollbar draggable aves".contains(searchLower)) {
            add(
                GlassSettingsItem(
                    title = "Fast Scroll Bar",
                    subtitle = "Show a draggable scroll bar with date bubble in media grids",
                    icon = Icons.Rounded.UnfoldMore,
                    trailingContent = {
                        androidx.compose.material3.Switch(
                            checked = settings.showScrollbar,
                            onCheckedChange = { vm.setShowScrollbar(it) }
                        )
                    }
                )
            )
        }

        if ("auto hide bottom bar scroll".contains(searchLower)) {
            add(
                GlassSettingsItem(
                    title = "Auto-hide Bottom Bar",
                    subtitle = "Slide bottom bar out of view when scrolling down in photo grids",
                    icon = Icons.Rounded.VerticalAlignBottom,
                    trailingContent = {
                        androidx.compose.material3.Switch(
                            checked = settings.autoHideBottomBar,
                            onCheckedChange = { vm.setAutoHideBottomBar(it) }
                        )
                    }
                )
            )
        }
    }

    if (items.isNotEmpty()) {
        GlassSettingsGroup(
            title = "General",
            items = items,
            modifier = modifier
        )
    }
}

@Composable
private fun PrivacySection(settings: AppSettings, vm: SettingsViewModel, searchLower: String, modifier: Modifier = Modifier) {
    var showSetPin by remember { mutableStateOf(false) }
    var enablingAfterPin by remember { mutableStateOf(false) }

    val privacyItems = buildList {
        if ("secure folder".contains(searchLower)) {
            add(
                GlassSettingsItem(
                    title = "Secure Folder",
                    subtitle = "Hide photos behind biometrics or a PIN",
                    icon = Icons.Rounded.Lock,
                    trailingContent = {
                        Switch(
                            checked = settings.secureFolderEnabled,
                            onCheckedChange = { on ->
                                if (on && !settings.hasPin) { enablingAfterPin = true; showSetPin = true }
                                else vm.setSecureEnabled(on)
                            }
                        )
                    }
                )
            )
        }
        if (settings.secureFolderEnabled) {
            if ("change secure folder pin password".contains(searchLower)) {
                add(
                    GlassSettingsItem(
                        title = "Change Secure Folder PIN",
                        icon = Icons.Rounded.Password,
                        onClick = { showSetPin = true }
                    )
                )
            }
            if ("biometric unlock fingerprint face".contains(searchLower)) {
                add(
                    GlassSettingsItem(
                        title = "Biometric Unlock",
                        subtitle = "Use fingerprint or face to unlock",
                        icon = Icons.Rounded.Fingerprint,
                        trailingContent = {
                            Switch(
                                checked = settings.biometricUnlock,
                                onCheckedChange = { vm.setBiometricUnlock(it) }
                            )
                        }
                    )
                )
            }
            if ("show in albums secure folder".contains(searchLower)) {
                add(
                    GlassSettingsItem(
                        title = "Show in Albums",
                        subtitle = "Display the Secure Folder card in Albums",
                        icon = Icons.Rounded.Visibility,
                        trailingContent = {
                            Switch(
                                checked = settings.showSecureInAlbums,
                                onCheckedChange = { vm.setShowSecureInAlbums(it) }
                            )
                        }
                    )
                )
            }
        }
    }

    if (privacyItems.isNotEmpty()) {
        GlassSettingsGroup(
            title = "Privacy",
            items = privacyItems,
            modifier = modifier
        )
    }

    if (showSetPin) {
        SetPinDialog(
            onConfirm = { pin ->
                vm.setPin(pin)
                if (enablingAfterPin) vm.setSecureEnabled(true)
                enablingAfterPin = false
                showSetPin = false
            },
            onDismiss = { enablingAfterPin = false; showSetPin = false },
        )
    }
}

@Composable
private fun AboutSection(searchLower: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var showLicenses by remember { mutableStateOf(false) }

    val aboutItems = buildList {
        if ("photos version gallery".contains(searchLower)) {
            add(
                GlassSettingsItem(
                    customContent = {
                        val appColors = LocalAppColors.current
                        Column(Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 16.dp)) {
                            Text("Photos", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = appColors.textPrimary)
                            Text("V${com.gorilla.gallery.BuildConfig.VERSION_NAME} Build ${com.gorilla.gallery.BuildConfig.VERSION_CODE}", fontSize = 13.sp, color = appColors.textSecondary, modifier = Modifier.padding(top = 4.dp))
                            Text("A premium offline gallery.", fontSize = 13.sp, color = appColors.textSecondary, modifier = Modifier.padding(top = 2.dp))
                        }
                    }
                )
            )
        }
        if ("developer aman".contains(searchLower)) {
            add(
                GlassSettingsItem(
                    title = "Developer",
                    subtitle = "Aman",
                    icon = Icons.Rounded.Person
                )
            )
        }
        if ("open source licenses".contains(searchLower)) {
            add(
                GlassSettingsItem(
                    title = "Open Source Licenses",
                    icon = Icons.Rounded.Info,
                    onClick = { showLicenses = true }
                )
            )
        }
    }

    if (aboutItems.isNotEmpty()) {
        Column(modifier) {
            GlassSettingsGroup(
                title = "About",
                items = aboutItems
            )
        }
    }

    if (showLicenses) {
        GlassAlertDialog(
            onDismissRequest = { showLicenses = false },
            title = "Open source licenses",
            text = "Jetpack Compose, Material 3 — Apache 2.0\n" +
                "AndroidX (Room, DataStore, Palette, Biometric, ExifInterface, Media3) — Apache 2.0\n" +
                "Coil — Apache 2.0\n" +
                "Accompanist — Apache 2.0",
            confirmLabel = "Close",
            onConfirm = { showLicenses = false }
        )
    }
}

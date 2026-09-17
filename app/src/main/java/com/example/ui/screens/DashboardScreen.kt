package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.DeviceHardwareInfo
import com.example.domain.model.LiveServerMetrics
import com.example.domain.model.ServerConfig
import com.example.domain.model.ServerStatus
import com.example.ui.components.StatusBadge
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceBorder
import com.example.ui.theme.ObsidianSurfaceElevated
import com.example.ui.theme.PumpkinOrange
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    server: ServerConfig?,
    metrics: LiveServerMetrics,
    localWifiIp: String,
    hardwareInfo: DeviceHardwareInfo? = null,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onRestart: () -> Unit,
    onUpdateConfig: (ServerConfig) -> Unit,
    onApplyHardwareRecommendation: () -> Unit = {},
    onCreateNewServer: () -> Unit = {},
    onNavigateToConsole: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (server == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.padding(32.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(ObsidianSurfaceElevated)
                        .border(1.dp, PumpkinOrange.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = PumpkinOrange, modifier = Modifier.size(28.dp))
                }
                Text(
                    text = "NO SERVER DEPLOYED",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "Create and run your native ARM64 Rust PumpkinMC server directly on your mobile device.",
                    fontSize = 14.sp,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Button(
                    onClick = onCreateNewServer,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PumpkinOrange,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.height(48.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Create New Server", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
        return
    }

    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current
    val isRunning = server.status == ServerStatus.RUNNING

    // Local buffered draft state for configuration
    var draftServer by remember(server.id) { mutableStateOf(server) }
    LaunchedEffect(server) {
        if (draftServer.id != server.id) {
            draftServer = server
        }
    }

    val isDirty = draftServer.serverVersion != server.serverVersion ||
        draftServer.workerThreads != server.workerThreads ||
        draftServer.rayonThreads != server.rayonThreads ||
        draftServer.allocatedRamMb != server.allocatedRamMb ||
        draftServer.maxStorageMb != server.maxStorageMb ||
        draftServer.gamemode != server.gamemode ||
        draftServer.difficulty != server.difficulty ||
        draftServer.onlineMode != server.onlineMode ||
        draftServer.pvp != server.pvp ||
        draftServer.viewDistance != server.viewDistance ||
        draftServer.simulationDistance != server.simulationDistance ||
        draftServer.playitEnabled != server.playitEnabled ||
        draftServer.lanModeEnabled != server.lanModeEnabled

    val coreOptions = listOf(1, 2, 4, 6, 8)
    val ramOptions = listOf(256, 512, 1024, 2048, 4096)
    val storageOptions = listOf(1024, 2048, 4096)
    val gamemodes = listOf("survival", "creative", "adventure")
    val difficulties = listOf("peaceful", "easy", "normal", "hard")

    var versionMenuExpanded by remember { mutableStateOf(false) }
    val versions = listOf(
        "26.3 (Latest 2026 Release)",
        "26.2 (Winter Update)",
        "26w Snapshot (Bleeding Edge)",
        "1.21.4 (Latest LTS)",
        "1.21.1 (Stable)",
        "1.20.6 (Tricky Trials)",
        "1.20.4 (Prior LTS)",
        "1.19.4 (Wild Update)",
        "1.16.5 (Legacy Nether)"
    )

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .padding(bottom = if (isDirty) 76.dp else 0.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top Action Row - Outside "New Server" button readily available
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("SERVER INSTANCE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextMuted, letterSpacing = 0.5.sp)
                Text(server.name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            }

            Button(
                onClick = onCreateNewServer,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ObsidianSurfaceElevated,
                    contentColor = TextPrimary
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.border(1.dp, PumpkinOrange.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = PumpkinOrange, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("New Server", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        // Main Server Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(ObsidianSurface)
                .border(1.dp, if (isRunning) PumpkinOrange.copy(alpha = 0.5f) else ObsidianSurfaceBorder, RoundedCornerShape(14.dp))
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = server.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(ObsidianSurfaceElevated)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "MC ${server.serverVersion.split(" ").first()}",
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = PumpkinOrange
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Port ${server.port}",
                                fontSize = 12.sp,
                                color = TextMuted,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    StatusBadge(status = server.status)
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Power Controls: Clean, sleek, high-contrast
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { if (isRunning) onStop() else onStart() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isRunning) ObsidianSurfaceElevated else PumpkinOrange,
                            contentColor = if (isRunning) TextPrimary else Color.Black
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .then(
                                if (isRunning) Modifier.border(1.dp, ObsidianSurfaceBorder, RoundedCornerShape(10.dp))
                                else Modifier
                            )
                    ) {
                        Icon(
                            imageVector = if (isRunning) Icons.Default.PowerSettingsNew else Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isRunning) "Stop Server" else "Start Server",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    if (isRunning) {
                        Button(
                            onClick = onRestart,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ObsidianSurfaceElevated,
                                contentColor = TextPrimary
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .height(46.dp)
                                .border(1.dp, ObsidianSurfaceBorder, RoundedCornerShape(10.dp))
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, tint = PumpkinOrange, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Restart", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }

        // Connection Addresses
        val activeAddress = if (server.playitEnabled && server.playitDomain.isNotEmpty()) {
            server.playitDomain
        } else {
            "$localWifiIp:${server.port}"
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(ObsidianSurface)
                .border(1.dp, ObsidianSurfaceBorder, RoundedCornerShape(12.dp))
                .clickable {
                    clipboard.setText(AnnotatedString(activeAddress))
                    Toast.makeText(context, "Copied: $activeAddress", Toast.LENGTH_SHORT).show()
                }
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (server.playitEnabled) "GLOBAL SERVER ADDRESS" else "LOCAL LAN ADDRESS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isRunning) activeAddress else "Server Offline",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Monospace,
                        color = if (isRunning) PumpkinOrange else TextMuted
                    )
                }

                if (isRunning) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(ObsidianSurfaceElevated)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = TextPrimary, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Copy", fontSize = 12.sp, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }

        // Essential Live Stats
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(ObsidianSurface)
                .border(1.dp, ObsidianSurfaceBorder, RoundedCornerShape(12.dp))
                .padding(vertical = 16.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatItem(
                label = "TPS",
                value = if (isRunning) "%.1f".format(metrics.tps) else "0.0"
            )
            Box(modifier = Modifier.width(1.dp).height(24.dp).background(ObsidianSurfaceBorder))
            StatItem(
                label = "RAM",
                value = if (isRunning) "${metrics.usedRamMb.toInt()} MB" else "0 MB"
            )
            Box(modifier = Modifier.width(1.dp).height(24.dp).background(ObsidianSurfaceBorder))
            StatItem(
                label = "PLAYERS",
                value = if (isRunning) "${metrics.onlinePlayers}/${metrics.maxPlayers}" else "0/${server.maxPlayers}"
            )
        }

        // Hardware & Multithreading Tuning
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(ObsidianSurface)
                .border(1.dp, ObsidianSurfaceBorder, RoundedCornerShape(12.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Memory, contentDescription = null, tint = PumpkinOrange, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "HARDWARE & MULTI-CORE TUNING",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 0.5.sp
                    )
                }

                if (hardwareInfo != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(PumpkinOrange.copy(alpha = 0.15f))
                            .border(1.dp, PumpkinOrange.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                            .clickable {
                                draftServer = draftServer.copy(
                                    workerThreads = hardwareInfo.recommendedCores,
                                    rayonThreads = hardwareInfo.recommendedCores,
                                    allocatedRamMb = hardwareInfo.recommendedRamMb
                                )
                                Toast.makeText(context, "Draft auto-tuned for ${hardwareInfo.deviceModel}! Press Save to apply.", Toast.LENGTH_SHORT).show()
                            }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Bolt, contentDescription = null, tint = PumpkinOrange, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Auto-Tune (${hardwareInfo.recommendedCores}C / ${if (hardwareInfo.recommendedRamMb >= 1024) "${hardwareInfo.recommendedRamMb / 1024}GB" else "${hardwareInfo.recommendedRamMb}MB"})",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = PumpkinOrange
                            )
                        }
                    }
                }
            }

            // Minecraft Version Selection
            Column {
                Text("Minecraft Version", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                Spacer(modifier = Modifier.height(6.dp))
                ExposedDropdownMenuBox(
                    expanded = versionMenuExpanded,
                    onExpandedChange = { versionMenuExpanded = !versionMenuExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = draftServer.serverVersion,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = versionMenuExpanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PumpkinOrange,
                            unfocusedBorderColor = ObsidianSurfaceBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = versionMenuExpanded,
                        onDismissRequest = { versionMenuExpanded = false },
                        modifier = Modifier.background(ObsidianSurface)
                    ) {
                        versions.forEach { ver ->
                            DropdownMenuItem(
                                text = { Text(ver, color = TextPrimary, fontSize = 13.sp) },
                                onClick = {
                                    draftServer = draftServer.copy(serverVersion = ver)
                                    versionMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(ObsidianSurfaceBorder))

            // CPU Multi-Threading / Tokio & Rayon Cores (With Low-End Mobile Safe Mode)
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("CPU Worker Threads", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                        Text("Tokio async workers & Rayon chunk threads", fontSize = 11.sp, color = TextMuted)
                    }
                    Text(
                        text = if (draftServer.workerThreads == 1) "1 Core (Low-End)" else "${draftServer.workerThreads} Cores",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PumpkinOrange
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    coreOptions.forEach { cores ->
                        val isSelected = draftServer.workerThreads == cores
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) PumpkinOrange else ObsidianSurfaceElevated)
                                .border(1.dp, if (isSelected) PumpkinOrange else ObsidianSurfaceBorder, RoundedCornerShape(8.dp))
                                .clickable { draftServer = draftServer.copy(workerThreads = cores, rayonThreads = cores) }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (cores == 1) "1 (Low)" else "$cores Cores",
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.Black else TextSecondary
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (draftServer.workerThreads == 1) {
                        "⚡ Low-End Phone Safe Mode: Single Tokio worker with cooperative async reactor. Uses under 20MB RAM, zero thermal throttling on budget quad-core devices."
                    } else {
                        "PumpkinMC in Rust natively parallelizes terrain and networking across ARM64 cores without Java VM bottlenecks."
                    },
                    fontSize = 10.sp,
                    color = if (draftServer.workerThreads == 1) PumpkinOrange else TextMuted
                )
            }

            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(ObsidianSurfaceBorder))

            // RAM Allocation
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Allocated RAM", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                    Text(if (draftServer.allocatedRamMb >= 1024) "${draftServer.allocatedRamMb / 1024} GB" else "${draftServer.allocatedRamMb} MB", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PumpkinOrange)
                }
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ramOptions.forEach { mb ->
                        val isSelected = draftServer.allocatedRamMb == mb
                        val label = if (mb >= 1024) "${mb / 1024}GB" else "${mb}MB"
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) PumpkinOrange else ObsidianSurfaceElevated)
                                .border(1.dp, if (isSelected) PumpkinOrange else ObsidianSurfaceBorder, RoundedCornerShape(8.dp))
                                .clickable { draftServer = draftServer.copy(allocatedRamMb = mb) }
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.Black else TextSecondary
                            )
                        }
                    }
                }
            }

            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(ObsidianSurfaceBorder))

            // Storage Limit Quota
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Storage, contentDescription = null, tint = PumpkinOrange, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("World Storage Limit", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                    }
                    Text("${draftServer.maxStorageMb / 1024} GB", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PumpkinOrange)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    storageOptions.forEach { mb ->
                        val isSelected = draftServer.maxStorageMb == mb
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) PumpkinOrange else ObsidianSurfaceElevated)
                                .border(1.dp, if (isSelected) PumpkinOrange else ObsidianSurfaceBorder, RoundedCornerShape(8.dp))
                                .clickable { draftServer = draftServer.copy(maxStorageMb = mb) }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${mb / 1024} GB Quota",
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.Black else TextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Gameplay & World Rules
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(ObsidianSurface)
                .border(1.dp, ObsidianSurfaceBorder, RoundedCornerShape(12.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Tune, contentDescription = null, tint = PumpkinOrange, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "GAMEPLAY & WORLD RULES",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    letterSpacing = 0.5.sp
                )
            }

            // Gamemode
            Column {
                Text("Default Gamemode", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    gamemodes.forEach { mode ->
                        val isSelected = draftServer.gamemode.equals(mode, ignoreCase = true)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) PumpkinOrange else ObsidianSurfaceElevated)
                                .border(1.dp, if (isSelected) PumpkinOrange else ObsidianSurfaceBorder, RoundedCornerShape(8.dp))
                                .clickable { draftServer = draftServer.copy(gamemode = mode) }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = mode.replaceFirstChar { it.uppercase() },
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.Black else TextSecondary
                            )
                        }
                    }
                }
            }

            // Difficulty
            Column {
                Text("Difficulty", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    difficulties.forEach { diff ->
                        val isSelected = draftServer.difficulty.equals(diff, ignoreCase = true)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) PumpkinOrange else ObsidianSurfaceElevated)
                                .border(1.dp, if (isSelected) PumpkinOrange else ObsidianSurfaceBorder, RoundedCornerShape(8.dp))
                                .clickable { draftServer = draftServer.copy(difficulty = diff) }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = diff.replaceFirstChar { it.uppercase() },
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.Black else TextSecondary
                            )
                        }
                    }
                }
            }

            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(ObsidianSurfaceBorder))

            // Online Mode (Mojang Auth)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Online Mode (Mojang Auth)", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                    Text(if (draftServer.onlineMode) "Enforces official accounts" else "Allows cracked & offline players", fontSize = 12.sp, color = TextMuted)
                }
                Switch(
                    checked = draftServer.onlineMode,
                    onCheckedChange = { draftServer = draftServer.copy(onlineMode = it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.Black,
                        checkedTrackColor = PumpkinOrange,
                        uncheckedThumbColor = TextMuted,
                        uncheckedTrackColor = ObsidianSurfaceElevated
                    )
                )
            }

            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(ObsidianSurfaceBorder))

            // PvP Combat
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("PvP Combat", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                    Text("Allows player-vs-player damage", fontSize = 12.sp, color = TextMuted)
                }
                Switch(
                    checked = draftServer.pvp,
                    onCheckedChange = { draftServer = draftServer.copy(pvp = it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.Black,
                        checkedTrackColor = PumpkinOrange,
                        uncheckedThumbColor = TextMuted,
                        uncheckedTrackColor = ObsidianSurfaceElevated
                    )
                )
            }

            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(ObsidianSurfaceBorder))

            // View Distance Slider
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("View Distance", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                    Text("${draftServer.viewDistance} chunks", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PumpkinOrange)
                }
                Slider(
                    value = draftServer.viewDistance.toFloat(),
                    onValueChange = { draftServer = draftServer.copy(viewDistance = it.toInt()) },
                    valueRange = 4f..16f,
                    colors = SliderDefaults.colors(
                        thumbColor = PumpkinOrange,
                        activeTrackColor = PumpkinOrange,
                        inactiveTrackColor = ObsidianSurfaceBorder
                    )
                )
            }

            // Simulation Distance Slider
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Simulation Distance", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                    Text("${draftServer.simulationDistance} chunks", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PumpkinOrange)
                }
                Slider(
                    value = draftServer.simulationDistance.toFloat(),
                    onValueChange = { draftServer = draftServer.copy(simulationDistance = it.toInt()) },
                    valueRange = 4f..12f,
                    colors = SliderDefaults.colors(
                        thumbColor = PumpkinOrange,
                        activeTrackColor = PumpkinOrange,
                        inactiveTrackColor = ObsidianSurfaceBorder
                    )
                )
            }
        }

        // Network Toggles
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(ObsidianSurface)
                .border(1.dp, ObsidianSurfaceBorder, RoundedCornerShape(12.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "NETWORK & ACCESS",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = TextMuted,
                letterSpacing = 0.5.sp
            )

            // Playit.gg Auto Tunnel
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Global Free Tunnel (playit.gg)", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                    Text("Allows players worldwide to join without port forwarding", fontSize = 12.sp, color = TextMuted)
                }
                Switch(
                    checked = draftServer.playitEnabled,
                    onCheckedChange = { draftServer = draftServer.copy(playitEnabled = it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.Black,
                        checkedTrackColor = PumpkinOrange,
                        uncheckedThumbColor = TextMuted,
                        uncheckedTrackColor = ObsidianSurfaceElevated
                    )
                )
            }

            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(ObsidianSurfaceBorder))

            // LAN Broadcast
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Local WiFi (LAN Mode)", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                    Text("Discovers devices on your local home network", fontSize = 12.sp, color = TextMuted)
                }
                Switch(
                    checked = draftServer.lanModeEnabled,
                    onCheckedChange = { draftServer = draftServer.copy(lanModeEnabled = it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.Black,
                        checkedTrackColor = PumpkinOrange,
                        uncheckedThumbColor = TextMuted,
                        uncheckedTrackColor = ObsidianSurfaceElevated
                    )
                )
            }
        }
    }

    // Floating Save / Discard Bar (Only appears when changes are made)
    AnimatedVisibility(
        visible = isDirty,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(ObsidianSurfaceElevated)
                .border(1.5.dp, PumpkinOrange, RoundedCornerShape(14.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Unsaved Changes",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = PumpkinOrange
                    )
                    Text(
                        text = "Settings won't apply until saved",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { draftServer = server },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ObsidianSurface,
                            contentColor = TextMuted
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Discard", fontSize = 12.sp)
                    }

                    Button(
                        onClick = {
                            onUpdateConfig(draftServer)
                            Toast.makeText(context, "Server settings saved & applied!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PumpkinOrange,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Save", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextMuted, letterSpacing = 0.5.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = TextPrimary)
    }
}

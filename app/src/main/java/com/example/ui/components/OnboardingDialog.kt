package com.example.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.ObsidianDark
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceBorder
import com.example.ui.theme.ObsidianSurfaceElevated
import com.example.ui.theme.PumpkinOrange
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch

data class OnboardingSlide(
    val title: String,
    val subtitle: String,
    val badge: String,
    val icon: ImageVector,
    val description: String,
    val highlights: List<String>
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingDialog(
    onDismiss: () -> Unit
) {
    val slides = listOf(
        OnboardingSlide(
            title = "Powered by PumpkinMC",
            subtitle = "Native Rust Minecraft Engine",
            badge = "CORE ARCHITECTURE",
            icon = Icons.Default.Bolt,
            description = "This mobile host is engineered with and powered by PumpkinMC—an ultra-fast, multithreaded Minecraft server written in bare-metal Rust. No heavy Java Virtual Machine, no bloated runtimes.",
            highlights = listOf(
                "Bare-metal ARM64 native machine instructions",
                "Constant 20.0 TPS with zero garbage-collector stutters",
                "Full open-source credit to the PumpkinMC engine project"
            )
        ),
        OnboardingSlide(
            title = "Ultra Lightweight & Efficient",
            subtitle = "Under 35MB Base RAM Usage",
            badge = "BATTERY & RESOURCE FRIENDLY",
            icon = Icons.Default.Speed,
            description = "Unlike traditional Spigot or Paper servers requiring 2GB–4GB of RAM that overheat phones, PumpkinMC idles at just 18MB to 35MB RAM, saving battery and preventing Android OS background termination.",
            highlights = listOf(
                "98% lower memory footprint than standard Java servers",
                "Cooperative async Tokio network architecture",
                "Hardware thermal protection keeps your phone cool"
            )
        ),
        OnboardingSlide(
            title = "Deeply Configurable",
            subtitle = "Fine-Tuned For Any Android Device",
            badge = "HARDWARE AUTO-TUNING",
            icon = Icons.Default.Tune,
            description = "Whether running on a flagship Snapdragon 8 or a budget quad-core phone, the app detects your device chipset and dynamically tunes core worker threads, tickrate, view distance, and memory buffers.",
            highlights = listOf(
                "Rayon work-stealing multithreaded terrain generator",
                "Explicit Save bar ensures settings apply strictly on demand",
                "WASM plugins & live in-app TOML/JSON configuration editor"
            )
        ),
        OnboardingSlide(
            title = "Play With Friends Anywhere",
            subtitle = "Zero Port Forwarding Needed",
            badge = "EFFORTLESS MULTIPLAYER",
            icon = Icons.Default.Group,
            description = "Play over local WiFi with zero setup, or toggle the built-in free Playit.gg tunnel to share a public link with friends anywhere in the world. Supports Java edition & Bedrock crossplay (Android/iOS/Consoles).",
            highlights = listOf(
                "Automatic local WiFi and hotspot IP discovery",
                "Integrated global playit.gg secure tunnel",
                "Geyser & Floodgate crossplay for mobile & console players"
            )
        )
    )

    val pagerState = rememberPagerState(pageCount = { slides.size })
    val scope = rememberCoroutineScope()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(20.dp))
                .border(1.5.dp, PumpkinOrange.copy(alpha = 0.6f), RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(containerColor = ObsidianDark)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(PumpkinOrange.copy(alpha = 0.15f))
                            .border(1.dp, PumpkinOrange.copy(alpha = 0.35f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "POWERED BY PUMPKINMC",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = PumpkinOrange,
                            letterSpacing = 0.5.sp
                        )
                    }

                    TextButton(onClick = onDismiss) {
                        Text("Skip", fontSize = 12.sp, color = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Slide Content Pager
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(340.dp)
                ) { page ->
                    val slide = slides[page]
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(ObsidianSurfaceElevated)
                                .border(1.dp, PumpkinOrange.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(slide.icon, contentDescription = null, tint = PumpkinOrange, modifier = Modifier.size(34.dp))
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = slide.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = slide.subtitle,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = PumpkinOrange,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = slide.description,
                            fontSize = 12.sp,
                            color = TextSecondary,
                            textAlign = TextAlign.Center,
                            lineHeight = 17.sp,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Highlights box
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(ObsidianSurface)
                                .border(1.dp, ObsidianSurfaceBorder, RoundedCornerShape(10.dp))
                                .padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            slide.highlights.forEach { highlight ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = null,
                                        tint = PumpkinOrange,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = highlight,
                                        fontSize = 11.sp,
                                        color = TextPrimary,
                                        lineHeight = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Dots indicator
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    repeat(slides.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 3.dp)
                                .size(if (isSelected) 8.dp else 6.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) PumpkinOrange else ObsidianSurfaceElevated)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Footer Actions
                val isLastPage = pagerState.currentPage == slides.size - 1
                Button(
                    onClick = {
                        if (isLastPage) {
                            onDismiss()
                        } else {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PumpkinOrange,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                ) {
                    Text(
                        text = if (isLastPage) "Get Started" else "Next",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        if (isLastPage) Icons.Default.Check else Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Small credit note
                Text(
                    text = "Special thanks to the open-source PumpkinMC project & community (github.com/Pumpkin-MC)",
                    fontSize = 9.sp,
                    color = TextMuted,
                    textAlign = TextAlign.Center,
                    lineHeight = 12.sp
                )
            }
        }
    }
}

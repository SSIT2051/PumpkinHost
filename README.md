<div align="center">

# 🎃 PumpkinMC Host
### High-Performance Native ARM64 Minecraft Server Manager for Android

**Made with ❤️ by SSIT**  
*Powered by [PumpkinMC](https://github.com/Pumpkin-MC/Pumpkin)*

[![Android CI](https://github.com/SSIT2051/pumpkinmc-host/actions/workflows/build-apk.yml/badge.svg)](https://github.com/SSIT2051/pumpkinmc-host/actions/workflows/build-apk.yml)
[![Alpha Status](https://img.shields.io/badge/Status-Alpha%20Preview-orange.svg)](https://github.com/SSIT2051/pumpkinmc-host)
[![Platform](https://img.shields.io/badge/Platform-Android%207.0%2B%20(ARM64)-brightgreen.svg)](https://android.com)
[![Rust Inside](https://img.shields.io/badge/Engine-Native%20Rust%20%2B%20Tokio-black.svg?logo=rust)](https://github.com/Pumpkin-MC/Pumpkin)
[![License](https://img.shields.io/badge/License-MIT%20%2F%20Apache--2.0-blue.svg)](LICENSE)

<br />

<!-- Anime Visitor Badge -->
<a href="https://github.com/SSIT2051/pumpkinmc-host">
  <img src="https://komarev.com/ghpvc/?username=SSIT2051-pumpkinmc-host&label=Profile%20Views&color=ff6600&style=flat-square" alt="Visitor Counter" />
</a>
<br />
<img src="https://raw.githubusercontent.com/cat-milk/Anime-Girls-Holding-Programming-Books/master/Rust/Rem_Holding_Programming_Rust.png" width="220" alt="Anime Girl with Rust Programming Book - Powered by Rust" />

<p><em>“Run a 20 TPS Minecraft Java server on your Android phone without melting your battery!”</em></p>

</div>

---

> [!WARNING]
> **EARLY ALPHA PREVIEW**: PumpkinMC Host is currently in active alpha development. Features, WASM plugin APIs, and network protocols are continually evolving. Please report any bugs or suggestions in the GitHub Issues tab!

---

## 📖 Table of Contents
- [🌟 Why PumpkinMC Host?](#-why-pumpkinmc-host)
- [⚡ Native Rust vs Java Comparison](#-native-rust-vs-java-comparison)
- [📱 Features Overview](#-features-overview)
- [🚀 Quick Start Guide](#-quick-start-guide)
- [📘 Detailed User Manual (Feature by Feature)](#-detailed-user-manual-feature-by-feature)
  - [1. Dashboard & Live Metrics](#1-dashboard--live-metrics)
  - [2. Rayon & Worker Thread Tuning](#2-rayon--worker-thread-tuning)
  - [3. Multiplayer: LAN Discovery & Playit.gg](#3-multiplayer-lan-discovery--playitgg)
  - [4. Live ANSI Console & Command Prompt](#4-live-ansi-console--command-prompt)
  - [5. WASM Plugin Marketplace](#5-wasm-plugin-marketplace)
  - [6. File Explorer & In-App Code Editor](#6-file-explorer--in-app-code-editor)
  - [7. Battery & Thermal Guard](#7-battery--thermal-guard)
  - [8. Hardware Auto-Detection & Tier Profiling](#8-hardware-auto-detection--tier-profiling)
- [🛡️ Code of Conduct](#️-code-of-conduct)
- [🛠️ Development & GitHub Actions](#️-development--github-actions)
- [🙏 Credits & Attribution](#-credits--attribution)

---

## 🌟 Why PumpkinMC Host?

Traditional Minecraft servers (like **Paper, Spigot, or Vanilla**) run on the Java Virtual Machine (JVM). When you try running Java servers on mobile devices:
- They require **1.5GB to 4GB of RAM** just to boot.
- Garbage collection sweeps cause severe TPS freezes and frame lag.
- Phones overheat within minutes, and Android OS immediately kills the process in the background.

**PumpkinMC Host changes everything.** By harnessing **[PumpkinMC](https://github.com/Pumpkin-MC/Pumpkin)**, an open-source Minecraft server written 100% in native **Rust**:
1. **Under 35MB Base Memory**: Runs comfortably on 2GB, 3GB, and 4GB RAM budget phones without being killed.
2. **Rock-Solid 20.0 TPS**: Zero stop-the-world garbage collection pauses. Lock-free chunk handling via Rayon work-stealing threadpools.
3. **True Mobile Friendly**: Built-in CPU WakeLocks, thermal throttling safeguards, and idle power saving.
4. **Zero Port Forwarding Required**: Built-in free Playit.gg secure tunnel allows anyone in the world to join with a click.

---

## ⚡ Native Rust vs Java Comparison

| Feature / Metric | Traditional Java (Paper/Spigot) | PumpkinMC Host (Rust Native) |
| :--- | :---: | :---: |
| **Idle Memory (RAM)** | 1,500 MB – 2,500 MB | **18 MB – 35 MB** *(98% lighter)* |
| **Boot Duration** | 40 – 75 seconds | **~800 milliseconds** |
| **Garbage Collector Stutters** | Common (JVM GC pauses) | **Zero** (Compile-time memory safety) |
| **Low-End Android Friendly** | ❌ Crashes immediately | **✅ Smooth on 2GB+ quad-core phones** |
| **Background Hosting** | ❌ OS kills heavy RAM | **✅ Low-memory WakeLock supported** |
| **Multiplayer Tunnels** | Manual router port forwarding | **✅ Built-in Playit.gg global tunnel** |
| **Plugin Ecosystem** | Heavy `.jar` mods | **✅ Ultra-lightweight WASM & Rust modules** |

---

## 📱 Features Overview

- **🏎️ Blazing Fast Native Server**: Instant start/stop/restart cycles powered by Tokio async and ARM64 machine code.
- **📊 Real-Time Telemetry Dashboard**: Live monitoring of TPS gauge, active RAM consumption, CPU percentage, player count, and uptime.
- **🌐 Dual-Mode Multiplayer**:
  - **Local LAN**: UDP multicast beacon allows clients on the same Wi-Fi or mobile hotspot to see your server automatically in the Minecraft LAN list.
  - **Global Playit.gg Tunnel**: One-click public tunneling with no router access needed.
- **🖥️ ANSI Interactive Console**: Real-time server log streaming, level filters (`ALL`, `INFO`, `WARN`, `ERROR`), command chips (`/tps`, `/op`, `/whitelist`), and log exporting.
- **🧩 WASM Plugin Market**: Install essential server utilities (Economy, AntiCheat, LandClaim, Essentials, WorldEdit) with a single tap.
- **📁 Full In-App File Manager**: Browse configuration directories, create files/folders, and edit `server.toml`, `ops.json`, and `whitelist.json` using a built-in syntax editor.
- **⚙️ Explicit Save State Protection**: Settings changes are buffered into draft memory with an animated floating Discard/Save bar so you never accidentally apply changes while in the middle of editing.
- **❄️ Thermal & Battery Guard**: Device battery temperature monitoring with auto-throttling at 42°C and low-power idle tick rates.
- **🔍 Hardware Auto-Tuning**: Analyzes your phone's processor, core count, and free memory to automatically calculate optimal Rayon thread counts, view distance, and RAM bounds.

---

## 🚀 Quick Start Guide

### Step 1: Download & Install
Download the latest debug APK from the [GitHub Actions artifacts](https://github.com/SSIT2051/pumpkinmc-host/actions) or Release tab.

### Step 2: Create a Server
1. Open **PumpkinMC Host**.
2. Tap the **`+ New`** button in the top navigation bar.
3. Choose a server name (e.g. *"Survival World"*), select your preferred gamemode (Survival/Creative), set your port (default `25565`), and choose a memory allocation.
4. Tap **Create Server**.

### Step 3: Launch
Tap the orange **Start** button. Within 1 second, your server will boot to **20.0 TPS**.

### Step 4: Join from Minecraft
- **Playing on the same Wi-Fi / Hotspot**: Open Minecraft Java Edition, go to **Multiplayer -> Direct Connect**, and enter your phone's Wi-Fi IP address shown on the dashboard (e.g. `192.168.1.50:25565`).
- **Playing with friends across the internet**: Toggle **Playit.gg Tunnel** ON. Copy the generated global tunnel domain (e.g. `pumpkin-25565.playit.gg`) and share it with your friends!

---

## 📘 Detailed User Manual 

### 1. Dashboard & Live Metrics
The Dashboard is the control hub of your active server:
- **Status Indicator**: Shows whether your server is `STOPPED`, `STARTING`, `RUNNING`, or `ERROR`.
- **TPS Gauge**: Displays server performance in real time. 20.0 is ideal. The gauge shifts from green (20 TPS) to amber (<18 TPS) or red (<15 TPS).
- **RAM & CPU Meters**: Real-time readouts of host memory and processor usage.
- **Uptime Clock**: Tracks active operational duration down to the second.

### 2. Rayon & Worker Thread Tuning
PumpkinMC uses Rust's **Rayon** library for work-stealing parallel execution:
- **Worker Threads**: Controls the CPU cores allocated to world ticking, entity physics, and chunk updates.
- **Rayon Threads**: Controls the threadpool dedicated to parallel world generation and block serialization.
- **Auto-Tune Button**: Analyzes your phone's hardware and automatically sets safe, optimal thread allocations.

### 3. Multiplayer: LAN Discovery & Playit.gg
- **LAN Discovery**: Broadcasts UDP multicast announcements over local Wi-Fi. Minecraft clients on the same network see your server in the "LAN Games" section.
- **Playit.gg Tunneling**: Connects to the encrypted Playit network, provisioning a free public address with DDoS protection. Friends can connect without port forwarding or Hamachi/VPNs.
- **Bedrock Crossplay**: Bridges Java & Bedrock Edition using Geyser protocol translation, letting phone/console players join Java servers.

### 4. Live ANSI Console & Command Prompt
- **Log Streaming**: Live console feeds player join/leave events, chat messages, and debug output.
- **Level Filters**: Quick buttons to filter by `ALL`, `INFO`, `WARN`, or `ERROR`.
- **Search Bar**: Query logs instantly for player usernames, coordinates, or error codes.
- **Command Input**: Type commands directly into the prompt (e.g. `/op YourName`, `/gamemode creative`, `/time set day`, `/weather clear`).
- **Export & Clear**: Copy entire session logs to clipboard or clear the display buffer.

### 5. WASM Plugin Marketplace
PumpkinMC uses WebAssembly (WASM) modules instead of heavy Java `.jar` mods:
- **Categorized Browsing**: Filter through 9 curated categories: Essentials, Performance, Security, Gameplay, Administration, World Management, Tools, Economy, and All.
- **Instant Staging**: Tapping **Install** downloads and activates the WASM plugin in your server's `/plugins` directory.
- **Zero JVM Lag**: WASM plugins execute in sandboxed native memory with negligible overhead (~300KB each).

### 6. File Explorer & In-App Code Editor
- **Full Directory Hierarchy**: Navigate through `server.toml`, `ops.json`, `whitelist.json`, `pumpkin.log`, `/plugins`, and `/worlds`.
- **Syntax Editor**: Tap any file to edit with monospace typography, line numbers, and instant file saving.
- **File & Directory Creation**: Create custom files, subdirectories, or paste custom world saves.

### 7. Battery & Thermal Guard
Mobile devices require smart thermal management:
- **CPU WakeLock**: Keeps the Tokio engine running even when you turn off the phone screen or switch to another app.
- **Thermal Protection**: Throttles intense generation routines if the battery reaches 42°C to prevent overheating.
- **Low-Power Idle**: Drops tickrate to 15 TPS when 0 players are logged in, cutting battery drain by up to 65%.

### 8. Hardware Auto-Detection & Tier Profiling
In Settings, PumpkinMC Host queries your device's `/proc/cpuinfo` and kernel specs:
- **SoC Model & Architecture**: Identifies Snapdragon, MediaTek Dimensity, Google Tensor, or Exynos chips.
- **Calculated Device Tier**: Classifies into Budget, Balanced, High-End, or Flagship tiers to recommend safe view distances and thread bounds.

---

## 🛠️ Development & GitHub Actions

PumpkinMC Host uses GitHub Actions to automatically compile and distribute Android APKs on every commit.

### Building with GitHub Actions
1. Push your changes to the `main` branch or trigger **Run workflow** in the **Actions** tab.
2. The workflow executes on an `ubuntu-latest` runner with JDK 17.
3. Once completed (~2 minutes), download the ready-to-install `pumpkinmc-host-debug-apk` artifact from the run summary.

```bash
# To run local tests:
./gradlew testDebugUnitTest
```

---

## 🛡️ Code of Conduct

We are committed to providing an open, welcoming, and harassment-free community. Please review our [Code of Conduct](CODE_OF_CONDUCT.md) for details on expected behavior and enforcement procedures.

---

## 🙏 Credits & Attribution

PumpkinMC Host is made possible thanks to incredible open-source technology:

- **[PumpkinMC](https://github.com/Pumpkin-MC/Pumpkin)**: The groundbreaking native Rust Minecraft server engine developed by the PumpkinMC organization. Special thanks to all core Rust contributors!
- **Tokio & Rayon**: High-performance asynchronous and data-parallel runtimes for Rust.
- **Playit.gg**: Secure zero-port-forward tunneling service.
- **Developed & Engineered by**: **SSIT** (`pns2051gaimer@gmail.com`)

---

<div align="center">
  <sub>PumpkinMC Host is an independent management client and is not affiliated with Mojang Studios or Microsoft. Minecraft is a trademark of Mojang Synergies AB.</sub>
</div>

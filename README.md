# 🚀 Spaceship Escape

**Spaceship Escape** is an advanced endless Android game developed in **Kotlin**.  
The project was originally created as part of a **User Interfaces Development** course and later significantly expanded with sensors, animations, sound, persistence, maps, and modular UI architecture.

The player controls a **spaceship** that must dodge falling **meteors**, collect **galaxies**, and survive as long as possible while achieving high scores.

---

## 🎮 Game Overview

- The spaceship moves between fixed lanes at the bottom of the screen
- Meteors fall from the top of the screen
- Galaxies can be collected for bonus points
- The game supports both **button-based** and **sensor-based** controls
- The game ends when all lives are lost and a custom dialog is displayed

---

## ✨ Key Features

### 🚀 Gameplay
- 🛣️ **Lane-Based Movement**  
  The spaceship moves left and right between predefined lanes.

- ☄️ **Random Falling Meteors**  
  Meteors are generated randomly and fall at a configurable speed.

- 🌌 **Galaxy Collection & Bonus Points**  
  Collecting galaxies grants bonus points to the player.

- ❤️ **Lives System**  
  The player starts with **3 lives**.  
  Each collision with a meteor removes one life.

---

### 🎛️ Controls
- 🎮 **Button Controls**  
  On-screen left (`←`) and right (`→`) buttons.

- 📱 **Tilt (Sensor) Controls**  
  Implemented using a custom **TiltDetector**:
  - Tilt **left / right** → move the spaceship
  - Tilt **forward** → increase game speed
  - Tilt **backward** → decrease game speed  
  Button controls can be disabled to play **only with sensors**.

---

### 🔊 Audio
- ☄️ **Meteor Collision Sound**
- 🌌 **Galaxy Collection Sound**
- 🎵 **Background Game Music**  
  Background music can be toggled on/off from the main menu.

---

### 🖥️ User Interface & Screens
- 🚀 **Splash Screen**
  - Displayed on app launch
  - Implemented using **Lottie animations**

- 🏠 **Main Menu Screen**
  - Start Game
  - Navigate to High Scores
  - Switch between Button / Sensor controls
  - Game speed selector (Fast / Slow)
  - Background sound toggle

- 🏁 **Game Over Dialog**
  - Custom dialog displayed when the game ends
  - Shows the final score
  - Includes a button to navigate directly to the High Scores screen  
  (replaces the classic Game Over screen)

---

### 🏆 High Scores System
- 📊 **Top 10 High Scores Table**
- 💾 Scores are stored persistently using **SharedPreferences**
- 📦 **Gson** is used to serialize and deserialize score objects
- 🧩 The Scores screen is implemented using **Fragments**
- 🔁 **Callbacks** are used for communication between fragments and the hosting activity
- 📍 Clicking a score displays the **geographic location** where it was achieved

---

### 🗺️ Map Integration
- 🗺️ Score locations are displayed using **Google Maps**
- Each high score can be viewed on the map with its corresponding location

---

### 🎨 Visuals
- 🖼️ Custom **App Icon**
- Clean and responsive UI
- Space-themed design with consistent visuals across all screens

---

## 📸 Screenshots

### 🚀 Splash Screen
Animated splash screen using **Lottie**.

<p align="center">
  <img src="https://github.com/user-attachments/assets/e2b4d537-7701-4012-8938-ff701e080ae9" width="300"/>
</p>

---

### 🏠 Main Menu
Game configuration and navigation screen (controls mode, speed, sound).

<p align="center">
  <img src="https://github.com/user-attachments/assets/63d936d2-5c86-40cf-a080-a06522421004" width="300"/>
</p>

---

### 🎮 Gameplay
Real-time gameplay with meteors, galaxies, score, and lives.

<p align="center">
  <img src="https://github.com/user-attachments/assets/f041cbd8-0c95-4138-84bb-4cc26c35f4a0" width="250"/>
</p>

---

### 🏁 Game Over Dialog
Custom dialog showing final score with navigation to high scores.

<p align="center">
  <img src="https://github.com/user-attachments/assets/661728bc-eee5-4c2d-a4e3-937808c75d7a" width="300"/>
</p>


---

### 🏆 High Scores & Map
Top 10 scores table with **Google Maps** integration.

<p align="center">
  <img src="https://github.com/user-attachments/assets/c6f2cdb6-f10e-49ec-ab08-63bae682c725" width="300"/>
</p>


---

## 🛠️ Built With

- **Android Studio**
- **Kotlin**
- **Android SDK**
- **SharedPreferences**
- **Gson**
- **Fragments**
- **Callbacks**
- **Sensors API**
- **Lottie**
- **Google Maps API**
- **MediaPlayer / Sound effects

---

## 🚀 Getting Started

To run this project locally:

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/spaceship-escape.git



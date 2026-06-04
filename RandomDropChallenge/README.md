# 🎲 RandomDropChallenge

A high-energy Minecraft challenge plugin inspired by popular creators like **BastiGHG**. Every block you break drops a completely random item in a random quantity. Can you survive and beat the game when dirt drops Diamonds and Obsidian drops Rotten Flesh?

## ✨ Features

* **Total Randomization:** Every block type is mapped to a unique random item drop at the start of each challenge.
* **Weighted Drop Quantities:** Uses a non-linear probability curve (Power-Law) to make large stacks rare and exciting.
* **Interactive UI:**
* **Epic Start Effects:** Visual titles and sounds when the challenge begins.
* **Live Timer:** A real-time tracker displayed in the Actionbar (above the hotbar).


* **Dynamic Controls:** Full command system with sub-commands and Tab-Completion.
* **Smart Filtering:** Automatically excludes "illegal" items (like air or technical blocks) to prevent server crashes.

## 🚀 Commands

| Command | Description | Permission |
| --- | --- | --- |
| `/challenge randomdropchallenge start` | Starts the timer and shuffles drops | OP |
| `/challenge randomdropchallenge stop` | Stops the challenge and clears timer | OP |
| `/challenge randomdropchallenge shuffle` | Re-rolls all block-to-item assignments | OP |

## 🛠️ Installation

1. Download the `RandomDropChallenge.jar` from your `target` folder.
2. Place the JAR file into your server's `plugins` folder.
3. Ensure you are running **Paper 1.21.1** or newer and **Java 21+**.
4. Restart your server or load the plugin.
5. Type `/challenge randomdropchallenge start` to begin!

## 💻 Development

This plugin was built using:

* **Java 21/25**
* **Paper API** (1.21.1-R0.1-SNAPSHOT)
* **Maven** for dependency management
* **Kyori Adventure API** for rich text components

### Building from Source

```bash
git clone https://github.com/yourusername/RandomDropChallenge.git
cd RandomDropChallenge
mvn clean package

```

## 📜 License

This project is licensed under the MIT License - feel free to use it for your own challenges!

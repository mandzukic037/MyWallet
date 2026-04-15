# 💳 MyWallet V2.7: Intelligent Personal Finance Architect

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Swing](https://img.shields.io/badge/Swing-GUI-blue?style=for-the-badge)
![AI-Powered](https://img.shields.io/badge/AI-Smart_Categorization-green?style=for-the-badge)

**MyWallet** is a desktop financial ecosystem. It leverages a custom-built heuristic engine to automate expense classification, providing a seamless and intelligent user experience for modern wealth management.

---

## 🚀 Key Engineering Highlights

### 🧠 AIService: Smart Heuristic Categorization
The core of MyWallet's intelligence lies in the `service.AIService`. 
* **Automated Vendor Analysis:** When a transaction is logged, the AI layer scans the vendor metadata.
* **Smart Mapping:** Automatically correlates new entries with existing `StoreType` profiles, reducing manual categorization.

### 🎨 Pixel-Perfect Custom UI
Built on a reactive `Theme` engine, the UI dynamically adapts to user preferences.
* **Deep Dark Mode:** Implemented via a recursive tree-traversal algorithm that updates all Swing components in real-time.
* **Custom Graphics:** `DonutChartItem` provides a vectorized, visual representation of resource allocation.
* **Enhanced Tables:** Customized `ZebraTable` rendering for high-density financial data.

### 🧵 Multi-threaded Data Persistence
To ensure zero UI freezing, all data operations are handled by `StorageService`:
* **Async I/O:** Record-keeping is offloaded to background threads.
* **Serialization:** Robust object persistence for `Expense`, `Income`, and `Store` entities.

---

## 🏗 Project Structure

The project follows a modular **Clean Architecture** pattern:

```text
src/
├── 📦 common        # Core Data Models (Expense, Income, Store, StoreType)
├── 📦 service       # Logic Layer (AIService, StorageService for async tasks)
├── 📦 storage       # Persistence Layer (Specialized storage for each entity)
├── 📦 tools         # Utility Layer (DateUtils and formatting engines)

└── 📦 ui            # Presentation Layer (Custom components, Theme, and Main Frames)

```
---

## 🛠 Tech Stack & Core Technologies

| Module | Implementation | Key Responsibility |
| :--- | :--- | :--- |
| **Language** | **Java 17** | Core application logic and object-oriented structure. |
| **GUI Framework** | **Java Swing / AWT** | Custom-rendered components with dynamic theme support. |
| **Intelligence** | **Implemented AI API** | `AIService` for automated vendor and store categorization. |
| **Concurrency** | **Managed ExecutorService** | Async I/O operations to ensure a lag-free UI experience. |
| **Persistence** | **Object Serialization** | Robust local data storage for Expenses, Incomes, and Stores. |

---

## ⚙️ Installation & Usage

1.  **Clone the Repository:**
    ```bash
    git clone https://github.com/mandzukic037/mywallet.git
    ```

2.  **Set your API Key:**
    
    Open `src/service/AiService.java` and replace the API key with your own:
    
    ```java
    private static final String API_KEY = "YOUR_API_KEY_HERE";
    ```

    ⚠️ **Important:**  
    Do not use the existing key. Generate your own Gemini API key.

3.  **Build:** Open the project directory in any modern Java IDE (IntelliJ IDEA, Eclipse, or VS Code).

4.  **Run:** Navigate to the `ui` package and execute `MainApp.java` to launch the application.

---

### 🤝 Contributing
Contributions, issues, and feature requests are welcome! Feel free to check the [issues page](https://github.com/mandzukic037/mywallet/issues).

Developed by Aleksa Mandžukić 
*Empowering financial freedom through intelligent software.*


---

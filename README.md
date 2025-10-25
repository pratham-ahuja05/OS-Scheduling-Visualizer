# OS Scheduling Visualizer (JavaFX)

![Java](https://img.shields.io/badge/Language-Java-orange?style=for-the-badge)
![JavaFX](https://img.shields.io/badge/GUI-JavaFX-blue?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

A **real-time CPU Scheduling Visualizer** built in Java using JavaFX.  
Simulates core OS scheduling algorithms with interactive process management, dynamic Gantt chart visualization, and real-time performance metrics.

---

## ✨ Features

| Category | Features |
|----------|---------|
| **Scheduling Algorithms** | FCFS, SJF (Preemptive/Non-preemptive), Priority (Preemptive/Non-preemptive), Round Robin |
| **Process Management** | Add/Edit/Delete processes, Random process generator, Input: ID, Arrival Time, Burst Time, Priority |
| **Gantt Chart Visualization** | Color-coded bars, Running/Waiting/Preempted highlights, Real-time updates |
| **Interactive Simulation** | Play/Pause/Resume, Step-by-step execution, Speed slider, Ready & Waiting queue visualization |
| **Metrics & Statistics** | Average Waiting Time (AWT), Average Turnaround Time (ATAT), CPU Utilization, Throughput |
| **UI Enhancements** | Light/Dark theme toggle, Unique process colors, Tooltips on hover |
| **Export & Reporting** | Export Gantt chart as PNG, Save/Load process lists as CSV |

---

## 💻 Installation & Usage

### 1. Clone the repository
```bash
git clone https://github.com/pratham-ahuja05/OS-Scheduling-Visualizer.git
cd OS-Scheduling-Visualizer
2. Install Java SDK and JavaFX

Install Java 25 SDK or later

Download JavaFX 25 from OpenJFX

3. Compile Java files
javac --module-path "C:\javafx-sdk-25.0.1\lib" --add-modules javafx.controls Main.java Process.java Scheduler.java CSVUtil.java

4. Run the application
java --module-path "C:\javafx-sdk-25.0.1\lib" --add-modules javafx.controls Main

📂 File Structure
OS-Scheduling-Visualizer/
│
├─ Main.java           # Main application & UI
├─ Process.java        # Process class definition
├─ Scheduler.java      # Scheduling algorithms & simulation
├─ CSVUtil.java        # CSV export/import functionality
├─ style.css           # UI styling
└─ README.md           # Project documentation

🛠 Technologies Used

Java 25

JavaFX 25

Timeline Animations & Gantt Chart Visualization

CSV Export/Import

📜 License

MIT License – see LICENSE
 file.

👨‍💻 Author

Pratham Ahuja

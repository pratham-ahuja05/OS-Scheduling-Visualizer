# OS Scheduling Visualizer (JavaFX)

![Java](https://img.shields.io/badge/Language-Java-orange?style=for-the-badge)
![JavaFX](https://img.shields.io/badge/GUI-JavaFX-blue?style=for-the-badge)

A real-time **CPU Scheduling Visualizer** built in Java using the JavaFX framework. This application simulates core operating system scheduling algorithms, providing interactive process management, a dynamic Gantt chart visualization, and real-time performance metrics.

---

## 📸 Screenshots

*(This is a great place to add a screenshot or GIF of your application in action!)*

`![Application-Screenshot](PATH_TO_YOUR_IMAGE.png)`

---

## ✨ Features

| Category | Features |
|:---|:---|
| **Scheduling Algorithms** | FCFS, SJF (Preemptive/Non-preemptive), Priority (Preemptive/Non-preemptive), Round Robin |
| **Process Management** | Add/Edit/Delete processes, Random process generator, Input: ID, Arrival Time, Burst Time, Priority |
| **Gantt Chart Visualization** | Color-coded bars, Running/Waiting/Preempted highlights, Real-time updates |
| **Interactive Simulation** | Play/Pause/Resume, Step-by-step execution, Speed slider, Ready & Waiting queue visualization |
| **Metrics & Statistics** | Average Waiting Time (AWT), Average Turnaround Time (ATAT), CPU Utilization, Throughput |
| **UI Enhancements** | Light/Dark theme toggle, Unique process colors, Tooltips on hover |
| **Export & Reporting** | Export Gantt chart as PNG, Save/Load process lists as CSV |

---

## 💻 Setup and Usage

Follow these steps to get the project up and running on your local machine.

### Prerequisites

* **Java SDK (JDK):** Version 25 or newer.
* **JavaFX SDK:** Version 25 or newer. You can download it from [OpenJFX](https://openjfx.io/).

### Installation & Running

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/pratham-ahuja05/OS-Scheduling-Visualizer.git](https://github.com/pratham-ahuja05/OS-Scheduling-Visualizer.git)
    cd OS-Scheduling-Visualizer
    ```

2.  **Set up your JavaFX path:**
    Unzip the downloaded JavaFX SDK and note the path to its `lib` directory. We'll refer to this as `$PATH_TO_JAVAFX_SDK`.

3.  **Compile the project:**
    (Replace `$PATH_TO_JAVAFX_SDK` with the actual path to your JavaFX `lib` folder, e.g., `"C:\javafx-sdk-25.0.1\lib"`)

    ```bash
    # This command compiles all .java files in the directory
    javac --module-path "$PATH_TO_JAVAFX_SDK" --add-modules javafx.controls *.java
    ```

4.  **Run the application:**
    (Again, replace `$PATH_TO_JAVAFX_SDK` with your path)

    ```bash
    java --module-path "$PATH_TO_JAVAFX_SDK" --add-modules javafx.controls Main
    ```

---

## 🚀 How to Use

1.  **Run the application** using the steps above.
2.  **Add Processes:**
    * Manually add processes by specifying **Arrival Time**, **Burst Time**, and **Priority** (if applicable).
    * Use the **"Randomize"** button to generate a set of test processes.
3.  **Select Algorithm:** Choose an algorithm from the dropdown (e.g., FCFS, SJF, Round Robin).
4.  **Run Simulation:**
    * Click **"Play"** to run the simulation in real-time.
    * Use the **slider** to control the simulation speed.
    * **"Step"** to execute one time unit at a time.
    * **"Pause"** and **"Resume"** to control the flow.
5.  **Analyze Results:**
    * Observe the **Gantt Chart** as it builds.
    * Check the **Ready & Waiting** queues to see process states.
    * Review the final **Metrics** (AWT, ATAT) once the simulation is complete.
6.  **Export:**
    * Save your process list as a **CSV** to load later.
    * Export the final Gantt chart as a **PNG** image.

---


📂 File Structure
OS-Scheduling-Visualizer/
│
├─ Main.java           # Main application & UI
├─ Process.java        # Process class definition
├─ Scheduler.java      # Scheduling algorithms & simulation
├─ CSVUtil.java        # CSV export/import functionality
├─ style.css           # UI styling
└─ README.md           # Project documentation
---

## 🤝 Contributing

Contributions are welcome! If you have suggestions for improvements or want to add more algorithms:

1.  Fork the repository.
2.  Create a new branch (`git checkout -b feature/NewAlgorithm`).
3.  Make your changes.
4.  Commit your changes (`git commit -m 'Add: NewAlgorithm'`).
5.  Push to the branch (`git push origin feature/NewAlgorithm`).
6.  Open a Pull Request.

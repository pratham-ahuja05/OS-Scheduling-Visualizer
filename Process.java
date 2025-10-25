public class Process {
    public int id;
    public int arrival;
    public int burst;
    public int priority;
    public int remaining; // for preemptive algorithms
    public int waiting;
    public int turnaround;
    public String colorHex;

    public Process(int id, int arrival, int burst, int priority) {
        this.id = id;
        this.arrival = arrival;
        this.burst = burst;
        this.priority = priority;
        this.remaining = burst;
        this.waiting = 0;
        this.turnaround = 0;
        this.colorHex = null;
    }

    public Process copy() {
        Process p = new Process(id, arrival, burst, priority);
        p.remaining = remaining;
        p.waiting = waiting;
        p.turnaround = turnaround;
        p.colorHex = colorHex;
        return p;
    }
}

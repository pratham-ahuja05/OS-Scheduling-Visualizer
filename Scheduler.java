import java.util.*;
import java.util.stream.Collectors;

public class Scheduler {

    public enum Algo {
        FCFS, SJF_NONPREEMPTIVE, SJF_PREEMPTIVE,
        PRIORITY_NONPREEMPTIVE, PRIORITY_PREEMPTIVE, ROUND_ROBIN
    }

    public static class Slice {
        public int pid;
        public int start;
        public int duration;
        public boolean preempted;
        public Slice(int pid, int start, int duration, boolean preempted) {
            this.pid = pid;
            this.start = start;
            this.duration = duration;
            this.preempted = preempted;
        }
    }

    public static List<Slice> simulate(List<Process> processes, Algo algo, int quantum) {
        // Reset all process times
        for (Process p : processes) p.remaining = p.burst;

        List<Slice> slices = new ArrayList<>();
        List<Process> remaining = new ArrayList<>(processes);

        int time = 0;
        Queue<Process> queue = new LinkedList<>();

        switch (algo) {
            case FCFS:
                remaining.sort(Comparator.comparingInt(p -> p.arrival));
                while (!remaining.isEmpty()) {
                    final int currentTime = time;
                    List<Process> arrived = remaining.stream()
                            .filter(p -> p.arrival <= currentTime)
                            .collect(Collectors.toList());
                    if (arrived.isEmpty()) {
                        time++;
                        continue;
                    }
                    Process p = arrived.get(0);
                    slices.add(new Slice(p.id, time, p.burst, false));
                    time += p.burst;
                    p.waiting = time - p.arrival - p.burst;
                    p.turnaround = time - p.arrival;
                    remaining.remove(p);
                }
                break;

            case ROUND_ROBIN:
                remaining.sort(Comparator.comparingInt(p -> p.arrival));
                while (!remaining.isEmpty()) {
                    final int currentTime = time;
                    remaining.stream()
                            .filter(p -> p.arrival <= currentTime && !queue.contains(p) && p.remaining > 0)
                            .forEach(queue::add);
                    if (queue.isEmpty()) {
                        time++;
                        continue;
                    }
                    Process p = queue.poll();
                    int exec = Math.min(quantum, p.remaining);
                    p.remaining -= exec;
                    slices.add(new Slice(p.id, time, exec, p.remaining > 0));
                    time += exec;

                    // Add newly arrived processes
                    final int tm = time;
                    remaining.stream()
                            .filter(pr -> pr.arrival <= tm && !queue.contains(pr) && pr.remaining > 0)
                            .forEach(queue::add);

                    if (p.remaining > 0) queue.add(p);
                    else {
                        p.turnaround = time - p.arrival;
                        p.waiting = p.turnaround - p.burst;
                        remaining.remove(p);
                    }
                }
                break;

            // Other algorithms (SJF, Priority) can be implemented similarly:
            case SJF_NONPREEMPTIVE:
                while (!remaining.isEmpty()) {
                    final int ct = time;
                    List<Process> avail = remaining.stream()
                            .filter(p -> p.arrival <= ct)
                            .collect(Collectors.toList());
                    if (avail.isEmpty()) { time++; continue; }
                    avail.sort(Comparator.comparingInt(p -> p.burst));
                    Process p = avail.get(0);
                    slices.add(new Slice(p.id, time, p.burst, false));
                    time += p.burst;
                    p.waiting = time - p.arrival - p.burst;
                    p.turnaround = time - p.arrival;
                    remaining.remove(p);
                }
                break;

            case SJF_PREEMPTIVE:
                while (!remaining.isEmpty()) {
                    final int ct = time;
                    List<Process> avail = remaining.stream()
                            .filter(p -> p.arrival <= ct && p.remaining > 0)
                            .collect(Collectors.toList());
                    if (avail.isEmpty()) { time++; continue; }
                    avail.sort(Comparator.comparingInt(p -> p.remaining));
                    Process p = avail.get(0);
                    slices.add(new Slice(p.id, time, 1, p.remaining > 1));
                    p.remaining--;
                    time++;
                    if (p.remaining == 0) {
                        p.turnaround = time - p.arrival;
                        p.waiting = p.turnaround - p.burst;
                        remaining.remove(p);
                    }
                }
                break;

            case PRIORITY_NONPREEMPTIVE:
                while (!remaining.isEmpty()) {
                    final int ct = time;
                    List<Process> avail = remaining.stream()
                            .filter(p -> p.arrival <= ct)
                            .collect(Collectors.toList());
                    if (avail.isEmpty()) { time++; continue; }
                    avail.sort(Comparator.comparingInt(p -> p.priority));
                    Process p = avail.get(0);
                    slices.add(new Slice(p.id, time, p.burst, false));
                    time += p.burst;
                    p.waiting = time - p.arrival - p.burst;
                    p.turnaround = time - p.arrival;
                    remaining.remove(p);
                }
                break;

            case PRIORITY_PREEMPTIVE:
                while (!remaining.isEmpty()) {
                    final int ct = time;
                    List<Process> avail = remaining.stream()
                            .filter(p -> p.arrival <= ct && p.remaining > 0)
                            .collect(Collectors.toList());
                    if (avail.isEmpty()) { time++; continue; }
                    avail.sort(Comparator.comparingInt(p -> p.priority));
                    Process p = avail.get(0);
                    slices.add(new Slice(p.id, time, 1, p.remaining > 1));
                    p.remaining--;
                    time++;
                    if (p.remaining == 0) {
                        p.turnaround = time - p.arrival;
                        p.waiting = p.turnaround - p.burst;
                        remaining.remove(p);
                    }
                }
                break;
        }

        return slices;
    }
}

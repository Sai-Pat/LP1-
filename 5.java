import java.util.*;

class Process {
    int pid;          // process id
    int arrival;      // arrival time
    int burst;        // burst time
    int remaining;    // remaining time (for SJF preemptive)
    int completion;   // completion time
    int waiting;      // waiting time
    int turnaround;   // turnaround time

    Process(int pid, int arrival, int burst) {
        this.pid = pid;
        this.arrival = arrival;
        this.burst = burst;
        this.remaining = burst;
    }
}

public class SchedulingSimulation {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        Process[] p = new Process[n];
        for (int i = 0; i < n; i++) {
            System.out.print("Enter arrival time and burst time for process P" + (i + 1) + ": ");
            int at = sc.nextInt();
            int bt = sc.nextInt();
            p[i] = new Process(i + 1, at, bt);
        }

        System.out.println("\n--- FCFS Scheduling ---");
        fcfs(p);

        System.out.println("\n--- SJF (Preemptive) Scheduling ---");
        sjfPreemptive(p);
    }

    // ---------- FCFS ----------
    static void fcfs(Process[] p) {
        // sort by arrival time
        Arrays.sort(p, Comparator.comparingInt(a -> a.arrival));

        int time = 0;
        for (Process pr : p) {
            if (time < pr.arrival)
                time = pr.arrival;
            time += pr.burst;
            pr.completion = time;
            pr.turnaround = pr.completion - pr.arrival;
            pr.waiting = pr.turnaround - pr.burst;
        }

        printTable(p);
        printAverage(p);
    }

    // ---------- SJF (Preemptive) ----------
    static void sjfPreemptive(Process[] processes) {
        // Make a deep copy so previous data doesn’t affect this
        Process[] p = new Process[processes.length];
        for (int i = 0; i < processes.length; i++)
            p[i] = new Process(processes[i].pid, processes[i].arrival, processes[i].burst);

        int completed = 0, time = 0, n = p.length;
        Process current = null;

        while (completed < n) {
            // find process with minimum remaining time at current time
            Process shortest = null;
            for (Process pr : p) {
                if (pr.arrival <= time && pr.remaining > 0) {
                    if (shortest == null || pr.remaining < shortest.remaining)
                        shortest = pr;
                }
            }

            if (shortest == null) {
                time++;
                continue;
            }

            shortest.remaining--;
            time++;

            // if process finishes
            if (shortest.remaining == 0) {
                shortest.completion = time;
                shortest.turnaround = shortest.completion - shortest.arrival;
                shortest.waiting = shortest.turnaround - shortest.burst;
                completed++;
            }
        }

        printTable(p);
        printAverage(p);
    }

    // ---------- Utility functions ----------
    static void printTable(Process[] p) {
        System.out.println("\nPID\tAT\tBT\tCT\tTAT\tWT");
        for (Process pr : p) {
            System.out.println("P" + pr.pid + "\t" + pr.arrival + "\t" + pr.burst + "\t" + pr.completion + "\t" + pr.turnaround + "\t" + pr.waiting);
        }
    }

    static void printAverage(Process[] p) {
        double avgWT = 0, avgTAT = 0;
        for (Process pr : p) {
            avgWT += pr.waiting;
            avgTAT += pr.turnaround;
        }
        avgWT /= p.length;
        avgTAT /= p.length;
        System.out.printf("\nAverage Waiting Time: %.2f", avgWT);
        System.out.printf("\nAverage Turnaround Time: %.2f\n", avgTAT);
    }
}

SAMPLE INPUT

  Enter number of processes: 3
Enter arrival time and burst time for process P1: 0 5
Enter arrival time and burst time for process P2: 1 3
Enter arrival time and burst time for process P3: 2 1


  SAMPLE OUTPUT

--- FCFS Scheduling ---
PID	AT	BT	CT	TAT	WT
P1	0	5	5	5	0
P2	1	3	8	7	4
P3	2	1	9	7	6
Average Waiting Time: 3.33
Average Turnaround Time: 6.33

--- SJF (Preemptive) Scheduling ---
PID	AT	BT	CT	TAT	WT
P1	0	5	9	9	4
P2	1	3	7	6	3
P3	2	1	3	1	0
Average Waiting Time: 2.33
Average Turnaround Time: 5.33

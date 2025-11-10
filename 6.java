import java.util.*;

class Process {
    int pid;          // process ID
    int arrival;      // arrival time
    int burst;        // burst time
    int priority;     // priority (for Priority Scheduling)
    int completion;   // completion time
    int turnaround;   // turnaround time
    int waiting;      // waiting time

    Process(int pid, int arrival, int burst, int priority) {
        this.pid = pid;
        this.arrival = arrival;
        this.burst = burst;
        this.priority = priority;
    }
}

public class FCFS_PriorityScheduling {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        Process[] p = new Process[n];

        for (int i = 0; i < n; i++) {
            System.out.print("Enter Arrival Time, Burst Time and Priority for P" + (i + 1) + ": ");
            int at = sc.nextInt();
            int bt = sc.nextInt();
            int pr = sc.nextInt();
            p[i] = new Process(i + 1, at, bt, pr);
        }

        System.out.println("\n--- FCFS Scheduling ---");
        fcfs(p);

        System.out.println("\n--- Priority Scheduling ---");
        priorityScheduling(p);
    }

    // ---------- FCFS ----------
    static void fcfs(Process[] processes) {
        // Create copy so that previous results don't affect Priority scheduling
        Process[] p = Arrays.stream(processes)
                .map(pr -> new Process(pr.pid, pr.arrival, pr.burst, pr.priority))
                .toArray(Process[]::new);

        Arrays.sort(p, Comparator.comparingInt(a -> a.arrival)); // sort by arrival time
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

    // ---------- Priority Scheduling (Non-preemptive) ----------
    static void priorityScheduling(Process[] processes) {
        // Make a fresh copy
        Process[] p = Arrays.stream(processes)
                .map(pr -> new Process(pr.pid, pr.arrival, pr.burst, pr.priority))
                .toArray(Process[]::new);

        int completed = 0, time = 0, n = p.length;
        boolean[] done = new boolean[n];

        while (completed < n) {
            // Find process with highest priority that has arrived
            Process selected = null;
            int index = -1;

            for (int i = 0; i < n; i++) {
                if (!done[i] && p[i].arrival <= time) {
                    if (selected == null || p[i].priority < selected.priority) {
                        selected = p[i];
                        index = i;
                    }
                }
            }

            if (selected == null) {
                time++; // No process arrived yet
                continue;
            }

            time += selected.burst;
            selected.completion = time;
            selected.turnaround = selected.completion - selected.arrival;
            selected.waiting = selected.turnaround - selected.burst;
            done[index] = true;
            completed++;
        }

        printTable(p);
        printAverage(p);
    }

    // ---------- Helper Functions ----------
    static void printTable(Process[] p) {
        System.out.println("\nPID\tAT\tBT\tPR\tCT\tTAT\tWT");
        for (Process pr : p) {
            System.out.println("P" + pr.pid + "\t" + pr.arrival + "\t" + pr.burst + "\t" + pr.priority + "\t" +
                    pr.completion + "\t" + pr.turnaround + "\t" + pr.waiting);
        }
    }

    static void printAverage(Process[] p) {
        double totalWT = 0, totalTAT = 0;
        for (Process pr : p) {
            totalWT += pr.waiting;
            totalTAT += pr.turnaround;
        }
        System.out.printf("\nAverage Waiting Time: %.2f", totalWT / p.length);
        System.out.printf("\nAverage Turnaround Time: %.2f\n", totalTAT / p.length);
    }
}

SAMPLE INPUT

  Enter number of processes: 3
Enter Arrival Time, Burst Time and Priority for P1: 0 5 2
Enter Arrival Time, Burst Time and Priority for P2: 1 3 1
Enter Arrival Time, Burst Time and Priority for P3: 2 8 3


  SAMPLE OUTPUT
  --- FCFS Scheduling ---
PID	AT	BT	PR	CT	TAT	WT
P1	0	5	2	5	5	0
P2	1	3	1	8	7	4
P3	2	8	3	16	14	6
Average Waiting Time: 3.33
Average Turnaround Time: 8.67

--- Priority Scheduling ---
PID	AT	BT	PR	CT	TAT	WT
P1	0	5	2	8	8	3
P2	1	3	1	4	3	0
P3	2	8	3	16	14	6
Average Waiting Time: 3.00
Average Turnaround Time: 8.33

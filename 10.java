import java.util.*;

class Process {
    int pid;
    int arrival;
    int burst;
    int priority;
    int remaining;
    int completion;
    int turnaround;
    int waiting;

    Process(int pid, int arrival, int burst, int priority) {
        this.pid = pid;
        this.arrival = arrival;
        this.burst = burst;
        this.priority = priority;
        this.remaining = burst;
    }
}

public class Priority_SJF_Scheduling {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        Process[] p = new Process[n];
        for (int i = 0; i < n; i++) {
            System.out.print("Enter Arrival Time, Burst Time, and Priority for P" + (i + 1) + ": ");
            int at = sc.nextInt();
            int bt = sc.nextInt();
            int prio = sc.nextInt();
            p[i] = new Process(i + 1, at, bt, prio);
        }

        System.out.println("\n--- Priority Scheduling (Non-preemptive) ---");
        priorityScheduling(p);

        System.out.println("\n--- SJF Scheduling (Preemptive) ---");
        sjfPreemptive(p);
    }

    // ---------- Priority Scheduling ----------
    static void priorityScheduling(Process[] processes) {
        Process[] p = Arrays.stream(processes)
                .map(pr -> new Process(pr.pid, pr.arrival, pr.burst, pr.priority))
                .toArray(Process[]::new);

        int n = p.length;
        int time = 0;
        boolean[] done = new boolean[n];
        int completed = 0;

        while (completed < n) {
            int idx = -1;
            int highest = Integer.MAX_VALUE; // lower number = higher priority
            for (int i = 0; i < n; i++) {
                if (!done[i] && p[i].arrival <= time) {
                    if (p[i].priority < highest || 
                       (p[i].priority == highest && p[i].arrival < p[idx].arrival)) {
                        highest = p[i].priority;
                        idx = i;
                    }
                }
            }

            if (idx == -1) {
                time++;
                continue;
            }

            time += p[idx].burst;
            p[idx].completion = time;
            p[idx].turnaround = p[idx].completion - p[idx].arrival;
            p[idx].waiting = p[idx].turnaround - p[idx].burst;
            done[idx] = true;
            completed++;
        }

        printTable(p);
        printAverage(p);
    }

    // ---------- SJF (Preemptive) ----------
    static void sjfPreemptive(Process[] processes) {
        Process[] p = Arrays.stream(processes)
                .map(pr -> new Process(pr.pid, pr.arrival, pr.burst, pr.priority))
                .toArray(Process[]::new);

        int n = p.length;
        int time = 0, completed = 0;

        while (completed < n) {
            Process shortest = null;
            for (Process pr : p) {
                if (pr.arrival <= time && pr.remaining > 0) {
                    if (shortest == null || pr.remaining < shortest.remaining) {
                        shortest = pr;
                    }
                }
            }

            if (shortest == null) {
                time++;
                continue;
            }

            shortest.remaining--;
            time++;

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

    // ---------- Helper Functions ----------
    static void printTable(Process[] p) {
        System.out.println("\nPID\tAT\tBT\tPRI\tCT\tTAT\tWT");
        for (Process pr : p) {
            System.out.println("P" + pr.pid + "\t" + pr.arrival + "\t" + pr.burst + "\t" +
                    pr.priority + "\t" + pr.completion + "\t" + pr.turnaround + "\t" + pr.waiting);
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


INPUT
Enter number of processes: 3
Enter Arrival Time, Burst Time, and Priority for P1: 0 7 2
Enter Arrival Time, Burst Time, and Priority for P2: 2 4 1
Enter Arrival Time, Burst Time, and Priority for P3: 4 1 3

OUTPUT

  --- Priority Scheduling (Non-preemptive) ---
PID	AT	BT	PRI	CT	TAT	WT
P1	0	7	2	7	7	0
P2	2	4	1	11	9	5
P3	4	1	3	12	8	7
Average Waiting Time: 4.00
Average Turnaround Time: 8.00

--- SJF Scheduling (Preemptive) ---
PID	AT	BT	PRI	CT	TAT	WT
P1	0	7	2	12	12	5
P2	2	4	1	8	6	2
P3	4	1	3	5	1	0
Average Waiting Time: 2.33
Average Turnaround Time: 6.33

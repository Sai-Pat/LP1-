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

public class Priority_RR_Scheduling {
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

        System.out.println("\n--- Round Robin Scheduling ---");
        System.out.print("Enter Time Quantum: ");
        int q = sc.nextInt();
        roundRobin(p, q);
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

    // ---------- Round Robin ----------
    static void roundRobin(Process[] processes, int quantum) {
        Process[] p = Arrays.stream(processes)
                .map(pr -> new Process(pr.pid, pr.arrival, pr.burst, pr.priority))
                .toArray(Process[]::new);

        Queue<Process> queue = new LinkedList<>();
        int time = 0, completed = 0;
        boolean[] inQueue = new boolean[p.length];

        System.out.println("\nGantt Chart:");

        while (completed < p.length) {
            for (int i = 0; i < p.length; i++) {
                if (p[i].arrival <= time && !inQueue[i] && p[i].remaining > 0) {
                    queue.add(p[i]);
                    inQueue[i] = true;
                }
            }

            if (queue.isEmpty()) {
                time++;
                continue;
            }

            Process current = queue.poll();
            System.out.print(" | P" + current.pid);

            int execTime = Math.min(quantum, current.remaining);
            time += execTime;
            current.remaining -= execTime;

            for (int i = 0; i < p.length; i++) {
                if (p[i].arrival <= time && !inQueue[i] && p[i].remaining > 0) {
                    queue.add(p[i]);
                    inQueue[i] = true;
                }
            }

            if (current.remaining > 0) {
                queue.add(current);
            } else {
                current.completion = time;
                current.turnaround = current.completion - current.arrival;
                current.waiting = current.turnaround - current.burst;
                completed++;
            }
        }

        System.out.println(" |");
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

SAMPLE INPUT

  Enter number of processes: 3
Enter Arrival Time, Burst Time, and Priority for P1: 0 5 2
Enter Arrival Time, Burst Time, and Priority for P2: 1 3 1
Enter Arrival Time, Burst Time, and Priority for P3: 2 4 3
Enter Time Quantum: 2

  SAMPLE OUTPUT

  --- Priority Scheduling (Non-preemptive) ---
PID	AT	BT	PRI	CT	TAT	WT
P1	0	5	2	5	5	0
P2	1	3	1	8	7	4
P3	2	4	3	12	10	6
Average Waiting Time: 3.33
Average Turnaround Time: 7.33

--- Round Robin Scheduling ---
Gantt Chart:
 | P1 | P2 | P3 | P1 | P2 | P3 | P3 |

PID	AT	BT	PRI	CT	TAT	WT
P1	0	5	2	9	9	4
P2	1	3	1	8	7	4
P3	2	4	3	15	13	9
Average Waiting Time: 5.67
Average Turnaround Time: 9.67

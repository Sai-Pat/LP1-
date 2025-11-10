import java.util.*;

class Process {
    int pid;          // process id
    int arrival;      // arrival time
    int burst;        // burst time
    int remaining;    // remaining time (for RR)
    int completion;   // completion time
    int turnaround;   // turnaround time
    int waiting;      // waiting time

    Process(int pid, int arrival, int burst) {
        this.pid = pid;
        this.arrival = arrival;
        this.burst = burst;
        this.remaining = burst;
    }
}

public class FCFS_RR_Scheduling {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        Process[] p = new Process[n];
        for (int i = 0; i < n; i++) {
            System.out.print("Enter Arrival Time and Burst Time for P" + (i + 1) + ": ");
            int at = sc.nextInt();
            int bt = sc.nextInt();
            p[i] = new Process(i + 1, at, bt);
        }

        System.out.println("\n--- FCFS Scheduling ---");
        fcfs(p);

        System.out.println("\n--- Round Robin Scheduling ---");
        System.out.print("Enter Time Quantum: ");
        int q = sc.nextInt();
        roundRobin(p, q);
    }

    // ---------- FCFS ----------
    static void fcfs(Process[] processes) {
        // Create copy to avoid interference
        Process[] p = Arrays.stream(processes)
                .map(pr -> new Process(pr.pid, pr.arrival, pr.burst))
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

    // ---------- ROUND ROBIN ----------
    static void roundRobin(Process[] processes, int quantum) {
        // Fresh copy
        Process[] p = Arrays.stream(processes)
                .map(pr -> new Process(pr.pid, pr.arrival, pr.burst))
                .toArray(Process[]::new);

        Queue<Process> queue = new LinkedList<>();
        int time = 0, completed = 0;
        boolean[] inQueue = new boolean[p.length];

        System.out.println("\nGantt Chart:");

        while (completed < p.length) {
            // Add processes that have arrived by this time
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

            // Add newly arrived processes during this time
            for (int i = 0; i < p.length; i++) {
                if (p[i].arrival <= time && !inQueue[i] && p[i].remaining > 0) {
                    queue.add(p[i]);
                    inQueue[i] = true;
                }
            }

            if (current.remaining > 0) {
                queue.add(current); // put back to queue
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
        System.out.println("\nPID\tAT\tBT\tCT\tTAT\tWT");
        for (Process pr : p) {
            System.out.println("P" + pr.pid + "\t" + pr.arrival + "\t" + pr.burst + "\t" +
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
Enter Arrival Time and Burst Time for P1: 0 5
Enter Arrival Time and Burst Time for P2: 1 3
Enter Arrival Time and Burst Time for P3: 2 6

--- FCFS Scheduling ---
--- Round Robin Scheduling ---
Enter Time Quantum: 2


SAMPLE OUTPUT

  --- FCFS Scheduling ---
PID	AT	BT	CT	TAT	WT
P1	0	5	5	5	0
P2	1	3	8	7	4
P3	2	6	14	12	6
Average Waiting Time: 3.33
Average Turnaround Time: 8.00

--- Round Robin Scheduling ---
Gantt Chart:
 | P1 | P2 | P3 | P1 | P2 | P3 | P3 |

PID	AT	BT	CT	TAT	WT
P1	0	5	9	9	4
P2	1	3	8	7	4
P3	2	6	15	13	7
Average Waiting Time: 5.00
Average Turnaround Time: 9.67

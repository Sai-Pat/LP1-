import java.util.*;

class Process {
    int pid;
    int arrival;
    int burst;
    int remaining;
    int completion;
    int turnaround;
    int waiting;

    Process(int pid, int arrival, int burst) {
        this.pid = pid;
        this.arrival = arrival;
        this.burst = burst;
        this.remaining = burst;
    }
}

public class SJF_RR_Scheduling {
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

        System.out.println("\n--- SJF (Preemptive) Scheduling ---");
        sjfPreemptive(p);

        System.out.println("\n--- Round Robin Scheduling ---");
        System.out.print("Enter Time Quantum: ");
        int q = sc.nextInt();
        roundRobin(p, q);
    }

    // ---------- SJF (Preemptive) ----------
    static void sjfPreemptive(Process[] processes) {
        Process[] p = Arrays.stream(processes)
                .map(pr -> new Process(pr.pid, pr.arrival, pr.burst))
                .toArray(Process[]::new);

        int time = 0, completed = 0, n = p.length;

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

    // ---------- Round Robin ----------
    static void roundRobin(Process[] processes, int quantum) {
        Process[] p = Arrays.stream(processes)
                .map(pr -> new Process(pr.pid, pr.arrival, pr.burst))
                .toArray(Process[]::new);

        Queue<Process> queue = new LinkedList<>();
        int time = 0, completed = 0;
        boolean[] inQueue = new boolean[p.length];

        System.out.println("\nGantt Chart:");

        while (completed < p.length) {
            // Add newly arrived processes
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

            // Add newly arrived processes during execution
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
Enter Arrival Time and Burst Time for P1: 0 7
Enter Arrival Time and Burst Time for P2: 2 4
Enter Arrival Time and Burst Time for P3: 4 1
Enter Time Quantum: 2


SAMPLW OUTPUT

--- SJF (Preemptive) Scheduling ---
PID	AT	BT	CT	TAT	WT
P1	0	7	12	12	5
P2	2	4	8	6	2
P3	4	1	5	1	0
Average Waiting Time: 2.33
Average Turnaround Time: 6.33

--- Round Robin Scheduling ---
Gantt Chart:
 | P1 | P2 | P3 | P1 | P2 | P1 |

PID	AT	BT	CT	TAT	WT
P1	0	7	13	13	6
P2	2	4	10	8	4
P3	4	1	5	1	0
Average Waiting Time: 3.33
Average Turnaround Time: 7.33

  

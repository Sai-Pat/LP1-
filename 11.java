import java.util.*;

public class MemoryAllocation {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of memory blocks: ");
        int m = sc.nextInt();
        int[] memory = new int[m];
        System.out.println("Enter sizes of memory blocks: ");
        for (int i = 0; i < m; i++) memory[i] = sc.nextInt();

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        int[] processes = new int[n];
        System.out.println("Enter sizes of processes: ");
        for (int i = 0; i < n; i++) processes[i] = sc.nextInt();

        System.out.println("\n--- First Fit Allocation ---");
        firstFit(memory, processes);

        System.out.println("\n--- Best Fit Allocation ---");
        bestFit(memory, processes);
    }

    // ---------- First Fit ----------
    static void firstFit(int[] memoryBlocks, int[] processes) {
        int m = memoryBlocks.length;
        int n = processes.length;
        int[] block = Arrays.copyOf(memoryBlocks, m);
        int[] allocation = new int[n];
        Arrays.fill(allocation, -1);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (block[j] >= processes[i]) {
                    allocation[i] = j;
                    block[j] -= processes[i];
                    break;
                }
            }
        }

        printAllocation(allocation);
    }

    // ---------- Best Fit ----------
    static void bestFit(int[] memoryBlocks, int[] processes) {
        int m = memoryBlocks.length;
        int n = processes.length;
        int[] block = Arrays.copyOf(memoryBlocks, m);
        int[] allocation = new int[n];
        Arrays.fill(allocation, -1);

        for (int i = 0; i < n; i++) {
            int bestIdx = -1;
            for (int j = 0; j < m; j++) {
                if (block[j] >= processes[i]) {
                    if (bestIdx == -1 || block[j] < block[bestIdx]) {
                        bestIdx = j;
                    }
                }
            }
            if (bestIdx != -1) {
                allocation[i] = bestIdx;
                block[bestIdx] -= processes[i];
            }
        }

        printAllocation(allocation);
    }

    // ---------- Helper Function ----------
    static void printAllocation(int[] allocation) {
        System.out.println("Process No\tBlock No");
        for (int i = 0; i < allocation.length; i++) {
            if (allocation[i] != -1)
                System.out.println("P" + (i+1) + "\t\t" + (allocation[i]+1));
            else
                System.out.println("P" + (i+1) + "\t\tNot Allocated");
        }
    }
}


INPUT

  Enter number of memory blocks: 5
Enter sizes of memory blocks: 100 500 200 300 600
Enter number of processes: 4
Enter sizes of processes: 212 417 112 426


  OUTPUT

  --- First Fit Allocation ---
Process No	Block No
P1		2
P2		5
P3		2
P4		Not Allocated

--- Best Fit Allocation ---
Process No	Block No
P1		4
P2		5
P3		3
P4		Not Allocated

import java.util.*;

public class LRUPageReplacement {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of pages: ");
        int n = sc.nextInt();
        int[] pages = new int[n];
        System.out.println("Enter page reference sequence: ");
        for (int i = 0; i < n; i++) pages[i] = sc.nextInt();

        System.out.print("Enter number of frames: ");
        int framesCount = sc.nextInt();

        lruPageReplacement(pages, framesCount);
    }

    static void lruPageReplacement(int[] pages, int framesCount) {
        int[] frames = new int[framesCount];
        int[] lastUsed = new int[framesCount]; // stores last used time
        Arrays.fill(frames, -1); // -1 means empty frame
        Arrays.fill(lastUsed, -1);
        int time = 0;
        int pageFaults = 0;

        System.out.println("\nPage Reference\tFrames");
        for (int page : pages) {
            time++;
            boolean hit = false;

            // Check if page is already in frames
            for (int i = 0; i < framesCount; i++) {
                if (frames[i] == page) {
                    hit = true;
                    lastUsed[i] = time; // update last used time
                    break;
                }
            }

            if (!hit) {
                // Find the LRU page (least recently used)
                int lruIndex = 0;
                for (int i = 0; i < framesCount; i++) {
                    if (frames[i] == -1) { // empty frame
                        lruIndex = i;
                        break;
                    } else if (lastUsed[i] < lastUsed[lruIndex]) {
                        lruIndex = i;
                    }
                }

                frames[lruIndex] = page;
                lastUsed[lruIndex] = time;
                pageFaults++;
            }

            // Print current frames
            System.out.print(page + "\t\t");
            for (int f : frames) {
                if (f != -1) System.out.print(f + " ");
                else System.out.print("- ");
            }
            System.out.println(hit ? "(Hit)" : "(Fault)");
        }

        System.out.println("\nTotal Page Faults: " + pageFaults);
    }
}

INPUT

Enter number of pages: 12
Enter page reference sequence: 1 2 3 4 1 2 5 1 2 3 4 5
Enter number of frames: 3

OUTPUT

  Page Reference	Frames
1		1 - - (Fault)
2		1 2 - (Fault)
3		1 2 3 (Fault)
4		4 2 3 (Fault)
1		4 1 3 (Fault)
2		2 1 3 (Fault)
5		5 2 3 (Fault)
1		1 5 3 (Fault)
2		2 1 5 (Fault)
3		3 2 1 (Fault)
4		4 3 2 (Fault)
5		5 4 3 (Fault)

Total Page Faults: 12

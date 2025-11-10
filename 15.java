import java.util.*;

public class FIFOPageReplacement {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of pages: ");
        int n = sc.nextInt();
        int[] pages = new int[n];
        System.out.println("Enter page reference sequence: ");
        for (int i = 0; i < n; i++) pages[i] = sc.nextInt();

        System.out.print("Enter number of frames: ");
        int framesCount = sc.nextInt();

        fifoPageReplacement(pages, framesCount);
    }

    static void fifoPageReplacement(int[] pages, int framesCount) {
        int[] frames = new int[framesCount];
        Arrays.fill(frames, -1); // -1 means empty frame
        int pointer = 0; // points to the oldest page
        int pageFaults = 0;

        System.out.println("\nPage Reference\tFrames");
        for (int page : pages) {
            boolean hit = false;

            // Check if page is already in frames
            for (int i = 0; i < framesCount; i++) {
                if (frames[i] == page) {
                    hit = true;
                    break;
                }
            }

            if (!hit) {
                frames[pointer] = page; // replace the oldest page
                pointer = (pointer + 1) % framesCount; // move pointer circularly
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
2		4 1 2 (Fault)
5		5 1 2 (Fault)
1		5 1 2 (Hit)
2		5 1 2 (Hit)
3		3 1 2 (Fault)
4		4 3 2 (Fault)
5		5 3 4 (Fault)

Total Page Faults: 9

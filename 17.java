import java.util.*;

public class OptimalPageReplacement {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of pages: ");
        int n = sc.nextInt();
        int[] pages = new int[n];
        System.out.println("Enter page reference sequence: ");
        for (int i = 0; i < n; i++) pages[i] = sc.nextInt();

        System.out.print("Enter number of frames: ");
        int framesCount = sc.nextInt();

        optimalPageReplacement(pages, framesCount);
    }

    static void optimalPageReplacement(int[] pages, int framesCount) {
        int[] frames = new int[framesCount];
        Arrays.fill(frames, -1); // -1 means empty frame
        int pageFaults = 0;

        System.out.println("\nPage Reference\tFrames");
        for (int i = 0; i < pages.length; i++) {
            int page = pages[i];
            boolean hit = false;

            // Check if page is already in frames
            for (int j = 0; j < framesCount; j++) {
                if (frames[j] == page) {
                    hit = true;
                    break;
                }
            }

            if (!hit) {
                int replaceIndex = -1;

                // If empty frame exists, use it
                for (int j = 0; j < framesCount; j++) {
                    if (frames[j] == -1) {
                        replaceIndex = j;
                        break;
                    }
                }

                // If no empty frame, find the optimal page to replace
                if (replaceIndex == -1) {
                    int farthest = i + 1;
                    int indexToReplace = 0;
                    for (int j = 0; j < framesCount; j++) {
                        int k;
                        for (k = i + 1; k < pages.length; k++) {
                            if (frames[j] == pages[k]) {
                                if (k > farthest) {
                                    farthest = k;
                                    indexToReplace = j;
                                }
                                break;
                            }
                        }
                        if (k == pages.length) { // not found in future
                            indexToReplace = j;
                            break;
                        }
                    }
                    replaceIndex = indexToReplace;
                }

                frames[replaceIndex] = page;
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
2		2 1 4 (Fault)
5		5 2 4 (Fault)
1		1 5 4 (Fault)
2		2 1 5 (Fault)
3		3 2 5 (Fault)
4		4 3 5 (Fault)
5		5 4 3 (Fault)

Total Page Faults: 9

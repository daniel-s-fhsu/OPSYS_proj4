import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class project4 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int sizeOfVM, lengthOfRS, sizeOfLocus, rateOfMotion, numOfFrame;
        double prob;
        char choice;

        do {
            System.out.print("Enter size of virtual memory: ");
            sizeOfVM = input.nextInt();
            System.out.print("Enter length of reference string: ");
            lengthOfRS = input.nextInt();
            System.out.print("Enter size of locus: ");
            sizeOfLocus = input.nextInt();
            System.out.print("Enter rate of motion: ");
            rateOfMotion = input.nextInt();
            System.out.print("Enter probability of transition: ");
            prob = input.nextDouble();
            System.out.println("Enter number of frames: ");
            numOfFrame = input.nextInt();

            ArrayList<Integer> rs = createRS(sizeOfVM, lengthOfRS, sizeOfLocus, rateOfMotion, prob);
            System.out.println("Page faults using FIFO replacement algorithm: ");
            System.out.println(FIFOReplacement(rs, numOfFrame) + " times.");
            System.out.println("Page faults using LRU replacement algorithm: ");
            System.out.println(LRUReplacement(rs, numOfFrame) + " times.");
            System.out.print("Do you want to have another test? ");
            choice = input.nextLine().toUpperCase().charAt(0);
        } while (choice == 'Y');
    }

    public static int isInArray(int[] frames, int page) {
        // Return the index if page is in frames array,
        // otherwise return -1
        for(int i=0; i<frames.length; i++) {
            if(frames[i] == page) return i;
        }
        return -1;
    }

    public static ArrayList<Integer> createRS(int sizeOfVM, int length,
                                int sizeOfLocus, int rateOfMotion, double prob) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        int start = 0;
        int n; // one page number in reference string

        while(result.size() < length) {
            for (int i = 0; i < rateOfMotion; i++) {
                n = (int) (Math.random() * sizeOfLocus + start);
                result.add(n);
            }

            if (Math.random() < prob) {
                start = (int) Math.random() * sizeOfVM;
            } else {
                start = (start + 1) % sizeOfVM;
            }
        }
        return result;
    }

    public static int FIFOReplacement(ArrayList<Integer> rs, int numOfFrames) {
        // init frames
        int[] frames = new int[numOfFrames];
        int i, oldest = 0, count = 0;   //index of oldest and count of faults

        for(i = 0; i < frames.length; i++) {
            frames[i] = -1;  // no page loaded
        }

        for(i = 0; i < rs.size(); i++) {
            // Check for page fault
            if(isInArray(frames, rs.get(i)) == -1) { 
                frames[oldest] = rs.get(i);
                count++;
                oldest = (oldest + 1) % (frames.length);
            }
        }
        return count;
    }

    public static int LRUReplacement(ArrayList<Integer> rs, int numOfFrames) {
        int[] frames = new int[numOfFrames];
        int i, j, first = 0, count =0;
        
        for (i=0; i < numOfFrames; i++) frames[i] = -1;
        for (i=0; i < rs.size(); i++) {
            int index = isInArray(frames, rs.get(i));
            int least;
            if (index == -1) {
                least = rs.get(i);
                count++;
                index = 0;  // the first element will be removed
            } else {
                least = frames[index]; // this should be moved to the end
            }
            for (j = index; j < frames.length - 1; j++)
                frames[j] = frames[j+1];
            frames[j] = least;
        }
        return count;
    }

    public static int[] test() {
        int[] result = new int[2];
        ArrayList<Integer> rs = new ArrayList<Integer>(Arrays.asList(0,1,4,0,2,3,0,1,0,2));
        result[0] = FIFOReplacement(rs, 4);
        result[1] = LRUReplacement(rs, 4);
        return result;
    }
}
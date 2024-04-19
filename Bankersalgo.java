package deadlock;

import java.util.*;

public class Bankersalgo {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int countofr, countofp;
        int[] instance, available;
        int[][] allocation, max, need;

        System.out.print("Enter the number of resources : ");
        countofr = scanner.nextInt();

        instance = new int[countofr];
        available = new int[countofr];

        for (int i = 0; i < countofr; i++) {
            System.out.print("Enter the max instances of resource R[" + i + "]: ");
            instance[i] = scanner.nextInt();
            available[i] = instance[i];
        }

        System.out.print("\nEnter the number of processes : ");
        countofp = scanner.nextInt();

        allocation = new int[countofp][countofr];
        max = new int[countofp][countofr];
        need = new int[countofp][countofr];

        System.out.println("\nEnter the allocation matrix:");
        for (int i = 0; i < countofp; i++) {
            System.out.println("For Process P[" + i + "]");
            for (int j = 0; j < countofr; j++) {
                System.out.print("Allocation of resource R[" + j + "]: ");
                allocation[i][j] = scanner.nextInt();
                available[j] -= allocation[i][j];
            }
        }

        System.out.println("\nEnter the max matrix:");
        for (int i = 0; i < countofp; i++) {
            System.out.println("For Process P[" + i + "]");
            for (int j = 0; j < countofr; j++) {
                System.out.print("Max demand of resource R[" + j + "]: ");
                max[i][j] = scanner.nextInt();
                need[i][j] = max[i][j] - allocation[i][j];
            }
        }

        // Display the input data
        System.out.println("\nAllocation Matrix:");
        printMatrix(allocation);

        System.out.println("\nMax Matrix:");
        printMatrix(max);

        System.out.println("\nNeed Matrix:");
        printMatrix(need);

        // Find and display the safe sequence using Banker's Algorithm
        List<Integer> safeSequence = findSafeSequence(allocation, need, available, countofp, countofr);
        if (safeSequence != null) {
            System.out.println("\nSafe Sequence: " + safeSequence);
        } else {
            System.out.println("\nNo safe sequence found. System is in an unsafe state.DeadLock Occurs");
        }

        scanner.close();
    }

    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }

    private static List<Integer> findSafeSequence(int[][] allocation, int[][] need, int[] available,
                                                  int numProcesses, int numResources) {
        List<Integer> safeSequence = new ArrayList<>();
        boolean[] finish = new boolean[numProcesses];
        int[] work = Arrays.copyOf(available, numResources);

        int count = 0;
        while (count < numProcesses) {
            boolean found = false;
            for (int i = 0; i < numProcesses; i++) {
                if (!finish[i] && canAllocate(need[i], work)) {
                    // Process i can be executed
                    for (int j = 0; j < numResources; j++) {
                        work[j] += allocation[i][j];
                    }
                    finish[i] = true;
                    safeSequence.add(i);
                    count++;
                    found = true;
                }
            }
            if (!found) {
                return null; // Deadlock detected
            }
        }
        return safeSequence;
    }

    private static boolean canAllocate(int[] need, int[] work) {
        for (int i = 0; i < need.length; i++) {
            if (need[i] > work[i]) {
                return false;
            }
        }
        return true;
    }
}


import java.util.Scanner;
import java.util.Arrays;

public class NonPreemptivePriority {

    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            int n;
            int[] arrivalTimes, burstTimes, priorities, completionTimes, turnaroundTimes, waitingTimes;
            boolean[] isCompleted;

            // Input the number of processes
            System.out.println("Enter the number of processes: ");
            n = input.nextInt();

            // Initialize all the arrays
            arrivalTimes = new int[n];
            burstTimes = new int[n];
            priorities = new int[n];
            completionTimes = new int[n];
            turnaroundTimes = new int[n];
            waitingTimes = new int[n];
            isCompleted = new boolean[n];

            // User input arrival, burst time, and priority
            for (int i = 0; i < n; i++) {
                System.out.println("Enter the Arrival Time of process " + (i + 1) + ": ");
                arrivalTimes[i] = input.nextInt();

                System.out.println("Enter the Burst Time of process " + (i + 1) + ": ");
                burstTimes[i] = input.nextInt();

                System.out.println("Enter the Priority of process " + (i + 1) + " (lower number means higher priority): ");
                priorities[i] = input.nextInt();
            }

            // Non-preemptive priority scheduling
            priorityScheduling(n, arrivalTimes, burstTimes, priorities, completionTimes, turnaroundTimes, waitingTimes, isCompleted);
        }
    }

    public static void priorityScheduling(int n, int[] arrivalTimes, int[] burstTimes, int[] priorities, int[] completionTimes, int[] turnaroundTimes, int[] waitingTimes, boolean[] isCompleted) {
        int currentTime = 0, completedProcesses = 0;
        int[] processOrder = new int[n]; // Track the order of processes in the Gantt chart
        int[] startTimes = new int[n];   // Track the start times of processes in the Gantt chart

        System.out.println("\nProcesses execution (Non-preemptive Priority Scheduling):");

        while (completedProcesses < n) {
            int highestPriorityProcess = -1;
            int highestPriority = Integer.MAX_VALUE;

            // Find the next process with the highest priority (lowest priority value)
            for (int i = 0; i < n; i++) {
                if (!isCompleted[i] && arrivalTimes[i] <= currentTime && priorities[i] < highestPriority) {
                    highestPriorityProcess = i;
                    highestPriority = priorities[i];
                }
            }

            if (highestPriorityProcess == -1) {
                // No process is ready to execute; increment time
                currentTime++;
            } else {
                // Process the selected highest priority process
                startTimes[completedProcesses] = currentTime; // Record start time
                processOrder[completedProcesses] = highestPriorityProcess + 1; // Record process ID
                currentTime += burstTimes[highestPriorityProcess];
                completionTimes[highestPriorityProcess] = currentTime;
                turnaroundTimes[highestPriorityProcess] = completionTimes[highestPriorityProcess] - arrivalTimes[highestPriorityProcess];
                waitingTimes[highestPriorityProcess] = turnaroundTimes[highestPriorityProcess] - burstTimes[highestPriorityProcess];
                isCompleted[highestPriorityProcess] = true;
                completedProcesses++;

                System.out.println("Process " + (highestPriorityProcess + 1) + " completed at time " + currentTime);
            }
        }

        // Gantt Chart
        System.out.println("\nGantt Chart:");
        // Print process timeline
        for (int i = 0; i < n; i++) {
            System.out.print("|   P" + processOrder[i] + "   ");
        }
        System.out.println("|");
        // Print completion times
        System.out.print(startTimes[0]);
        for (int i = 0; i < n; i++) {
            System.out.printf("%8d", completionTimes[processOrder[i] - 1]);
        }
        System.out.println();

        // Table to show summary
        System.out.println("\nSummary:");
        System.out.printf("%-10s%-15s%-15s%-15s%-15s%-15s%-15s\n", "Process", "Arrival Time", "Burst Time", "Priority", "Finish Time", "Turnaround Time", "Waiting Time");
        for (int i = 0; i < n; i++) {
            System.out.printf("%-10d%-15d%-15d%-15d%-15d%-15d%-15d\n", i + 1, arrivalTimes[i], burstTimes[i], priorities[i], completionTimes[i], turnaroundTimes[i], waitingTimes[i]);
        }

        // Calculate and print average turnaround and waiting times
        double avgTurnaround = Arrays.stream(turnaroundTimes).average().orElse(0.0);
        double avgWaiting = Arrays.stream(waitingTimes).average().orElse(0.0);
        System.out.println("\nAverage Turnaround Time: " + avgTurnaround);
        System.out.println("Average Waiting Time: " + avgWaiting);
    }
}

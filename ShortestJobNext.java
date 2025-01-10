import java.util.Scanner;
import java.util.Arrays;

public class ShortestJobNext {
    
    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            int n;
            int[] arrivalTimes, burstTimes, completionTimes, turnaroundTimes, waitingTimes;
            boolean[] isCompleted;
            
            //input the number of processes
            System.out.println("Enter the number of processes: ");
            n = input.nextInt();
            
            //initialize all the arrays
            arrivalTimes = new int[n];
            burstTimes = new int[n];
            completionTimes = new int[n];
            turnaroundTimes = new int[n];
            waitingTimes = new int[n];
            isCompleted = new boolean[n];
            
            //user input arrival & burst time
            for (int i = 0; i < n; i++) {
               System.out.println("Enter the Arrival Time of process " + (i + 1) +": ");
               arrivalTimes[i] = input.nextInt();
               
               System.out.println("Enter the Burst Time of process " + (i + 1) + ": ");
               burstTimes[i] = input.nextInt();
            }
            
            //nonpreemptive sjf scheduling
            sjfScheduling(n, arrivalTimes, burstTimes, completionTimes, turnaroundTimes, waitingTimes, isCompleted);
            }
        }
       public static void sjfScheduling(int n, int[] arrivalTimes, int[] burstTimes, int[] completionTimes, int[] turnaroundTimes, int[] waitingTimes, boolean[] isCompleted) {
    int currentTime = 0, completedProcesses = 0;
    int[] processOrder = new int[n]; // Track the order of processes in the Gantt chart
    int[] startTimes = new int[n];   // Track the start times of processes in the Gantt chart

    System.out.println("\nProcesses execution (SJF Scheduling):");

    while (completedProcesses < n) {
        int shortestProcess = -1;
        int shortestBurstTime = Integer.MAX_VALUE;

        // Find the next process with the shortest burst time
        for (int i = 0; i < n; i++) {
            if (!isCompleted[i] && arrivalTimes[i] <= currentTime && burstTimes[i] < shortestBurstTime) {
                shortestProcess = i;
                shortestBurstTime = burstTimes[i];
            }
        }

        if (shortestProcess == -1) {
            // No process is ready to execute; increment time
            currentTime++;
        } else {
            // Process the selected shortest process
            startTimes[completedProcesses] = currentTime; // Record start time
            processOrder[completedProcesses] = shortestProcess + 1; // Record process ID
            currentTime += burstTimes[shortestProcess];
            completionTimes[shortestProcess] = currentTime;
            turnaroundTimes[shortestProcess] = completionTimes[shortestProcess] - arrivalTimes[shortestProcess];
            waitingTimes[shortestProcess] = turnaroundTimes[shortestProcess] - burstTimes[shortestProcess];
            isCompleted[shortestProcess] = true;
            completedProcesses++;

            System.out.println("Process " + (shortestProcess + 1) + " completed at time " + currentTime);
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
    System.out.printf("%-10s%-15s%-15s%-15s%-15s%-15s\n", "Process", "Arrival Time", "Burst Time", "Finish Time", "Turnaround Time", "Waiting Time");
    for (int i = 0; i < n; i++) {
        System.out.printf("%-10d%-15d%-15d%-15d%-15d%-15d\n", i + 1, arrivalTimes[i], burstTimes[i], completionTimes[i], turnaroundTimes[i], waitingTimes[i]);
    }

    // Calculate and print average turnaround and waiting times
    double avgTurnaround = Arrays.stream(turnaroundTimes).average().orElse(0.0);
    double avgWaiting = Arrays.stream(waitingTimes).average().orElse(0.0);
    System.out.println("\nAverage Turnaround Time: " + avgTurnaround);
    System.out.println("Average Waiting Time: " + avgWaiting);
  }
}

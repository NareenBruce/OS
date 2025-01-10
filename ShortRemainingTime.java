import java.util.*;

class ProcessScheduling {
    int id, ArrivalTime, BurstTime, TimeRemaining, TimeCompletion, WaitingTime, TurnAroundTime;

    ProcessScheduling(int id, int ArrivalTime, int BurstTime) {
        this.id = id;
        this.ArrivalTime = ArrivalTime;
        this.BurstTime = BurstTime;
        this.TimeRemaining = BurstTime;
    }
}

public class ShortRemainingTime {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("   Shortest Remaining Time Scheduling   ");
        System.out.println("========================================");


        int n = 0;

        // to put number of processes 
        // also making sure it follows the instruction of the assignment which is number of processes = x, 3 < x < 10
        while (true) {
            System.out.print("Insert your number of processes (3 to 10): ");
            try {
                n = Integer.parseInt(scanner.nextLine());
                if (n < 3 || n > 10) {
                    System.out.println("Warning: Please enter a number between 3 and 10.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Warning: Invalid input. Please enter a valid number.");
            }
        }

        // for user to put in their desired process details
        List<ProcessScheduling> processes = new ArrayList<>();
        System.out.println("\nEnter the details of the processes:");
        for (int i = 0; i < n; i++) {
            while (true) {
                try {
                    System.out.printf("Process %d:\n", i + 1);
                    System.out.print("  Arrival Time: ");
                    int ArrivalTime = Integer.parseInt(scanner.nextLine());
                    System.out.print("  Burst Time: ");
                    int BurstTime = Integer.parseInt(scanner.nextLine());
                    processes.add(new ProcessScheduling(i + 1, ArrivalTime, BurstTime));
                    System.out.println();
                    break; // Exit loop after successful input
                } catch (NumberFormatException e) {
                    System.out.println("Warning: Invalid input. Please enter numeric values for Arrival Time and Burst Time.");
                }
            }
        }

        // to rearrange the processes based on the arrival time so its easierto read the table
        processes.sort(Comparator.comparingInt(p -> p.ArrivalTime));

        // Variables for simulation
        int CurrentTime = 0;
        int completed = 0;
        ProcessScheduling currentProcessScheduling = null;
        List<String> ganttChart = new ArrayList<>();
        int lastProcessId = -1; // just to keep track of the last process

        while (completed < n) {
            // finding the process with the shortest burst time
            ProcessScheduling nextProcessScheduling = null;
            for (ProcessScheduling p : processes) {
                if (p.ArrivalTime <= CurrentTime && p.TimeRemaining > 0) {
                    if (nextProcessScheduling == null || p.TimeRemaining < nextProcessScheduling.TimeRemaining ||
                            (p.TimeRemaining == nextProcessScheduling.TimeRemaining && p.ArrivalTime < nextProcessScheduling.ArrivalTime)) {
                        nextProcessScheduling = p;
                    }
                }
            }

            if (nextProcessScheduling == null) {
                // increasing time if no process is ready
                CurrentTime++;
            } else {
                // updating gantt chart based on the user's input especially if its new
                if (nextProcessScheduling.id != lastProcessId) {
                    ganttChart.add("P" + nextProcessScheduling.id);
                    lastProcessId = nextProcessScheduling.id;
                }

                // process execution already
                nextProcessScheduling.TimeRemaining--;
                CurrentTime++;
                currentProcessScheduling = nextProcessScheduling;

                // process doneee
                if (nextProcessScheduling.TimeRemaining == 0) {
                    nextProcessScheduling.TimeCompletion = CurrentTime;
                    nextProcessScheduling.TurnAroundTime = nextProcessScheduling.TimeCompletion - nextProcessScheduling.ArrivalTime;
                    nextProcessScheduling.WaitingTime = nextProcessScheduling.TurnAroundTime - nextProcessScheduling.BurstTime;
                    completed++;
                }
            }
        }

        // just a simple gantt chart
        System.out.println("\nGantt Chart: " + String.join(" -> ", ganttChart));

        // details of the final arrangement and details
        System.out.println("\nProcess Details:");
        System.out.printf("%-10s%-15s%-15s%-15s%-15s%-15s\n", "Process", "Arrival Time", "Burst Time", "Finish Time", "TAT", "WT");
        for (ProcessScheduling p : processes) {
            System.out.printf("%-10s%-15d%-15d%-15d%-15d%-15d\n", "P" + p.id, p.ArrivalTime, p.BurstTime, p.TimeCompletion, p.TurnAroundTime, p.WaitingTime);
        }

        // Results: Average TAT and WT
        double avgTurnAroundTime = processes.stream().mapToDouble(p -> p.TurnAroundTime).average().orElse(0.0);
        double avgWaitingTime = processes.stream().mapToDouble(p -> p.WaitingTime).average().orElse(0.0);
        System.out.printf("\nAverage TurnAround Time (TAT): %.2f\n", avgTurnAroundTime);
        System.out.printf("Average Waiting Time (WT): %.2f\n", avgWaitingTime);
    }
}

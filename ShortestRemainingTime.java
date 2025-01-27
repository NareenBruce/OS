import java.util.*;

public class ShortestRemainingTime {
    public static List<String> schedule(List<Process> processes) {
        // Sort processes by arrival time initially
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        PriorityQueue<Process> queue = new PriorityQueue<>(
            Comparator.comparingInt((Process p) -> p.SRTremainingTime)  // Compare by remaining time
                      .thenComparingInt(p -> p.arrivalTime) // If same, compare by arrival time
        );

        int currentTime = processes.get(0).arrivalTime; // Start with the earliest arrival time
        List<String> ganttChart = new ArrayList<>();
        int index = 0;
        Process currentProcess = null;
        int startTime = currentTime;

        while (true) {
            // Add processes that have arrived by the current time
            while (index < processes.size() && processes.get(index).arrivalTime <= currentTime) {
                queue.add(processes.get(index));
                index++;
            }

            // If no process is running and the queue is empty, jump to the next process arrival
            if (currentProcess == null && queue.isEmpty()) {
                if (index < processes.size()) {
                    currentTime = processes.get(index).arrivalTime;
                    continue;  // No processes to run, jump to next arrival time
                }
                break;  // All processes are done
            }

            // If there's a current process, handle potential preemption
            if (currentProcess != null && currentProcess.SRTremainingTime > 0) {
                // Check if there's a new process with shorter remaining time
                if (!queue.isEmpty() && queue.peek().SRTremainingTime < currentProcess.SRTremainingTime) {
                    queue.add(currentProcess);  // Preempt the current process
                    ganttChart.add("P" + currentProcess.id + "(" + startTime + "-" + currentTime + ")");
                    currentProcess = null;  // Set current process to null for preemption
                }
            }

            // If there's no current process, pick the next one from the queue
            if (currentProcess == null && !queue.isEmpty()) {
                currentProcess = queue.poll();  // Select process with shortest remaining time
                startTime = currentTime;
            }

            // Run the current process for 1 time unit
            currentTime++;
            currentProcess.SRTremainingTime--;

            // If the current process finishes, update its details and reset
            if (currentProcess.SRTremainingTime == 0) {
                ganttChart.add("P" + currentProcess.id + "(" + startTime + "-" + currentTime + ")");
                currentProcess.finishTime = currentTime;
                currentProcess.turnaroundTime = currentProcess.finishTime - currentProcess.arrivalTime;
                currentProcess.waitingTime = currentProcess.turnaroundTime - currentProcess.burstTime;
                currentProcess = null; // Reset after process finishes
            }
        }

        return ganttChart;
    }
}
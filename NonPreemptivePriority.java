import java.util.*;


public class NonPreemptivePriority {
    public static List<String> schedule(List<Process> processes) {
        // Sort the processes by arrival time to ensure they are considered in the correct order
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        PriorityQueue<Process> queue = new PriorityQueue<>(
            Comparator.comparingInt((Process p) -> p.priority)
                .thenComparingInt(p -> p.arrivalTime)
        );

        int currentTime = processes.get(0).arrivalTime; // Start from the first process's arrival time
        List<String> ganttChart = new ArrayList<>();
        int index = 0;

        while (true) {
            // Add all processes that have arrived by the current time
            while (index < processes.size() && processes.get(index).arrivalTime <= currentTime) {
                queue.add(processes.get(index));
                index++;
            }

            if (queue.isEmpty()) {
                if (index == processes.size()) break; // All processes are done
                // Jump to the next arrival time if the queue is empty
                currentTime = processes.get(index).arrivalTime;
                continue;
            }

            Process currentProcess = queue.poll();
            ganttChart.add("P" + currentProcess.id + " (" + currentTime + "-" + (currentTime + currentProcess.burstTime) + ")");
            currentTime += currentProcess.burstTime;
            currentProcess.finishTime = currentTime;
            currentProcess.turnaroundTime = currentProcess.finishTime - currentProcess.arrivalTime;
            currentProcess.waitingTime = currentProcess.turnaroundTime - currentProcess.burstTime;
        }
        return ganttChart;
    }

}

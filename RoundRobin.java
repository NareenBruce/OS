import java.util.*;

public class RoundRobin {
    public static List<String> schedule(List<Process> processes, int timeQuantum) {
        Queue<Process> queue = new LinkedList<>();
        int currentTime = 0;
        List<String> ganttChart = new ArrayList<>();

        // Sort processes by arrival time
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        int index = 0;

        while (true) {
            // Add all processes that have arrived by the current time
            while (index < processes.size() && processes.get(index).arrivalTime <= currentTime) {
                queue.add(processes.get(index));
                index++;
            }

            if (queue.isEmpty()) {
                if (index == processes.size()) break; // All processes are done
                currentTime = processes.get(index).arrivalTime; // Jump to the next arrival time
                continue;
            }

            Process currentProcess = queue.poll();
            int executionTime = Math.min(timeQuantum, currentProcess.RRremainingTime);
            ganttChart.add("P" + currentProcess.id + " (" + currentTime + "-" + (currentTime + executionTime) + ")");
            currentTime += executionTime;
            currentProcess.RRremainingTime -= executionTime;

            // Add arriving processes during the execution of the current process
            while (index < processes.size() && processes.get(index).arrivalTime <= currentTime) {
                queue.add(processes.get(index));
                index++;
            }

            if (currentProcess.RRremainingTime > 0) {
                queue.add(currentProcess); // Re-add to the queue if not finished
            } else {
                currentProcess.finishTime = currentTime;
                currentProcess.turnaroundTime = currentProcess.finishTime - currentProcess.arrivalTime;
                currentProcess.waitingTime = currentProcess.turnaroundTime - currentProcess.burstTime;
            }
        }

        return ganttChart;
    }

}

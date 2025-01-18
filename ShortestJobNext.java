import java.util.*;

public class ShortestJobNext {
    public static List<String> schedule(List<Process> processes) {
        PriorityQueue<Process> queue = new PriorityQueue<>(Comparator.comparingInt(p -> p.burstTime));
        int currentTime = 0;
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
                currentTime = processes.get(index).arrivalTime; // Jump to the next arrival time
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
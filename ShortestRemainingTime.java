import java.util.*;

public class ShortestRemainingTime {
    public static List<String> schedule(List<Process> processes) {
        PriorityQueue<Process> queue = new PriorityQueue<>(Comparator.comparingInt(p -> p.SRTremainingTime));
        int currentTime = 0;
        List<String> ganttChart = new ArrayList<>();
        int index = 0;
        Process currentProcess = null;
        int startTime = 0;

        while (true) {
            while (index < processes.size() && processes.get(index).arrivalTime <= currentTime) {
                queue.add(processes.get(index));
                index++;
            }

            if (currentProcess != null && currentProcess.SRTremainingTime > 0) {
                if (!queue.isEmpty() && queue.peek().SRTremainingTime < currentProcess.SRTremainingTime) {
                    queue.add(currentProcess);
                    ganttChart.add("P" + currentProcess.id + "(" + startTime + "-" + currentTime + ")");
                    currentProcess = null;
                }
            }

            if (currentProcess == null) {
                if (queue.isEmpty()) {
                    if (index == processes.size()) break;
                    currentTime = processes.get(index).arrivalTime;
                    continue;
                }
                currentProcess = queue.poll();
                startTime = currentTime;
            }

            currentTime++;
            currentProcess.SRTremainingTime--;

            if (currentProcess.SRTremainingTime == 0) {
                ganttChart.add("P" + currentProcess.id + "(" + startTime + "-" + currentTime + ")");
                currentProcess.finishTime = currentTime;
                currentProcess.turnaroundTime = currentProcess.finishTime - currentProcess.arrivalTime;
                currentProcess.waitingTime = currentProcess.turnaroundTime - currentProcess.burstTime;
                currentProcess = null;
            }
        }

        return ganttChart;
    }
}
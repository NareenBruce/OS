import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class RoundRobinGUI extends JFrame {
    private JTextField numProcessesField = new JTextField(10);
    private JTextField timeQuantumField = new JTextField(10);
    private JTextArea inputArea = new JTextArea(5, 30);
    private JTextArea resultArea = new JTextArea(10, 40);

    public RoundRobinGUI() {
        // Set up the frame
        this.setTitle("Round Robin Scheduler");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 500);

        // Create panels
        JPanel inputPanel = new JPanel();
        JPanel controlPanel = new JPanel();
        JPanel outputPanel = new JPanel();

        inputPanel.setLayout(new GridLayout(3, 2));
        inputPanel.add(new JLabel("Number of Processes: "));
        inputPanel.add(numProcessesField);
        inputPanel.add(new JLabel("Time Quantum: "));
        inputPanel.add(timeQuantumField);
        inputPanel.add(new JLabel("Processes (arrival, burst) per line: "));
        inputPanel.add(new JScrollPane(inputArea));

        JButton calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(new CalculateListener());
        controlPanel.add(calculateButton);

        resultArea.setEditable(false);
        outputPanel.add(new JScrollPane(resultArea));

        // Add panels to frame
        this.add(inputPanel, BorderLayout.NORTH);
        this.add(controlPanel, BorderLayout.CENTER);
        this.add(outputPanel, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    private class CalculateListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int numProcesses = Integer.parseInt(numProcessesField.getText());
                int timeQuantum = Integer.parseInt(timeQuantumField.getText());
                String[] inputLines = inputArea.getText().split("\\n");
                ArrayList<Process> processes = new ArrayList<>();

                for (int i = 0; i < numProcesses; i++) {
                    String[] data = inputLines[i].split(",");
                    int arrivalTime = Integer.parseInt(data[0].trim());
                    int burstTime = Integer.parseInt(data[1].trim());
                    processes.add(new Process(i + 1, arrivalTime, burstTime));
                }

                ArrayList<GanttEntry> ganttChart = new ArrayList<>();
                String result = scheduleProcesses(processes, timeQuantum, ganttChart);
                resultArea.setText(result);
                displayGanttChart(ganttChart);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error in input: " + ex.getMessage());
            }
        }
    }

    private String scheduleProcesses(ArrayList<Process> processes, int timeQuantum, ArrayList<GanttEntry> ganttChart) {
        StringBuilder result = new StringBuilder();
        ArrayList<Process> queue = new ArrayList<>(processes);

        int time = 0;
        while (!queue.isEmpty()) {
            Process current = queue.remove(0);

            if (current.getArrivalTime() <= time) {
                int executionTime = Math.min(current.getBurstTime(), timeQuantum);
                ganttChart.add(new GanttEntry(current.getId(), time, time + executionTime));
                time += executionTime;
                current.setBurstTime(current.getBurstTime() - executionTime);
                if (current.getBurstTime() > 0) {
                    queue.add(current);
                }
            } else {
                time++;
                queue.add(current); // Wait for the next process to arrive
            }
        }

        // Format the Gantt chart result
        result.append("Gantt Chart:\n");
        for (GanttEntry entry : ganttChart) {
            result.append("P").append(entry.getProcessId())
                    .append(" [").append(entry.getStartTime())
                    .append(" - ").append(entry.getEndTime()).append("]\n");
        }
        return result.toString();
    }

    private void displayGanttChart(ArrayList<GanttEntry> ganttChart) {
        JFrame chartFrame = new JFrame("Gantt Chart");
        chartFrame.setSize(800, 200);
        chartFrame.setLayout(new FlowLayout());

        for (GanttEntry entry : ganttChart) {
            JLabel processLabel = new JLabel("P" + entry.getProcessId());
            processLabel.setOpaque(true);
            processLabel.setBackground(Color.CYAN);
            processLabel.setPreferredSize(new Dimension((entry.getEndTime() - entry.getStartTime()) * 50, 50));
            processLabel.setHorizontalAlignment(SwingConstants.CENTER);
            chartFrame.add(processLabel);
        }

        chartFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new RoundRobinGUI();
    }
}

class Process {
    private int id;
    private int arrivalTime;
    private int burstTime;

    public Process(int id, int arrivalTime, int burstTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
    }

    public int getId() {
        return id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }
}

class GanttEntry {
    private int processId;
    private int startTime;
    private int endTime;

    public GanttEntry(int processId, int startTime, int endTime) {
        this.processId = processId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getProcessId() {
        return processId;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }
}

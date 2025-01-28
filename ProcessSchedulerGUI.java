import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class ProcessSchedulerGUI extends JFrame {
    private JTextField processCountField;
    private JButton startButton;
    private JPanel inputPanel;
    private List<Process> processes;

    public ProcessSchedulerGUI() {
        setTitle("Process Scheduling Simulator");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input Panel
        inputPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        inputPanel.add(new JLabel("Number of Processes (3-10):"));
        processCountField = new JTextField(5);
        inputPanel.add(processCountField);
        add(inputPanel, BorderLayout.CENTER);

        // Start Button
        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int processCount = Integer.parseInt(processCountField.getText());
                if (processCount < 3 || processCount > 10) {
                    JOptionPane.showMessageDialog(null, "Number of processes must be between 3 and 10.");
                    return;
                }
                processes = new ArrayList<>();
                int timeQuantum = Integer.parseInt(JOptionPane.showInputDialog("Enter Time Quantum for RR:"));
                for (int i = 0; i < processCount; i++) {
                    String arrival = JOptionPane.showInputDialog("Enter Arrival Time for Process " + (i + 1) + ":");
                    String burst = JOptionPane.showInputDialog("Enter Burst Time for Process " + (i + 1) + ":");
                    String priority = JOptionPane.showInputDialog("Enter Priority for Process " + (i + 1) + ":");
                    processes.add(new Process(i, Integer.parseInt(arrival), Integer.parseInt(burst), Integer.parseInt(priority)));
                }
                showSRTResults();
                showSJNResults();
                showNPPResults();
                showRRResults(timeQuantum);
            }
        });
        add(startButton, BorderLayout.SOUTH);
    }

    private void showSRTResults() {
        // Create a new window to display SRT results
        JFrame resultFrame = new JFrame("Shorstest Remaining Time Scheduling Results");
        resultFrame.setSize(800, 600);
        resultFrame.setLayout(new BorderLayout());

        // Run SRT algorithm
        List<String> ganttChart = ShortestRemainingTime.schedule(new ArrayList<>(processes));

        // Display Gantt Chart
        GanttChartPanel ganttChartPanel = new GanttChartPanel(ganttChart);
        resultFrame.add(ganttChartPanel, BorderLayout.CENTER);
 
        // Display Process Table and Averages
        JTextArea tableOutput = new JTextArea();
        tableOutput.setEditable(false);
        tableOutput.append("Process Table:\nPID\tBurst\tArrival\tFinish\tTurnaround\tWaiting\n");
        double totalTurnaround = 0;
        double totalWaiting = 0;
        for (Process p : processes) {
            tableOutput.append(p.id + "\t" + p.burstTime + "\t" + p.arrivalTime + "\t" + p.finishTime + "\t" + p.turnaroundTime + "\t" + p.waitingTime + "\n");
            totalTurnaround += p.turnaroundTime;
            totalWaiting += p.waitingTime;
        }
        double avgTurnaround = totalTurnaround / processes.size();
        double avgWaiting = totalWaiting / processes.size();
        tableOutput.append("\nTotal Turnaround Time: " + String.format("%.2f", totalTurnaround) + "\n");
        tableOutput.append("Total Waiting Time: " + String.format("%.2f", totalWaiting) + "\n");
        tableOutput.append("Average Turnaround Time: " + String.format("%.2f", avgTurnaround) + "\n");
        tableOutput.append("Average Waiting Time: " + String.format("%.2f", avgWaiting) + "\n");

        resultFrame.add(new JScrollPane(tableOutput), BorderLayout.SOUTH);
        resultFrame.setVisible(true);
    }

    private void showSJNResults() {
        // Create a new window to display SJNresults
        JFrame resultFrame = new JFrame("Shortest Job Next Scheduling Results");
        resultFrame.setSize(800, 600);
        resultFrame.setLayout(new BorderLayout());


        // Run SJN algorithm
        List<String> ganttChart = ShortestJobNext.schedule(new ArrayList<>(processes));

        // Display Gantt Chart
        GanttChartPanel ganttChartPanel = new GanttChartPanel(ganttChart);
        resultFrame.add(ganttChartPanel, BorderLayout.CENTER);

        // Display Process Table and Averages
        JTextArea tableOutput = new JTextArea();
        tableOutput.setEditable(false);
        tableOutput.append("Process Table:\nPID\tBurst\tArrival\tFinish\tTurnaround\tWaiting\n");
        double totalTurnaround = 0;
        double totalWaiting = 0;
        for (Process p : processes) {
            tableOutput.append(p.id + "\t" + p.burstTime + "\t" + p.arrivalTime + "\t" + p.finishTime + "\t" + p.turnaroundTime + "\t" + p.waitingTime + "\n");
            totalTurnaround += p.turnaroundTime;
            totalWaiting += p.waitingTime;
        }
        double avgTurnaround = totalTurnaround / processes.size();
        double avgWaiting = totalWaiting / processes.size();
        tableOutput.append("\nTotal Turnaround Time: " + String.format("%.2f", totalTurnaround) + "\n");
        tableOutput.append("Total Waiting Time: " + String.format("%.2f", totalWaiting) + "\n");
        tableOutput.append("Average Turnaround Time: " + String.format("%.2f", avgTurnaround) + "\n");
        tableOutput.append("Average Waiting Time: " + String.format("%.2f", avgWaiting) + "\n");

        resultFrame.add(new JScrollPane(tableOutput), BorderLayout.SOUTH);
        resultFrame.setVisible(true);
    }

    private void showNPPResults() {
        // Create a new window to display NPPresults
        JFrame resultFrame = new JFrame("Non-Preemptive Priority Scheduling Results");
        resultFrame.setSize(800, 600);
        resultFrame.setLayout(new BorderLayout());

        // Run NPP algorithm
        List<String> ganttChart = NonPreemptivePriority.schedule(new ArrayList<>(processes));

        // Display Gantt Chart
        GanttChartPanel ganttChartPanel = new GanttChartPanel(ganttChart);
        resultFrame.add(ganttChartPanel, BorderLayout.CENTER);

        // Display Process Table and Averages
        JTextArea tableOutput = new JTextArea();
        tableOutput.setEditable(false);
        tableOutput.append("Process Table:\nPID\tBurst\tPriority\tArrival\tFinish\tTurnaround\tWaiting\n");
        double totalTurnaround = 0;
        double totalWaiting = 0;
        for (Process p : processes) {
            tableOutput.append(p.id + "\t" + p.burstTime+ "\t" + p.priority + "\t" + p.arrivalTime + "\t" + p.finishTime + "\t" + p.turnaroundTime + "\t" + p.waitingTime + "\n");
            totalTurnaround += p.turnaroundTime;
            totalWaiting += p.waitingTime;
        }
        double avgTurnaround = totalTurnaround / processes.size();
        double avgWaiting = totalWaiting / processes.size();
        tableOutput.append("\nTotal  Turnaround Time: " + String.format("%.2f", totalTurnaround) + "\n");
        tableOutput.append("Total  Waiting Time: " + String.format("%.2f", totalWaiting) + "\n");
        tableOutput.append("Average Turnaround Time: " + String.format("%.2f", avgTurnaround) + "\n");
        tableOutput.append("Average Waiting Time: " + String.format("%.2f", avgWaiting) + "\n");

        resultFrame.add(new JScrollPane(tableOutput), BorderLayout.SOUTH);
        resultFrame.setVisible(true);
    }

    private void showRRResults(int timeQuantum) {
        // Create a new window to display RRresults
        JFrame resultFrame = new JFrame("Round Robin Scheduling Results");
        resultFrame.setSize(800, 600);
        resultFrame.setLayout(new BorderLayout());

        // Run RR algorithm
        List<String> ganttChart = RoundRobin.schedule(new ArrayList<>(processes), timeQuantum);
        
        // Display Gantt Chart
        GanttChartPanel ganttChartPanel = new GanttChartPanel(ganttChart);
        resultFrame.add(ganttChartPanel, BorderLayout.CENTER);

        // Display Process Table and Averages
        JTextArea tableOutput = new JTextArea();
        tableOutput.setEditable(false);
        tableOutput.append("Process Table:\nPID\tBurst\tPriority\tArrival\tFinish\tTurnaround\tWaiting\n");
        double totalTurnaround = 0;
        double totalWaiting = 0;
        for (Process p : processes) {
            tableOutput.append(p.id + "\t" + p.burstTime+ "\t" + p.priority + "\t" + p.arrivalTime + "\t" + p.finishTime + "\t" + p.turnaroundTime + "\t" + p.waitingTime + "\n");
            totalTurnaround += p.turnaroundTime;
            totalWaiting += p.waitingTime;
        }
        double avgTurnaround = totalTurnaround / processes.size();
        double avgWaiting = totalWaiting / processes.size();
        tableOutput.append("\nTotal Turnaround Time: " + String.format("%.2f", totalTurnaround) + "\n");
        tableOutput.append("Total Waiting Time: " + String.format("%.2f", totalWaiting) + "\n");
        tableOutput.append("Average Turnaround Time: " + String.format("%.2f", avgTurnaround) + "\n");
        tableOutput.append("Average Waiting Time: " + String.format("%.2f", avgWaiting) + "\n");

        resultFrame.add(new JScrollPane(tableOutput), BorderLayout.SOUTH);
        resultFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ProcessSchedulerGUI().setVisible(true);
        });
    }
}
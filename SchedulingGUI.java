import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SchedulingGUI {

    // Main Frame and Components
    private JFrame frame;
    private JPanel inputPanel;
    private JTabbedPane resultTabs;

    public SchedulingGUI() {
        frame = new JFrame("CPU Scheduling Algorithms");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLayout(new BorderLayout());

        inputPanel = createInputPanel();
        resultTabs = new JTabbedPane();

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(resultTabs, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1));

        // Input fields for process count
        JPanel processCountPanel = new JPanel();
        processCountPanel.add(new JLabel("Number of Processes (3 to 10):"));
        JTextField processCountField = new JTextField(5);
        processCountPanel.add(processCountField);

        panel.add(processCountPanel);

        // Input for time quantum
        JPanel timeQuantumPanel = new JPanel();
        timeQuantumPanel.add(new JLabel("Time Quantum:"));
        JTextField timeQuantumField = new JTextField(5);
        timeQuantumPanel.add(timeQuantumField);

        panel.add(timeQuantumPanel);

        // Input table for process details
        JButton generateFieldsButton = new JButton("Generate Fields");
        panel.add(generateFieldsButton);

        JPanel inputTablePanel = new JPanel();
        generateFieldsButton.addActionListener(e -> {
            inputTablePanel.removeAll();
            int processCount = Integer.parseInt(processCountField.getText());
            if (processCount < 3 || processCount > 10) {
                JOptionPane.showMessageDialog(frame, "Please enter a process count between 3 and 10.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            inputTablePanel.setLayout(new GridLayout(processCount + 1, 4));

            inputTablePanel.add(new JLabel("Process ID"));
            inputTablePanel.add(new JLabel("Arrival Time"));
            inputTablePanel.add(new JLabel("Burst Time"));
            inputTablePanel.add(new JLabel("Priority"));

            for (int i = 0; i < processCount; i++) {
                inputTablePanel.add(new JLabel("P" + i));
                inputTablePanel.add(new JTextField(5));
                inputTablePanel.add(new JTextField(5));
                inputTablePanel.add(new JTextField(5));
            }

            inputTablePanel.revalidate();
            inputTablePanel.repaint();
        });

        JScrollPane scrollPane = new JScrollPane(inputTablePanel);
        panel.add(scrollPane);

        // Submit button
        JButton submitButton = new JButton("Submit");
        panel.add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Collect process data and call scheduling functions
                List<Process> processes = collectProcessData(inputTablePanel);
                if (processes != null) {
                    addNonPreemptivePriorityTab(processes);
                }
            }
        });

        return panel;
    }

    private List<Process> collectProcessData(JPanel tablePanel) {
        List<Process> processes = new ArrayList<>();
        Component[] components = tablePanel.getComponents();
        try {
            for (int i = 4; i < components.length; i += 4) { // Skip header row
                int id = i / 4 - 1;
                int arrival = Integer.parseInt(((JTextField) components[i + 1]).getText());
                int burst = Integer.parseInt(((JTextField) components[i + 2]).getText());
                int priority = Integer.parseInt(((JTextField) components[i + 3]).getText());
                processes.add(new Process(id, arrival, burst, priority));
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid input in one or more fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return processes;
    }

    private void addNonPreemptivePriorityTab(List<Process> processes) {
        NonPreemptivePriority scheduler = new NonPreemptivePriority(processes);
        scheduler.execute();

        JPanel resultsPanel = new JPanel(new BorderLayout());
        JTextArea resultsArea = new JTextArea();
        resultsArea.setText(scheduler.getGanttChart() + "\n\n" + scheduler.formatResults());
        resultsArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(resultsArea);
        resultsPanel.add(scrollPane, BorderLayout.CENTER);

        resultTabs.addTab("Non-Preemptive Priority", resultsPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SchedulingGUI::new);
    }

    // Process Class
    static class Process {
        int id;
        int arrival;
        int burst;
        int priority;
        int finish;
        int tat;
        int wait;

        Process(int id, int arrival, int burst, int priority) {
            this.id = id;
            this.arrival = arrival;
            this.burst = burst;
            this.priority = priority;
        }
    }

    // Non-Preemptive Priority Scheduling Class
    static class NonPreemptivePriority {
        private final List<Process> processes;
        private final List<Integer> executionOrder;
        private final List<Integer> timeStamps;

        public NonPreemptivePriority(List<Process> processes) {
            this.processes = processes;
            this.executionOrder = new ArrayList<>();
            this.timeStamps = new ArrayList<>();
        }

        public void execute() {
            int time = 0;
            List<Process> executed = new ArrayList<>();

            while (executed.size() < processes.size()) {
                Process next = null;
                for (Process p : processes) {
                    if (!executed.contains(p) && p.arrival <= time) {
                        if (next == null || p.priority < next.priority) {
                            next = p;
                        }
                    }
                }

                if (next != null) {
                    time = Math.max(time, next.arrival);
                    timeStamps.add(time); // Add start time to time stamps
                    time += next.burst;
                    next.finish = time;
                    next.tat = next.finish - next.arrival;
                    next.wait = next.tat - next.burst;
                    executed.add(next);
                    executionOrder.add(next.id);
                } else {
                    time++;
                }
            }
            timeStamps.add(time); // Add final time to time stamps
        }

        public String formatResults() {
            StringBuilder result = new StringBuilder();
            int totalTAT = 0, totalWait = 0;

            result.append("Process\tArrival\tBurst\tPriority\tFinish\tTAT\tWait\n");
            for (Process process : processes) {
                totalTAT += process.tat;
                totalWait += process.wait;
                result.append(String.format("P%d\t%d\t%d\t%d\t\t%d\t%d\t%d\n",
                        process.id, process.arrival, process.burst, process.priority,
                        process.finish, process.tat, process.wait));
            }

            float avgTAT = (float) totalTAT / processes.size();
            float avgWait = (float) totalWait / processes.size();

            result.append(String.format("\nAverage Turnaround Time: %.2f", avgTAT));
            result.append(String.format("\nAverage Waiting Time: %.2f", avgWait));

            return result.toString();
        }

        public String getGanttChart() {
            StringBuilder ganttChart = new StringBuilder("\nGantt Chart:\n");
        
            // Print process timeline
            int currentTime = 0;
            ganttChart.append("|");
            for (int id : executionOrder) {
                Process process = processes.stream().filter(p -> p.id == id).findFirst().orElse(null);
                if (process != null) {
                    ganttChart.append(String.format("   P%d   |", process.id));
                    currentTime = Math.max(currentTime, process.arrival); // Align with the arrival time
                    currentTime += process.burst; // Advance by the burst time
                }
            }
            ganttChart.append("\n");
        
            // Print time stamps
            currentTime = 0;
            ganttChart.append(currentTime);
            for (int id : executionOrder) {
                Process process = processes.stream().filter(p -> p.id == id).findFirst().orElse(null);
                if (process != null) {
                    currentTime = Math.max(currentTime, process.arrival); // Align with the arrival time
                    currentTime += process.burst; // Advance by the burst time
                    ganttChart.append(String.format("%8d", currentTime));
                }
            }
            ganttChart.append("\n");
        
            return ganttChart.toString();
        }
        
    }
}

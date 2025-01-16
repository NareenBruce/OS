import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ProcessScheduler extends JFrame{
    private JTextField numProcessesField = new JTextField(10);
    private JTextField timeQuantumField = new JTextField(10);
    private JTextArea inputArea = new JTextArea(5, 30);

    public ProcessScheduler(){
        //set up the JFrame for Round Robin GUI
        this.setTitle("Process Scheduling Calculator");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 500);

        JPanel inputPanel = new JPanel();
        JPanel controlPanel = new JPanel();

        inputPanel.setLayout(new GridLayout(3, 2));
        inputPanel.add(new JLabel ("Number of processes(3-10):"));
        inputPanel.add(numProcessesField);
        inputPanel.add(new JLabel("Time Quantum: "));
        inputPanel.add(timeQuantumField);
        inputPanel.add(new JLabel("Processes (arrival, burst time, priority) by line:"));
        inputPanel.add(new JScrollPane(inputArea));

        JButton calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(new Calculate());
        controlPanel.add(calculateButton);

        this.add(inputPanel, BorderLayout.NORTH);
        this.add(controlPanel, BorderLayout.CENTER);

        this.setVisible(true);
    }

    //listener for calcualte button
    private class Calculate implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            try{
                int numProcess= Integer.parseInt(numProcessesField.getText());
                if  (numProcess < 3 || numProcess > 10){
                    JOptionPane.showMessageDialog(null, "Number of processes must be between 3 and 10.");
                    return;
                }
                int timeQuantum = Integer.parseInt(timeQuantumField.getText());
                String[] inputLines= inputArea.getText().split("\\n");
                ArrayList<Process> processes =  new ArrayList<>();

                for(int i=0; i< numProcess; i++){
                    String[] data = inputLines[i].split(",");
                    int arrivalTime= Integer.parseInt(data[0].trim());
                    int burstTime= Integer.parseInt(data[1].trim());
                    int priority= Integer.parseInt(data[2].trim());
                    processes.add(new Process(i+1, arrivalTime, burstTime, priority));
                }

                for(Process process: processes){
                    System.out.println(process);
                }

                ArrayList<GanttEntry> ganttChart = new ArrayList<>();
                ArrayList<GanttEntryPR> ganttChartPR = new ArrayList<>();
                String result1 = RRscheduleProcesses(processes, timeQuantum, ganttChart);
                String result2 = PRscheduleProcesses(processes, ganttChartPR);
                System.out.println(result1);
                System.out.println("--------------------");
                System.out.println(result2);
                displayRR(ganttChart, processes);
                displayPR(ganttChartPR, processes);
                }
            catch(Exception ex){
                JOptionPane.showMessageDialog(null, "Invalid input. Please try again.");
                }
            }
    }

    //The calculation logic for Round Robin
    private String RRscheduleProcesses(ArrayList<Process> processes, int timeQuantum, ArrayList<GanttEntry> ganttChart) {
            StringBuilder result1 = new StringBuilder();
            ArrayList<Process> queue = new ArrayList<>(processes);
    
            int time = 0;
            while (!queue.isEmpty()) {
                Process current = queue.remove(0);
    
                if (current.getArrivalTime() <= time) {
                    int executionTime = Math.min(current.getBurstTime(), timeQuantum);
                    ganttChart.add(new GanttEntry(current.getId(), time, time + executionTime, timeQuantum));
                    time += executionTime;
                    current.setBurstTime(current.getBurstTime() - executionTime);
                    if (current.getBurstTime() > 0) {
                        queue.add(current);
                    }
                } else {
                    time++;
                    queue.add(current);
                }
            }

        // Format the Gantt chart result
        result1.append("Gantt Chart:\n");
        for (GanttEntry entry : ganttChart) {
            result1.append("P").append(entry.getProcessId())
                    .append(" [").append(entry.getStartTime())
                    .append(" - ").append(entry.getEndTime()).append("]\n");
        }
        return result1.toString();

    }

    //The code to display Gantt Chart and Table for Round Robin
    private void displayRR(ArrayList<GanttEntry> ganttChart, ArrayList<Process> processes) {
        // Frame setup
        JFrame chartFrame = new JFrame("Round Robin Gantt Chart");
        chartFrame.setSize(1200, 600);
        chartFrame.setLayout(new BorderLayout(10, 10));
    
        // Constants for styling
        final int CELL_HEIGHT = 50;
        final int CELL_WIDTH = 80;
        final Color BORDER_COLOR = new Color(0, 0, 0);
        final Font PROCESS_FONT = new Font("Arial", Font.BOLD, 12);
        final Font TIME_FONT = new Font("Arial", Font.PLAIN, 11);
        final Font TITLE_FONT = new Font("Arial", Font.BOLD, 14);
    
        // Create main container panel with padding
        JPanel containerPanel = new JPanel(new BorderLayout(0, 20));
        containerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
        // Create process sequence panel (above Gantt chart)
        JPanel processSequencePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        for (GanttEntry entry : ganttChart) {
            JLabel sequenceLabel = new JLabel(
                String.format("P%d(%d)", entry.getProcessId(), entry.getTimeQuantum()));
            sequenceLabel.setFont(PROCESS_FONT);
            processSequencePanel.add(sequenceLabel);
           
            int burstTime = 0;
            for (Process process : processes) {
                if (process.getId() == entry.getProcessId()) {
                burstTime = process.getFburstTime();
                System.out.println(burstTime);
                break;
                }
            }

            // Add separator between process labels
            if (ganttChart.indexOf(entry) < ganttChart.size() - 1) {
                for (int i = 0; i < burstTime; i++) {
                processSequencePanel.add(new JLabel(" "));
                }
            }
        }
    
        // Create Gantt chart panel
        JPanel ganttPanel = new JPanel(new GridBagLayout());
        JPanel chartLabelPanel = new JPanel(new BorderLayout());
        JLabel chartLabel = new JLabel("Round Robin with Quantum " + (ganttChart.get(0).getTimeQuantum()));
        chartLabel.setFont(TITLE_FONT);
        chartLabelPanel.add(chartLabel, BorderLayout.WEST);
        chartLabelPanel.add(processSequencePanel, BorderLayout.SOUTH);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0);
    
        // Create the chart entries
        for (int i = 0; i < ganttChart.size(); i++) {
            GanttEntry entry = ganttChart.get(i);
            
            // Process cell panel
            JPanel processPanel = new JPanel(new BorderLayout());
            processPanel.setPreferredSize(new Dimension(CELL_WIDTH, CELL_HEIGHT));
            processPanel.setBackground(Color.WHITE);
            processPanel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
    
            // Simple process label (just "P0", "P1", etc.)
            JLabel processLabel = new JLabel("P" + entry.getProcessId());
            processLabel.setFont(PROCESS_FONT);
            processLabel.setHorizontalAlignment(SwingConstants.CENTER);
            processLabel.setOpaque(true);
            processPanel.add(processLabel, BorderLayout.CENTER);
    
            // Time label
            JLabel timeLabel = new JLabel(String.valueOf(entry.getStartTime()));
            timeLabel.setFont(TIME_FONT);
            timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
    
            // Add components to grid
            gbc.gridx = i;
            gbc.gridy = 0;
            ganttPanel.add(processPanel, gbc);
    
            gbc.gridy = 1;
            ganttPanel.add(timeLabel, gbc);
    
            // Add arrival time indicators
            for (Process process : processes) {
                if (process.getArrivalTime() > entry.getStartTime() && process.getArrivalTime() < entry.getEndTime()) {
                    // Create arrival indicator
                    JPanel indicatorPanel = new JPanel(new BorderLayout());
                    indicatorPanel.setPreferredSize(new Dimension(2, CELL_HEIGHT + 20));
                    indicatorPanel.setBackground(Color.RED);
                    
                    // Add arrival time label
                    JLabel arrivalLabel = new JLabel("P" + process.getId() + " arrives");
                    arrivalLabel.setFont(TIME_FONT);
                    arrivalLabel.setForeground(Color.RED);
                    
                    // Position indicator
                    double position = (double)(process.getArrivalTime() - entry.getStartTime()) / 
                                    (entry.getEndTime() - entry.getStartTime());
                    int xPos = (int)(i * CELL_WIDTH + position * CELL_WIDTH);
                    
                    // Add indicator using absolute positioning
                    JLayeredPane layeredPane = new JLayeredPane();
                    layeredPane.setPreferredSize(new Dimension(CELL_WIDTH, CELL_HEIGHT + 20));
                    indicatorPanel.setBounds(xPos, 0, 2, CELL_HEIGHT + 20);
                    arrivalLabel.setBounds(xPos - 30, CELL_HEIGHT + 5, 80, 15);
                    
                    layeredPane.add(indicatorPanel, JLayeredPane.DEFAULT_LAYER);
                    layeredPane.add(arrivalLabel, JLayeredPane.DEFAULT_LAYER);
                    
                    gbc.gridx = i;
                    gbc.gridy = 0;
                    ganttPanel.add(layeredPane, gbc);
                }
            }
        }
    
        // Add final time label
        if (!ganttChart.isEmpty()) {
            GanttEntry lastEntry = ganttChart.get(ganttChart.size() - 1);
            JLabel finalTimeLabel = new JLabel(String.valueOf(lastEntry.getEndTime()));
            finalTimeLabel.setFont(TIME_FONT);
            finalTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
            
            gbc.gridx = ganttChart.size();
            gbc.gridy = 1;
            ganttPanel.add(finalTimeLabel, gbc);
        }
    
        // Create table data
        String[] columnNames = {
            "Process", "Arrival Time", "Finish Time", "Burst Time", 
            "Turnaround Time", "Waiting Time"
        };
        Object[][] data = new Object[processes.size()][6];
    
        // Calculate and populate table data
        double avgTurnaroundTime = 0;
        double avgWaitingTime = 0;
    
        for (int i = 0; i < processes.size(); i++) {
            Process process = processes.get(i);
            int finishTime = getFinishTime(ganttChart, process.getId());
            int turnaroundTime = finishTime - process.getArrivalTime();
            int waitingTime = turnaroundTime - process.getFburstTime();
    
            data[i][0] = "P" + process.getId();
            data[i][1] = process.getArrivalTime();
            data[i][2] = finishTime;
            data[i][3] = process.getFburstTime();
            data[i][4] = turnaroundTime;
            data[i][5] = waitingTime;
    
            avgTurnaroundTime += turnaroundTime;
            avgWaitingTime += waitingTime;
        }
    
        // Create and configure table
        JTable processTable = new JTable(data, columnNames);
        processTable.setFillsViewportHeight(true);
        processTable.setDefaultEditor(Object.class, null);  // Make table non-editable
        processTable.getTableHeader().setReorderingAllowed(false);  // Prevent column reordering
    
        // Create averages panel
        JPanel averagesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        averagesPanel.add(new JLabel(String.format("Average Turnaround Time: %.2f",
            avgTurnaroundTime / processes.size())));
        averagesPanel.add(new JLabel("  |  "));
        averagesPanel.add(new JLabel(String.format("Average Waiting Time: %.2f",
            avgWaitingTime / processes.size())));
    
        // Layout assembly
        JPanel chartContainer = new JPanel(new BorderLayout(0, 10));
        chartContainer.add(chartLabelPanel, BorderLayout.NORTH);
        chartContainer.add(ganttPanel, BorderLayout.CENTER);
    
        JPanel tableContainer = new JPanel(new BorderLayout(0, 10));
        tableContainer.add(new JScrollPane(processTable), BorderLayout.CENTER);
        tableContainer.add(averagesPanel, BorderLayout.SOUTH);
    
        // Add components to main container
        containerPanel.add(chartContainer, BorderLayout.NORTH);
        containerPanel.add(tableContainer, BorderLayout.CENTER);
    
        // Final frame setup
        chartFrame.add(containerPanel);
        chartFrame.setLocationRelativeTo(null);
        chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        chartFrame.setVisible(true);
    }
    
    // Helper method to find finish time of a process
    private int getFinishTime(ArrayList<GanttEntry> ganttChart, int processId) {
        int finishTime = 0;
        for (int i = ganttChart.size() - 1; i >= 0; i--) {
            GanttEntry entry = ganttChart.get(i);
            if (entry.getProcessId() == processId) {
                finishTime = entry.getEndTime();
                break;
            }
        }
        return finishTime;
    }
    
    private String PRscheduleProcesses(ArrayList<Process> processes, ArrayList<GanttEntryPR> ganttChartPR) {
        StringBuilder result2 = new StringBuilder();
        
        // Create a copy of processes to preserve the original list
        ArrayList<Process> remainingProcesses = new ArrayList<>(processes);
        
        int currentTime = 0;
        
        // Continue until all processes are scheduled
        while (!remainingProcesses.isEmpty()) {
            Process selectedProcess = null;
            int selectedIndex = -1;
            
            // Find the process with highest priority among arrived processes
            for (int i = 0; i < remainingProcesses.size(); i++) {
                Process process = remainingProcesses.get(i);
                if (process.getArrivalTime() <= currentTime) {
                    if (selectedProcess == null || process.getPriority() > selectedProcess.getPriority()) {
                        selectedProcess = process;
                        selectedIndex = i;
                    }
                }
            }
            
            // If no process is available at current time, advance time to next arrival
            if (selectedProcess == null) {
                int nextArrivalTime = Integer.MAX_VALUE;
                for (Process process : remainingProcesses) {
                    if (process.getArrivalTime() > currentTime && process.getArrivalTime() < nextArrivalTime) {
                        nextArrivalTime = process.getArrivalTime();
                    }
                }
                currentTime = nextArrivalTime;
                continue;
            }
            
            // Execute the selected process
            ganttChartPR.add(new GanttEntryPR(
                selectedProcess.getId(),
                currentTime,
                currentTime + selectedProcess.getBurstTime(),
                selectedProcess.getPriority()
            ));
            
            // Update process finish time and current time
            selectedProcess.setFinishTime(currentTime + selectedProcess.getBurstTime());
            currentTime += selectedProcess.getBurstTime();
            
            // Remove the executed process
            remainingProcesses.remove(selectedIndex);
        }
    
        // Display the results using the same format as Round Robin
        displayPR(ganttChartPR, processes);
    
        // Format the output string (for debugging or logging)
        result2.append("Priority Scheduling Gantt Chart:\n");
        for (GanttEntryPR entry : ganttChartPR) {
            result2.append("P").append(entry.getProcessId())
                  .append(" [").append(entry.getStartTime())
                  .append(" - ").append(entry.getEndTime())
                  .append("] Priority: ").append(entry.getPriority())
                  .append("\n");
        }
    
        return result2.toString();
    }
    
    // Helper method specifically for Priority Scheduling display
    private void displayPR(ArrayList<GanttEntryPR> ganttChartPR, ArrayList<Process> processes) {
        // Frame setup
        JFrame chartFrame = new JFrame("Non-preemptive Priority Scheduling");
        chartFrame.setSize(1200, 600);
        chartFrame.setLayout(new BorderLayout(10, 10));
    
        // Constants for styling
        final int CELL_HEIGHT = 50;
        final int CELL_WIDTH = 80;
        final Color BORDER_COLOR = new Color(0, 0, 0);
        final Font PROCESS_FONT = new Font("Arial", Font.BOLD, 12);
        final Font TIME_FONT = new Font("Arial", Font.PLAIN, 11);
        final Font TITLE_FONT = new Font("Arial", Font.BOLD, 14);
    
        // Create main container panel with padding
        JPanel containerPanel = new JPanel(new BorderLayout(0, 20));
        containerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
        // Create process sequence panel (above Gantt chart)
        JPanel processSequencePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        for (GanttEntryPR entry : ganttChartPR) {
            JLabel sequenceLabel = new JLabel(
                String.format("P%d(%d)", entry.getProcessId(), entry.getPriority()));
            sequenceLabel.setFont(PROCESS_FONT);
            processSequencePanel.add(sequenceLabel);
            
            if (ganttChartPR.indexOf(entry) < ganttChartPR.size() - 1) {
                processSequencePanel.add(new JLabel(" "));
            }
        }
    
        // Create Gantt chart panel
        JPanel ganttPanel = new JPanel(new GridBagLayout());
        JPanel chartLabelPanel = new JPanel(new BorderLayout());
        JLabel chartLabel = new JLabel("Non-preemptive Priority Scheduling");
        chartLabel.setFont(TITLE_FONT);
        chartLabelPanel.add(chartLabel, BorderLayout.WEST);
        chartLabelPanel.add(processSequencePanel, BorderLayout.SOUTH);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0);
    
        // Create the chart entries
        for (int i = 0; i < ganttChartPR.size(); i++) {
            GanttEntryPR entry = ganttChartPR.get(i);
            
            // Process cell panel
            JPanel processPanel = new JPanel(new BorderLayout());
            processPanel.setPreferredSize(new Dimension(CELL_WIDTH, CELL_HEIGHT));
            processPanel.setBackground(Color.WHITE);
            processPanel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
    
            // Process label
            JLabel processLabel = new JLabel("P" + entry.getProcessId());
            processLabel.setFont(PROCESS_FONT);
            processLabel.setHorizontalAlignment(SwingConstants.CENTER);
            processLabel.setOpaque(true);
            processPanel.add(processLabel, BorderLayout.CENTER);
    
            // Time label
            JLabel timeLabel = new JLabel(String.valueOf(entry.getStartTime()));
            timeLabel.setFont(TIME_FONT);
            timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
    
            gbc.gridx = i;
            gbc.gridy = 0;
            ganttPanel.add(processPanel, gbc);
    
            gbc.gridy = 1;
            ganttPanel.add(timeLabel, gbc);
    
            // Add arrival indicators
            for (Process process : processes) {
                if (process.getArrivalTime() > entry.getStartTime() && 
                    process.getArrivalTime() < entry.getEndTime()) {
                    
                    JPanel indicatorPanel = new JPanel(new BorderLayout());
                    indicatorPanel.setPreferredSize(new Dimension(2, CELL_HEIGHT + 20));
                    indicatorPanel.setBackground(Color.RED);
                    
                    JLabel arrivalLabel = new JLabel("P" + process.getId() + " arrives");
                    arrivalLabel.setFont(TIME_FONT);
                    arrivalLabel.setForeground(Color.RED);
                    
                    double position = (double)(process.getArrivalTime() - entry.getStartTime()) / 
                                    (entry.getEndTime() - entry.getStartTime());
                    int xPos = (int)(i * CELL_WIDTH + position * CELL_WIDTH);
                    
                    JLayeredPane layeredPane = new JLayeredPane();
                    layeredPane.setPreferredSize(new Dimension(CELL_WIDTH, CELL_HEIGHT + 20));
                    indicatorPanel.setBounds(xPos, 0, 2, CELL_HEIGHT + 20);
                    arrivalLabel.setBounds(xPos - 30, CELL_HEIGHT + 5, 80, 15);
                    
                    layeredPane.add(indicatorPanel, JLayeredPane.DEFAULT_LAYER);
                    layeredPane.add(arrivalLabel, JLayeredPane.DEFAULT_LAYER);
                    
                    gbc.gridx = i;
                    gbc.gridy = 0;
                    ganttPanel.add(layeredPane, gbc);
                }
            }
        }
    
        // Add final time label
        if (!ganttChartPR.isEmpty()) {
            GanttEntryPR lastEntry = ganttChartPR.get(ganttChartPR.size() - 1);
            JLabel finalTimeLabel = new JLabel(String.valueOf(lastEntry.getEndTime()));
            finalTimeLabel.setFont(TIME_FONT);
            finalTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
            
            gbc.gridx = ganttChartPR.size();
            gbc.gridy = 1;
            ganttPanel.add(finalTimeLabel, gbc);
        }
    
        // Create table
        String[] columnNames = {
            "Process", "Priority", "Arrival Time", "Burst Time", 
            "Finish Time", "Turnaround Time", "Waiting Time"
        };
        Object[][] data = new Object[processes.size()][7];
    
        // Calculate and populate table data
        double avgTurnaroundTime = 0;
        double avgWaitingTime = 0;
    
        for (int i = 0; i < processes.size(); i++) {
            Process process = processes.get(i);
            int finishTime = process.getFinishTime();
            int turnaroundTime = finishTime - process.getArrivalTime();
            int waitingTime = turnaroundTime - process.getBurstTime();
    
            data[i][0] = "P" + process.getId();
            data[i][1] = process.getPriority();
            data[i][2] = process.getArrivalTime();
            data[i][3] = process.getBurstTime();
            data[i][4] = finishTime;
            data[i][5] = turnaroundTime;
            data[i][6] = waitingTime;
    
            avgTurnaroundTime += turnaroundTime;
            avgWaitingTime += waitingTime;
        }
    
        // Create and configure table
        JTable processTable = new JTable(data, columnNames);
        processTable.setFillsViewportHeight(true);
        processTable.setDefaultEditor(Object.class, null);
        processTable.getTableHeader().setReorderingAllowed(false);
    
        // Create averages panel
        JPanel averagesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        averagesPanel.add(new JLabel(String.format("Average Turnaround Time: %.2f",
            avgTurnaroundTime / processes.size())));
        averagesPanel.add(new JLabel("  |  "));
        averagesPanel.add(new JLabel(String.format("Average Waiting Time: %.2f",
            avgWaitingTime / processes.size())));
    
        // Layout assembly
        JPanel chartContainer = new JPanel(new BorderLayout(0, 10));
        chartContainer.add(chartLabelPanel, BorderLayout.NORTH);
        chartContainer.add(new JScrollPane(ganttPanel), BorderLayout.CENTER);
    
        JPanel tableContainer = new JPanel(new BorderLayout(0, 10));
        tableContainer.add(new JScrollPane(processTable), BorderLayout.CENTER);
        tableContainer.add(averagesPanel, BorderLayout.SOUTH);
    
        containerPanel.add(chartContainer, BorderLayout.NORTH);
        containerPanel.add(tableContainer, BorderLayout.CENTER);
    
        chartFrame.add(containerPanel);
        chartFrame.setLocationRelativeTo(null);
        chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        chartFrame.setVisible(true);
    }
    
        //The main method tha runs the program
        public static void main(String[] args){
        new ProcessScheduler();
    }
}

class GanttEntry {
    private int processId;
    private int startTime;
    private int endTime;
    private int timeQuantum;

    public GanttEntry(int processId, int startTime, int endTime, int timeQuantum){ {
        this.processId = processId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.timeQuantum = timeQuantum;
        }
    }

    public int getProcessId(){
        return processId;
    }

    public int getStartTime(){
        return startTime;
    }

    public int getEndTime(){
        return endTime;
    }

    public int getTimeQuantum(){
        return timeQuantum;
    }
}

class GanttEntryPR{
    private int processId;
    private int startTime;
    private int endTime;
    private int priority;

    public GanttEntryPR(int processId, int startTime, int endTime, int priority){ {
        this.processId = processId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.priority = priority;
      
        }
    }

    public int getProcessId(){
        return processId;
    }

    public int getStartTime(){
        return startTime;
    }

    public int getEndTime(){
        return endTime;
    }

    public int getPriority(){
        return priority;
    }
}


class Process {
    private int id;
    private int arrivalTime;
    private int burstTime;
    private int priority;
    private int fburstTime;
   

    public Process(int id, int arrivalTime, int burstTime, int priority) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        fburstTime= burstTime;
    }

    
    @Override
    public String toString() {
        return "Process{id=" + id + ", arrivalTime=" + arrivalTime + ", burstTime=" + burstTime + ", priority=" + priority + "}";
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

    public int getFburstTime(){
        return fburstTime;
    }

    public int getPriority(){
        return priority;
    }

    public int getFinishTime() {
        return burstTime;
    }

    public void setFinishTime(int finishTime) {
        this.burstTime = finishTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }


}

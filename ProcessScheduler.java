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
        inputPanel.add(new JLabel ("Number of processes:"));
        inputPanel.add(numProcessesField);
        inputPanel.add(new JLabel("Time Quantum: "));
        inputPanel.add(timeQuantumField);
        inputPanel.add(new JLabel("Processes (arrival, burst time, priority) by line:"));
        inputPanel.add(new JScrollPane(inputArea));

        JButton calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(new RoundRobin());
        controlPanel.add(calculateButton);

        this.add(inputPanel, BorderLayout.NORTH);
        this.add(controlPanel, BorderLayout.CENTER);

        this.setVisible(true);
    }

    private class RoundRobin implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            try{
                System.out.println("Round Robin");
                int numProcess= Integer.parseInt(numProcessesField.getText());
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
                String result = scheduleProcesses(processes, timeQuantum, ganttChart);
                System.out.println(result);
                display(ganttChart, processes);
                }
            catch(Exception ex){
                JOptionPane.showMessageDialog(null, "Invalid input. Please try again.");
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
                    queue.add(current);
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

    private void display(ArrayList<GanttEntry> ganttChart, ArrayList<Process> processes) {
        JFrame chartFrame = new JFrame("Gantt Chart");
        chartFrame.setSize(800, 400);
        chartFrame.setLayout(new BorderLayout());

        JPanel ganttPanel = new JPanel();
        ganttPanel.setLayout(new FlowLayout());

        for (GanttEntry entry : ganttChart) {
            JPanel processPanel = new JPanel();
            processPanel.setLayout(new BorderLayout());

            JLabel processLabel = new JLabel("P" + entry.getProcessId());
            processLabel.setHorizontalAlignment(SwingConstants.CENTER);
            processLabel.setOpaque(true);

            JLabel timeLabel = new JLabel(entry.getStartTime() +"  ");
            timeLabel.setHorizontalAlignment(SwingConstants.CENTER);

            processPanel.add(processLabel, BorderLayout.CENTER);
            processPanel.add(timeLabel, BorderLayout.SOUTH);

            ganttPanel.add(processPanel);
        }

        // Create table data
        String[] columnNames = {"Process", "Arrival Time", "Finish Time", "Burst Time", "Turnaround Time", "Waiting Time"};
        Object[][] data = new Object[processes.size()][6];

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
        }

        JTable processTable = new JTable(data, columnNames);
        JScrollPane tableScrollPane = new JScrollPane(processTable);

        chartFrame.add(ganttPanel, BorderLayout.NORTH);
        chartFrame.add(tableScrollPane, BorderLayout.CENTER);

        chartFrame.setVisible(true);
    }

    private int getFinishTime(ArrayList<GanttEntry> ganttChart, int processId) {
        int finishTime = 0;
        for (GanttEntry entry : ganttChart) {
            if (entry.getProcessId() == processId) {
                finishTime = entry.getEndTime();
            }
        }
        return finishTime;
    }

    
    public int SRT(){
            this.setTitle("SRT Scheduler");
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setSize(600, 500);
            JPanel chart = new JPanel();
            JPanel table = new JPanel();

            chart.setLayout(new GridLayout(1,7));

            chart.add(new JLabel("This is Gantt Chart"));

            table.setLayout(new GridLayout(5,7));
            table.add(new JLabel("This is the Table"));

            this.add(chart, BorderLayout.NORTH);
            this.add(table, BorderLayout.CENTER);

            this.setVisible(true);
            return 0;
        }
    
    public int SJN(){
            this.setTitle("SJN Scheduler");
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setSize(600, 500);
            JPanel chart = new JPanel();
            JPanel table = new JPanel();

            chart.setLayout(new GridLayout(1,7));

            chart.add(new JLabel("This is Gantt Chart"));

            table.setLayout(new GridLayout(5,7));
            table.add(new JLabel("This is the Table"));

            this.add(chart, BorderLayout.NORTH);
            this.add(table, BorderLayout.CENTER);

            this.setVisible(true);
            return 0;
        }
    
    public int PrioritySch(){
            this.setTitle("Non-Preemptive Priority Scheduler");
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setSize(600, 500);
            JPanel chart = new JPanel();
            JPanel table = new JPanel();

            chart.setLayout(new GridLayout(1,7));

            chart.add(new JLabel("This is Gantt Chart"));

            table.setLayout(new GridLayout(5,7));
            table.add(new JLabel("This is the Table"));

            this.add(chart, BorderLayout.NORTH);
            this.add(table, BorderLayout.CENTER);

            this.setVisible(true);
            return 0;
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
    
    public GanttEntry(int processId, int startTime, int endTime) {
        this.processId = processId;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }


}

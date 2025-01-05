
import javax.swing.*;
import java.awt.*;

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
        inputPanel.add(new JLabel("Processes (arrival,burst time,priority) by line:"));
        inputPanel.add(new JScrollPane(inputArea));

        JButton calculateButton = new JButton("Calculate");
        controlPanel.add(calculateButton);

        this.add(inputPanel, BorderLayout.NORTH);
        this.add(controlPanel, BorderLayout.CENTER);

        this.setVisible(true);
    }

    public int RoundRobin(){
            //set up the Jframe for the user input
            this.setTitle("Round Robin Scheduler");
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
    
    public int Priority(){
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
        ProcessScheduler scheduler = new ProcessScheduler();
        scheduler.RoundRobin();
    }
}
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;

public class RoundRobin extends JFrame{
    private JTextField numProcessesField = new JTextField(10);
    private JTextField timeQuantumField = new JTextField(10);
    private JTextArea inputArea = new JTextArea(5, 30);
    private JTextArea resultArea = new JTextArea(10, 40);

    public RoundRobin(){
        //set up the JFrame for GUI
        this.setTitle("Round Robin Scheduler");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 500);

        JPanel inputPanel = new JPanel();
        JPanel controlPanel = new JPanel();
        JPanel outputPanel = new JPanel();

        inputPanel.setLayout(new GridLayout(3, 2));
        inputPanel.add(new JLabel ("Number of processes:"));
        inputPanel.add(numProcessesField);
        inputPanel.add(new JLabel("Time Quantum: "));
        inputPanel.add(timeQuantumField);
        inputPanel.add(new JLabel("Processes (arrival,burst time) by line:"));
        inputPanel.add(new JScrollPane(inputArea));
    }


    //The main method tha runs the program
    public static void main(String[] args){
        try (Scanner input = new Scanner(System.in)) {
            int n, arrival, burst, priority,timeQuantum;

            System.out.println("Enter the number of processes: ");
            n = input.nextInt();
            System.out.println("Enter the Arrival time of process x: ");
            arrival = input.nextInt();
            System.out.println("Enter the Burst Time of process x: ");
            burst = input.nextInt();
            System.out.println("Enter the Priority of process x: ");
            priority = input.nextInt();
            System.out.println("Enter the Time Quantum for Round Robin: ");
            timeQuantum = input.nextInt();
            
            System.out.println("Number of p: "+n+ " Arrival time: "+arrival+" Burst: "+burst+ " Priority: "+priority + " TQ: "+timeQuantum);
        }
    }
}
import java.awt.*;
import java.util.List;
import javax.swing.*;

class GanttChartPanel extends JPanel {
    private List<String> ganttChart;  // List of process execution info in format "P1(0-4)"

    public GanttChartPanel(List<String> ganttChart) {
        this.ganttChart = ganttChart;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = 50; // Starting x position for the Gantt chart (pixel offset)
        int y = 50; // Starting y position for the Gantt chart
        int height = 30; // Height of each process block
        int widthScale = 20; // Scale for width (1 unit = 20 pixels)

        // Sort the gantt chart by start times for correct ordering
        ganttChart.sort((entry1, entry2) -> {
            int start1 = Integer.parseInt(entry1.split("[()\\-]")[1]);
            int start2 = Integer.parseInt(entry2.split("[()\\-]")[1]);
            return Integer.compare(start1, start2);
        });

        // Draw Gantt Chart
        for (String entry : ganttChart) {
            String[] parts = entry.split("[()\\-]");
            String process = parts[0];
            int start = Integer.parseInt(parts[1]);
            int end = Integer.parseInt(parts[2]);

            // Adjust the start and end times based on the minimum time
            int adjustedStart = start;
            int adjustedEnd = end;
            int width = (adjustedEnd - adjustedStart) * widthScale;

            // Draw process block (gray color)
            g.setColor(Color.GRAY);
            g.fillRect(x + adjustedStart * widthScale, y, width, height);
            g.setColor(Color.BLACK);
            g.drawRect(x + adjustedStart * widthScale, y, width, height);

            // Draw process label
            g.drawString(process, x + adjustedStart * widthScale + 5, y + 20);

            // Draw time labels
            g.drawString(String.valueOf(start), x + adjustedStart * widthScale, y + height + 15);
            g.drawString(String.valueOf(end), x + adjustedEnd * widthScale, y + height + 15);
        }
    }
}

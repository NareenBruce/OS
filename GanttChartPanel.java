import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class GanttChartPanel extends JPanel {
    private List<String> ganttChart;
    private List<Integer> arrivalTimes;

    public GanttChartPanel(List<String> ganttChart, List<Integer> arrivalTimes) {
        this.ganttChart = ganttChart;
        this.arrivalTimes = arrivalTimes;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = 50; // Starting x position
        int y = 50; // Starting y position
        int height = 30; // Height of each process block
        int widthScale = 20; // Scale for width (1 unit = 20 pixels)

        // Collect all time labels to avoid duplicates
        Set<Integer> existingLabels = new HashSet<>();
        for (String entry : ganttChart) {
            String[] parts = entry.split("[()\\-]");
            int start = Integer.parseInt(parts[1]);
            int end = Integer.parseInt(parts[2]);
            existingLabels.add(start);
            existingLabels.add(end);
        }

        // Draw vertical lines for arrival times
        g.setColor(Color.BLACK); // Color for vertical lines
        for (int arrivalTime : arrivalTimes) {
            int lineX = x + arrivalTime * widthScale;
            g.drawLine(lineX, y - 10, lineX, y + height + 10); // Draw vertical line

            // Draw arrival time number below the line only if it doesn't already exist
            if (!existingLabels.contains(arrivalTime)) {
                g.setColor(Color.BLACK);
                g.drawString(String.valueOf(arrivalTime), lineX - 5, y + height + 25);
            }
        }

        // Draw Gantt Chart
        for (String entry : ganttChart) {
            String[] parts = entry.split("[()\\-]");
            String process = parts[0];
            int start = Integer.parseInt(parts[1]);
            int end = Integer.parseInt(parts[2]);
            int width = (end - start) * widthScale;

            // Draw process block (gray color)
            g.setColor(Color.GRAY);
            g.fillRect(x, y, width, height);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, width, height);

            // Draw process label
            g.drawString(process, x + 5, y + 20);

            // Draw time labels
            g.drawString(String.valueOf(start), x, y + height + 15);
            g.drawString(String.valueOf(end), x + width, y + height + 15);

            x += width; // Move to the next position
        }
    }
}
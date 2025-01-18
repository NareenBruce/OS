class Process {
    int id; // Process ID
    int arrivalTime; // Time when the process arrives
    int burstTime; // CPU burst time required
    int SRTremainingTime; // Remaining burst time (for SRT)
    int RRremainingTime; // Remaining burst time (for RR)
    int priority; // Priority of the process
    int waitingTime; // Time spent waiting
    int turnaroundTime; // Total time from arrival to completion
    int finishTime; // Time when the process finishes execution

    public Process(int id, int arrivalTime, int burstTime, int priority) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.SRTremainingTime = burstTime;
        this.RRremainingTime = burstTime;
        this.priority = priority;
        this.waitingTime = 0;
        this.turnaroundTime = 0;
        this.finishTime = 0;
    }
}
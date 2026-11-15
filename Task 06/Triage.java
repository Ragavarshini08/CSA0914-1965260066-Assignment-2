
import java.util.Comparator;
import java.util.PriorityQueue;

class Patient {
    final String name;
    final int severity;
    final long arrival;

    Patient(String name, int severity, long arrival) {
        this.name = name;
        this.severity = severity;
        this.arrival = arrival;
    }

    @Override
    public String toString() {
        return name + "(S" + severity + ")";
    }
}

public class Triage {

    private long counter = 0;

    private PriorityQueue<Patient> queue =
        new PriorityQueue<>(
            Comparator.comparingInt((Patient p) -> p.severity)
                      .thenComparingLong(p -> p.arrival)
        );

    // Admit a patient
    void admit(String name, int severity) {
        if (severity < 1 || severity > 5) {
            throw new IllegalArgumentException(
                "Severity must be between 1 and 5"
            );
        }

        queue.add(new Patient(name, severity, counter++));
    }

    // Remove the next patient
    Patient next() {
        return queue.poll();
    }

    // Performance testing
    static void testPerformance(int n) {

        PriorityQueue<Patient> q = new PriorityQueue<>(
            Comparator.comparingInt((Patient p) -> p.severity)
                      .thenComparingLong(p -> p.arrival)
        );

        long start = System.nanoTime();

        // Add patients
        for (int i = 0; i < n; i++) {
            q.add(new Patient(
                "Patient" + i,
                (i % 5) + 1,
                i
            ));
        }

        long addTime = System.nanoTime() - start;

        start = System.nanoTime();

        // Remove all patients
        while (!q.isEmpty()) {
            q.poll();
        }

        long removeTime = System.nanoTime() - start;

        System.out.println("Patients: " + n);
        System.out.printf(
            "Adding time: %.3f ms%n",
            addTime / 1_000_000.0
        );

        System.out.printf(
            "Removing time: %.3f ms%n",
            removeTime / 1_000_000.0
        );

        System.out.println();
    }

    public static void main(String[] args) {

        Triage er = new Triage();

        // Admit eight patients
        er.admit("Ravi", 3);
        er.admit("Meena", 1);
        er.admit("John", 3);
        er.admit("Asha", 5);
        er.admit("Kumar", 1);
        er.admit("Lata", 2);
        er.admit("Nila", 2);
        er.admit("Arun", 4);

        // Display treatment order
        System.out.println("Patient Treatment Order:");

        Patient p;

        while ((p = er.next()) != null) {
            System.out.print(p + " ");
        }

        System.out.println("\n");

        // Performance investigation
        System.out.println("Performance Investigation:");

        testPerformance(100);
        testPerformance(1000);
        testPerformance(10000);
    }
}

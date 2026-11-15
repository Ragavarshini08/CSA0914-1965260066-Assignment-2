
public class Q7Benchmark {

    // Original implementation using String
    static String buildUsingString(String[] fields) {

        String result = "";

        for (String field : fields) {
            result = result + field + " | ";
        }

        return result;
    }

    // Redesigned implementation using StringBuilder
    static String buildUsingBuilder(String[] fields) {

        StringBuilder sb = new StringBuilder(512);

        for (String field : fields) {
            sb.append(field).append(" | ");
        }

        return sb.toString();
    }

    // Compare performance
    static void test(int ticketCount) {

        String[] fields = new String[50];

        // Create 50 fields
        for (int i = 0; i < fields.length; i++) {
            fields[i] = "Field" + i;
        }

        // Warm-up
        for (int i = 0; i < 100; i++) {
            buildUsingString(fields);
            buildUsingBuilder(fields);
        }

        String originalOutput = "";
        String revisedOutput = "";

        // Measure String execution time
        long start = System.nanoTime();

        for (int i = 0; i < ticketCount; i++) {
            originalOutput = buildUsingString(fields);
        }

        long stringTime = System.nanoTime() - start;

        // Measure StringBuilder execution time
        start = System.nanoTime();

        for (int i = 0; i < ticketCount; i++) {
            revisedOutput = buildUsingBuilder(fields);
        }

        long builderTime = System.nanoTime() - start;

        // Display results
        System.out.println("\nTickets: " + ticketCount);

        System.out.println("String Time: "
                + stringTime / 1_000_000.0 + " ms");

        System.out.println("StringBuilder Time: "
                + builderTime / 1_000_000.0 + " ms");

        System.out.println("Outputs Match: "
                + originalOutput.equals(revisedOutput));
    }

    public static void main(String[] args) {

        System.out.println("Experimental Comparison");

        test(100);

        test(1000);

        test(10000);
    }
}

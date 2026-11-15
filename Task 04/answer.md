# Task 04 – Choosing a Map for a Pharmacy Inventory

**Marks:** 6  
**Bloom’s Level:** K4 – Analyse

## Problem Context – Hospital Pharmacy Stock Lookup

A hospital pharmacy keeps stock for each medicine, identified by a unique medicine code. Pharmacists look up stock by code hundreds of times a day.

Every evening, the system generates two reports:

1. **Code-order report:** All medicines arranged in ascending order of medicine codes.
2. **Insertion-order report:** Medicines displayed in the order in which they were added.

Three Java Map implementations are considered:

- HashMap
- TreeMap
- LinkedHashMap

## 1. Problem Analysis

A hospital pharmacy maintains medicine stock details using unique medicine codes.

The choice of Map affects lookup performance, insertion performance and report generation.

### A. HashMap

- `put()`: O(1) average time.
- `get()`: O(1) average time.
- Iteration: O(n) in terms of entries, with additional bucket-capacity overhead.
- Does not maintain insertion order or sorted order.
- Requires sorting when a code-order report is generated.

### B. TreeMap

- `put()`: O(log n).
- `get()`: O(log n).
- Iteration: O(n).
- Automatically maintains medicines in ascending key order.
- Naturally produces the code-order report.
- Requires additional storage if insertion order must also be preserved.

### C. LinkedHashMap

- `put()`: O(1) average time.
- `get()`: O(1) average time.
- Iteration: O(n).
- Maintains insertion order.
- Naturally produces the insertion-order report.
- Requires sorting when a code-order report is generated.

## 2. Expected Performance Before Implementation

Before implementation, the expected behaviour is:

| Map | Lookup Performance | Ordering |
|---|---|---|
| HashMap | O(1) average | No guaranteed order |
| TreeMap | O(log n) | Sorted key order |
| LinkedHashMap | O(1) average | Insertion order |

**Prediction:**

- HashMap is expected to provide fast lookups.
- TreeMap is expected to take more time for insertion and lookup because of tree traversal.
- LinkedHashMap is expected to provide fast lookups while maintaining insertion order.
- TreeMap should produce the code-order report without additional sorting.
- LinkedHashMap should produce the insertion-order report without additional sorting.

## 3. Which Map Produces Each Report?

### A. Code-Order Report

- TreeMap produces the report directly because it maintains keys in sorted order.
- HashMap requires copying the entries and sorting them by medicine code.
- LinkedHashMap also requires sorting the entries by medicine code.

### B. Insertion-Order Report

- LinkedHashMap produces the report directly because it maintains insertion order.
- HashMap does not guarantee insertion order.
- TreeMap sorts medicines by code and does not preserve insertion order.

### Extra Work When the Wrong Map Is Chosen

- **HashMap:** Sorting is required for the code-order report. Its insertion order cannot be reconstructed reliably.
- **TreeMap:** Insertion order must be stored separately if required.
- **LinkedHashMap:** Sorting is required for the code-order report.

## 4. Design of Experiments

Three different inventory sizes are selected to compare performance.

| Experiment | Number of Medicines |
|---|---:|
| Experiment 1 | 100 |
| Experiment 2 | 10,000 |
| Experiment 3 | 100,000 |

The following operations are measured:

1. Inserting medicines using `put()`.
2. Performing 100,000 lookups using `get()`.
3. Producing the code-order report.
4. Producing the insertion-order report.

### Expected Observations

- HashMap should provide fast average lookup performance.
- TreeMap should provide sorted medicine codes automatically.
- LinkedHashMap should maintain the insertion order.
- The execution time may increase as the number of medicines increases.
- The actual execution time depends on the computer and Java environment.

## 5. Java Implementation

The following program implements HashMap, TreeMap and LinkedHashMap and compares their lookup and report-generation performance using three inventory sizes.

### Complete Java Program

**File name: `PharmacyBenchmark.java`**

```java
import java.util.*;

public class PharmacyBenchmark {

    static void test(int n) {

        Map<Integer, String> hashMap = new HashMap<>();
        Map<Integer, String> treeMap = new TreeMap<>();
        Map<Integer, String> linkedMap = new LinkedHashMap<>();

        // Insert medicines
        for (int i = n; i >= 1; i--) {
            hashMap.put(i, "Medicine" + i);
            treeMap.put(i, "Medicine" + i);
            linkedMap.put(i, "Medicine" + i);
        }

        int lookups = 100000;

        // HashMap lookup
        long start = System.nanoTime();

        for (int i = 0; i < lookups; i++) {
            hashMap.get((i % n) + 1);
        }

        long hashTime = System.nanoTime() - start;

        // TreeMap lookup
        start = System.nanoTime();

        for (int i = 0; i < lookups; i++) {
            treeMap.get((i % n) + 1);
        }

        long treeTime = System.nanoTime() - start;

        // LinkedHashMap lookup
        start = System.nanoTime();

        for (int i = 0; i < lookups; i++) {
            linkedMap.get((i % n) + 1);
        }

        long linkedTime = System.nanoTime() - start;

        // Code-order report
        start = System.nanoTime();

        Map<Integer, String> codeReport =
                new TreeMap<>(hashMap);

        long codeTime = System.nanoTime() - start;

        // Insertion-order report
        start = System.nanoTime();

        List<String> insertionReport =
                new ArrayList<>(linkedMap.values());

        long insertionTime = System.nanoTime() - start;

        System.out.println("\nMedicines: " + n);

        System.out.println("HashMap Lookup: "
                + hashTime / 1_000_000.0 + " ms");

        System.out.println("TreeMap Lookup: "
                + treeTime / 1_000_000.0 + " ms");

        System.out.println("LinkedHashMap Lookup: "
                + linkedTime / 1_000_000.0 + " ms");

        System.out.println("Code Report: "
                + codeTime / 1_000_000.0 + " ms");

        System.out.println("Insertion Report: "
                + insertionTime / 1_000_000.0 + " ms");

        System.out.println("Code order correct: "
                + isSorted(codeReport));

        System.out.println("Insertion order correct: "
                + insertionReport.get(0).equals(
                        "Medicine" + n));
    }

    static boolean isSorted(Map<Integer, String> map) {

        int previous = 0;

        for (int key : map.keySet()) {

            if (key < previous) {
                return false;
            }

            previous = key;
        }

        return true;
    }

    public static void main(String[] args) {

        test(100);
        test(10000);
        test(100000);
    }
}
```

## 6. Output

The following output was obtained by compiling and executing the above program using Java 21.

```text
Medicines: 100
HashMap Lookup: 6.36628 ms
TreeMap Lookup: 6.198837 ms
LinkedHashMap Lookup: 2.200759 ms
Code Report: 0.324208 ms
Insertion Report: 0.284308 ms
Code order correct: true
Insertion order correct: true

Medicines: 10000
HashMap Lookup: 3.798819 ms
TreeMap Lookup: 11.509235 ms
LinkedHashMap Lookup: 6.645952 ms
Code Report: 4.691334 ms
Insertion Report: 0.184388 ms
Code order correct: true
Insertion order correct: true

Medicines: 100000
HashMap Lookup: 6.891611 ms
TreeMap Lookup: 9.381176 ms
LinkedHashMap Lookup: 5.275896 ms
Code Report: 25.881126 ms
Insertion Report: 3.535692 ms
Code order correct: true
Insertion order correct: true
```

**Note:** Execution times may vary depending on the computer, processor, Java version and system load. The program measures lookup and report-generation times; insertion is performed before those measurements.

## 7. Explanation of the Program

1. Three Map objects are created: HashMap, TreeMap and LinkedHashMap.
2. Medicines are inserted in reverse order using their medicine codes.
3. The program performs 100,000 lookup operations using `get()`.
4. `System.nanoTime()` measures the execution time of each lookup operation.
5. A TreeMap copy of the HashMap is created to generate the code-order report.
6. An ArrayList is created from LinkedHashMap values to generate the insertion-order report.
7. The `isSorted()` method checks whether medicine codes are arranged in ascending order.
8. The final statement checks whether the first inserted medicine appears first in the insertion-order report.

### Explanation of the Output

- The program successfully executes for 100, 10,000 and 100,000 medicines.
- All three Map implementations perform the lookup operations.
- The code-order report is generated using TreeMap.
- The insertion-order report is generated using LinkedHashMap.
- `Code order correct: true` confirms that medicine codes are arranged in ascending order.
- `Insertion order correct: true` confirms that the insertion order is maintained.

Both validation results are `true` for all three experiments.

## 8. Scalability Inquiry

An acceptable response time for one medicine lookup is defined as less than 1 millisecond under normal operating conditions.

### Expected Deployment Behaviour

- HashMap provides fast average lookup performance.
- LinkedHashMap provides similarly efficient lookup while maintaining insertion order.
- TreeMap provides predictable O(log n) lookup performance.

The remaining performance cost comes from:

- Hashing.
- Key comparisons.
- Memory access.
- Resizing.
- Report generation.

The actual response time should be verified by testing under normal operating conditions.

## 9. Why Hashtable Is Not Preferred

Hashtable is a legacy Java collection.

Reasons why it is not preferred:

1. Its methods are synchronized, which can introduce unnecessary overhead.
2. It does not allow null keys or null values.
3. Modern alternatives such as HashMap, TreeMap and LinkedHashMap provide more flexible collection management.

### Recommendation

LinkedHashMap is a suitable choice when the pharmacy requires both fast stock lookups and insertion-order reports.

A sorted copy can be created when the code-order report is required.

If code-order reporting is the most important requirement, TreeMap is another suitable choice.

## 10. Alternative Concept and Complexity Analysis

A sorted array of medicines can also be used instead of a Map.

### Using Arrays.binarySearch()

- Medicines are stored in an array sorted by medicine code.
- Binary search is used to find a medicine using its code.
- New medicines are inserted at their correct sorted positions.
- The report can be generated directly by traversing the sorted array.

### Time Complexity

| Operation | Time Complexity | Reason |
|---|---|---|
| Lookup | O(log n) | Binary search repeatedly divides the search range. |
| Insertion | O(n) | Elements must be shifted to maintain sorted order. |
| Code-order report | O(n) | Medicines are already sorted. |
| Space | O(n) | An array stores n medicine references. |

### Comparison of Data Structures

| Data Structure | Lookup | Report Generation | Space |
|---|---|---|---|
| HashMap | O(1) average | O(n log n) for sorted report | O(n) |
| TreeMap | O(log n) | O(n) code-order iteration | O(n) |
| LinkedHashMap | O(1) average | O(n) insertion-order iteration | O(n) |
| Sorted Array | O(log n) | O(n) code-order reporting | O(n) |

## 11. Conclusion

HashMap, TreeMap and LinkedHashMap provide different advantages for pharmacy inventory management.

- **HashMap:** Suitable for fast medicine stock lookup.
- **TreeMap:** Suitable for maintaining medicines in sorted code order.
- **LinkedHashMap:** Suitable for maintaining insertion order with efficient lookup.

For the hospital pharmacy, LinkedHashMap is a suitable overall choice because it supports fast lookups and preserves the order in which medicines were added.

A sorted copy can be generated for the daily code-order report.

Therefore, LinkedHashMap provides a good balance between lookup performance and insertion-order reporting for the given pharmacy inventory system.

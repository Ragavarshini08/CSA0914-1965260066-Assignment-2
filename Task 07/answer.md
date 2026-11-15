# Task 07 – Diagnose, Redesign and Validate

**Bloom’s Level:** K5 – Evaluate

## A. Diagnose the Bottleneck

### Original Problem

In Q1, a railway e-ticket was generated using passenger details such as PNR, passenger name, coach, berth, fare and journey information.

The original implementation used the String class with repeated concatenation (+) to build the ticket.

### Identified Limitations

1. String is immutable. Once a string is created, its contents cannot be changed.
2. Every concatenation creates a new String containing the previous content and the new text.
3. Previously constructed characters must be copied repeatedly.
4. This creates unnecessary intermediate String objects.
5. Memory usage and garbage collection activity increase when many tickets are generated.
6. Performance becomes less efficient when the number of fields and tickets increases.

**Root Cause:** Repeated concatenation using immutable String objects causes unnecessary copying and object creation.

## B. Design the Redesign

The redesigned solution uses StringBuilder to generate individual railway tickets.

### Proposed Changes

1. Replace repeated String concatenation with StringBuilder.
2. Use the append() method to add ticket fields.
3. Use toString() to obtain the final ticket.
4. Use a suitable initial capacity to reduce unnecessary buffer resizing.
5. Keep the original ticket content and formatting unchanged.

### Why StringBuilder?

- StringBuilder is mutable.
- It modifies an existing character buffer.
- It avoids creating a new String object for every append operation.
- It is suitable for individual ticket generation when each thread has its own builder.

For a shared booking log accessed by multiple threads, StringBuffer can be considered because its append methods are synchronized.

## C. Implement the Redesigned Solution

The Java program is provided in `Q7Benchmark.java`.

It compares two implementations:

1. Original approach – repeated String concatenation.
2. Redesigned approach – StringBuilder.

Each ticket contains 50 fields.

## D. Experimental Validation

The program tests three workload sizes:

| Test | Number of Tickets | Fields per Ticket |
|---|---:|---:|
| Small workload | 100 | 50 |
| Medium workload | 1,000 | 50 |
| Large workload | 10,000 | 50 |

### Measurements

The program records:

- Execution time of repeated String concatenation.
- Execution time of StringBuilder.
- Whether both implementations produce identical output.

### Expected Observations

1. For 100 tickets, the performance difference may be small.
2. For 1,000 tickets, the difference may become more noticeable.
3. For 10,000 tickets, StringBuilder is expected to perform better.
4. Both implementations must generate identical ticket content.
5. StringBuilder is expected to create fewer intermediate objects.

**Note:** Actual execution times depend on the computer, Java version and system load. The program measures execution time, not exact memory consumption or object counts.

## E. Complexity and Alternative Concept Analysis

Let n represent the number of fields in one ticket, assuming bounded field lengths.

### Time Complexity

| Method | Time Complexity | Explanation |
|---|---|---|
| Repeated String concatenation | O(n²) | Previously constructed characters are repeatedly copied. |
| StringBuilder | O(n) | Appending is amortized constant time per field. |
| StringBuffer | O(n) | Similar to StringBuilder, with synchronization overhead. |

### Space Complexity

- String: Creates multiple intermediate String objects, resulting in higher temporary memory allocation.
- StringBuilder: Requires O(L) space for the character buffer.
- StringBuffer: Requires O(L) space, with synchronization overhead.

Here, L represents the total number of characters in the generated ticket.

### Alternative Concepts

**1. String.join()**

Combines multiple strings using a specified delimiter.

```java
String ticket = String.join(" | ",
    "PNR12345", "Sana", "S2", "B15", "500",
    "Chennai to Bangalore"
);
```

**2. StringJoiner**

Provides a convenient way to join strings using a delimiter, prefix and suffix.

```java
StringJoiner ticket = new StringJoiner(" | ");

ticket.add("PNR12345");
ticket.add("Sana");
ticket.add("S2");
ticket.add("B15");

System.out.println(ticket);
```

**3. String.format()**

Useful when the ticket requires a fixed output format.

```java
String ticket = String.format(
    "PNR: %s | Name: %s | Coach: %s | Berth: %s",
    "12345", "Sana", "S2", "B15"
);
```

String.join() and StringJoiner are suitable when fields are already available as strings. String.format() is useful for fixed formatting. StringBuilder is suitable for repeated incremental construction.

## F. Defend the Final Design

The redesigned solution addresses the main limitation of repeated String concatenation.

- StringBuilder modifies an existing buffer instead of creating a new String for every appended field.
- It reduces unnecessary intermediate object creation.
- It improves efficiency for generating a large number of tickets.
- It maintains the same ticket content as the original implementation.
- It is suitable for individual ticket generation in a multithreaded railway reservation system when each thread uses its own StringBuilder.

### Final Recommendation

Use StringBuilder for individual railway e-ticket generation because it provides efficient text construction and reduces unnecessary memory allocation.

Use StringBuffer when multiple threads need synchronized access to the same mutable booking log.

## Conclusion

The original implementation using repeated String concatenation has a performance limitation because String is immutable. The redesigned implementation using StringBuilder reduces repeated copying and intermediate object creation.

Experimental validation checks execution time and confirms that both implementations produce identical output. Therefore, StringBuilder is the preferred approach for generating individual railway e-tickets under heavy workloads.

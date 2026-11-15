# Task 06 – Priority, Order and Fairness in a Queue

**Bloom’s Level:** K5 – Evaluate  
**Problem Context:** Hospital Emergency Triage

## 1. Problem Analysis

The hospital emergency department assigns each patient a severity level from 1 to 5.

- Severity 1 represents a critical patient.
- Severity 5 represents a minor condition.
- The patient with the lowest severity number must be treated first.
- If two patients have the same severity, the patient who arrived earlier must be treated first.

A suitable data structure is Java's `PriorityQueue` with a custom `Comparator`.

## 2. Why a Plain Queue Is Not Sufficient

- A normal Queue follows First In, First Out (FIFO).
- It treats patients according to arrival order only.
- A critical patient arriving later may have to wait behind a less severe patient.
- Therefore, a normal Queue does not satisfy the hospital's priority rule.
- A PriorityQueue selects the patient with the highest treatment priority.

## 3. PriorityQueue with a Suitable Comparator

The Comparator uses two conditions:

1. Compare severity in ascending order. A lower severity number receives higher priority.
2. If severity is equal, compare arrival time in ascending order. The earlier patient receives priority.

Comparator:

```java
Comparator<Patient> patientOrder =
    Comparator.comparingInt((Patient p) -> p.severity)
              .thenComparingLong(p -> p.arrival);
```

This ensures that critical patients are treated first and patients with equal severity are treated in arrival order.

## 4. Complete Java Implementation

The complete program is provided in `Triage.java`.

It creates eight patients, displays their treatment order and performs performance tests using 100, 1,000 and 10,000 patients.

## 5. Correctness Check Using Eight Patients

The test data is:

| Patient | Severity | Arrival Order |
|---|---:|---:|
| Ravi | 3 | 1 |
| Meena | 1 | 2 |
| John | 3 | 3 |
| Asha | 5 | 4 |
| Kumar | 1 | 5 |
| Lata | 2 | 6 |
| Nila | 2 | 7 |
| Arun | 4 | 8 |

The correct treatment order is:

1. Meena (S1)
2. Kumar (S1)
3. Lata (S2)
4. Nila (S2)
5. Ravi (S3)
6. John (S3)
7. Arun (S4)
8. Asha (S5)

The order follows severity first and arrival time when severity is equal.

### Why printing PriorityQueue directly does not show treatment order

- A PriorityQueue internally uses a heap.
- A heap keeps the highest-priority element at its root.
- It does not maintain all elements as a completely sorted list.
- Therefore, printing the PriorityQueue directly does not guarantee treatment order.
- To display the correct order without modifying the original queue, create a copy and repeatedly call `poll()` on the copy.

## 6. Growth Investigation and Time Complexity

The program tests three queue sizes:

- 100 patients
- 1,000 patients
- 10,000 patients

### Predicted time complexity

| Operation | Time Complexity | Reason |
|---|---|---|
| Adding a patient (`add()`) | O(log n) | The heap is rearranged after insertion. |
| Removing the next patient (`poll()`) | O(log n) | The heap is rearranged after removal. |
| Viewing the next patient (`peek()`) | O(1) | The root element is accessed directly. |
| Displaying all patients in order | O(n log n) | Repeated removal from a copied queue. |
| Space complexity | O(n) | The queue stores all waiting patients. |

### Expected observations

- At 100 patients, adding and removing patients should be very fast.
- At 1,000 patients, the operations should take slightly more time.
- At 10,000 patients, the operations should still scale efficiently because each insertion and removal takes O(log n) time.
- Actual timings depend on the computer, Java version and system load.
- Multiple trials and average timings provide more reliable measurements.

## 7. Starvation Problem and Fairness Rule

### What is starvation?

Starvation occurs when a patient with a low treatment priority waits for a very long time because higher-priority patients continuously arrive.

For example, a severity-5 patient may keep waiting if new severity-1, severity-2 or severity-3 patients repeatedly arrive.

### Proposed solution: Priority Aging

- Introduce a waiting-time limit, such as 30 minutes.
- If a patient waits longer than the limit, improve their effective priority.
- For example, a severity-5 patient's effective priority may improve to severity 4 after the waiting limit.
- Further waiting can gradually improve priority, subject to clinical safety rules.
- The hospital should regularly reassess patients because their medical condition may change.

The patient's arrival time must be stored to calculate waiting duration. The PriorityQueue must be rebuilt or updated whenever priorities change because Java's PriorityQueue does not automatically reorder an element when a field used by its Comparator changes.

The aging rule must be approved by medical staff and must never delay a genuinely critical patient.

## 8. Alternative Queue Implementations

### A. LinkedList Maintained in Sorted Order

- Patients are inserted at the correct position based on severity and arrival order.
- The first patient in the list is always the next patient to be treated.
- Insertion takes O(n) time because the correct position must be found.
- Removing the first patient takes O(1) time.
- It is suitable for small queues but insertion becomes costly for large queues.

### B. Five Separate FIFO Queues

- Maintain one FIFO queue for each severity level.
- Check the queues from severity 1 to severity 5.
- Remove the first patient from the highest-priority non-empty queue.
- Insertion and removal take O(1) time.
- This approach is suitable when there are only five fixed severity levels.
- Additional logic is needed to manage the queues and implement priority aging.

### Comparison

| Approach | Insertion | Removal of Next Patient | Main Advantage |
|---|---|---|---|
| PriorityQueue | O(log n) | O(log n) | Automatically manages priority |
| Sorted LinkedList | O(n) | O(1) | Simple ordered structure |
| Five FIFO Queues | O(1) | O(1) | Efficient for five fixed severity levels |

## 9. Deployment Decision

The PriorityQueue design can serve as an efficient starting point for a city hospital receiving 5,000 patients per day.

Reasons:

- Adding a patient takes O(log n) time.
- Removing the next patient takes O(log n) time.
- The queue requires O(n) space.
- It correctly handles severity and arrival-order rules.
- It can manage a large number of waiting patients efficiently.

However, a real hospital system also requires:

- Thread-safe access when multiple registration counters admit patients.
- Reliable storage of patient details and arrival timestamps.
- Regular clinical reassessment.
- A medically approved priority-aging policy.
- Monitoring, logging and recovery mechanisms.

**Final Decision:** PriorityQueue is suitable for the core triage ordering mechanism. A production hospital system must add concurrency control, persistent patient records and clinically approved fairness rules before deployment.

## 10. Conclusion

Java PriorityQueue with a custom Comparator is suitable for emergency patient triage because it treats the most severe patient first and preserves arrival order among patients with equal severity.

Its insertion and removal operations take O(log n) time. Priority aging can reduce starvation, provided it is approved by medical staff and does not compromise emergency treatment.

Therefore, PriorityQueue is an efficient and suitable choice for the hospital triage system.

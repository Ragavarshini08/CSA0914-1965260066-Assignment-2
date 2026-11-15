# Task 05 – Derive a Generic Ranking Component from a Principle

**Bloom’s Level:** K4 – Analyse

**Problem Context – Inter-College Sports Leaderboard**

A sports meet ranks teams by points (higher first). Ties are broken by goal difference (higher first), then by team name (alphabetical). The same ranking component will later be reused for ranking players, colleges and quiz teams.

## 1. Problem Analysis

An inter-college sports leaderboard ranks teams based on three conditions:

1. Higher points receive a higher rank.
2. If points are equal, higher goal difference receives a higher rank.
3. If both points and goal difference are equal, team names are arranged alphabetically.

The ranking system should also be reusable for players, colleges and quiz teams. Java Comparator, Comparable and generics can be used to achieve this.

## 2. Design Approach

### A. Comparable vs Comparator

**Comparable:**
- Defines the natural ordering of objects.
- Requires implementing `compareTo()` inside the class.
- Suitable when a class has one fixed natural ordering.

**Comparator:**
- Defines external comparison rules.
- Does not require modifying the original class.
- Multiple comparators can be created for different ranking requirements.
- More suitable for reusable ranking systems.

**Decision:** Comparator is preferred because the same leaderboard can rank teams, players, colleges and quiz participants using different ranking rules.

### B. Multi-Field Comparison

The ranking uses the following comparison order:

- Points: Descending order.
- Goal difference: Descending order.
- Team name: Ascending alphabetical order.

Java methods used:
- `Comparator.comparingInt()`
- `thenComparingInt()`
- `thenComparing()`
- `reversed()`

### C. Generic Leaderboard Class

A generic class `Leaderboard<T>` can store different types of objects.

- `T` represents the type of object.
- `Comparator<? super T>` accepts a comparator for T or one of its supertypes.
- `List<T>` stores the ranking items.
- The `ranking()` method returns a sorted copy without modifying the original list.

### D. Use of Wildcards

`List<? extends T>` is useful when accepting a list containing objects of type T or its subclasses.

This improves flexibility and code reusability.

## 3. Complete Java Implementation

**File name: LeaderboardDemo.java**

```java
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class Team {

    final String name;
    final int points;
    final int goalDiff;

    Team(String name, int points, int goalDiff) {
        this.name = name;
        this.points = points;
        this.goalDiff = goalDiff;
    }

    @Override
    public String toString() {
        return name + "(" + points + "," + goalDiff + ")";
    }
}

class Leaderboard<T> {

    private final List<T> items = new ArrayList<>();
    private final Comparator<? super T> order;

    Leaderboard(Comparator<? super T> order) {
        this.order = order;
    }

    void add(T item) {
        items.add(item);
    }

    void addAll(List<? extends T> newItems) {
        items.addAll(newItems);
    }

    List<T> ranking() {

        // TODO 1 completed: Sort a copy, not the original list
        List<T> sorted = new ArrayList<>(items);

        sorted.sort(order);

        return sorted;
    }
}

public class LeaderboardDemo {

    public static void main(String[] args) {

        // TODO 2 completed: Define the three ranking conditions
        Comparator<Team> byRank =
            Comparator.comparingInt((Team t) -> t.points)
                .reversed()
                .thenComparing(
                    Comparator.comparingInt(
                        (Team t) -> t.goalDiff
                    ).reversed()
                )
                .thenComparing(t -> t.name);

        Leaderboard<Team> board = new Leaderboard<>(byRank);

        board.add(new Team("Panthers", 9, 4));
        board.add(new Team("Eagles", 9, 6));
        board.add(new Team("Cobras", 6, 1));
        board.add(new Team("Bisons", 9, 4));

        System.out.println("Leaderboard:");
        System.out.println(board.ranking());
    }
}
```

## 4. Expected Output

```text
Leaderboard:
[Eagles(9,6), Bisons(9,4), Panthers(9,4), Cobras(6,1)]
```

## 5. Explanation of the Output

The teams are ranked using three conditions.

**1. Points – Highest First**

- Eagles: 9 points.
- Panthers: 9 points.
- Bisons: 9 points.
- Cobras: 6 points.

Cobras are placed last because they have fewer points.

**2. Goal Difference – Highest First**

- Eagles: 6 goal difference.
- Bisons: 4 goal difference.
- Panthers: 4 goal difference.

Eagles are ranked above the other teams because they have a higher goal difference.

**3. Team Name – Alphabetical Order**

Bisons comes before Panthers alphabetically.

Therefore, Bisons is ranked above Panthers because both have 9 points and a goal difference of 4, but Bisons comes first alphabetically.

## 6. Testing the Design

Two different test cases are designed to verify the ranking system.

### Test 1: Multiple Ties

**Input:**

```java
board.add(new Team("Panthers", 9, 4));
board.add(new Team("Eagles", 9, 6));
board.add(new Team("Cobras", 6, 1));
board.add(new Team("Bisons", 9, 4));
```

**Expected Output:**

```text
[Eagles(9,6), Bisons(9,4), Panthers(9,4), Cobras(6,1)]
```

**Observation:**
- Points determine the primary ranking.
- Goal difference breaks ties in points.
- Alphabetical order breaks ties in both points and goal difference.

### Test 2: Deliberately Designed Tie-Breaking Test

**Input:**

```java
board.add(new Team("Zebras", 12, 5));
board.add(new Team("Apples", 12, 5));
board.add(new Team("Lions", 12, 8));
board.add(new Team("Tigers", 10, 9));
board.add(new Team("Bears", 10, 9));
```

**Expected Output:**

```text
[Lions(12,8), Apples(12,5), Zebras(12,5), Bears(10,9), Tigers(10,9)]
```

**Observation:**
- Lions ranks first because it has the highest goal difference among teams with 12 points.
- Apples comes before Zebras alphabetically.
- Bears comes before Tigers alphabetically.

## 7. Alternative Concept and Complexity Analysis

### A. Alternative 1 – Comparable Interface

The `Comparable` interface can be implemented inside the Team class using the `compareTo()` method.

- It defines the natural ordering of objects.
- It is suitable when only one fixed ranking order is required.
- It is less flexible when different ranking rules are needed.

### B. Alternative 2 – Multiple Sorting Operations

The list can be sorted using three separate sorting operations:

1. Sort by team name in alphabetical order.
2. Sort by goal difference in descending order.
3. Sort by points in descending order.

This approach is less convenient because multiple sorting operations are required. A single Comparator with multiple conditions is easier to maintain.

### C. Complexity Analysis

Let `n` represent the number of teams.

| Operation | Time Complexity |
|---|---|
| Adding a team | O(1) amortized |
| Creating a sorted copy | O(n) |
| Sorting the leaderboard | O(n log n) |
| Displaying the ranking | O(n) |
| Total space required | O(n) |

**Explanation:**

- Adding a team takes O(1) amortized time.
- Creating a copy of the list takes O(n) time.
- Sorting takes O(n log n) time.
- The sorted list requires O(n) additional space.

## 8. Reusability of the Generic Component

The `Leaderboard<T>` class can be reused for different ranking systems.

1. **Sports Teams:** Rank teams by points, goal difference and team name.
2. **Players:** Rank players by scores and player names.
3. **Colleges:** Rank colleges based on performance scores.
4. **Quiz Teams:** Rank teams based on total marks and team names.

Only the Comparator needs to be changed according to the ranking requirements. The generic leaderboard class remains the same.

## 9. Conclusion

The generic leaderboard system provides an efficient and reusable method for ranking teams.

- Comparator supports multiple ranking conditions.
- Generics allow the same component to work with different object types.
- The sorted copy preserves the original list.
- Multiple tie-breaking conditions are handled correctly.
- Sorting requires O(n log n) time.

**Final Recommendation:** Use a generic `Leaderboard<T>` class with a custom Comparator because it provides flexibility, reusability and efficient ranking for teams, players, colleges and quiz participants.

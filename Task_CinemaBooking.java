import java.util.*;

class Task_CinemaBooking {

    static class Seat {
        final char row;
        final int number;

        Seat(char row, int number) {
            this.row = row;
            this.number = number;
        }

        @Override
        public String toString() {
            return "" + row + number;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Seat other = (Seat) obj;
            return row == other.row && number == other.number;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, number);
        }
    }

    public static void main(String[] args) {
        HashMap<Seat, String> bookings = new HashMap<>();
        HashSet<Seat> broken = new HashSet<>();

        bookings.put(new Seat('A', 1), "Ali");
        bookings.put(new Seat('A', 2), "Sara");
        bookings.put(new Seat('B', 5), "Omar");
        bookings.put(new Seat('C', 3), "Mona");
        System.out.println("Bookings:           " + bookings);

        System.out.println("Who booked C3:      " + bookings.get(new Seat('C', 3)));

        System.out.println("Is A2 taken:        " + bookings.containsKey(new Seat('A', 2)));
        System.out.println("Is E6 taken:        " + bookings.containsKey(new Seat('E', 6)));

        String taken = bookings.putIfAbsent(new Seat('B', 5), "Youssef");
        if (taken == null) {
            System.out.println("Youssef -> B5:      booked");
        } else {
            System.out.println("Youssef -> B5:      refused, taken by " + taken);
        }

        String user = bookings.remove(new Seat('A', 1));
        bookings.put(new Seat('D', 2), user);
        System.out.println("After Ali moved:    " + bookings);

        broken.add(new Seat('E', 1));
        broken.add(new Seat('E', 2));
        if (broken.contains(new Seat('E', 1))) {
            System.out.println("Hana -> E1:         refused, seat is broken");
        } else {
            bookings.put(new Seat('E', 1), "Hana");
            System.out.println("Hana -> E1:         booked");
        }

        System.out.println("Seats booked:       " + bookings.size());

        System.out.println();
        printChart(bookings, broken);
    }

    static void printChart(HashMap<Seat, String> bookings, HashSet<Seat> broken) {
        System.out.println("     1  2  3  4  5  6");
        for (char row = 'A'; row <= 'E'; row++) {
            System.out.print(row + "  ");
            for (int number = 1; number <= 6; number++) {
                Seat seat = new Seat(row, number);
                if (bookings.containsKey(seat)) {
                    System.out.print("  X");
                } else if (broken.contains(seat)) {
                    System.out.print("  #");
                } else {
                    System.out.print("  .");
                }
            }
            System.out.println();
        }
    }
}

/*
 QUESTIONS - write your answers here

 Q1.Why did we have to override hashcode() and equals()?
 The default implementation in Object compares references, so two different objects in memory are never equal. In our code, we create a new Seat every time we check or book. Overriding hashCode() groups seats with the same row and number into the same hash bucket, and equals() lets HashMap verify they actually match instead of checking if they are the exact same instance.

 Q2. Delete your hashCode() but keep equals(), and run again.
     What happens? Why?
 Nothing gets found, all searches return null or false, and the chart prints only dots.
 Because HashMap first uses the key's hashCode to find the bucket where the item is stored. If hashCode is not overridden, two identical seats end up with different hash codes, so the map looks in the wrong bucket and never calls equals().

 Q3. Why are row and number final? What could go wrong if the number
     of a booked seat could be changed after it was put in the map?
 To make Seat immutable so its identity doesn't change after insertion. If row or number changes while it's in the map, its hash code changes as well, but the seat remains stored in its original bucket. If we query the map with the updated seat, it looks in a new bucket and fails to locate it.

 Q4. Change equals(Object o) to equals(Seat o). What happens?
     Then also remove @Override and run again. What happens now?
 With @Override, the compiler throws an error because equals(Seat) doesn't override Object.equals(Object).
 Without @Override, the code compiles, but at runtime the lookups break because HashMap explicitly calls equals(Object). Since we didn't override it, it executes the original Object.equals (which checks ==) and our equals(Seat) is completely ignored.

 BONUS. Replace the whole Seat class with this one line:
            record Seat(char row, int number) {}
        Run it. What happens, and why?
 The program still works and prints the same results.
 That is because Java records are meant for holding data and the compiler automatically provides final fields, constructor, and auto-generated equals() and hashCode() that compare all fields.
*/

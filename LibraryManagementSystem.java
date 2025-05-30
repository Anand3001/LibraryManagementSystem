package LibraryManagementSystem;

import java.sql.*;
import java.util.Scanner;
import java.sql.DriverManager;
public class  LibraryManagementSystem {
    private static final String url = "jdbc:mysql://localhost:3306/library";
    private static final String username = "root";
    private static final String password = "anand3001";

    public static void main(String[] args) {
        try {
            java.lang.Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Scanner sc = new Scanner(System.in);
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            student s = new student(connection, sc);
            book b = new book(connection, sc);
            while (true) {
                System.out.println("-----------LIBRARY MANAGEMENT SYSTEM--------------- ");
                System.out.println("1. Add Students");
                System.out.println("2. View Students");
                System.out.println("3. View Books");
                System.out.println("4. Add Issue date");
                System.out.println("5. Add Return Date");
                System.out.println("6. Show Issue date");
                System.out.println("7.Show Return date ");
                System.out.println("8. Exit");
                System.out.println("Enter your choice: ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        // Add student
                        s.addStudent();
                        System.out.println();
                        break;
                    case 2:
                        // View student
                        s.viewStudent();
                        System.out.println();
                        break;
                    case 3:
                        // View book
                        b.viewBook();
                        System.out.println();
                        break;
                    case 4:
                        // Issue Date
                        issueBook(s, b, connection, sc);
                        System.out.println();
                        break;
                    case 5:
                        returnBook(s, b, connection, sc);
                        System.out.println();
                        break;
                    case 6:
                        showIssueDates(connection);
                        System.out.println();
                        break;
                    case 7:
                        showReturnDates(connection);
                        break;
                    case 8:
                        System.out.println("THANK YOU! FOR USING LIBRARY MANAGEMENT SYSTEM!!");
                        return;
                    default:
                        System.out.println("Enter valid choice!!!");
                        break;
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void issueBook(student s, book b, Connection connection, Scanner sc) {
        System.out.print("Enter Loan ID: ");
        int loanId = sc.nextInt();
        System.out.print("Enter Book ID: ");
        int bookId = sc.nextInt();
        System.out.print("Enter Student ID: ");
        int studentId = sc.nextInt();
        System.out.print("Enter Issue Date (YYYY-MM-DD): ");
        String issueDate = sc.next();

        if (s.getStudentById(studentId) && b.getBookById(bookId)) {
            if (checkBookAvailability(bookId, connection)) {
                // Insert with user-provided Loan ID
                String sql = "INSERT INTO loan (Loan_Id, Book_Id, Student_Id, Issue_Date, Fine_Amount) VALUES (?, ?, ?, ?, 0.00)";
                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                    ps.setInt(1, loanId);
                    ps.setInt(2, bookId);
                    ps.setInt(3, studentId);
                    ps.setString(4, issueDate);
                    int rows = ps.executeUpdate();
                    System.out.println(rows > 0 ? "Book issued successfully!" : "Issue failed.");
                } catch (SQLIntegrityConstraintViolationException e) {
                    System.out.println("Loan ID already exists. Please enter a unique Loan ID.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Book is currently not available.");

            }
        } else {
            System.out.println("Either the student or book does not exist.");
        }
    }


    public static boolean checkBookAvailability(int bookId, Connection connection) {
        String query = "SELECT COUNT(*) FROM loan WHERE book_id = ? AND return_date IS NULL";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0; // Book is available if not currently issued
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void returnBook(student s, book b, Connection connection, Scanner sc) {
        System.out.print("Enter Loan ID: ");
        int loanId = sc.nextInt();
        System.out.print("Enter Return Date (YYYY-MM-DD): ");
        String returnDate = sc.next();

        String sql = "UPDATE loan SET Return_Date = ? WHERE Loan_Id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, returnDate);
            ps.setInt(2, loanId);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Return date added successfully!" : "Update failed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void showIssueDates(Connection connection) {
        String sql = "SELECT * FROM loan WHERE Issue_Date IS NOT NULL";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("Issued Books:");
            while (rs.next()) {
                System.out.printf("Loan ID: %d, Book ID: %d, Student ID: %d, Issue Date: %s%n",
                        rs.getInt("loan_id"), rs.getInt("book_id"),
                        rs.getInt("student_id"), rs.getDate("issue_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void showReturnDates(Connection connection) {
        String sql = "SELECT * FROM loan WHERE return_date IS NOT NULL";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("Returned Books:");
            while (rs.next()) {
                System.out.printf("Loan ID: %d, Book ID: %d, Student ID: %d, Return Date: %s%n",
                        rs.getInt("loan_id"), rs.getInt("book_id"),
                        rs.getInt("student_id"), rs.getDate("return_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}



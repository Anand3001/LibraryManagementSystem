package LibraryManagementSystem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class student {
    private Connection connection;
    private Scanner scanner;

    public student(Connection connection,Scanner scanner){
        this.connection=connection;
        this.scanner=scanner;
    }
    public void addStudent() {
        System.out.println("Enter Student Id:");
        int id=scanner.nextInt();
        System.out.println("Enter Student Name");
        String name=scanner.next();
        System.out.println("Enter Student Roll number:");
        int roll = scanner.nextInt();
        System.out.println("Enter Department :");
        String department = scanner.next();
        System.out.println("Enter phone number:");
        String phone=scanner.next();

        try {
            String query = "INSERT INTO student(Student_Id,Name,Roll_no,Department,Phone) VALUES(?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setInt(3, roll);
            preparedStatement.setString(4,department);
            preparedStatement.setString(5,phone);
            int affectedRow = preparedStatement.executeUpdate();
            if (affectedRow > 0) {
                System.out.println("Student added successfully");
            } else {
                System.out.println("Failed to add Student");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void viewStudent(){
        String query="select * from student";
        try{
            PreparedStatement preparedStatement= connection.prepareStatement(query);
            ResultSet resultSet=preparedStatement.executeQuery();
            System.out.println("Students:");
            System.out.println("+-------------+--------------+------------------+------------------+------------+");
            System.out.println("|  Student_Id |    Name      |  Roll_no         | Department       | Phone      |");    ;
            System.out.println("+-------------+--------------+------------------+------------------+------------+");
            while (resultSet.next()){
                int id=resultSet.getInt("Student_Id");
                String name=resultSet.getString("Name");
                int roll=resultSet.getInt("Roll_no");
                String department=resultSet.getString("Department");
                String phone=resultSet.getString("Phone");
                System.out.printf("|%-14s|%-15s|%-19s|%-19s|%-13s| \n",id,name,roll,department,phone);
                System.out.println("+-------------+--------------+------------------+------------------+------------+");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public boolean getStudentById(int Student_Id) {
        String query = "SELECT * FROM student WHERE Student_Id=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, Student_Id);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }
            else{
                return false;
            }

        }
        catch (SQLException e) {
            e.printStackTrace();

        }
        return false;
    }
}


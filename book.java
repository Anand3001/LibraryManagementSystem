package LibraryManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class book {

        private Connection con;


        public book(Connection con,Scanner sc){
            this.con=con;

        }


        public void viewBook(){
            String query="SELECT * FROM book";
            try{
                PreparedStatement ps=con.prepareStatement(query);
                ResultSet rs=ps.executeQuery();
                System.out.println("student:");
                System.out.println(" +------------+------------------+----------------+");
                System.out.println(" | Book ID    |  Book  Name      | Author Name    |");
                System.out.println(" +------------+------------------+----------------+");
                while (rs.next()){
                    int id=rs.getInt("Book_Id");
                    String name=rs.getString("Book_Name");
                    String author= rs.getString("Author_Name");
                     System.out.printf("|%-13s|%-19s|%-17s|\n",id,name,author );
                    System.out.println("+------------+------------------+----------------+");
                }
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }
        public boolean getBookById(int Book_id) {
            String query = "SELECT * FROM book WHERE Book_Id=?";
            try {
                PreparedStatement pst = con.prepareStatement(query);
                pst.setInt(1, Book_id);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    return true;
                } else {
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }
    }



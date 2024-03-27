import java.sql.*;
import java.util.ArrayList;

public class StudentDemo6 
{
    public static void main( String[] args ) 
    {
        String url = "jdbc:mysql://localhost:3306/SMS_Malcolm";
        String user = "root";
        String password = "paige2001";
        StringBuilder output = new StringBuilder( "" );
        ArrayList<Student> students = new ArrayList<>( );
        
        try ( Connection myConn = DriverManager.getConnection( url, user, password ) ) 
        {
            Statement myStmt = myConn.createStatement( );
            ResultSet myRS = myStmt.executeQuery( "Select * FROM students" );
            while ( myRS.next () ) 
            {
                Address tmpaddress = new Address( myRS.getString( "streetname" ), myRS.getString( "streetnumber" ), 
                    myRS.getString( "apartment" ), myRS.getString( "city" ), myRS.getString( "stu_state" ),
                    myRS.getString( "zip" ) );
                
                Student tmpstudent = new Student( myRS.getInt( "id" ),
                    myRS.getString( "firstname" ), myRS.getString( "lastName" ), myRS.getString( "classify" ), tmpaddress );
                
                students.add( tmpstudent );
            }
        } 
        catch (Exception e) 
        {
            System.out.println("ERROR " + e.getLocalizedMessage());
        }
        /* print the title for list of students 'Student List' */
        System.out.println( "\n\nStudent List \n" + "-------------\n" );
        try ( Connection myConn = DriverManager.getConnection( url, user, password ) )
        {
            Statement myStmt = myConn.createStatement();

            for( Student s:students)
            {
                String stu_Name = s.getFirstName()+" "+ s.getLastName() ;
                stu_Name = String.format("%-20s",stu_Name );
            
                String stu_address = s.getAddress().getStreetNumber() + " " + s.getAddress().getStreetName() + " " + s.getAddress().getApartment();
                stu_address = String.format( "%-30s", stu_address );
                stu_address += String.format( "%-25s", (" " + s.getAddress().getCity()+", " + s.getAddress().getState() + " " + s.getAddress().getZip() ));
            
                output.append( stu_Name ).append( s.getId()+"   ").append(s.getClassification()+"  \t").append( stu_address );

                // New code to execute the JOIN query
                String query = "SELECT d.dept_name, ce.course_id FROM current_enrollments ce JOIN department d ON ce.dept_id = d.dept_id WHERE ce.student_id = " + s.getId() + ";";
                try (ResultSet rs = myStmt.executeQuery(query)) {
                    // First, print the student's basic information.
                    System.out.printf("%s %2d %s %s\n", stu_Name, s.getId(), s.getClassification(), stu_address.trim());
            
                    // Now, print the "Courses:" label once, then list all courses.
                    System.out.println("Courses:");
                    while (rs.next()) {
                        String deptName = rs.getString("dept_name");
                        String courseId = rs.getString("course_id");
                        System.out.printf("    %-20s %s\n", deptName, courseId);
                    }
                }
                // Print a blank line after each student's courses for separation.
                System.out.println();
            }
        }
        catch (SQLException e) {
            System.out.println("Database connection or query execution failed: " + e.getMessage());
        }
    }
}

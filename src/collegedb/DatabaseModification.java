package collegedb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;

public class DatabaseModification {
    public static void connectToDatabase() {
        conn = null;
        try {
            // loads the class object for the MySQL driver into the DriverManager
            Class.forName("com.mysql.cj.jdbc.Driver");

            // attempt to establish a connection to the specified database via the DriverManager
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Student?serverTimezone=UTC",
                    "root", "saifulislam123"); // TODO: find out how to hide this information
            if (conn != null) { // check the connection
                System.out.println("We have connected to our database!");
            }
        }  catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            ex.printStackTrace();
        }  catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // create the tables in the database
    public static void createTables() {
        try {
            if (conn != null) {
                PreparedStatement stmt1 = conn.prepareStatement("CREATE TABLE Students " +
                        " (studentID INT UNSIGNED NOT NULL, " +
                        " firstName VARCHAR(255), " +
                        " lastName VARCHAR(255), " +
                        " email VARCHAR(255), " +
                        " sex CHAR(1), " +
                        " PRIMARY KEY (studentID)," +
                        " CHECK (sex = 'F' OR sex = 'M'))");;
                stmt1.execute();

                PreparedStatement stmt2 = conn.prepareStatement("CREATE TABLE Courses " +
                        " (courseID CHAR(10) NOT NULL, " +
                        " courseTitle VARCHAR(255), " +
                        " department CHAR(5), " +
                        " PRIMARY KEY (courseID))");
                stmt2.execute();

                PreparedStatement stmt3 = conn.prepareStatement("CREATE TABLE Classes " +
                        " (courseID CHAR(10) NOT NULL, " +
                        " studentID INT UNSIGNED NOT NULL, " +
                        " section VARCHAR(255) NOT NULL, " +
                        " year INT UNSIGNED, " +
                        " semester CHAR(6), " +
                        " GPA CHAR(1), " +
                        " PRIMARY KEY (courseID, studentID, section)," +
                        " CHECK (GPA = 'A' OR GPA = 'B' OR GPA = 'C' OR GPA = 'D' OR GPA = 'F' OR GPA = 'W'))");
                stmt3.execute();
            }
        }  catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // if previous tables exists, they will be deleted
    public static void deletePreviousTables() {
        try {
            if (conn != null) {
                PreparedStatement stmt1 = conn.prepareStatement("DROP TABLE Students");
                stmt1.execute();

                PreparedStatement stmt2 = conn.prepareStatement("DROP TABLE Courses");
                stmt2.execute();

                PreparedStatement stmt3 = conn.prepareStatement("DROP TABLE Classes");
                stmt3.execute();
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // insert a record into Students table
    public static void insertIntoStudents(int studentID, String firstName, String lastName, String email, char sex) {
        try {
            PreparedStatement stmt = conn.prepareStatement
                    ("INSERT INTO Students (studentID, firstName, lastName, email, sex) VALUES(" +
                            studentID + ", '" + firstName + "', '" + lastName + "', '" + email + "', '" + sex + "')",
                            ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // insert a record into Courses table
    public static void insertIntoCourses(String courseID, String courseTitle, String department) {
        try {
            PreparedStatement stmt = conn.prepareStatement
                    ("INSERT INTO Courses (courseID, courseTitle, department) VALUES('" +
                            courseID + "', '" + courseTitle + "', '" + department + "')",
                            ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // insert a record into Classes table
    public static void insertIntoClasses(String courseID, int studentID, String section, int year, String semester, char GPA) {
        try {
            PreparedStatement stmt = conn.prepareStatement
                    ("INSERT INTO Classes (courseID, studentID, section, year, semester, GPA) VALUES('" +
                            courseID + "', " + studentID + ", '" + section + "', " + year + ", '" + semester + "', '" +
                            GPA + "')", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // display a table with the # of students for each letter grade assigned in CSc 22100 of the Spring 2020 Semester
    public static void tableEnrolledInCSc22100Spr2020() {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT GPA, COUNT(studentID) FROM Classes " +
                    "WHERE courseID = 'CSC 22100' AND year = 2020 AND semester = 'Spring' GROUP BY GPA");
            ResultSet rSet = stmt.executeQuery();
            DatabaseModification.showResults("Classes", rSet);
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // return a HashMap with the # of students for each letter grade
    public static HashMap<Character, Integer> GPAsAndStudents() {
        HashMap<Character, Integer> hm = new HashMap<>(6);
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT GPA FROM Classes");
            ResultSet rSet = stmt.executeQuery();
            String allGPAs = "";
            while (rSet.next()) { // TODO: fix this to use constant space
                String row = rSet.getString("GPA");
                if (row != null) {
                    allGPAs += row + "";
                }
            }
            for (int i = 0; i < allGPAs.length(); i++) {
                char currGPA = allGPAs.charAt(i);
                if (!hm.containsKey(currGPA)) {
                    hm.put(currGPA, 1);
                } else {
                    int oldFreq = hm.get(currGPA);
                    hm.replace(currGPA, ++oldFreq);
                }
            }
        }  catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            ex.printStackTrace();
        }
        return hm;
    }

    public static void showTables() {
        DatabaseModification.showValues(conn);
    }

    // obtains and displays a ResultSet from the tables
    public static void showValues(Connection conn) {
        try {
            PreparedStatement stmt1 = conn.prepareStatement("SELECT * FROM Students");
            ResultSet rset1 = stmt1.executeQuery();
            DatabaseModification.showResults("Students", rset1);

            PreparedStatement stmt2 = conn.prepareStatement("SELECT * FROM Courses");
            ResultSet rset2 = stmt2.executeQuery();
            DatabaseModification.showResults("Courses", rset2);

            PreparedStatement stmt3 = conn.prepareStatement("SELECT * FROM Classes");
            ResultSet rset3 = stmt3.executeQuery();
            DatabaseModification.showResults("Classes", rset3);
        }  catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // displays the contents of the specified ResultSet
    public static void showResults(String tableName, ResultSet rSet) {
        try {
            ResultSetMetaData rsmd = rSet.getMetaData();
            int numColumns = rsmd.getColumnCount();
            String[] colLabels = new String[13];
            String resultString = null;
            if (numColumns > 0) {
                resultString = "\nTable: " + tableName + "\n" +
                        "==================================================================\n";
                resultString += "| ";
                for (int colNum = 1; colNum <= numColumns; colNum++) {
                    String colLabel = rsmd.getColumnLabel(colNum);
                    colLabels[colNum - 1] = colLabel;
                    resultString += colLabel + " | "; // TODO: format string to fit fixed length and have centered text (better to do by creating a function)
                }
            }
            System.out.println(resultString);
            System.out.println(
                    "==================================================================");
            while (rSet.next()) {
                resultString = "| ";
                for (int colNum = 1; colNum <= numColumns; colNum++) {
                    String column = String.format("%1$" + colAndSizeOfCol.get(colLabels[colNum-1]) + "s", rSet.getString(colNum)); // TODO: format string to fit fixed length and have centered text (better to do by creating a function)
                    if (column != null)
                        resultString += column + " | ";
                }
                System.out.println(resultString + '\n' +
                        "------------------------------------------------------------------");
            }
        }  catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void formatString(String str, int len) { // TODO: implement this function

    }

    public static void closeDatabase() {
        try {
            if (conn != null) {
                conn.close();
            }
        }  catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static HashMap<String, Integer> colAndSizeOfCol = new HashMap<>() {{ // TODO: see if words are in chronological order
        put("courseID", 12);
        put("courseTitle", 50);
        put("COUNT(studentID)", 16);
        put("department", 6);
        put("email", 37);
        put("firstName", 15);
        put("GPA", 3);
        put("lastName", 15);
        put("section", 4);
        put("semester", 6);
        put("sex", 3);
        put("studentID", 9);
        put("year", 4);
    }};

    private static Connection conn;
}
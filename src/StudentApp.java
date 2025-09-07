import java.sql.*;
import java.util.Scanner;

class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/studentdb?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = ""; 

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
    
class StudentDAO {
    public void addStudent(String name, int age, String grade) {
        String sql = "INSERT INTO students (name, age, grade) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, grade);
            stmt.executeUpdate();
            System.out.println(" Student added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewStudents() {
        String sql = "SELECT * FROM students";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n--- Student List ---");
            while (rs.next()) {
                System.out.printf("%d | %s | %d | %s\n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("grade"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStudent(int id, String name, int age, String grade) {
        String sql = "UPDATE students SET name=?, age=?, grade=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, grade);
            stmt.setInt(4, id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println(" Student updated successfully!");
            } else {
                System.out.println(" Student not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println(" Student deleted successfully!");
            } else {
                System.out.println(" Student not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void searchStudentByName(String name) {
        String sql = "SELECT * FROM students WHERE name LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%"); 
            ResultSet rs = stmt.executeQuery();

            System.out.println("\n--- Search Results ---");
            boolean found = false;
            while (rs.next()) {
                System.out.printf("%d | %s | %d | %s\n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("grade"));
                found = true;
            }
            if (!found) {
                System.out.println(" No student found with that name.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

public class StudentApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentDAO dao = new StudentDAO();

        while (true) {
            System.out.println("\n=== Student Management System ===");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Search Student by Name");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            // Validate menu input
            if (!sc.hasNextInt()) {
                System.out.println(" Please enter a valid number (1-6).");
                sc.nextLine(); 
                continue;
            }
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    sc.nextLine(); 
                    System.out.print("Enter name: ");
                    String name = sc.nextLine().trim();
                    if (name.isEmpty()) {
                        System.out.println(" Name cannot be empty.");
                        break;
                    }

                    System.out.print("Enter age: ");
                    if (!sc.hasNextInt()) {
                        System.out.println(" Age must be a number.");
                        sc.nextLine();
                        break;
                    }
                    int age = sc.nextInt();
                    sc.nextLine();
                    if (age <= 0) {
                        System.out.println(" Age must be greater than 0.");
                        break;
                    }

                    System.out.print("Enter grade: ");
                    String grade = sc.nextLine().trim();
                    if (grade.isEmpty()) {
                        System.out.println(" Grade cannot be empty.");
                        break;
                    }

                    dao.addStudent(name, age, grade);
                    break;

                case 2:
                    dao.viewStudents();
                    break;

                case 3:
                    System.out.print("Enter student ID to update: ");
                    if (!sc.hasNextInt()) {
                        System.out.println(" ID must be a number.");
                        sc.nextLine();
                        break;
                    }
                    int updateId = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter new name: ");
                    String newName = sc.nextLine().trim();
                    if (newName.isEmpty()) {
                        System.out.println(" Name cannot be empty.");
                        break;
                    }

                    System.out.print("Enter new age: ");
                    if (!sc.hasNextInt()) {
                        System.out.println(" Age must be a number.");
                        sc.nextLine();
                        break;
                    }
                    int newAge = sc.nextInt();
                    sc.nextLine();
                    if (newAge <= 0) {
                        System.out.println(" Age must be greater than 0.");
                        break;
                    }

                    System.out.print("Enter new grade: ");
                    String newGrade = sc.nextLine().trim();
                    if (newGrade.isEmpty()) {
                        System.out.println(" Grade cannot be empty.");
                        break;
                    }

                    dao.updateStudent(updateId, newName, newAge, newGrade);
                    break;

                case 4:
                    System.out.print("Enter student ID to delete: ");
                    if (!sc.hasNextInt()) {
                        System.out.println(" ID must be a number.");
                        sc.nextLine();
                        break;
                    }
                    int deleteId = sc.nextInt();
                    dao.deleteStudent(deleteId);
                    break;

                case 5:
                    sc.nextLine(); 
                    System.out.print("Enter name to search: ");
                    String searchName = sc.nextLine().trim();
                    if (searchName.isEmpty()) {
                        System.out.println(" Name cannot be empty.");
                        break;
                    }
                    dao.searchStudentByName(searchName);
                    break;

                case 6:
                    System.out.println(" Exiting... Goodbye!");
                    sc.close();
                    System.exit(0);

                default:
                    System.out.println(" Invalid choice. Please enter a number between 1 and 6.");
            }
        }
    }
}



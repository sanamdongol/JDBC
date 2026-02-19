package testjdbc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class StudentCrudApp {
    public StudentCrudApp() {
        JFrame frame = new JFrame("Student Record Management System");
        frame.setSize(400, 400);

        JTabbedPane tabbedPane = new JTabbedPane();
        // Create UI end=======================
        tabbedPane.addTab("Create", createPanel());
        tabbedPane.addTab("Read", readPanel());
        tabbedPane.addTab("Update", updatePanel());
        tabbedPane.addTab("Delete", deletePanel());

        frame.getContentPane().add(tabbedPane);
        frame.setVisible(true);

    }

    private JPanel createPanel() {

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));

        JLabel lblId = new JLabel("Student ID:");
        JTextField txtId = new JTextField();

        JLabel lblName = new JLabel("Name:");
        JTextField txtName = new JTextField();

        JLabel lblAge = new JLabel("Age:");
        JTextField txtAge = new JTextField();

        JLabel lblCourse = new JLabel("Course:");
        JTextField txtCourse = new JTextField();

        JButton btnAdd = new JButton("Add Student");

        // Add components to panel
        panel.add(lblId);
        panel.add(txtId);

        panel.add(lblName);
        panel.add(txtName);

        panel.add(lblAge);
        panel.add(txtAge);

        panel.add(lblCourse);
        panel.add(txtCourse);

        panel.add(new JLabel("")); // Empty space
        panel.add(btnAdd);

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Get values from text fields
                    String name = txtName.getText();
                    int age = Integer.parseInt(txtAge.getText());
                    String course = txtCourse.getText();

                    // Connection
                    Connection con = DBConnection.getConnection();

                    // ✅ AUTO-INCREMENT ID handled by DB
                    String sql = "INSERT INTO students(name, age, course) VALUES (?, ?, ?)";

                    PreparedStatement pst = con.prepareStatement(sql);

                    pst.setString(1, name);
                    pst.setInt(2, age);
                    pst.setString(3, course);

                    // Execute Insert
                    int rows = pst.executeUpdate();

                    if (rows > 0) {
                        JOptionPane.showMessageDialog(panel,
                                "✅ Student Added Successfully!");
                    }

                    // Clear Fields
                    txtName.setText("");
                    txtAge.setText("");
                    txtCourse.setText("");

                    pst.close();
                    con.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(panel,
                            "❌ Error Adding Student!");
                }
            }
        });
        return panel;
    }
    private JPanel readPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JTextArea displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        JButton btnLoad = new JButton("Load Students");
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnLoad, BorderLayout.SOUTH);
        btnLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Clear previous data
                    displayArea.setText("");
                    // Get connection
                    Connection con = DBConnection.getConnection();
                    // SQL SELECT Query
                    String sql = "SELECT * FROM students";
                    // Create Statement
                    Statement stmt = con.createStatement();
                    // Execute Query
                    ResultSet rs = stmt.executeQuery(sql);
                    // Print heading
                    displayArea.append("ID\tName\tAge\tCourse\n");
                    displayArea.append("----------------------------------------\n");
                    // Read ResultSet row by row
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String name = rs.getString("name");
                        int age = rs.getInt("age");
                        String course = rs.getString("course");
                        // Append to TextArea
                        displayArea.append(id + "\t" + name + "\t" +
                                age + "\t" + course + "\n");
                    }

                    // Close resources
                    rs.close();
                    stmt.close();
                    con.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(panel,
                            "❌ Error Loading Students!");
                }
            }
        });
        return panel;
    }

    private JPanel updatePanel() {

        JTextField txtUpdateId, txtUpdateName, txtUpdateCourse;
        JButton btnUpdate;


        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));

        // ID Field
        panel.add(new JLabel("Student ID:"));
        txtUpdateId = new JTextField();
        panel.add(txtUpdateId);

        // New Name Field
        panel.add(new JLabel("New Name:"));
        txtUpdateName = new JTextField();
        panel.add(txtUpdateName);

        // New Course Field
        panel.add(new JLabel("New Course:"));
        txtUpdateCourse = new JTextField();
        panel.add(txtUpdateCourse);

        // Update Button
        btnUpdate = new JButton("Update Student");


        panel.add(new JLabel(""));
        panel.add(btnUpdate);

        // Button Event
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Get input values
                    int id = Integer.parseInt(txtUpdateId.getText());
                    String newName = txtUpdateName.getText();
                    String newCourse = txtUpdateCourse.getText();

                    // Connection
                    Connection con = DBConnection.getConnection();

                    // SQL UPDATE Query
                    String sql = "UPDATE students SET name = ?, course = ? WHERE id = ?";

                    // PreparedStatement
                    PreparedStatement pst = con.prepareStatement(sql);

                    pst.setString(1, newName);
                    pst.setString(2, newCourse);
                    pst.setInt(3, id);

                    // Execute Update
                    int rows = pst.executeUpdate();

                    if (rows > 0) {
                        JOptionPane.showMessageDialog(panel,
                                "✅ Student Updated Successfully!");
                    } else {
                        JOptionPane.showMessageDialog(panel,
                                "⚠ No Student Found with ID: " + id);
                    }

                    // Clear fields after update
                    txtUpdateId.setText("");
                    txtUpdateName.setText("");
                    txtUpdateCourse.setText("");

                    // Close resources
                    pst.close();
                    con.close();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(panel,
                            "❌ Error Updating Student!");
                }
            }
        });

        return panel;
    }

    // ==================================================
// TAB 4: DELETE STUDENT PANEL
// ==================================================
    private JPanel deletePanel() {
        JTextField txtDeleteId;
        JButton btnDelete;

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2, 10, 10));

        // ID Field
        panel.add(new JLabel("Student ID:"));
        txtDeleteId = new JTextField();
        panel.add(txtDeleteId);

        // Delete Button
        btnDelete = new JButton("Delete Student");

        // Button Event
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Get ID from field
                    int id = Integer.parseInt(txtDeleteId.getText());

                    // Confirmation Dialog
                    int confirm = JOptionPane.showConfirmDialog(
                            panel,
                            "Are you sure you want to delete Student ID: " + id + "?",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (confirm != JOptionPane.YES_OPTION) {
                        return;
                    }

                    // Connection
                    Connection con = DBConnection.getConnection();

                    // SQL DELETE Query
                    String sql = "DELETE FROM students WHERE id = ?";

                    // PreparedStatement
                    PreparedStatement pst = con.prepareStatement(sql);

                    pst.setInt(1, id);

                    // Execute Delete
                    int rows = pst.executeUpdate();

                    if (rows > 0) {
                        JOptionPane.showMessageDialog(panel,
                                "✅ Student Deleted Successfully!");
                    } else {
                        JOptionPane.showMessageDialog(panel,
                                "⚠ No Student Found with ID: " + id);
                    }

                    // Clear Field
                    txtDeleteId.setText("");

                    // Close resources
                    pst.close();
                    con.close();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(panel,
                            "❌ Error Deleting Student!");
                }
            }
        });

        panel.add(new JLabel(""));
        panel.add(btnDelete);

        return panel;
    }

    public static void main(String[] agrs) {
        new StudentCrudApp();

        // Call connection method
        DBConnection.getConnection();
    }

}

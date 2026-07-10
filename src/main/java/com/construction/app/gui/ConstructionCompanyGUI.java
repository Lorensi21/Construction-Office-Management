package com.construction.app.gui;
import com.construction.app.domain.*;
import com.construction.app.service.Construction;
import org.springframework.stereotype.Component;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Component
public class ConstructionCompanyGUI extends JFrame {

    private final Construction construction;

    private JList<Project> projectList;
    private DefaultListModel<Project> projectListModel;
    private JTable employeeTable;
    private DefaultTableModel employeeTableModel;

    private List<Project> memoryProjects;
    private List<Employee> memoryEmployees;

    private final Color BACKGROUND_COLOR = new Color(30, 39, 46);
    private final Color PANEL_COLOR = new Color(72, 84, 96);
    private final Color TEXT_COLOR = new Color(210, 218, 226);
    private final Color ACCENT_COLOR = new Color(11, 232, 129);

    public ConstructionCompanyGUI(Construction construction) {
        this.construction = construction;
        initUI();

    }

    private void initUI() {
        setTitle("Construction Office Management System");
        setSize(1150, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);

        UIManager.put("OptionPane.background", BACKGROUND_COLOR);
        UIManager.put("Panel.background", BACKGROUND_COLOR);
        UIManager.put("OptionPane.messageForeground", TEXT_COLOR);

        projectListModel = new DefaultListModel<>();
        projectList = new JList<>(projectListModel);
        projectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        projectList.setBackground(PANEL_COLOR);
        projectList.setForeground(Color.WHITE);
        projectList.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane projectScrollPane = new JScrollPane(projectList);
        projectScrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR), "Projects"));
        ((javax.swing.border.TitledBorder) projectScrollPane.getBorder()).setTitleColor(ACCENT_COLOR);
        projectScrollPane.getViewport().setBackground(PANEL_COLOR);

        JButton addProjectBtn = styleButton("Add New Project");

        JPanel leftPanel = new JPanel(new BorderLayout(0, 10));
        leftPanel.setBackground(BACKGROUND_COLOR);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        leftPanel.add(projectScrollPane, BorderLayout.CENTER);
        leftPanel.add(addProjectBtn, BorderLayout.SOUTH);
        addProjectBtn.addActionListener(e -> openCreateProjectDialog());

        String[] columns = {"Employee Name & Qualification", "Description", "StartRoleDate", "EndRoleDate"};
        employeeTableModel = new DefaultTableModel(columns, 0);
        employeeTable = new JTable(employeeTableModel);
        employeeTable.setBackground(PANEL_COLOR);
        employeeTable.setForeground(Color.WHITE);
        employeeTable.setGridColor(BACKGROUND_COLOR);
        employeeTable.setRowHeight(30);
        employeeTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        TableColumnModel columnModel = employeeTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(280);
        columnModel.getColumn(1).setPreferredWidth(200);
        columnModel.getColumn(2).setPreferredWidth(110);
        columnModel.getColumn(3).setPreferredWidth(110);

        JTableHeader tableHeader = employeeTable.getTableHeader();
        tableHeader.setBackground(ACCENT_COLOR);
        tableHeader.setForeground(Color.BLACK);
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane employeeScrollPane = new JScrollPane(employeeTable);
        employeeScrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR), "Employees Assigned to Selected Project"));
        ((javax.swing.border.TitledBorder) employeeScrollPane.getBorder()).setTitleColor(ACCENT_COLOR);
        employeeScrollPane.getViewport().setBackground(PANEL_COLOR);

        JButton assignEmployeeBtn = styleButton("Assign Employee to Project");



        JPanel rightPanel = new JPanel(new BorderLayout(0, 10));
        rightPanel.setBackground(BACKGROUND_COLOR);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rightPanel.add(employeeScrollPane, BorderLayout.CENTER);
        rightPanel.add(assignEmployeeBtn, BorderLayout.SOUTH);
        rightPanel.setVisible(false);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(420);
        splitPane.setBackground(BACKGROUND_COLOR);
        splitPane.setBorder(null);
        add(splitPane);

        projectList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Project selectedProject = projectList.getSelectedValue();
                if (selectedProject != null) {
                    updateEmployeeTable(selectedProject);
                    rightPanel.setVisible(true);
                }
            }
        });

        assignEmployeeBtn.addActionListener(e -> {
            Project selectedProject = projectList.getSelectedValue();
            openAssignmentDialog(selectedProject);
        });
    }

    // O(1) DB Read
    public void loadMemoryData() {
        memoryProjects = construction.loadAllProjectsInMemory();
        memoryEmployees = construction.loadAllEmployees();

        projectListModel.clear();
        for (Project p : memoryProjects) {
            projectListModel.addElement(p);
        }
    }

    private void updateEmployeeTable(Project project) {
        employeeTableModel.setRowCount(0);
        if (project.getProjectRoles().isEmpty()) {
            employeeTableModel.addRow(new Object[]{"No employees assigned.", "-", "-", "-"});
            return;
        }

        for (ProjectRole role : project.getProjectRoles()) {
            String fullName = role.getEmployee().getName() + " " + role.getEmployee().getSurname() + " - " + role.getSpecificQualificationUsed();
            employeeTableModel.addRow(new Object[]{
                    fullName,
                    role.getDescription(),
                    role.getStartRoleDate().toString(),
                    role.getEndRoleDate().toString()
            });
        }
    }
//assign emp to project
    private void openAssignmentDialog(Project project) {
        JDialog dialog = new JDialog(this, "Assign Employee to: " + project.getName(), true);
        dialog.setSize(550, 360);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(BACKGROUND_COLOR);
        dialog.setLayout(new GridLayout(6, 2, 10, 10));

        JLabel empLabel = new JLabel(" Select Employee:");
        empLabel.setForeground(TEXT_COLOR);

        JComboBox<Employee> empCombo = new JComboBox<>();
        memoryEmployees.forEach(empCombo::addItem);

        JLabel jobLabel = new JLabel(" Select Qualification:");
        jobLabel.setForeground(TEXT_COLOR);
        JComboBox<String> jobCombo = new JComboBox<>();

        empCombo.addActionListener(e -> updateJobCombo((Employee) empCombo.getSelectedItem(), jobCombo));
        if(empCombo.getItemCount() > 0) {
            updateJobCombo((Employee) empCombo.getSelectedItem(), jobCombo);
        }

        JLabel descLabel = new JLabel(" Role Description:");
        descLabel.setForeground(TEXT_COLOR);
        JTextField descField = new JTextField("Site Supervisor");

        MaskFormatter dateFormatter = null;
        try {
            dateFormatter = new MaskFormatter("####-##-##");
            dateFormatter.setPlaceholderCharacter('_');
        } catch (ParseException ignored) {}

        JLabel startLabel = new JLabel(" StartRoleDate (YYYY-MM-DD):");
        startLabel.setForeground(TEXT_COLOR);
        JFormattedTextField startField = new JFormattedTextField(dateFormatter);
        startField.setText("2024-05-01");

        JLabel endLabel = new JLabel(" EndRoleDate (YYYY-MM-DD):");
        endLabel.setForeground(TEXT_COLOR);
        JFormattedTextField endField = new JFormattedTextField(dateFormatter);
        endField.setText("2025-10-31");

        JButton saveBtn = styleButton("Save Assignment");
        JButton cancelBtn = styleButton("Cancel");

        saveBtn.addActionListener(e -> {
            try {
                LocalDate newStart = LocalDate.parse(startField.getText());
                LocalDate newEnd = LocalDate.parse(endField.getText());
                Employee selectedEmp = (Employee) empCombo.getSelectedItem();
                String qualification = (String) jobCombo.getSelectedItem();
                String description = descField.getText().trim();

                int confirm = JOptionPane.showConfirmDialog(dialog, "Are you sure you want to assign this employee?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {

                    construction.assignEmployee(project, selectedEmp, qualification, description, newStart, newEnd);

                    JOptionPane.showMessageDialog(dialog, "Assignment Saved Successfully!");
                    updateEmployeeTable(project); // Update UI
                    dialog.dispose();
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid date format!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalStateException | IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.add(empLabel); dialog.add(empCombo);
        dialog.add(jobLabel); dialog.add(jobCombo);
        dialog.add(descLabel); dialog.add(descField);
        dialog.add(startLabel); dialog.add(startField);
        dialog.add(endLabel); dialog.add(endField);
        dialog.add(saveBtn); dialog.add(cancelBtn);
        ((JComponent)dialog.getContentPane()).setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        dialog.setVisible(true);
    }

//create project
    private void openCreateProjectDialog() {
        JDialog dialog = new JDialog(this, "Create New Project", true);
        dialog.setSize(450, 300);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(BACKGROUND_COLOR);
        dialog.setLayout(new GridLayout(5, 2, 10, 10));

        JLabel nameLabel = new JLabel(" Project Name:");
        nameLabel.setForeground(TEXT_COLOR);
        JTextField nameField = new JTextField();

        JLabel budgetLabel = new JLabel(" Budget ($):");
        budgetLabel.setForeground(TEXT_COLOR);
        JTextField budgetField = new JTextField("100000.0");

        JLabel catLabel = new JLabel(" Category:");
        catLabel.setForeground(TEXT_COLOR);
        JComboBox<ProjectCategory> catCombo = new JComboBox<>(ProjectCategory.values());

        JLabel extraLabel = new JLabel(" Company Name:");
        extraLabel.setForeground(TEXT_COLOR);
        JTextField extraField = new JTextField();

        catCombo.addActionListener(e -> {
            if (catCombo.getSelectedItem() == ProjectCategory.BUSINESS) {
                extraLabel.setText(" Company Name:");
            } else {
                extraLabel.setText(" Resident Capacity:");
            }
        });

        JButton saveBtn = styleButton("Create");
        JButton cancelBtn = styleButton("Cancel");

        saveBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Project Name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Double budget = Double.parseDouble(budgetField.getText().trim());
                if (budget < 0) {
                    JOptionPane.showMessageDialog(dialog, "Budget cannot be negative!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                ProjectCategory category = (ProjectCategory) catCombo.getSelectedItem();
                String extraDetail = extraField.getText().trim();

                // Save to db
                construction.createProject(name, budget, category, extraDetail);

                JOptionPane.showMessageDialog(dialog, "Project Created Successfully!");

                // Refresh the memory list and GUI
                loadMemoryData();
                dialog.dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Budget must be a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        dialog.add(nameLabel); dialog.add(nameField);
        dialog.add(budgetLabel); dialog.add(budgetField);
        dialog.add(catLabel); dialog.add(catCombo);
        dialog.add(extraLabel); dialog.add(extraField);
        dialog.add(saveBtn); dialog.add(cancelBtn);

        ((JComponent)dialog.getContentPane()).setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        dialog.setVisible(true);
    }

    private void updateJobCombo(Employee employee, JComboBox<String> jobCombo) {
        jobCombo.removeAllItems();
        if (employee != null && employee.getEmployeeTypes() != null) {
            if (employee.getEmployeeTypes().contains(EmployeeType.MANAGER)) jobCombo.addItem(employee.getManagementTier());
            if (employee.getEmployeeTypes().contains(EmployeeType.ENGINEER)) jobCombo.addItem("Engineer (" + employee.getSpecialty() + ")");
            if (employee.getEmployeeTypes().contains(EmployeeType.LANDSCAPE_ARCHITECT)) jobCombo.addItem("Landscape Architect");
        }
    }

    private JButton styleButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(ACCENT_COLOR);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        return btn;
    }
}

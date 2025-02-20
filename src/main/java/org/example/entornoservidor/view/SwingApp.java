package org.example.entornoservidor.view;

import org.example.entornoservidor.model.Employee;
import org.example.entornoservidor.repository.EmployeeRepository;
import org.example.entornoservidor.repository.Repository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class SwingApp extends JFrame {

    private final Repository<Employee> employeeRepository;
    private final JTable employeeTable;

    // Campos de búsqueda avanzada
    private final JTextField nombreField;
    private final JTextField cargoField;
    private final JTextField departamentoField;
    private final JTextField salarioMinField;
    private final JTextField salarioMaxField;

    public SwingApp() throws SQLException {
        // Configurar la ventana
        setTitle("Gestión de Empleados");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);

        // Centrar la ventana
        setLocationRelativeTo(null);

        // Crear una tabla para mostrar los empleados
        employeeTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        add(scrollPane, BorderLayout.CENTER);

        // Crear un panel para la búsqueda avanzada
        JPanel searchPanel = new JPanel(new GridLayout(2, 5, 5, 5));

        nombreField = new JTextField();
        cargoField = new JTextField();
        departamentoField = new JTextField();
        salarioMinField = new JTextField();
        salarioMaxField = new JTextField();

        searchPanel.add(new JLabel("Nombre:"));
        searchPanel.add(nombreField);
        searchPanel.add(new JLabel("Cargo:"));
        searchPanel.add(cargoField);
        searchPanel.add(new JLabel("Departamento:"));
        searchPanel.add(departamentoField);
        searchPanel.add(new JLabel("Salario Min:"));
        searchPanel.add(salarioMinField);
        searchPanel.add(new JLabel("Salario Max:"));
        searchPanel.add(salarioMaxField);

        // Botón para aplicar filtros de búsqueda
        JButton buscarButton = new JButton("Buscar");
        buscarButton.addActionListener(e -> {
            try {
                aplicarFiltros();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        searchPanel.add(buscarButton);

        add(searchPanel, BorderLayout.NORTH);

        // Crear botones para acciones
        JButton agregarButton = new JButton("Agregar");
        JButton actualizarButton = new JButton("Actualizar");
        JButton eliminarButton = new JButton("Eliminar");

        // Configurar el panel de botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(agregarButton);
        buttonPanel.add(actualizarButton);
        buttonPanel.add(eliminarButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Establecer estilos para los botones
        agregarButton.setBackground(new Color(46, 204, 113));
        agregarButton.setForeground(Color.BLACK);
        agregarButton.setFocusPainted(false);
        agregarButton.setOpaque(true);
        agregarButton.setBorderPainted(false);

        actualizarButton.setBackground(new Color(52, 152, 219));
        actualizarButton.setForeground(Color.BLACK);
        actualizarButton.setFocusPainted(false);
        actualizarButton.setOpaque(true);
        actualizarButton.setBorderPainted(false);

        eliminarButton.setBackground(new Color(231, 76, 60));
        eliminarButton.setForeground(Color.BLACK);
        eliminarButton.setFocusPainted(false);
        eliminarButton.setOpaque(true);
        eliminarButton.setBorderPainted(false);

        // Crear el objeto Repository para acceder a la base de datos
        employeeRepository = new EmployeeRepository();

        // Cargar los empleados iniciales en la tabla sin filtros
        refreshEmployeeTable(null, null, null, null, null);

        // Agregar ActionListener para los botones
        agregarButton.addActionListener(e -> {
            try {
                agregarEmpleado();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        actualizarButton.addActionListener(e -> actualizarEmpleado());

        eliminarButton.addActionListener(e -> eliminarEmpleado());
    }

    private void refreshEmployeeTable(String nombre, String cargo, String departamento, Float salarioMin, Float salarioMax) throws SQLException {
        List<Employee> employees = employeeRepository.findByFilters(nombre, cargo, departamento, salarioMin, salarioMax);

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nombre");
        model.addColumn("Cargo");
        model.addColumn("Departamento");
        model.addColumn("Rango Salarial");

        for (Employee employee : employees) {
            Object[] rowData = {
                    employee.getId(),
                    employee.getNombre(),
                    employee.getCargo(),
                    employee.getDepartamento(),
                    employee.getRango_salarial(),
            };
            model.addRow(rowData);
        }

        employeeTable.setModel(model);
    }

    private void aplicarFiltros() throws SQLException {
        String nombre = nombreField.getText().trim().isEmpty() ? null : nombreField.getText().trim();
        String cargo = cargoField.getText().trim().isEmpty() ? null : cargoField.getText().trim();
        String departamento = departamentoField.getText().trim().isEmpty() ? null : departamentoField.getText().trim();

        Float salarioMin = null, salarioMax = null;
        try {
            if (!salarioMinField.getText().trim().isEmpty()) {
                salarioMin = Float.parseFloat(salarioMinField.getText().trim());
            }
            if (!salarioMaxField.getText().trim().isEmpty()) {
                salarioMax = Float.parseFloat(salarioMaxField.getText().trim());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese valores numéricos válidos para salario", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        refreshEmployeeTable(nombre, cargo, departamento, salarioMin, salarioMax);
    }

    private void agregarEmpleado() throws SQLException {
        JTextField nombreField = new JTextField();
        JTextField cargoField = new JTextField();
        JTextField departamentoField = new JTextField();
        JTextField rangoSalarialField = new JTextField();

        Object[] fields = {
                "Nombre:", nombreField,
                "Cargo:", cargoField,
                "Departamento:", departamentoField,
                "Rango Salarial:", rangoSalarialField,
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Agregar Empleado", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            Employee employee = new Employee();
            employee.setNombre(nombreField.getText());
            employee.setCargo(cargoField.getText());
            employee.setDepartamento(departamentoField.getText());
            employee.setRango_salarial(Float.parseFloat(rangoSalarialField.getText()));

            employeeRepository.save(employee);
            refreshEmployeeTable(null, null, null, null, null);
            JOptionPane.showMessageDialog(this, "Empleado agregado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void actualizarEmpleado() {
        String empleadoIdStr = JOptionPane.showInputDialog(this, "Ingrese el ID del empleado a actualizar:", "Actualizar Empleado", JOptionPane.QUESTION_MESSAGE);
        if (empleadoIdStr != null) {
            try {
                int empleadoId = Integer.parseInt(empleadoIdStr);
                Employee empleado = employeeRepository.getById(empleadoId);

                if (empleado != null) {
                    JTextField nombreField = new JTextField(empleado.getNombre());
                    JTextField cargoField = new JTextField(empleado.getCargo());
                    JTextField departamentoField = new JTextField(empleado.getDepartamento());
                    JTextField rangoSalarialField = new JTextField(String.valueOf(empleado.getRango_salarial()));

                    Object[] fields = {
                            "Nombre:", nombreField,
                            "Cargo:", cargoField,
                            "Departamento:", departamentoField,
                            "Rango Salarial:", rangoSalarialField,
                    };

                    int confirmResult = JOptionPane.showConfirmDialog(this, fields, "Actualizar Empleado", JOptionPane.OK_CANCEL_OPTION);
                    if (confirmResult == JOptionPane.OK_OPTION) {
                        empleado.setNombre(nombreField.getText());
                        empleado.setCargo(cargoField.getText());
                        empleado.setDepartamento(departamentoField.getText());
                        empleado.setRango_salarial(Float.parseFloat(rangoSalarialField.getText()));

                        employeeRepository.save(empleado);
                        refreshEmployeeTable(null, null, null, null, null);
                    }
                }
            } catch (NumberFormatException | SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al actualizar empleado", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarEmpleado() {
        // Obtener el ID del empleado a eliminar
        String empleadoIdStr = JOptionPane.showInputDialog(this, "Ingrese el ID del empleado a eliminar:", "Eliminar Empleado", JOptionPane.QUESTION_MESSAGE);
        if (empleadoIdStr != null) {
            try {
                int empleadoId = Integer.parseInt(empleadoIdStr);

                // Confirmar la eliminación del empleado
                int confirmResult = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar el empleado?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
                if (confirmResult == JOptionPane.YES_OPTION) {
                    // Eliminar el empleado de la base de datos
                    employeeRepository.delete(empleadoId);

                    // Actualizar la tabla de empleados en la interfaz
                    refreshEmployeeTable();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Ingrese un valor numérico válido para el ID del empleado", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshEmployeeTable() {
    }
}


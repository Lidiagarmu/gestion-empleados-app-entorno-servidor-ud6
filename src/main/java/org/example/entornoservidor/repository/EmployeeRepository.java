package org.example.entornoservidor.repository;
import org.example.entornoservidor.model.Employee;
import org.example.entornoservidor.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepository implements Repository<Employee> {



    //------------------------CLASE CON MÉTODOS BUSCARUNO, BUSCARTODOS, GUARDAR, BORRAR...
    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }


    @Override //metodo para leer todos
    public List<Employee> findAll() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM empleados";

        try (Connection myCon = getConnection();
             Statement myStat = myCon.createStatement();
             ResultSet myRes = myStat.executeQuery(sql)) {
            while (myRes.next()) {
                employees.add(createEmployee(myRes));
                //el metodo createEmployee() lo extrajimos. Lo tenemos abajo detodo.
            }
        }
        return employees;
    }


    @Override //metodo para leer uno
    public Employee getById(Integer id) throws SQLException {
        Employee employee = null;
        String sql = "SELECT * FROM empleados WHERE id = ?";

        try (Connection myCon = getConnection();
             PreparedStatement myStat = myCon.prepareStatement(sql)) {
            myStat.setInt(1, id);
            try (ResultSet myRes = myStat.executeQuery()) {
                if (myRes.next()) {
                    employee = createEmployee(myRes);
                }
            }
        }
        return employee;
    }


    @Override //metodo para insertar o guardar empleado
    public void save(Employee employee) throws SQLException {
        String sql;
        if (employee.getId() != null && employee.getId() > 0) {
            sql = "UPDATE empleados SET nombre=?, cargo=?, departamento=?, rango_salarial=? WHERE id=?";
        } else {
            sql = "INSERT INTO empleados (nombre, cargo, departamento, rango_salarial) VALUES (?, ?, ?, ?)";
        }

        try (Connection myCon = getConnection();
             PreparedStatement myStat = myCon.prepareStatement(sql)) {
            myStat.setString(1, employee.getNombre());
            myStat.setString(2, employee.getCargo());
            myStat.setString(3, employee.getDepartamento());
            myStat.setFloat(4, employee.getRango_salarial());

            if (employee.getId() != null && employee.getId() > 0) {
                myStat.setInt(5, employee.getId());
            }

            myStat.executeUpdate();
        }
    }

    @Override //metodo para borrar empleado
    public void delete(Integer id) {
        String sql = "DELETE FROM empleados WHERE id=?";

        try (Connection myCon = getConnection();
             PreparedStatement myStat = myCon.prepareStatement(sql)) {
            myStat.setInt(1, id);
            myStat.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




    private Employee createEmployee(ResultSet myRes) throws SQLException {
        return new Employee(
                myRes.getInt("id"),
                myRes.getString("nombre"),
                myRes.getString("cargo"),
                myRes.getString("departamento"),
                myRes.getFloat("rango_salarial")
        );
    }


    //Funcion para busqueda por filtros
    private final Connection connection;

    public EmployeeRepository() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    public List<Employee> findByFilters(String nombre, String cargo, String departamento, Float salarioMin, Float salarioMax) throws SQLException {
        String sql = "SELECT * FROM empleados WHERE 1=1";
        List<Object> params = new ArrayList<>();

        if (nombre != null) {
            sql += " AND nombre LIKE ?";
            params.add("%" + nombre + "%");
        }
        if (cargo != null) {
            sql += " AND cargo LIKE ?";
            params.add("%" + cargo + "%");
        }
        if (departamento != null) {
            sql += " AND departamento LIKE ?";
            params.add("%" + departamento + "%");
        }
        if (salarioMin != null) {
            sql += " AND rango_salarial >= ?";
            params.add(salarioMin);
        }
        if (salarioMax != null) {
            sql += " AND rango_salarial <= ?";
            params.add(salarioMax);
        }

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }

            ResultSet resultSet = statement.executeQuery();
            List<Employee> employees = new ArrayList<>();
            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setId(resultSet.getInt("id"));
                employee.setNombre(resultSet.getString("nombre"));
                employee.setCargo(resultSet.getString("cargo"));
                employee.setDepartamento(resultSet.getString("departamento"));
                employee.setRango_salarial(resultSet.getFloat("rango_salarial"));
                employees.add(employee);
            }
            return employees;
        }
    }

}





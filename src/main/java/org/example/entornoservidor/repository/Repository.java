package org.example.entornoservidor.repository;

import java.sql.SQLException;
import java.util.List;

public interface Repository <T> {

    //metodos que desarrollaremos en EmployeeRepository

    List<T> findAll() throws SQLException;
    T getById(Integer id) throws SQLException;
    void save (T t) throws SQLException;
    void delete (Integer id);

    List<T> findByFilters(String nombre, String cargo, String departamento, Float salarioMin, Float salarioMax) throws SQLException;
}

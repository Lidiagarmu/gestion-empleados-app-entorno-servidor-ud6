package org.example.entornoservidor.util;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {


    //-------------CLASE PARA CONECTAR BASE DE DATOS
    private static String url = "jdbc:mysql://localhost:3306/app_empleados";
    private static String user = "root";
    private static String password = "" ;

    private static BasicDataSource pool;

    public static BasicDataSource getInstance() throws SQLException {

        if(pool == null){
            pool = new BasicDataSource();
            pool.setUrl(url);
            pool.setUsername(user);
            pool.setPassword(password);

            //estas configuraciones dependen de lo que vayamos a necesitar
            pool.setInitialSize(3); //tamaño inicial de nuestro pool de conexiones
            pool.setMinIdle(3); //numero minimo de conexiones inactivas que se deben de mantener
            pool.setMaxIdle(10); // establece el nume maxi de conexiones inact que se deben mantener
            pool.setMaxTotal(10); //numero de conexiones total que el pool puede mantener activas simultaneamente
        } return  pool;
    }

    public static Connection getConnection() throws SQLException {
        return getInstance().getConnection();

    }
}

package org.example.entornoservidor.model;

public class Employee {

    //entidad que representa los datos para nuestros empleados
        private Integer id;
        private String nombre;
        private String cargo;
        private String departamento;
        private Float rango_salarial;

        public Employee() {}

        public Employee(Integer id, String nombre, String cargo, String departamento, Float rango_salarial) {
            this.id = id;
            this.nombre = nombre;
            this.cargo = cargo;
            this.departamento = departamento;
            this.rango_salarial = rango_salarial;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getCargo() {
            return cargo;
        }

        public void setCargo(String cargo) {
            this.cargo = cargo;
        }

        public String getDepartamento() {
            return departamento;
        }

        public void setDepartamento(String departamento) {
            this.departamento = departamento;
        }

        public Float getRango_salarial() {
            return rango_salarial;
        }

        public void setRango_salarial(Float rango_salarial) {
            this.rango_salarial = rango_salarial;
        }

        @Override
        public String toString() {
            return "Empleado{" +
                    "ID=" + id +
                    ", Nombre='" + nombre + '\'' +
                    ", Cargo='" + cargo + '\'' +
                    ", Departamento='" + departamento + '\'' +
                    ", Rango Salarial=" + rango_salarial +
                    '}';
        }
    }
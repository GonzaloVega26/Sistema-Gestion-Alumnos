/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ar.vega.gonzalo.modelo;

import java.util.Date;

/**
 *
 * @author Gonzalo Vega <ar.vega.gonzalo>
 */
public class PersonaModelo {
    protected long dni;
    protected String nombre;
    protected String apellido;
    protected String domicilio;
    protected String telefono;
    protected Date fechaNacimiento;

    public PersonaModelo() {
    }

    
    public long getDni() {
        return dni;
    }

    public void setDni(long dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    
     public Object[] getDatos() {
        return new Object[]{nombre, apellido, dni, fechaNacimiento, telefono, domicilio};
    }

    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.vega.gonzalo.dao;

import ar.vega.gonzalo.modelo.ProfesorModelo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProfesorDAO {

    private ArrayList<ProfesorModelo> profesores;
    private ProfesorModelo profesor;
    
    //QUERIES
    private final String SQL_SELECT = "SELECT * FROM profesor";
    private final String SQL_GETONE = "SELECT * FROM profesor WHERE prof_dni=?";
    private final String SQL_INSERT = "INSERT INTO profesor (prof_dni, prof_nombre, prof_apellido,"
                            + " prof_domicilio, prof_telefono,prof_fec_nac) VALUES (?,?,?,?,?,?)";
    private final String SQL_DELETE = "DELETE FROM profesor WHERE prof_dni = ?";
    private final String SQL_UPDATE = "UPDATE profesor SET prof_nombre=?, prof_apellido=?,"
                        + " prof_domicilio=?, prof_telefono=?, prof_fec_nac=? WHERE prof_dni=?";
    

    public ArrayList<ProfesorModelo> traeProfesoresDAO() {
        profesores = new ArrayList();
        try (Connection conn = SQLQuery.getInstance().getConnection();
                PreparedStatement consulta = conn.prepareStatement(SQL_SELECT);
                ResultSet results = consulta.executeQuery();) {

            while (results.next()) {
                profesor = new ProfesorModelo();
                profesor.setDni(results.getLong("prof_dni"));
                profesor.setNombre(results.getString("prof_nombre"));
                profesor.setApellido(results.getString("prof_apellido"));
                profesor.setDomicilio(results.getString("prof_domicilio"));
                profesor.setTelefono(results.getString("prof_telefono"));
                profesor.setFechaNacimiento(results.getDate("prof_fec_nac"));
                profesores.add(profesor);
            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProfesorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return profesores;

    }

    public ProfesorModelo traeUNProfesorDAO(Long dni) {
        ProfesorModelo profesor = new ProfesorModelo();
        try (Connection conn = SQLQuery.getInstance().getConnection();
                PreparedStatement consulta = conn.prepareStatement(SQL_GETONE);
            ) {
            consulta.setLong(1, dni);
            try (ResultSet resultados = consulta.executeQuery();) {
                if (resultados.next()) {
                    profesor.setDni(resultados.getLong("prof_dni"));
                    profesor.setNombre(resultados.getString("prof_nombre"));
                    profesor.setApellido(resultados.getString("prof_apellido"));
                    profesor.setFechaNacimiento(resultados.getDate("prof_fec_nac"));
                    profesor.setDomicilio(resultados.getString("prof_domicilio"));
                    profesor.setTelefono(resultados.getString("prof_telefono"));
                } 
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AlumnoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return profesor;

    }

    public boolean cargaDatosDAO(ProfesorModelo profesor) {
        try (Connection conn = SQLQuery.getInstance().getConnection();
            PreparedStatement consulta = conn.prepareStatement(
                    SQL_INSERT);
                ) {
            consulta.setLong(1, profesor.getDni());
            consulta.setString(2, profesor.getNombre());
            consulta.setString(3, profesor.getApellido());
            consulta.setString(4, profesor.getDomicilio());
            consulta.setString(5, profesor.getTelefono());
            consulta.setDate(6, new java.sql.Date(profesor.getFechaNacimiento().getTime())); //Fecha Nacimiento
            consulta.execute();

            return true;

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AlumnoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    public boolean bajaByDNIDAO(Long dni) {
        try (Connection conn = SQLQuery.getInstance().getConnection();
                PreparedStatement consulta = conn.prepareStatement(SQL_DELETE);) {
            consulta.setLong(1, dni);
            consulta.executeUpdate();

            return true;

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AlumnoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean modificaDAO(ProfesorModelo profesor) {
        try (Connection conn = SQLQuery.getInstance().getConnection();
                PreparedStatement consulta = conn.prepareStatement(SQL_UPDATE);
            ) {
            consulta.setString(1, profesor.getNombre());
            consulta.setString(2, profesor.getApellido());
            consulta.setString(3, profesor.getDomicilio());
            consulta.setString(4, profesor.getTelefono());
            consulta.setDate(5, new java.sql.Date(profesor.getFechaNacimiento().getTime()));
            consulta.setLong(6, profesor.getDni());
            consulta.executeUpdate();
            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProfesorDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean dniRepetidoDAO(ProfesorModelo profesor) {

        try (Connection conn = SQLQuery.getInstance().getConnection(); PreparedStatement consulta = conn.prepareStatement("SELECT * FROM profesor WHERE dni_prof=?"); ResultSet resultados = consulta.executeQuery();) {
            consulta.setLong(1, profesor.getDni());
            ResultSet hojadeResultados = consulta.executeQuery();
            if (hojadeResultados.next()) {
                return true;
            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AlumnoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}

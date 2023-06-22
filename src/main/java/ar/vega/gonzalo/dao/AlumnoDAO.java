package ar.vega.gonzalo.dao;

import ar.vega.gonzalo.exception.DBException;
import ar.vega.gonzalo.modelo.AlumnoModelo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AlumnoDAO {

    private ArrayList<AlumnoModelo> alumnos;
    private AlumnoModelo alumno;

    //Queries
    private final String SQL_INSERT = "INSERT INTO alumno (alu_dni, alu_nombre, alu_apellido, alu_domicilio, alu_telefono, alu_fec_nac)"
            + " VALUES (?,?,?,?,?,?)";
    private final String SQL_SELECT = "SELECT * FROM alumno";
    private final String SQL_GETONE = "SELECT * FROM alumno where alu_dni = ?";
    private final String SQL_DELETE = "DELETE FROM alumno WHERE alu_dni = ?";
    private final String SQL_UPDATE = "UPDATE alumno SET alu_nombre=?, alu_apellido=?,"
                + " alu_domicilio=?, alu_telefono=?, alu_fec_nac=? WHERE alu_dni=?";

    public boolean cargaDatosDAO(AlumnoModelo alumno) throws DBException {
        try (Connection conn = SQLQuery.getInstance().getConnection();
                PreparedStatement preparedStmt = conn.prepareStatement(SQL_INSERT);
            ) {
            preparedStmt.setLong(1, alumno.getDni()); //DNI
            preparedStmt.setString(2, alumno.getNombre()); //Nombre
            preparedStmt.setString(3, alumno.getApellido());  //Apellido
            preparedStmt.setString(4, alumno.getDomicilio()); //Domicilio
            preparedStmt.setString(5, alumno.getTelefono()); //Telefono
            preparedStmt.setDate(6, new java.sql.Date(alumno.getFechaNacimiento().getTime())); //Fecha Nacimiento
            preparedStmt.execute();
            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AlumnoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DBException("DNI : " + alumno.getDni() + " ya ha sido insertado");
        }

    }

    public ArrayList<AlumnoModelo> traeAlumnosDAO() {
        alumnos = new ArrayList();
        try (Connection conn = SQLQuery.getInstance().getConnection();
                PreparedStatement consulta = conn.prepareStatement(SQL_SELECT);
                ResultSet results = consulta.executeQuery();
            ) {
            while (results.next()) {
                alumno = new AlumnoModelo();
                alumno.setDni(results.getLong("alu_dni"));
                alumno.setNombre(results.getString("alu_nombre"));
                alumno.setApellido(results.getString("alu_apellido"));
                alumno.setDomicilio(results.getString("alu_domicilio"));
                alumno.setTelefono(results.getString("alu_telefono"));
                alumno.setFechaNacimiento(results.getDate("alu_fec_nac"));
                alumnos.add(alumno);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AlumnoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return alumnos;

    }

    public AlumnoModelo traeAlumnoDAO(Long dni) {
        AlumnoModelo alumno = new AlumnoModelo();
        try (Connection conn = SQLQuery.getInstance().getConnection();
                PreparedStatement consulta = conn.prepareStatement(SQL_GETONE);
            ) {
            consulta.setLong(1, dni);
            try (ResultSet results = consulta.executeQuery();) {
                while (results.next()) {
                    alumno.setDni(results.getLong("alu_dni"));
                    alumno.setNombre(results.getString("alu_nombre"));
                    alumno.setApellido(results.getString("alu_apellido"));
                    alumno.setDomicilio(results.getString("alu_domicilio"));
                    alumno.setTelefono(results.getString("alu_telefono"));
                    alumno.setFechaNacimiento(results.getDate("alu_fec_nac"));
                }
            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AlumnoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return alumno;

    }

    public boolean bajaByDNIDAO(Long dni) {
        try (Connection conn = SQLQuery.getInstance().getConnection();
                PreparedStatement consulta = conn.prepareStatement(SQL_DELETE);
            ) {
            consulta.setLong(1, dni);
            consulta.executeUpdate();

            return true;

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AlumnoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean modificaDAO(AlumnoModelo alumno) {
        try (Connection conn = SQLQuery.getInstance().getConnection();
                PreparedStatement consulta = conn.prepareStatement(SQL_UPDATE);
            ) {
            consulta.setString(1, alumno.getNombre());
            consulta.setString(2, alumno.getApellido());
            consulta.setString(3, alumno.getDomicilio());
            consulta.setString(4, alumno.getTelefono());
            consulta.setDate(5, new java.sql.Date(alumno.getFechaNacimiento().getTime()));
            consulta.setLong(6, alumno.getDni());
            consulta.executeUpdate();
            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AlumnoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

}

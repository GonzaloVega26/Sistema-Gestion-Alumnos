package ar.vega.gonzalo.dao;

import ar.vega.gonzalo.modelo.MateriaModelo;
import ar.vega.gonzalo.modelo.ProfesorModelo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MateriaDAO {

    private MateriaModelo materia;
    private ArrayList<MateriaModelo> materias;


    //QUERIES
    private final String SQL_SELECT = "SELECT * FROM materia M LEFT JOIN profesor P ON M.mat_prof_dni = P.prof_dni " +
                        "WHERE M.mat_prof_dni IS NULL OR P.prof_dni IS NOT NULL";
    private final String SQL_INSERT = "INSERT INTO materia (mat_nombre, cant_horas, mat_prof_dni) VALUES (?,?,?)";
    private final String SQL_DELETE = "DELETE FROM materia WHERE mat_cod = ?";
    private final String SQL_UPDATE = "UPDATE materia SET mat_nombre=?, cant_horas=?, mat_prof_dni=?  WHERE mat_cod=?";
    private final String SQL_GETONE = "SELECT * FROM materia WHERE mat_cod=?";
    public ArrayList<MateriaModelo> traeMateriasDAO() {
        materias = new ArrayList();
        try (Connection conn = SQLQuery.getInstance().getConnection();
                PreparedStatement consulta = conn.prepareStatement(SQL_SELECT);
                ResultSet resultados = consulta.executeQuery()
            ) {

            while (resultados.next()) {
                materia = new MateriaModelo();
                ProfesorModelo profesor = new ProfesorModelo();
                //Datos Materia
                materia.setCodigo(resultados.getInt("mat_cod"));
                materia.setNombre(resultados.getString("mat_nombre"));
                materia.setCantHoras(resultados.getInt("cant_horas"));
                //Datos profesor
                profesor.setDni(resultados.getLong("prof_dni"));
                profesor.setNombre(resultados.getString("prof_nombre"));
                profesor.setApellido(resultados.getString("prof_apellido"));
                profesor.setDomicilio(resultados.getString("prof_domicilio"));
                profesor.setTelefono(resultados.getString("prof_telefono"));
                profesor.setFechaNacimiento(resultados.getDate("prof_fec_nac"));
                
                materia.setProfesor(profesor);
                
                materias.add(materia);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(MateriaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return materias;

    }

    public int cargaDatosDAO(MateriaModelo materia) {
        try (Connection conn = SQLQuery.getInstance().getConnection();
                PreparedStatement consulta = conn.prepareStatement(SQL_INSERT,Statement.RETURN_GENERATED_KEYS);
            ) {
            consulta.setString(1, materia.getNombre());
            consulta.setInt(2, materia.getCantHoras());
            consulta.setLong(3, materia.getProfesor().getDni());

            consulta.executeUpdate();
            // Obtener el ID generado
            try (ResultSet generatedKeys = consulta.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idGenerado = generatedKeys.getInt(1);
                    return idGenerado;
                } else {
                    throw new SQLException("No se pudo obtener el ID generado.");
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(MateriaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return -1; // O cualquier valor que indique un error
        }

    }
    
    public boolean bajaDAO(int codigo) {
        try (Connection conn = SQLQuery.getInstance().getConnection();
                PreparedStatement consulta = conn.prepareStatement(SQL_DELETE);
            ) {
            consulta.setLong(1, codigo);
            consulta.executeUpdate();
            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(MateriaDAO.class.getName()).log(Level.SEVERE, null, ex);

            return false;
        }
    }

    public boolean modificaDAO(MateriaModelo materia) {

        try (Connection conn = SQLQuery.getInstance().getConnection();
                PreparedStatement consulta = conn.prepareStatement(SQL_UPDATE);
            ) {
            consulta.setString(1, materia.getNombre());
            consulta.setInt(2, materia.getCantHoras());
            consulta.setLong(3, materia.getProfesor().getDni());
            consulta.setInt(4, materia.getCodigo());
            consulta.executeUpdate();
            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(MateriaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }
    
    public MateriaModelo traeMateria(int codigo) {
        MateriaModelo materia = new MateriaModelo();
        try (Connection conn = SQLQuery.getInstance().getConnection();
                PreparedStatement consulta = conn.prepareStatement(SQL_GETONE);) {
            consulta.setLong(1, codigo);

            try (ResultSet resultado = consulta.executeQuery();) {
                if (resultado.next()) {
                    materia.setCodigo(resultado.getInt("mat_cod"));
                    materia.setNombre(resultado.getString("mat_nombre"));
                    materia.setCantHoras(resultado.getInt("cant_horas"));
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(MateriaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return materia;
    }

}

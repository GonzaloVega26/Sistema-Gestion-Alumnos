package ar.vega.gonzalo.dao;

import ar.vega.gonzalo.modelo.AlumnoModelo;
import ar.vega.gonzalo.modelo.MateriaModelo;
import ar.vega.gonzalo.modelo.CursadoModelo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CursadoDAO {

    private ArrayList<CursadoModelo> listaCursados;
    private CursadoModelo cursado;
    
    //QUERIES
    private final String SQL_SELECT = "SELECT * FROM cursado";
    private final String SQL_CURSADOS_MATERIAS = "SELECT * FROM alumno A JOIN cursado C ON "
            + "C.cur_alu_dni = A.alu_dni JOIN materia M ON M.mat_cod = C.cur_mat_cod "
                + "WHERE C.cur_mat_cod = ?";
    private final String SQL_INSERT = "INSERT INTO cursado (cur_alu_dni, cur_mat_cod, cur_nota) VALUES (?,?,?)";
    private final String SQL_UPDATE = "UPDATE cursado SET cur_nota=? WHERE cur_alu_dni=? AND cur_mat_cod=?";
    private final String SQL_DELETE = "DELETE FROM cursado WHERE cur_alu_dni = ? AND cur_mat_cod = ?";
    private final String SQL_NOINSCRIPTOS = "SELECT A.* FROM alumno A LEFT JOIN cursado C ON A.alu_dni = C.cur_alu_dni AND C.cur_mat_cod = ? WHERE C.cur_alu_dni IS NULL";
    
    public ArrayList<CursadoModelo> traeCursadosDAO() {
        listaCursados = new ArrayList();
        try (Connection conn = SQLQuery.getInstance().getConnection();
                PreparedStatement consulta = conn.prepareStatement(SQL_SELECT);
                ResultSet resultados = consulta.executeQuery();) {

            while (resultados.next()) {
                cursado = new CursadoModelo();
                MateriaModelo materia = new MateriaModelo();
                cursado.setMateria(materia);
                cursado.setAlumno(new AlumnoModelo());
                cursado.getAlumno().setDni(resultados.getLong(1));
                cursado.getMateria().setCodigo(resultados.getInt(2));
                cursado.setNota(resultados.getInt(3));
                listaCursados.add(cursado);
            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(CursadoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listaCursados;

    }
    
    public ArrayList<CursadoModelo> traeCursadosByMateriaDAO(int codigoMateria) {
        listaCursados = new ArrayList();
        try (Connection conn = SQLQuery.getInstance().getConnection(); 
                PreparedStatement consulta = conn.prepareStatement(
                    SQL_CURSADOS_MATERIAS);) {
            consulta.setInt(1, codigoMateria);
            try (ResultSet resultados = consulta.executeQuery()) {
                while (resultados.next()) {
                    cursado = new CursadoModelo(); //Reinicio Cursado
                    MateriaModelo materia = new MateriaModelo(); //Creo materia nueva
                    //Seteo datos de Materia
                    materia.setNombre(resultados.getString("mat_nombre"));
                    materia.setCodigo(Integer.parseInt(resultados.getString("mat_cod")));
                    materia.setCantHoras(Integer.parseInt(resultados.getString("cant_horas")));
                    cursado.setMateria(materia); //Se lo agrego a Cursado
                    AlumnoModelo alumno = new AlumnoModelo(); //Creo alumno nuevo
                    //Seteo de datos de alumno
                    alumno.setDni(resultados.getLong("alu_dni"));
                    alumno.setNombre(resultados.getString("alu_nombre"));
                    alumno.setApellido(resultados.getString("alu_apellido"));
                    cursado.setAlumno(alumno); // Se lo agregoa cursado
                    
                    cursado.setNota(resultados.getDouble("cur_nota"));
                    listaCursados.add(cursado);
                }
            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(CursadoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listaCursados;

    }

    public boolean cargaDatosDAO(CursadoModelo cursado) {
        try (Connection conn = SQLQuery.getInstance().getConnection();
                PreparedStatement consulta = conn.prepareStatement(
                        SQL_INSERT);
                ) {
            consulta.setLong(1, cursado.getAlumno().getDni());
            consulta.setInt(2, cursado.getMateria().getCodigo());
            consulta.setDouble(3, cursado.getNota());
            consulta.execute();
            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(CursadoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    public boolean bajaDAO(long dni, int codigoMateria) {
        try (Connection conn = SQLQuery.getInstance().getConnection();
                PreparedStatement consulta = conn.prepareStatement(SQL_DELETE);
            ) {
            consulta.setLong(1, dni);
            consulta.setInt(2, codigoMateria);
            consulta.executeUpdate();
            return true;

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(CursadoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean modificaDAO(CursadoModelo cursado) {
        try (Connection conn = SQLQuery.getInstance().getConnection();
                PreparedStatement consulta = conn.prepareStatement(SQL_UPDATE);
            ) {
            consulta.setDouble(1, cursado.getNota());
            consulta.setLong(2, cursado.getAlumno().getDni());
            consulta.setInt(3, cursado.getMateria().getCodigo());
            consulta.executeUpdate();
            return true;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(CursadoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }
    
    public ArrayList<AlumnoModelo> traeAlumnosNOInscriptos(int codigoMateria) {
        ArrayList<AlumnoModelo> lista = new ArrayList<>();
        try (Connection conn = SQLQuery.getInstance().getConnection();
                PreparedStatement consulta = conn.prepareStatement(SQL_NOINSCRIPTOS);
            ) {
            consulta.setInt(1, codigoMateria);
            try (ResultSet resultados = consulta.executeQuery();) {
                while (resultados.next()) {
                    AlumnoModelo alumno = new AlumnoModelo();
                    alumno.setNombre(resultados.getString("alu_nombre"));
                    alumno.setApellido(resultados.getString("alu_apellido"));
                    alumno.setDni(resultados.getLong("alu_dni"));
                    lista.add(alumno);
                }
            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(CursadoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

}

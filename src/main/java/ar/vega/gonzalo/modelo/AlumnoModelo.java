package ar.vega.gonzalo.modelo;

import ar.vega.gonzalo.dao.AlumnoDAO;
import ar.vega.gonzalo.exception.DBException;
import java.util.ArrayList;

public class AlumnoModelo extends PersonaModelo {

    private AlumnoDAO alumnoDAO = new AlumnoDAO();

    public AlumnoModelo() {
    }

    public boolean cargaDatos(AlumnoModelo alumno) throws DBException {
        return alumnoDAO.cargaDatosDAO(alumno);
    }

    public ArrayList<AlumnoModelo> traeAlumnos() {
        return alumnoDAO.traeAlumnosDAO();
    }

    public AlumnoModelo traeAlumno(Long dni) {
        return alumnoDAO.traeAlumnoDAO(dni);
    }

    public boolean baja(Long dni) {
        return alumnoDAO.bajaByDNIDAO(dni);

    }

    public boolean modifica(AlumnoModelo alumno) {
        return alumnoDAO.modificaDAO(alumno);
    }
    
    @Override
    public String toString() {
        return "AlumnoModelo{" + "dni=" + dni + ", nombre=" + nombre + ", apellido=" + apellido + ", domicilio=" + domicilio + ", telefono=" + telefono + ", fechaNacimiento=" + fechaNacimiento + ", alumnoDAO=" + alumnoDAO + '}';
    }
}

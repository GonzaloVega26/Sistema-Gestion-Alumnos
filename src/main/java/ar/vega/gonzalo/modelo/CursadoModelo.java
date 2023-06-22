package ar.vega.gonzalo.modelo;

import ar.vega.gonzalo.dao.CursadoDAO;
import java.util.ArrayList;

public class CursadoModelo {

    private double nota;
    private AlumnoModelo alumno;
    private MateriaModelo materia;
    private CursadoDAO cursadoDAO = new CursadoDAO();

    public CursadoModelo(int nota, AlumnoModelo alumno, MateriaModelo materia) {
        this.nota = nota;
        this.alumno = alumno;
        this.materia = materia;
    }

    public CursadoModelo() {
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public AlumnoModelo getAlumno() {
        return alumno;
    }

    public void setAlumno(AlumnoModelo alumno) {
        this.alumno = alumno;
    }

    public MateriaModelo getMateria() {
        return materia;
    }

    public void setMateria(MateriaModelo materia) {
        this.materia = materia;
    }

    public ArrayList<CursadoModelo> traeCursados() {
        return cursadoDAO.traeCursadosDAO();
    }
    
    public ArrayList<CursadoModelo> traeCursadosByMateria(int codigoMateria) {
        return cursadoDAO.traeCursadosByMateriaDAO(codigoMateria);
    }

    public ArrayList<AlumnoModelo> traeAlumnosNOInscriptos(int materiaCodigo) {
        return cursadoDAO.traeAlumnosNOInscriptos(materiaCodigo);
    }

    public boolean cargaDatos(CursadoModelo nota) {
        return cursadoDAO.cargaDatosDAO(nota);
    }

    public boolean baja(long dni, int codigoMateria) {
        return cursadoDAO.bajaDAO(dni, codigoMateria);
    }

    public boolean modifica(CursadoModelo nota) {
        return cursadoDAO.modificaDAO(nota);
    }

}

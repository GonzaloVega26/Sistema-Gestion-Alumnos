package ar.vega.gonzalo.modelo;

import ar.vega.gonzalo.dao.MateriaDAO;
import java.util.ArrayList;

public class MateriaModelo {

    private int codigo;
    private String nombre;
    private int cantHoras;
    private MateriaDAO materiaDAO = new MateriaDAO();
    private ProfesorModelo profesor;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantHoras() {
        return cantHoras;
    }

    public void setCantHoras(int cantHoras) {
        this.cantHoras = cantHoras;
    }

    public ProfesorModelo getProfesor() {
        return profesor;
    }

    public void setProfesor(ProfesorModelo profesor) {
        this.profesor = profesor;
    }

    
    public ArrayList<MateriaModelo> traeMaterias() {
        return materiaDAO.traeMateriasDAO();
    }

    public int cargaDatos(MateriaModelo materia) {
        return materiaDAO.cargaDatosDAO(materia);
    }

    public boolean baja(int codigo) {
        return materiaDAO.bajaDAO(codigo);
    }
    
    public boolean modifica(MateriaModelo materia) {
        return materiaDAO.modificaDAO(materia);
    }
    
    public MateriaModelo traeMateria(int codigo) {
        return materiaDAO.traeMateria(codigo);
    }

}

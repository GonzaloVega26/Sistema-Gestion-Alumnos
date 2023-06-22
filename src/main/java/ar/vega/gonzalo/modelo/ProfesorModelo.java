package ar.vega.gonzalo.modelo;

import ar.vega.gonzalo.dao.ProfesorDAO;
import java.util.ArrayList;

public class ProfesorModelo extends PersonaModelo {

    private ProfesorDAO profesorDAO = new ProfesorDAO();


    public boolean cargaDatos(ProfesorModelo profesor) {

        return profesorDAO.cargaDatosDAO(profesor);

    }

    public ArrayList<ProfesorModelo> traeProfesores() {
        return profesorDAO.traeProfesoresDAO();
    }

    public boolean baja(Long dni) {
        return profesorDAO.bajaByDNIDAO(dni);

    }
    
    public boolean modifica(ProfesorModelo profesor) {
        return profesorDAO.modificaDAO(profesor);
    }

    public ProfesorModelo traeUNProfesor(Long dni) {
        return profesorDAO.traeUNProfesorDAO(dni);
    }

    public boolean dniRepetido(ProfesorModelo profesor) {
        return !profesorDAO.dniRepetidoDAO(profesor);
    }
    

}

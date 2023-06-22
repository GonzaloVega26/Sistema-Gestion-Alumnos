package ar.vega.gonzalo.controladores;

import ar.vega.gonzalo.modelo.CursadoModelo;
import ar.vega.gonzalo.modelo.MateriaModelo;
import ar.vega.gonzalo.vista.CursadoListView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 *
 * @author gonzalo
 */
public class CursadoController{

    private CursadoListView cursadoView;
    CursadoModelo cursadoModelo = new CursadoModelo();

    public CursadoController() {
        this.cursadoView = new CursadoListView();
        ArrayList<MateriaModelo> listaMaterias = new MateriaModelo().traeMaterias();
        for (int i = 0; i < listaMaterias.size(); i++) {
            this.cursadoView.getjTabPane().add(new TableCursadoController(listaMaterias.get(i)).getTableView());
            this.cursadoView.getjTabPane().setTitleAt(i, listaMaterias.get(i).getNombre());
        }
        
        this.cursadoView.revalidate();
      
    }

    public CursadoListView getCursadoView() {
        return cursadoView;
    }

}

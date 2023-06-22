/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ar.vega.gonzalo.controladores;

import ar.vega.gonzalo.vista.MainView;
import ar.vega.gonzalo.vista.PersonasListView;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 *
 * @author Gonzalo Vega <ar.vega.gonzalo>
 */
public class MainController implements ActionListener {
    
    private MainView mainView;
    
    public MainController() {
        this.mainView = new MainView();
        this.mainView.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //Acciones de los botones
        this.mainView.getBtnAlumnos().addActionListener(this);
        this.mainView.getBtnMaterias().addActionListener(this);
        this.mainView.getBtnCursado().addActionListener(this);
        this.mainView.setVisible(true);
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource().equals(this.mainView.getBtnAlumnos())) {
            goToPersonasView();
            actualizarUI();
            this.mainView.getBtnAlumnos().setText("Personas");
        }
        if (e.getSource().equals(this.mainView.getBtnMaterias())) {
            goToMateriasView();
            actualizarUI();
            this.mainView.getBtnMaterias().setText("Materias");
        }
        if (e.getSource().equals(this.mainView.getBtnCursado())) {
            goToCursadoView();
            actualizarUI();
            this.mainView.getBtnCursado().setText("Cursados");
        }
    }
    
    private void goToPersonasView() {
        //Panel con Tabs
        JTabbedPane tabbedPanePersonas = new PersonasListView().getjTabPane();
        
        //Se agrega paneles de Alumno y Profesores
        this.mainView.getContentPane().add(tabbedPanePersonas, BorderLayout.CENTER);
        tabbedPanePersonas.add(new AlumnosListController().getTableView());
        tabbedPanePersonas.setTitleAt(0, "Alumnos");
        tabbedPanePersonas.add(new ProfesoresListController().getTableView());
        tabbedPanePersonas.setTitleAt(1, "Profesores");
        
    }
    
    private void goToMateriasView() {
        MateriasController materiasController = new MateriasController();
        this.mainView.getContentPane().add(materiasController.getMateriasView(), BorderLayout.CENTER);
        this.mainView.getContentPane().revalidate();
    }
    
    private void goToCursadoView() {
        //Se cargavista de cursado
        CursadoController cursadoController = new CursadoController();
        this.mainView.getContentPane().add(cursadoController.getCursadoView(), BorderLayout.CENTER);
        
    }
    
    private void actualizarUI() {
        //Limpiar Texto de todos los botones
        this.mainView.getBtnAlumnos().setText("");
        this.mainView.getBtnCursado().setText("");
        this.mainView.getBtnMaterias().setText("");
        
        limpiarPanelCentral();
        this.mainView.getContentPane().revalidate();
        this.mainView.getContentPane().repaint();

    }
    
    private void limpiarPanelCentral() {
        //Quita el contenedor central para cargar los nuevos
        this.mainView.getContentPane().remove(1);
        
    }
    
}

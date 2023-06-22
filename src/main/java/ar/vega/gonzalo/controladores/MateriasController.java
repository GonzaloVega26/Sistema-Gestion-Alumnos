/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ar.vega.gonzalo.controladores;

import ar.vega.gonzalo.vista.ItemMateria;
import ar.vega.gonzalo.modelo.MateriaModelo;
import ar.vega.gonzalo.modelo.ProfesorModelo;
import ar.vega.gonzalo.vista.MateriasView;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author Gonzalo Vega <ar.vega.gonzalo>
 */
public class MateriasController implements ActionListener {

    private MateriasView materiasView;
    private ArrayList<MateriaModelo> listaMaterias = new ArrayList<>();

    public MateriasController() {
        this.materiasView = new MateriasView();
        cargarMaterias();
        this.materiasView.getBtnNuevo().addActionListener(this);
        this.materiasView.getBtnCancelar().addActionListener(this);
        this.materiasView.getjSldColumnas().addChangeListener(e -> {
            if (!this.materiasView.getjSldColumnas().getValueIsAdjusting()) {
                GridLayout grid = new GridLayout(0, this.materiasView.getjSldColumnas().getValue());
                grid.setHgap(5);
                grid.setVgap(5);
                this.materiasView.getjPanel1().setLayout(grid);
                this.materiasView.getjPanel1().revalidate();
                this.materiasView.getjPanel1().repaint();

            }

        });
    }

    public MateriasView getMateriasView() {
        return materiasView;
    }

    public void cargarMaterias() {
        this.listaMaterias = new MateriaModelo().traeMaterias();
        for (MateriaModelo materia : listaMaterias) {
            ItemMateria item = new ItemMateria();
            item.getTxtTitulo().setText(materia.getNombre());
            item.getTxtCodigo().setText(String.valueOf(materia.getCodigo()));
            item.getjSelectProfesor().removeAllItems();
            if (materia.getProfesor().getNombre() == null) {
                item.getjSelectProfesor().addItem("Sin profesor Asignado");
            } else {
                item.getjSelectProfesor().addItem(materia.getProfesor().getNombre()
                        + " " + materia.getProfesor().getApellido() + " " + materia.getProfesor().getDni());

            }
            item.getjSelectProfesor().setSelectedIndex(0);
            item.getjSpinner().setValue(materia.getCantHoras());

            //Agregar Eventos a SubPanel
            addAccionesSubPanel(item);
            this.materiasView.getjPanel1().add(item);
            cambiarEstados(item, false);
            this.materiasView.getjPanel1().repaint();
            this.materiasView.getjPanel1().revalidate();
        }

    }

    private void addAccionesSubPanel(ItemMateria subPanel) {
        //Evento de los botones SubPanel
        subPanel.getBtnCreate().addActionListener((e) -> {
            this.onCreate(subPanel);
        });
        subPanel.getBtnDelete().addActionListener((e) -> {
            this.onDelete(subPanel);
        });
        subPanel.getBtnSave().addActionListener((e) -> {
            this.onSave(subPanel);
        });
        subPanel.getBtnEdit().addActionListener((e) -> {
            this.onEdit(subPanel);
        });
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource().equals(this.materiasView.getBtnNuevo())) {
            this.materiasView.getjPanel1().removeAll();
            this.materiasView.getjPanel1().repaint();
            ItemMateria newItem = new ItemMateria();

            //Agrega eventos al subpanel nuevo
            addAccionesSubPanel(newItem);
            this.materiasView.getjPanel1().add(newItem);//La nueva materia se agrega al principio
            this.materiasView.getjPanel1().repaint();
            onEdit(newItem); //Cambiamos a modo edicion
            //Alteramos algunos parametros por lo que es creacion
            newItem.getBtnCreate().setVisible(true); //Habilitamos el boton de creacion
            newItem.getBtnSave().setVisible(false); //Como no es edición deshabilitamos el de boton de edicion
            newItem.getTxtCodigo().setText("Autogenerado");
            newItem.getBtnDelete().setVisible(false); //Se deshabilitael boton de eliminar registro
            this.materiasView.getBtnCancelar().setVisible(true); // Habilitamos el boton de cancelar creacion

        }
        if (event.getSource().equals(this.materiasView.getBtnCancelar())) {
            this.materiasView.getjPanel1().removeAll();
            this.materiasView.getBtnCancelar().setVisible(false); // deshabilitamos el boton de cancelar creacion
            cargarMaterias();

        }

    }

    private ProfesorModelo obtenerProfesor(ItemMateria materiaCard) {
        String[] profesorArray = ((String) materiaCard.getjSelectProfesor().getSelectedItem()).split(" ");
        return new ProfesorModelo().traeUNProfesor(Long.valueOf(profesorArray[profesorArray.length - 1]));
    }

    public void onEdit(ItemMateria materiaCard) {
        cambiarEstados(materiaCard, true);
        materiaCard.getTxtTitulo().requestFocus();
        String profesorElegido = materiaCard.getjSelectProfesor().getSelectedItem().toString();
        materiaCard.getjSelectProfesor()
                .setModel(new DefaultComboBoxModel<>(
                        new ProfesorModelo().traeProfesores().stream().map(
                                profesor -> profesor.getNombre() + " " + profesor.getApellido()
                                + " " + profesor.getDni()).sorted().toArray(String[]::new)));
        materiaCard.getjSelectProfesor().setSelectedItem(profesorElegido);
        //Pasamos el foco al TextField del Titulo
        materiaCard.getTxtTitulo().requestFocus();
        materiaCard.getTxtTitulo().selectAll();
    }

    public void onSave(ItemMateria materiaCard) {
        if (validarDatos(materiaCard)) {
            cambiarEstados(materiaCard, false);
            MateriaModelo materia = new MateriaModelo().traeMateria(Integer.parseInt(materiaCard.getTxtCodigo().getText()));
            materia.setCantHoras(Integer.parseInt(String.valueOf(materiaCard.getjSpinner().getValue())));
            materia.setNombre(materiaCard.getTxtTitulo().getText());
            String[] profesorArray = ((String) materiaCard.getjSelectProfesor().getSelectedItem()).split(" ");
            materia.setProfesor(new ProfesorModelo().traeUNProfesor(Long.valueOf(profesorArray[profesorArray.length - 1])));
            materia.modifica(materia);
        }
    }

    public void onDelete(ItemMateria materiaCard) {
        int option = JOptionPane.showConfirmDialog(
                null, "¿Desea Eliminar la Materia?\n Se eliminarán los registros de cursados asociados",
                "Confirmación", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            // Se selecciona "Aceptar"
            MateriaModelo materia = new MateriaModelo()
                    .traeMateria(Integer.parseInt(materiaCard.getTxtCodigo().getText()));
            materia.baja(materia.getCodigo());
            this.materiasView.getjPanel1().remove(materiaCard);
            this.materiasView.getjPanel1().revalidate();
            this.materiasView.getjPanel1().repaint();
        }

    }

    private void cambiarEstados(ItemMateria materiaCard, boolean estado) {

        //Cambiar TextFields a editables
        materiaCard.getTxtTitulo().setEditable(estado);
        materiaCard.getjSpinner().setEnabled(estado);
        materiaCard.getjSelectProfesor().setEnabled(estado);
        //Modifica El ComboBox trayendo todas las opciones de profesores de la base de datos
        materiaCard.getBtnSave().setVisible(estado);
        materiaCard.getBtnEdit().setVisible(!estado);
    }

    public void onCreate(ItemMateria newItem) {
        MateriaModelo newMateria = new MateriaModelo();
        if (validarDatos(newItem)) {
            newMateria.setNombre(newItem.getTxtTitulo().getText());
            newMateria.setCantHoras(Integer.parseInt(String.valueOf(newItem.getjSpinner().getValue())));
            newMateria.setProfesor(obtenerProfesor(newItem));
            newItem.getBtnDelete().setVisible(false);
            int resultado = newMateria.cargaDatos(newMateria);
            if (resultado == -1) {
                JOptionPane.showMessageDialog(
                        null, "Hubo un error, vuelve a intentarlo",
                        "Aviso", JOptionPane.WARNING_MESSAGE);

            } else {
                this.materiasView.getjPanel1().removeAll();
                this.materiasView.getBtnCancelar().setVisible(false);
                cargarMaterias();

            }
        }
    }

    private boolean validarDatos(ItemMateria item) {
        if (item.getTxtTitulo().getText().isBlank()) {
            updateUI(item.getTxtTitulo(), item.getLblError(), "El título de la materia no puede estar vacío");
            return false;
        }
        if (item.getjSelectProfesor().getSelectedIndex() == -1) {
            updateUI(item.getjSelectProfesor(), item.getLblError(), "Debe seleccionar un profesor");
            return false;
        }
        if (Integer.parseInt(String.valueOf(item.getjSpinner().getValue())) <= 0) {
            updateUI(item.getjSpinner(), item.getLblError(), "Debe asignar una cantidad de horas válida");
            return false;
        }
        return true;

    }

    private void updateUI(Component comp, JLabel label, String mensaje) {
        label.setText(mensaje);
        label.setVisible(true);
        comp.requestFocus();
    }
}

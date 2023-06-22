/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ar.vega.gonzalo.controladores;

import ar.vega.gonzalo.customUI.cell.TableActionCellEditor;
import ar.vega.gonzalo.customUI.cell.TableActionCellRender;
import ar.vega.gonzalo.customUI.cell.TableActionEvent;
import ar.vega.gonzalo.modelo.AlumnoModelo;
import ar.vega.gonzalo.vista.TablePersonaView;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Gonzalo Vega <ar.vega.gonzalo>
 */
public class AlumnosListController {

    private TablePersonaView tableView;

    public TablePersonaView getTableView() {
        return tableView;
    }

    public AlumnosListController() {
        tableView = new TablePersonaView();
        tableView.getTxtTitulo().setText("ALUMNOS");
        tableView.getBtnNuevo().setIcon(new ImageIcon(getClass().getResource("/assets/new_student.png")));
        tableView.setVisible(true);
        loadPanelActions();
        btnNuevoActions();
        cargarAlumnos();

    }

    public void btnNuevoActions() {
        this.tableView.getBtnNuevo().addActionListener((e) -> {
            PersonaController personaController = new PersonaController(new AlumnoModelo());
            personaController.getPersonaView().addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    cargarAlumnos();
                }

            });
        });
    }

    private void loadPanelActions() {
        TableActionEvent event = new TableActionEvent() {
            JTable tableAlumnos = tableView.getTable();

            @Override
            public void onDelete(int row) {
                if (tableAlumnos.isEditing()) {
                    tableAlumnos.getCellEditor().stopCellEditing();
                }
                int option = JOptionPane.showConfirmDialog(
                        null, "¿Desea Eliminar el registro?", "Confirmación", JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    // Se selecciona "Aceptar"
                    DefaultTableModel model = (DefaultTableModel) tableAlumnos.getModel();
                    AlumnoModelo alumnoModelo = new AlumnoModelo();
                    alumnoModelo.baja((Long) model.getValueAt(row, 2));
                    model.removeRow(row);
                }

            }

            @Override
            public void onView(int row) {
                DefaultTableModel model = (DefaultTableModel) tableAlumnos.getModel();
                AlumnoModelo alumno = new AlumnoModelo();
                alumno = alumno.traeAlumno((Long) model.getValueAt(row, 2));
                PersonaController alumnoController = new PersonaController(alumno);
                alumnoController.getPersonaView().addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        cargarAlumnos();
                    }
                });
            }
        };
        this.tableView.getTable().getColumnModel().getColumn(7 - 1).setCellRenderer(new TableActionCellRender());
        this.tableView.getTable().getColumnModel().getColumn(7 - 1).setCellEditor(new TableActionCellEditor(event));
    }

    private void cargarAlumnos() {
        ((DefaultTableModel)  this.tableView.getTable().getModel()).setRowCount(0);
        AlumnoModelo alumnoModelo = new AlumnoModelo();
        ArrayList<AlumnoModelo> listaAlumnos = alumnoModelo.traeAlumnos();

        for (AlumnoModelo alumno : listaAlumnos) {
            ((DefaultTableModel)  this.tableView.getTable().getModel()).addRow(alumno.getDatos());
        }
        this.tableView.revalidate();
        this.tableView.repaint();
    }
}

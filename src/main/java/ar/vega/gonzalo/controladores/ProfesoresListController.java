/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ar.vega.gonzalo.controladores;

import ar.vega.gonzalo.customUI.cell.TableActionCellEditor;
import ar.vega.gonzalo.customUI.cell.TableActionCellRender;
import ar.vega.gonzalo.customUI.cell.TableActionEvent;
import ar.vega.gonzalo.modelo.ProfesorModelo;
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
public class ProfesoresListController {

    private TablePersonaView tableView;

    public TablePersonaView getTableView() {
        return tableView;
    }

    public ProfesoresListController() {
        tableView = new TablePersonaView();
        tableView.getTxtTitulo().setText("PROFESORES");
        tableView.getBtnNuevo().setIcon(new ImageIcon(getClass().getResource("/assets/new_teacher.png")));
        tableView.setVisible(true);
        loadPanelActions();
        btnNuevoActions();
        cargarProfesores();

    }

    public void btnNuevoActions() {
        this.tableView.getBtnNuevo().addActionListener((e) -> {
            PersonaController personaController = new PersonaController(new ProfesorModelo());
            personaController.getPersonaView().addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    cargarProfesores();
                }

            });
        });
    }

    private void loadPanelActions() {
        TableActionEvent event = new TableActionEvent() {
            JTable tableProfesores = tableView.getTable();

            @Override
            public void onDelete(int row) {
                if (tableProfesores.isEditing()) {
                    tableProfesores.getCellEditor().stopCellEditing();
                }
                int option = JOptionPane.showConfirmDialog(
                        null, "¿Desea Eliminar el registro?", "Confirmación", JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    // Se selecciona "Aceptar"
                    DefaultTableModel model = (DefaultTableModel) tableProfesores.getModel();
                    ProfesorModelo profesorModelo = new ProfesorModelo();
                    profesorModelo.baja((Long) model.getValueAt(row, 2));
                    model.removeRow(row);
                }

            }

            @Override
            public void onView(int row) {
                DefaultTableModel model = (DefaultTableModel) tableProfesores.getModel();
                ProfesorModelo profesorModelo = new ProfesorModelo();
                profesorModelo = profesorModelo.traeUNProfesor((Long) model.getValueAt(row, 2));
                PersonaController personaController = new PersonaController(profesorModelo);
                personaController.getPersonaView().addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        cargarProfesores();
                    }
                });
            }
        };
        this.tableView.getTable().getColumnModel().getColumn(7 - 1).setCellRenderer(new TableActionCellRender());
        this.tableView.getTable().getColumnModel().getColumn(7 - 1).setCellEditor(new TableActionCellEditor(event));
    }

    private void cargarProfesores() {
        ((DefaultTableModel) this.tableView.getTable().getModel()).setRowCount(0);
        ProfesorModelo profesorModelo = new ProfesorModelo();
        ArrayList<ProfesorModelo> listaProfesores = profesorModelo.traeProfesores();

        for (ProfesorModelo profesor : listaProfesores) {
            ((DefaultTableModel) this.tableView.getTable().getModel()).addRow(profesor.getDatos());
        }
        this.tableView.revalidate();
        this.tableView.repaint();
    }
}

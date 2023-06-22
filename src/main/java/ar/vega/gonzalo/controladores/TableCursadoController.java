
package ar.vega.gonzalo.controladores;

import ar.vega.gonzalo.customUI.cell.TableActionCellEditor;
import ar.vega.gonzalo.customUI.cell.TableActionCellRender;
import ar.vega.gonzalo.customUI.cell.TableActionEvent;
import ar.vega.gonzalo.modelo.AlumnoModelo;
import ar.vega.gonzalo.modelo.CursadoModelo;
import ar.vega.gonzalo.modelo.MateriaModelo;
import ar.vega.gonzalo.vista.TableCursadoView;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Gonzalo Vega <ar.vega.gonzalo>
 */
public class TableCursadoController {

    private TableCursadoView tableView;
    private MateriaModelo materia;

    public TableCursadoController(MateriaModelo materia) {
        this.tableView = new TableCursadoView();
        this.materia = materia;
        tableView.getTxtMateria().setText(materia.getNombre() + "   Código: " + materia.getCodigo());
        tableView.setVisible(true);
        loadPanelActions();
        btnInscribirActions();
        cargarAlumnosInscriptos();
    }

    public TableCursadoView getTableView() {
        return tableView;
    }

    public void btnInscribirActions() {

        this.tableView.getBtnInscribir().addActionListener((e) -> {
            InscripcionCursadoController controller = new InscripcionCursadoController(this.materia);
            controller.getInscripcionCursadoView().addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    cargarAlumnosInscriptos();
                }

            });
        });
    }

    private void loadPanelActions() {
        TableActionEvent event = new TableActionEvent() {
            JTable tableCursado = tableView.getTable();

            @Override
            public void onDelete(int row) {
                if (tableCursado.isEditing()) {
                    tableCursado.getCellEditor().stopCellEditing();
                }
                int option = JOptionPane.showConfirmDialog(
                        null, "¿Desea Eliminar el registro?", "Confirmación", JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    // Se selecciona "Aceptar"
                    DefaultTableModel model = (DefaultTableModel) tableCursado.getModel();
                    CursadoModelo cursadoModelo = new CursadoModelo();
                    cursadoModelo.baja(Long.valueOf((String) model.getValueAt(row, 3 - 1)), materia.getCodigo());
                    model.removeRow(row);
                }

            }

            @Override
            public void onView(int row) {
                DefaultTableModel model = (DefaultTableModel) tableCursado.getModel();
                AlumnoModelo alumno = new AlumnoModelo();
                alumno = alumno.traeAlumno(Long.valueOf(String.valueOf(model.getValueAt(row, 2))));
                CursadoModelo cursado = new CursadoModelo();
                double notaActualizada = actualizarNota();
                if(notaActualizada != -1){
                    cursado.setNota(notaActualizada);
                    cursado.setMateria(materia);
                    cursado.setAlumno(alumno);
                    cursado.modifica(cursado);
                    cargarAlumnosInscriptos();
                }
            }
        };
        this.tableView.getTable().getColumnModel().getColumn(5 - 1).setCellRenderer(new TableActionCellRender());
        this.tableView.getTable().getColumnModel().getColumn(5 - 1).setCellEditor(new TableActionCellEditor(event));
    }

    private void cargarAlumnosInscriptos() {
        ((DefaultTableModel) this.tableView.getTable().getModel()).setRowCount(0);
        CursadoModelo cursadoModelo = new CursadoModelo();
        ArrayList<CursadoModelo> listaAlumnosInscriptos = cursadoModelo.traeCursadosByMateria(materia.getCodigo());

        for (CursadoModelo cursado : listaAlumnosInscriptos) {
            AlumnoModelo alumno = cursado.getAlumno();
            Object[] datos = {alumno.getNombre(), alumno.getApellido(), String.valueOf(alumno.getDni()), cursado.getNota()};
            ((DefaultTableModel) this.tableView.getTable().getModel()).addRow(datos);
        }
        this.tableView.revalidate();
        this.tableView.repaint();
    }

    private double actualizarNota() {
        String inputValue = JOptionPane.showInputDialog(new JFrame("Input Dialog Example"), "Introduce la nota:");
        if (inputValue == null) {
            return -1;
        }
        if (validarNota(inputValue)) {
            return Double.parseDouble(inputValue);
        } else {
            JOptionPane.showMessageDialog(null, "Ingresa una nota válida entre 0 y 10",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return actualizarNota();
        }
    }

    private boolean validarNota(String input) {
        return input.matches("^(?:10(?:\\.0{1,2})?|[0-9](?:\\.[0-9]{1,2})?)$");
    }
}

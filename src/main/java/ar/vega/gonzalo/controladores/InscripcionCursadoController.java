
package ar.vega.gonzalo.controladores;

import ar.vega.gonzalo.modelo.AlumnoModelo;
import ar.vega.gonzalo.modelo.CursadoModelo;
import ar.vega.gonzalo.modelo.MateriaModelo;
import ar.vega.gonzalo.vista.InscripcionCursadoView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Gonzalo Vega <ar.vega.gonzalo>
 */
public class InscripcionCursadoController implements ActionListener {

    private InscripcionCursadoView inscripcionCursadoView;
    private MateriaModelo materia;
    ArrayList<AlumnoModelo> lista;

    public InscripcionCursadoController(MateriaModelo materia) {
        this.inscripcionCursadoView = new InscripcionCursadoView();
        this.inscripcionCursadoView.setLocationRelativeTo(null);
        this.inscripcionCursadoView.setTitle("Inscribir Alumno");
        this.lista = new CursadoModelo().traeAlumnosNOInscriptos(materia.getCodigo());
        this.materia = materia;
        this.inscripcionCursadoView.setVisible(true);
        cargarAlumnos(lista);
        this.inscripcionCursadoView.getBtnBuscar().addActionListener(this);
        this.inscripcionCursadoView.getTxtBuscar().addActionListener(this);
        this.inscripcionCursadoView.getBtnAgregar().addActionListener(this);
    }

    public InscripcionCursadoView getInscripcionCursadoView() {
        return inscripcionCursadoView;
    }

    private void cargarAlumnos(ArrayList<AlumnoModelo> listaAlu) {
        DefaultListModel<String> modelo = new DefaultListModel<>();
        this.inscripcionCursadoView.getListAlumnos().setModel(modelo);
        for (AlumnoModelo alumno : listaAlu) {
            modelo.addElement(alumno.getNombre() + " - " + alumno.getApellido() + " - " + alumno.getDni());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.inscripcionCursadoView.getBtnBuscar())) {
            cargarAlumnos(buscarEnLista(lista, this.inscripcionCursadoView.getTxtBuscar().getText()));
        }
        if (e.getSource().equals(this.inscripcionCursadoView.getTxtBuscar())) {
            cargarAlumnos(buscarEnLista(lista, this.inscripcionCursadoView.getTxtBuscar().getText()));
        }
        if (e.getSource().equals(this.inscripcionCursadoView.getBtnAgregar())) {
            
            CursadoModelo cursado = new CursadoModelo();
            //Se obtiene el alumno seleccionado de la lista, por configuracion solo puede ser uno
            String alumnosSeleccionado = this.inscripcionCursadoView.getListAlumnos().getSelectedValue();
            //Separamos por el divisor establecido al momento de cargar la lista
            String[] array = alumnosSeleccionado.split(" - ");
            //Buscamos el alumno en la base de datos
            AlumnoModelo alumno = new AlumnoModelo().traeAlumno(Long.valueOf(array[array.length - 1]));
            cursado.setAlumno(alumno);
            cursado.setMateria(materia);
            double nota = cargarNota();
            //Se verifica que se haya cargado una nota correctamente
            if (nota != -1) {
                cursado.setNota(nota);
                if (cursado.cargaDatos(cursado)) {
                    this.inscripcionCursadoView.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Se produjo un error, vuelva a intentarlo", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        }
    }
    
    public ArrayList<AlumnoModelo> buscarEnLista(ArrayList<AlumnoModelo> lista, String criterio) {
        criterio = criterio.toLowerCase();
        ArrayList<AlumnoModelo> resultados = new ArrayList<>();

        for (AlumnoModelo alumno : lista) {
            String nombre = alumno.getNombre().toLowerCase();
            String apellido = alumno.getApellido().toLowerCase();
            String dni = String.valueOf(alumno.getDni()).toLowerCase();

            if (nombre.contains(criterio)
                    || apellido.contains(criterio)
                    || dni.contains(criterio)) {
                resultados.add(alumno);
            }
        }

        return resultados;
    }

    private boolean validarNota(String input) {
        return input.matches("^(?:10(?:\\.0{1,2})?|[0-9](?:\\.[0-9]{1,2})?)$");
    }

    private double cargarNota() {
        String inputValue = JOptionPane.showInputDialog(new JFrame("Input Dialog Example"), "Introduce la nota:");
        if (inputValue == null) {
            return -1;
        }
        if (validarNota(inputValue)) {
            return Double.parseDouble(inputValue);
        } else {
            JOptionPane.showMessageDialog(null, "Ingresa una nota válida entre 0 y 10",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return cargarNota();
        }
    }
}

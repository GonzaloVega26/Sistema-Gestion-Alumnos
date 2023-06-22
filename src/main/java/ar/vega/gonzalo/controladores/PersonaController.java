/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ar.vega.gonzalo.controladores;

import ar.vega.gonzalo.exception.DBException;
import ar.vega.gonzalo.modelo.AlumnoModelo;
import ar.vega.gonzalo.modelo.PersonaModelo;
import ar.vega.gonzalo.modelo.ProfesorModelo;
import ar.vega.gonzalo.vista.PersonaView;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 *
 * @author Gonzalo Vega <ar.vega.gonzalo>
 * @param <T>
 */
public class PersonaController<T extends PersonaModelo> implements ActionListener {

    private PersonaView personaView = new PersonaView();
    private T persona;

    public PersonaController(T persona) {
        this.persona = persona;
        //Llenar datos
        fillViewData();
        //Listeners de los botones
        accionesBotones();
        personaView.setVisible(true);
    }

    private void fillViewData() {
        //Cargar datos alumno
        if (this.persona.getDni() == 0) {
            this.personaView.getTxtDni().setEditable(true);
            this.personaView.getTxtDni().setText("");
            this.personaView.getBtnReset().setVisible(false);
        } else {
            this.personaView.getTxtDni().setText(String.valueOf(this.persona.getDni()));

        }
        this.personaView.getTxtNombre().setText(this.persona.getNombre());
        this.personaView.getTxtApellido().setText(this.persona.getApellido());
        this.personaView.getTxtTelefono().setText(this.persona.getTelefono());
        this.personaView.getTxtDomicilio().setText(this.persona.getDomicilio());
        this.personaView.getDcNacimiento().setDate(this.persona.getFechaNacimiento());
    }

    private void accionesBotones() {
        this.personaView.getBtnSalir().addActionListener(this);
        this.personaView.getBtnGuardar().addActionListener(this);
        this.personaView.getBtnReset().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Eventos
        //Boton Guardar
        if (e.getSource().equals(this.personaView.getBtnGuardar())) {
            //Dos casos, 
            //CASO A: DNI == 0 => CREACION
            //CASO B: DNI != 0 => MODIFICACION
            if (this.persona.getDni() == 0) { //Esto se hace antes de obtener los datos
                if (validarDatos()) {
                    this.persona = obtenerDatosForm();
                    guardarAlumnoOProfesor(this.persona);
                }
            } else {
                if (validarDatos()) {
                    this.persona = obtenerDatosForm();
                    modificarAlumnoOProfesor(this.persona);
                }
            }

        } //Boton Reset
        else if (e.getSource().equals(personaView.getBtnReset())) {
            //Vaciar todos los campos
            this.personaView.getTxtNombre().setText("");
            this.personaView.getTxtApellido().setText("");
            this.personaView.getTxtTelefono().setText("");
            this.personaView.getTxtDomicilio().setText("");
            this.personaView.getDcNacimiento().setDate(null);

            //Boton Salir
        } else if (e.getSource().equals(personaView.getBtnSalir())) {
            this.personaView.dispose();
        }

    }

    private void guardarAlumnoOProfesor(PersonaModelo persona) {
        try {
            if (persona instanceof AlumnoModelo) {
                AlumnoModelo alumno = (AlumnoModelo) this.persona;
                alumno.cargaDatos(alumno);
            } else if (persona instanceof ProfesorModelo) {
                ProfesorModelo profesor = (ProfesorModelo) this.persona;
                profesor.cargaDatos(profesor);
            }
            this.personaView.dispose();
        } catch (DBException e) {
            this.persona.setDni(0); //Se resetea a cero porque sigue creando 
            timer(this.personaView.getLblErrDNI(),e.getMessage());
        }

    }

    private void modificarAlumnoOProfesor(PersonaModelo persona) {
        if (persona instanceof AlumnoModelo) {
            AlumnoModelo alumno = (AlumnoModelo) this.persona;
            alumno.modifica(alumno);
        } else if (persona instanceof ProfesorModelo) {
            ProfesorModelo profesor = (ProfesorModelo) this.persona;
            profesor.modifica(profesor);
        }
        this.personaView.dispose();

    }

    private T obtenerDatosForm() {
        persona.setNombre(this.personaView.getTxtNombre().getText());
        persona.setApellido(this.personaView.getTxtApellido().getText());
        persona.setDni(Long.valueOf(this.personaView.getTxtDni().getText()));
        persona.setTelefono(this.personaView.getTxtTelefono().getText());
        persona.setDomicilio(this.personaView.getTxtDomicilio().getText());
        persona.setFechaNacimiento(this.personaView.getDcNacimiento().getDate());
        return persona;
    }

    public PersonaView getPersonaView() {
        return personaView;
    }

    private boolean validarDatos() {
        if (this.personaView.getTxtNombre().getText().isBlank()) {
            timer(this.personaView.getLblErrNombre(),"*Nombre en Blanco");
            return false;
        }
        if (this.personaView.getTxtApellido().getText().isBlank()) {
            timer(this.personaView.getLblErrApellido(),"*Apellido en Blanco");
            return false;
        }
        if (this.personaView.getTxtDni().getText().isBlank() || !this.personaView.getTxtDni().getText().matches("^[0-9]{7,8}$")) {
            timer(this.personaView.getLblErrDNI(),"*El DNI debe tener 7 u 8 números");
            return false;
        }
        if (this.personaView.getDcNacimiento().getDate() == null) {
            timer(this.personaView.getLblErrNacimiento(),"*Fecha Inválida");
            return false;
        }
        return true;

    }

    private void timer(JLabel label, String message) {
        int duracion = 1200; // Duración en milisegundos (2 segundos)
        label.setForeground(Color.RED);
        label.setText(message);
        Timer timer = new Timer(duracion, e -> {
            label.setForeground(this.personaView.getjPanel1().getBackground()); //Cambiar al mismo color del fondo para "desaparecer"
            //label.setText("");
        });
        timer.setRepeats(false); // Ejecutar solo una vez

        // Iniciar el temporizador
        timer.start();
    }
}

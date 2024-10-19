/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Persona;
import Modelo.PersonaArray;
import Vista.VistaPresentacion;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class Controlador {
    private VistaPresentacion vista;
    private PersonaArray modelo;

    public Controlador(VistaPresentacion vista, PersonaArray modelo) {
        this.vista = vista;
        this.modelo = modelo;
        this.vista.setControlador(this);
        cargarTabla();
    }

    public void agregarPersona() {
        String codigo = vista.getCodigo();
        String nombre = vista.getNombre();
        String apellido = vista.getApellido();
        Date fechaNacimiento = vista.getFechaNacimiento();

        if (codigo.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || fechaNacimiento == null) {
            mostrarMensaje("Completar todos los campos");
            return;
        }

        int edad = calcularEdad(fechaNacimiento);
        if (!validarEdad(edad, fechaNacimiento)) {
            mostrarMensaje("La edad debe estar entre 0 y 110 años y no puede ser la fecha actual.");
            return;
        }

        Persona persona = new Persona(codigo, nombre, apellido, fechaNacimiento);
        modelo.agregarPersona(persona);
        cargarTabla();
        vista.limpiarCampos();
    }

    public void actualizarPersona(int selectedRow) {
        if (selectedRow == -1) {
            mostrarMensaje("Seleccione una fila para actualizar.");
            return;
        }

        String codigo = vista.getCodigo();
        String nombre = vista.getNombre();
        String apellido = vista.getApellido();
        Date fechaNacimiento = vista.getFechaNacimiento();

        if (codigo.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || fechaNacimiento == null) {
            mostrarMensaje("Completar todos los campos");
            return;
        }

        int edad = calcularEdad(fechaNacimiento);
        if (!validarEdad(edad, fechaNacimiento)) {
            mostrarMensaje("La edad debe estar entre 0 y 110 años y no puede ser la fecha actual.");
            return;
        }

        Persona persona = new Persona(codigo, nombre, apellido, fechaNacimiento);
        modelo.actualizarPersona(selectedRow, persona);
        cargarTabla();
        vista.limpiarCampos();
    }

    public void eliminarPersona(int selectedRow) {
        if (selectedRow != -1) {
            modelo.eliminarPersona(selectedRow);
            cargarTabla();
            vista.limpiarCampos();
        } else {
            mostrarMensaje("Selecciona un usuario para eliminar.");
        }
    }

    public void cargarTabla() {
        DefaultTableModel tableModel = vista.getTableModel();
        tableModel.setRowCount(0); // Limpiar la tabla
        for (Persona persona : modelo.getPersonas()) {
            String fechaFormateada = new SimpleDateFormat("dd/MM/yyyy").format(persona.getFechaDeNacimiento());
            int edad = calcularEdad(persona.getFechaDeNacimiento());
            tableModel.addRow(new Object[]{
                    persona.getCodigo(),
                    persona.getNombre(),
                    persona.getApellido(),
                    fechaFormateada,
                    edad // Agregar la edad a la tabla
            });
        }
    }

    public void limpiarTabla() {
        modelo = new PersonaArray(); // Reiniciar el modelo
        cargarTabla();
    }

    // Método para calcular la edad
    private int calcularEdad(Date fechaNacimiento) {
        Calendar fechaNacimientoCal = Calendar.getInstance();
        fechaNacimientoCal.setTime(fechaNacimiento);

        Calendar hoy = Calendar.getInstance();
        int edad = hoy.get(Calendar.YEAR) - fechaNacimientoCal.get(Calendar.YEAR);
        
        // Verificar si ya cumplió años este año
        if (hoy.get(Calendar.DAY_OF_YEAR) < fechaNacimientoCal.get(Calendar.DAY_OF_YEAR)) {
            edad--;
        }
        
        return edad;
    }

    // Método para validar la edad
    private boolean validarEdad(int edad, Date fechaNacimiento) {
        Calendar fechaNacimientoCal = Calendar.getInstance();
        fechaNacimientoCal.setTime(fechaNacimiento);
        Calendar hoy = Calendar.getInstance();

        // Verificar si la fecha de nacimiento es la misma que la fecha actual
        boolean fechaEsHoy = (hoy.get(Calendar.DAY_OF_MONTH) == fechaNacimientoCal.get(Calendar.DAY_OF_MONTH) &&
                              hoy.get(Calendar.MONTH) == fechaNacimientoCal.get(Calendar.MONTH) &&
                              hoy.get(Calendar.YEAR) == fechaNacimientoCal.get(Calendar.YEAR));
        
        return edad >= 0 && edad <= 110 && !fechaEsHoy; // Valida que la edad esté en el rango y que no sea la fecha actual
    }

    // Método para mostrar mensajes al usuario
    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
    
    
    
    // Método para guardar personas en un archivo de texto
public void guardarArchivo() {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("personas.txt"))) {
        for (Persona persona : modelo.getPersonas()) {
            String line = persona.getCodigo() + "," + persona.getNombre() + "," +
                          persona.getApellido() + "," + new SimpleDateFormat("dd/MM/yyyy").format(persona.getFechaDeNacimiento());
            writer.write(line);
            writer.newLine();
        }
        mostrarMensaje("Datos guardados correctamente.");
    } catch (IOException e) {
        mostrarMensaje("Error al guardar el archivo: " + e.getMessage());
    }
}

// Método para cargar personas desde un archivo de texto
public void cargarArchivo() {
    try (BufferedReader reader = new BufferedReader(new FileReader("personas.txt"))) {
        String line;
        limpiarTabla(); // Limpiar el modelo antes de cargar
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            String codigo = data[0];
            String nombre = data[1];
            String apellido = data[2];
            Date fechaNacimiento = new SimpleDateFormat("dd/MM/yyyy").parse(data[3]);
            Persona persona = new Persona(codigo, nombre, apellido, fechaNacimiento);
            modelo.agregarPersona(persona);
        }
        cargarTabla();
        mostrarMensaje("Datos cargados correctamente.");
    } catch (IOException | ParseException e) {
        mostrarMensaje("Error al cargar el archivo: " + e.getMessage());
    }
}

}

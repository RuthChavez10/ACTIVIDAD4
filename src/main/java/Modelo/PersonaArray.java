/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.ArrayList;
import java.util.List;

public class PersonaArray {
    private List<Persona> personas;

    public PersonaArray() {
        this.personas = new ArrayList<>();
    }

    public void agregarPersona(Persona persona) {
        personas.add(persona);
    }

    public void eliminarPersona(int index) {
        if (index >= 0 && index < personas.size()) {
            personas.remove(index);
        }
    }

    public void actualizarPersona(int index, Persona persona) {
        if (index >= 0 && index < personas.size()) {
            personas.set(index, persona);
        }
    }

    public List<Persona> getPersonas() {
        return personas;
    }

    public Persona getPersona(int index) {
        if (index >= 0 && index < personas.size()) {
            return personas.get(index);
        }
        return null;
    }

    public int getSize() {
        return personas.size();
    }
}
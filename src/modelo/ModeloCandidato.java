/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author marthy
 */
public class ModeloCandidato {
    
    private String nombre;
    private String apeM;
    private String apeP;
    private String numControl;
    private String correo;
    private String telefono;

    public ModeloCandidato() {
    }
    
public ModeloCandidato(String numControl, String nombre, String apeP, String apeM, String telefono ,String correo) {
    this.numControl = numControl;
    this.nombre = nombre;
    this.apeP = apeP;
    this.apeM = apeM;
    this.telefono = telefono;
    this.correo = correo;
   
}

    

    public String getNombre() {
        return nombre;
    }

    public String getApeM() {
        return apeM;
    }

    public String getApeP() {
        return apeP;
    }

    public String getNumControl() {
        return numControl;
    }

    public String getCorreo() {
        return correo;
    }
    public String getTelefono() {
        return telefono;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApeM(String apeM) {
        this.apeM = apeM;
    }

    public void setApeP(String apeP) {
        this.apeP = apeP;
    }

    public void setNumControl(String numControl) {
        this.numControl = numControl;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
     public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    
}

package modelo;

public class Docente {
    private String noEmpleado;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String telefono;
    private String correo;

    public Docente() {
    }

    public Docente(String noEmpleado, String nombre, String apellidoPaterno, String apellidoMaterno, String telefono, String correo) {
        this.noEmpleado = noEmpleado;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.telefono = telefono;
        this.correo = correo;
    }

    public String getNoEmpleado() {
        return noEmpleado;
    }

    public void setNoEmpleado(String noEmpleado) {
        this.noEmpleado = noEmpleado;
    }

    public String getNumeroEmpleado() {
        return noEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getApellidos() {
        return apellidoPaterno + " " + apellidoMaterno;
    }

    @Override
    public String toString() {
        return "Docente{" +
               "noEmpleado='" + noEmpleado + '\'' +
               ", nombre='" + nombre + '\'' +
               ", apellidoPaterno='" + apellidoPaterno + '\'' +
               ", apellidoMaterno='" + apellidoMaterno + '\'' +
               ", telefono='" + telefono + '\'' +
               ", correo='" + correo + '\'' +
               '}';
    }
}

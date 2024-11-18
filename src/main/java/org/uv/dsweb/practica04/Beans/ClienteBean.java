/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package org.uv.dsweb.practica04.Beans;

import java.io.Serializable;
import java.util.List;
import java.util.Random;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.uv.dsweb.practica04.DAOs.ClienteDAO;
import org.uv.dsweb.practica04.Entities.ClienteEntity;

/**
 *
 * @author ian
 */
@ManagedBean
@Named(value = "clienteBean")
@SessionScoped
public class ClienteBean implements Serializable{

    private final ClienteDAO dao;

    private String id;
    private String nombre;
    private String correo;

    private List<ClienteEntity> listaClientes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public List<ClienteEntity> getListaClientes() {
        return listaClientes;
    }

    public void setListaClientes(List<ClienteEntity> listaClientes) {
        this.listaClientes = listaClientes;
    }
    
    

    public String generarID() {
        Random random = new Random();
        StringBuilder cadena = new StringBuilder("C");
        for (int i = 0; i < 4; i++) {
            int numero = random.nextInt(9) + 1; // Genera un número entre 1 y 9
            cadena.append(numero);
        }
        id = cadena.toString();
        return id;
    }

    public void generarCliente() {
        if(validarDatos()){
            ClienteEntity cliente = new ClienteEntity();
            cliente.setId(id);
            cliente.setNombre(nombre);
            cliente.setCorreo(correo);
            dao.save(cliente);
            listaClientes = dao.findAll();
            addMessage(FacesMessage.SEVERITY_INFO, "Cliente agregado", "El cliente se ha creado con éxito");
        }
        else{
            addMessage(FacesMessage.SEVERITY_WARN, "Agregación fallida", "Los datos introducidos son incorrectos o inexistentes");
        }
    }

    public void actualizarCliente() {
        if(validarDatos()){
            ClienteEntity cliente = new ClienteEntity();
            cliente.setId(id);
            cliente.setNombre(nombre);
            cliente.setCorreo(correo);
            dao.update(cliente, id);
            listaClientes = dao.findAll();
            addMessage(FacesMessage.SEVERITY_INFO, "Cliente actualizado", "El cliente se ha actualizado con éxito");
        }
        else{
            addMessage(FacesMessage.SEVERITY_WARN, "Actualización fallida", "Los datos introducidos son incorrectos o inexistentes");
        }
    }

    public void eliminarCliente() {
        if(!id.isEmpty()){
            dao.delete(id);
            listaClientes = dao.findAll();
            addMessage(FacesMessage.SEVERITY_INFO, "Cliente eliminado", "El cliente se ha removido con éxito");
        }
        else{
            addMessage(FacesMessage.SEVERITY_WARN, "Eliminación fallida", "Los datos introducidos son incorrectos o inexistentes");
        }
    }

    public boolean validarDatos(){
        boolean respuesta = true;
        if(id.isEmpty() || nombre.isEmpty() || correo.isEmpty()){
            respuesta = false;
        }
        return respuesta;
    }
    
    public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }

    /**
     * Creates a new instance of ClienteBean
     */
    public ClienteBean() {
        this.dao = new ClienteDAO();
        this.id = "";
        this.nombre = "";
        this.correo = "";
        this.listaClientes = dao.findAll();
    }

}

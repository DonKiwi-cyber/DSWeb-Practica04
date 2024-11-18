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
import org.uv.dsweb.practica04.DAOs.ProductoDAO;
import org.uv.dsweb.practica04.Entities.ProductoEntity;

/**
 *
 * @author ian
 */
@ManagedBean
@Named(value = "productoBean")
@SessionScoped
public class ProductoBean implements Serializable{
    
    private final ProductoDAO dao;
    
    private String id;
    private String descripcion;
    private double precio;
    
    private List<ProductoEntity> listaProductos;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public List<ProductoEntity> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(List<ProductoEntity> listaProductos) {
        this.listaProductos = listaProductos;
    }

    public String generarId(){
        Random random = new Random();
        StringBuilder cadena = new StringBuilder("P");
        for (int i = 0; i < 4; i++) {
            int numero = random.nextInt(9) + 1; // Genera un número entre 1 y 9
            cadena.append(numero);
        }
        id = cadena.toString();
        return id;
    }
    
    public void guardarProducto(){
        if(validarDatos()){
            ProductoEntity producto = new ProductoEntity();
            producto.setId(id);
            producto.setDescripcion(descripcion);
            producto.setPrecio(precio);
            dao.save(producto);
            listaProductos = dao.findAll();
            addMessage(FacesMessage.SEVERITY_INFO, "Producto agregado", "El producto se ha creado con éxito");
        }
        else{
            addMessage(FacesMessage.SEVERITY_WARN, "Creación fallida", "Los datos introducidos son incorrectos o inexistentes");
        }
    }
    
    public void actualizarProducto(){
        if(validarDatos()){
            ProductoEntity producto = new ProductoEntity();
            producto.setId(id);
            producto.setDescripcion(descripcion);
            producto.setPrecio(precio);
            dao.update(producto, id);
            listaProductos = dao.findAll();
            addMessage(FacesMessage.SEVERITY_INFO, "Producto actualizado", "El producto se ha actualizado con éxito");
        }
        else{
            addMessage(FacesMessage.SEVERITY_WARN, "Actualización fallida", "Los datos introducidos son incorrectos o inexistentes");
        }
    }
    
    public void eliminarProducto(){
        if(!id.isEmpty()){
            dao.delete(id);
            listaProductos = dao.findAll();
            addMessage(FacesMessage.SEVERITY_INFO, "Producto eliminado", "El producto se ha removido con éxito");
        }
        else{
            addMessage(FacesMessage.SEVERITY_WARN, "Eliminación fallida", "Los datos introducidos son incorrectos o inexistentes");
        }
    }
    
    public boolean validarDatos(){
        boolean respuesta = true;
        if(id.isEmpty() || descripcion.isEmpty() || precio==0){
            respuesta = false;
        }
        return respuesta;
    }
    
    public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }
    /**
     * Creates a new instance of ProductoBean
     */
    public ProductoBean() {
        this.id = "";
        this.descripcion = "";
        this.precio = 0.0;
        this.dao = new ProductoDAO();
        this.listaProductos = dao.findAll();
    }
    
}

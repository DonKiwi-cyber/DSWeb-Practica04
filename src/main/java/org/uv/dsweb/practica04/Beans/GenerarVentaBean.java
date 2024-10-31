/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package org.uv.dsweb.practica04.Beans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.uv.dsweb.practica04.DAOs.ClienteDAO;
import org.uv.dsweb.practica04.DAOs.DetalleVentaDAO;
import org.uv.dsweb.practica04.DAOs.ProductoDAO;
import org.uv.dsweb.practica04.DAOs.VentaDAO;
import org.uv.dsweb.practica04.Entities.ClienteEntity;
import org.uv.dsweb.practica04.Entities.DetalleVentaEntity;
import org.uv.dsweb.practica04.Entities.ProductoEntity;
import org.uv.dsweb.practica04.Entities.VentaEntity;

/**
 *
 * @author ian
 */
@ManagedBean
@Named(value = "GenerarVentaBean")
@SessionScoped
public class GenerarVentaBean implements Serializable {

    private final VentaDAO daoVenta;
    private final DetalleVentaDAO daoDV;
    private final ClienteDAO daoCliente;
    private final ProductoDAO daoProducto;
    
    private String idVenta;
    private String idCliente;
    private Date fecha;
    
    private String idNuevoProducto;
    private int cantidadNuevoProducto;
    
    private List<ClienteEntity> clientes;
    private List<ProductoVentaPojo> productos;

    public String getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(String idVenta) {
        this.idVenta = idVenta;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getIdNuevoProducto() {
        return idNuevoProducto;
    }

    public void setIdNuevoProducto(String idNuevoProducto) {
        this.idNuevoProducto = idNuevoProducto;
    }

    public int getCantidadNuevoProducto() {
        return cantidadNuevoProducto;
    }

    public void setCantidadNuevoProducto(int cantidadNuevoProducto) {
        this.cantidadNuevoProducto = cantidadNuevoProducto;
    }

    public List<ClienteEntity> getClientes() {
        return clientes;
    }

    public void setClientes(List<ClienteEntity> clientes) {
        this.clientes = clientes;
    }

    public List<ProductoVentaPojo> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoVentaPojo> productos) {
        this.productos = productos;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    
    public void nuevaVenta(){
        idVenta = "";
        idCliente = "";
        fecha = new Date();
    
        idNuevoProducto = "";
        cantidadNuevoProducto = 0;
    
        clientes = new ArrayList<>();
        productos = new ArrayList<>();
    }
    
    public String generarIDVenta(String letra){
        Random random = new Random();
        StringBuilder cadena = new StringBuilder(letra);
        for (int i = 0; i < 4; i++) {
            int numero = random.nextInt(9) + 1; // Genera un número entre 1 y 9
            cadena.append(numero);
        }
        idVenta = cadena.toString();
        return idVenta;
    }
    
    public void guardarProducto(){
        if(validarDatosProducto()){
            ProductoEntity producto = daoProducto.findByID(idNuevoProducto);
            if(producto!=null){
                ProductoVentaPojo nuevoProducto = new ProductoVentaPojo();

                nuevoProducto.setId(producto.getId());
                nuevoProducto.setDescripcion(producto.getDescripcion());
                nuevoProducto.setPrecio(producto.getPrecio());
                nuevoProducto.setCantidad(cantidadNuevoProducto);
                nuevoProducto.setTotal(cantidadNuevoProducto * producto.getPrecio());
                productos.add(nuevoProducto);
            }
        }
    }
    
    public void guardarVenta(){
        if(validarDatosVenta()){
            VentaEntity venta = new VentaEntity();
            ClienteEntity cliente = daoCliente.findByID(idCliente);
        
            venta.setId(idVenta);
            venta.setCliente(cliente);
            venta.setNombreCliente(cliente.getNombre());
            venta.setCorreoCliente(cliente.getCorreo());
            venta.setFecha(fecha);

            daoVenta.save(venta);

            for (ProductoVentaPojo pojo : productos) {
                DetalleVentaEntity detalleVenta = new DetalleVentaEntity();

                detalleVenta.setId(generarIDVenta("DV"));
                detalleVenta.setVenta(venta);
                detalleVenta.setProducto(daoProducto.findByID(pojo.getId()));
                detalleVenta.setDescripcionProducto(pojo.getDescripcion());
                detalleVenta.setPrecioProducto(pojo.getPrecio());
                detalleVenta.setCantidadProducto(pojo.getCantidad());
                detalleVenta.setTotal(pojo.getTotal());

                daoDV.save(detalleVenta);
            }
        }
    }
    
    public void imprimirVenta(){
        
    }
    
    public void enviarVenta(){
        
    }
    
    public boolean validarDatosVenta(){
        boolean respuesta = true;
        if(idCliente.isEmpty() || idVenta.isEmpty() || productos.isEmpty()){
            respuesta = false;
            addMessage(FacesMessage.SEVERITY_ERROR, "Datos incompletos", "Falta información para guardar la venta");
        }
        return respuesta;
    }
    
    public boolean validarDatosProducto(){
        boolean respuesta = true;
        if(idNuevoProducto.isEmpty() || cantidadNuevoProducto==0){
            respuesta = false;
            addMessage(FacesMessage.SEVERITY_ERROR, "Datos incompletos", "Falta información para añadir un nuevo producto");
        }
        return respuesta;
    }
    
    public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }
    
    public GenerarVentaBean() {
        this.idVenta = "";
        this.idCliente = "";
        this.fecha = null;
    
        this.idNuevoProducto = "";
        this.cantidadNuevoProducto = 0;
    
        this.clientes = new ArrayList<>();
        this.productos = new ArrayList<>();
        
        this.daoVenta = new VentaDAO();
        this.daoDV = new DetalleVentaDAO();
        this.daoCliente = new ClienteDAO();
        this.daoProducto = new ProductoDAO();
        
    }
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.uv.dsweb.practica04.Entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author ian
 */
@Entity
@Table(name="venta")
public class VentaEntity implements Serializable {
    
    @Id
    @Column(name="id_venta")
    private String id;
    
    @Column()
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fecha;
    
    @Column(name="nombre_cliente")
    private String nombreCliente;
    
    @Column(name="correo_cliente")
    private String correoCliente;
    
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private ClienteEntity cliente;
    
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVentaEntity> detalleVentas;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getCorreoCliente() {
        return correoCliente;
    }

    public void setCorreoCliente(String correoCliente) {
        this.correoCliente = correoCliente;
    }

    public ClienteEntity getCliente() {
        return cliente;
    }

    public void setCliente(ClienteEntity cliente) {
        this.cliente = cliente;
    }

    public List<DetalleVentaEntity> getDetalleVentas() {
        return detalleVentas;
    }

    public void setDetalleVentas(List<DetalleVentaEntity> detalleVentas) {
        this.detalleVentas = detalleVentas;
    }
    
    
}

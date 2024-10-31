/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.uv.dsweb.practica04.Entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author ian
 */
@Entity
@Table(name="producto")
public class ProductoEntity implements Serializable {
    
    @Id
    @Column(name="id_producto")
    private String id;
    
    @Column()
    private String descripcion;
    
    @Column()
    private double precio;
    
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVentaEntity> detalleVentas;

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

    public List<DetalleVentaEntity> getDetalleVentas() {
        return detalleVentas;
    }

    public void setDetalleVentas(List<DetalleVentaEntity> detalleVentas) {
        this.detalleVentas = detalleVentas;
    }
    
}

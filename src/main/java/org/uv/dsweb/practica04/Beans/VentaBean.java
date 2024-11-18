/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package org.uv.dsweb.practica04.Beans;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class VentaBean implements Serializable {

    private final VentaDAO daoVenta;
    private final DetalleVentaDAO daoDV;
    private final ClienteDAO daoCliente;
    private final ProductoDAO daoProducto;

    private String idVenta;
    private String idCliente;
    private String nombreCliente;
    private Date fecha;

    private String idNuevoProducto;
    private int cantidadNuevoProducto;

    private double TOTAL;

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

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public double getTOTAL() {
        return TOTAL;
    }

    public void setTOTAL(double TOTAL) {
        this.TOTAL = TOTAL;
    }

    public void nuevaVenta() {
        idVenta = "";
        idCliente = "";
        nombreCliente = "";
        fecha = new Date();

        idNuevoProducto = "";
        cantidadNuevoProducto = 0;

        clientes = new ArrayList<>();
        productos = new ArrayList<>();

        TOTAL = 0.0;
    }

    public String generarIDVenta(String letra) {
        Random random = new Random();
        StringBuilder cadena = new StringBuilder(letra);
        for (int i = 0; i < 4; i++) {
            int numero = random.nextInt(9) + 1; // Genera un número entre 1 y 9
            cadena.append(numero);
        }
        idVenta = cadena.toString();
        return idVenta;
    }

    public void guardarProducto() {
        if (validarDatosProducto()) {
            ProductoEntity producto = daoProducto.findByID(idNuevoProducto);
            if (producto != null) {
                ProductoVentaPojo nuevoProducto = new ProductoVentaPojo();

                nuevoProducto.setId(producto.getId());
                nuevoProducto.setDescripcion(producto.getDescripcion());
                nuevoProducto.setPrecio(producto.getPrecio());
                nuevoProducto.setCantidad(cantidadNuevoProducto);
                nuevoProducto.setTotal(cantidadNuevoProducto * producto.getPrecio());
                productos.add(nuevoProducto);

                TOTAL += producto.getPrecio();
                addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Producto agregado con éxito");
            } else {
                addMessage(FacesMessage.SEVERITY_WARN, "Producto incorrecto", "El ID del producto introducido no existe o es incorrecto");
            }
        } else {
            addMessage(FacesMessage.SEVERITY_WARN, "Datos incorrectos", "Los datos ingresados son incorrectos o inexistentes");
        }
    }

    public void guardarVenta() {
        if (validarDatosVenta()) {
            VentaEntity venta = new VentaEntity();
            ClienteEntity cliente = daoCliente.findByID(idCliente);

            if (cliente != null) {
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
                addMessage(FacesMessage.SEVERITY_INFO, "Venta creada", "La venta y sus detalles se han guardado con éxito");
            } else {
                addMessage(FacesMessage.SEVERITY_WARN, "Cliente incorrecto", "El ID del cliente introducido no existe o es incorrecto");
            }
        } else {
            addMessage(FacesMessage.SEVERITY_WARN, "Datos incorrectos", "Los datos ingresados son incorrectos o inexistentes");
        }
    }

    public void imprimirVenta() throws FileNotFoundException {
//        try {
//            // Crear el documento PDF
//            Document documento = new Document();
//            PdfWriter.getInstance(documento, new FileOutputStream("/home/ian/Escritorio/reporteVenta.pdf"));
//            documento.open();
//
//            // Fuente para el título
//            Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
//            Font fuenteSubtitulo = FontFactory.getFont(FontFactory.HELVETICA, 12);
//
//            // Título del PDF
//            Paragraph titulo = new Paragraph("Reporte de Venta", fuenteTitulo);
//            titulo.setAlignment(Element.ALIGN_CENTER);
//            documento.add(titulo);
//
//            // Espacio en blanco
//            documento.add(new Paragraph("\n"));
//
//            // Información de DetalleVenta
//            documento.add(new Paragraph("ID Venta: " + idVenta, fuenteSubtitulo));
//            documento.add(new Paragraph("ID Cliente: " + idCliente, fuenteSubtitulo));
//            documento.add(new Paragraph("Fecha: " + fecha, fuenteSubtitulo));
//
//            // Espacio en blanco
//            documento.add(new Paragraph("\n"));
//
//            // Tabla para los productos
//            PdfPTable tabla = new PdfPTable(5); // Número de columnas
//            tabla.setWidthPercentage(100);
//
//            // Encabezados de la tabla
//            tabla.addCell(new PdfPCell(new Phrase("ID")));
//            tabla.addCell(new PdfPCell(new Phrase("Descripción")));
//            tabla.addCell(new PdfPCell(new Phrase("Precio")));
//            tabla.addCell(new PdfPCell(new Phrase("Cantidad")));
//            tabla.addCell(new PdfPCell(new Phrase("Total")));
//
//            // Agregar los productos a la tabla
//            for (ProductoVentaPojo producto : productos) {
//                tabla.addCell(String.valueOf(producto.getId()));
//                tabla.addCell(producto.getDescripcion());
//                tabla.addCell(String.valueOf(producto.getPrecio()));
//                tabla.addCell(String.valueOf(producto.getCantidad()));
//                tabla.addCell(String.valueOf(producto.getTotal()));
//            }
//
//            // Agregar la tabla al documento
//            documento.add(tabla);
//            documento.add(new Paragraph("\n"));
//            documento.add(new Paragraph("Total de venta: " + TOTAL, fuenteSubtitulo));
//
//            // Cerrar el documento
//            documento.close();
//            
//            addMessage(FacesMessage.SEVERITY_INFO, "Documento creado", "El PDF se ha generado y guardado con éxito");
//
//        } catch (DocumentException | IOException e) {
//            addMessage(FacesMessage.SEVERITY_ERROR, "ocurrió un error inesperado", "No pudo realizarse la impresión con éxito");
//            e.printStackTrace();
//        }

        VentaEntity venta = daoVenta.findByID(idVenta); // Replace with actual ID
        List<DetalleVentaEntity> detalles = venta.getDetalleVentas();

        // Load Jasper template
        JasperReport jasperReport;
        try {
            InputStream inputStream = new FileInputStream("/home/ian/NetBeansProjects/DSWeb-Practica04/reporte.jrxml");
            jasperReport = JasperCompileManager.compileReport(inputStream);
            // Set parameters
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("id", venta.getId());
            parameters.put("nombreCliente", venta.getNombreCliente());
            parameters.put("fecha", venta.getFecha());

            // Data source
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(detalles);

            // Fill report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            // Show PDF
            JasperViewer.viewReport(jasperPrint, false);
            JasperExportManager.exportReportToPdfFile(jasperPrint, "/home/ian/Escritorio/reporte_venta.pdf");
            System.out.println("documento creado con éxito");
        } catch (JRException ex) {
            Logger.getLogger(VentaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enviarVenta() {

    }

    public boolean validarDatosVenta() {
        boolean respuesta = true;
        if (idCliente.isEmpty() || idVenta.isEmpty() || productos.isEmpty()) {
            respuesta = false;
            addMessage(FacesMessage.SEVERITY_ERROR, "Datos incompletos", "Falta información para guardar la venta");
        }
        return respuesta;
    }

    public boolean validarDatosProducto() {
        boolean respuesta = true;
        if (idNuevoProducto.isEmpty() || cantidadNuevoProducto == 0) {
            respuesta = false;
            addMessage(FacesMessage.SEVERITY_ERROR, "Datos incompletos", "Falta información para añadir un nuevo producto");
        }
        return respuesta;
    }

    public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public VentaBean() {
        this.idVenta = "";
        this.idCliente = "";
        this.nombreCliente = "";
        this.fecha = null;

        this.idNuevoProducto = "";
        this.cantidadNuevoProducto = 0;

        this.clientes = new ArrayList<>();
        this.productos = new ArrayList<>();

        this.daoVenta = new VentaDAO();
        this.daoDV = new DetalleVentaDAO();
        this.daoCliente = new ClienteDAO();
        this.daoProducto = new ProductoDAO();

        this.TOTAL = 0.0;
    }

}

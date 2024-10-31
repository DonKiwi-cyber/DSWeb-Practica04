/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.uv.dsweb.practica04.DAOs;

import java.util.List;
import javax.persistence.Transient;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.uv.dsweb.practica04.Entities.DetalleVentaEntity;
import org.uv.dsweb.practica04.Entities.ProductoEntity;
import org.uv.dsweb.practica04.Entities.VentaEntity;
import org.uv.dsweb.practica04.Hibernate.HibernateUtil;

/**
 *
 * @author ian
 */
public class DetalleVentaDAO {
    
    SessionFactory sf = null;
    @Transient
    Transaction tra = null;
    Session session = null;
    
    public boolean save(DetalleVentaEntity detalleVenta) {
        boolean success = false;
        try {
            sf = HibernateUtil.getSessionFactory();
            session = sf.getCurrentSession();
            tra = session.beginTransaction();
            session.save(detalleVenta);
            tra.commit();
            session.close();
            success = true;
        } catch (HibernateException e) {
            if (tra != null) {
                tra.rollback();
            }
        }        
        return success;
    }
    
    public boolean update(DetalleVentaEntity detalleVenta, String id) {
        boolean success = false;
        try {
            sf = HibernateUtil.getSessionFactory();
            session = sf.getCurrentSession();
            tra = session.beginTransaction();
            DetalleVentaEntity detVen = session.get(DetalleVentaEntity.class, id);
            if (detVen != null) {
                detVen.setVenta(detalleVenta.getVenta());
                detVen.setProducto(detalleVenta.getProducto());
                detVen.setDescripcionProducto(detalleVenta.getDescripcionProducto());
                detVen.setCantidadProducto(detalleVenta.getCantidadProducto());
                detVen.setPrecioProducto(detalleVenta.getPrecioProducto());
                detVen.setTotal(detalleVenta.getTotal());
                session.update(detVen);
                tra.commit();
                success = true;
            }
            session.close();
        } catch (HibernateException e) {
            if (tra != null) {
                tra.rollback();
            }
        }        
        return success;
    }
    
    public boolean delete(String id) {
        boolean success = false;
        try {
            sf = HibernateUtil.getSessionFactory();
            session = sf.getCurrentSession();
            tra = session.beginTransaction();
            DetalleVentaEntity detVen = session.get(DetalleVentaEntity.class, id);
            if (detVen != null) {
                session.delete(detVen);
                tra.commit();
                success = true;
            }
            session.close();
        } catch (HibernateException e) {
            if (tra != null) {
                tra.rollback();
            }
        }        
        return success;
    }
    
    public DetalleVentaEntity findByID(String id) {
        try {
            sf = HibernateUtil.getSessionFactory();
            session = sf.getCurrentSession();
            tra = session.beginTransaction();
            DetalleVentaEntity detVen = session.get(DetalleVentaEntity.class, id);
            session.close();
            return detVen;
        } catch (HibernateException e) {
            if (tra != null) {
                tra.rollback();
            }
            return null;
        }        
    }
    
    public List<DetalleVentaEntity> findByProducto(String id){
        try {
            sf = HibernateUtil.getSessionFactory();
            session = sf.getCurrentSession();
            tra = session.beginTransaction();
            ProductoEntity producto = session.get(ProductoEntity.class, id);
            String hql = "FROM DetalleVentaEntity WHERE producto = :producto";
            Query<DetalleVentaEntity> query = session.createQuery(hql,DetalleVentaEntity.class);
            query.setParameter("producto", producto);
            List<DetalleVentaEntity> detalleVentas = query.getResultList();
            session.close();
            return detalleVentas;
        } catch (HibernateException e) {
            if (tra != null) {
                tra.rollback();
            }
            return null;
        }  
    }
    
    public List<DetalleVentaEntity> findByVenta(String id){
        try {
            sf = HibernateUtil.getSessionFactory();
            session = sf.getCurrentSession();
            tra = session.beginTransaction();
            VentaEntity venta = session.get(VentaEntity.class, id);
            String hql = "FROM DetalleVentaEntity WHERE venta = :venta";
            Query<DetalleVentaEntity> query = session.createQuery(hql,DetalleVentaEntity.class);
            query.setParameter("venta", venta);
            List<DetalleVentaEntity> detalleVentas = query.getResultList();
            session.close();
            return detalleVentas;
        } catch (HibernateException e) {
            if (tra != null) {
                tra.rollback();
            }
            return null;
        }  
    }
    
    public List<DetalleVentaEntity> findAll() {
        try {
            sf = HibernateUtil.getSessionFactory();
            session = sf.getCurrentSession();
            tra = session.beginTransaction();
            List<DetalleVentaEntity> detalleVentas;
            detalleVentas = session.createQuery("From DetalleVentaEntity detalleVentas order by detalleVentas.id").list();
            tra.commit();
            session.close();
            return detalleVentas;
        } catch (HibernateException e) {
            if (tra != null) {
                tra.rollback();
            }
            return null;
        }
    }
}

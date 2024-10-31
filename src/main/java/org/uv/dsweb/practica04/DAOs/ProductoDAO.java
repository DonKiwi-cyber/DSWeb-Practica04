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
import org.uv.dsweb.practica04.Entities.ProductoEntity;
import org.uv.dsweb.practica04.Hibernate.HibernateUtil;

/**
 *
 * @author ian
 */
public class ProductoDAO {
    
    SessionFactory sf = null;
    @Transient
    Transaction tra = null;
    Session session = null;
    
    public boolean save(ProductoEntity producto) {
        boolean success = false;
        try {
            sf = HibernateUtil.getSessionFactory();
            session = sf.getCurrentSession();
            tra = session.beginTransaction();
            session.save(producto);
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
    
    public boolean update(ProductoEntity producto, String id) {
        boolean success = false;
        try {
            sf = HibernateUtil.getSessionFactory();
            session = sf.getCurrentSession();
            tra = session.beginTransaction();
            ProductoEntity prod = session.get(ProductoEntity.class, id);
            if (prod != null) {
                prod.setDescripcion(producto.getDescripcion());
                prod.setPrecio(producto.getPrecio());
                session.update(prod);
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
            ProductoEntity prod = session.get(ProductoEntity.class, id);
            if (prod != null) {
                session.delete(prod);
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
    
    public ProductoEntity findByID(String id) {
        try {
            sf = HibernateUtil.getSessionFactory();
            session = sf.getCurrentSession();
            tra = session.beginTransaction();
            ProductoEntity emp = session.get(ProductoEntity.class, id);
            session.close();
            return emp;
        } catch (HibernateException e) {
            if (tra != null) {
                tra.rollback();
            }
            return null;
        }        
    }
    
    public List<ProductoEntity> findAll() {
        try {
            sf = HibernateUtil.getSessionFactory();
            session = sf.getCurrentSession();
            tra = session.beginTransaction();
            List<ProductoEntity> productos;
            productos = session.createQuery("From ProductoEntity productos order by productos.id").list();
            tra.commit();
            session.close();
            return productos;
        } catch (HibernateException e) {
            if (tra != null) {
                tra.rollback();
            }
            return null;
        }
    }
}

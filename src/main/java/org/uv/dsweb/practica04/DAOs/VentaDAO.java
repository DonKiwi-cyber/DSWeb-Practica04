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
import org.uv.dsweb.practica04.Entities.ClienteEntity;
import org.uv.dsweb.practica04.Entities.VentaEntity;
import org.uv.dsweb.practica04.Hibernate.HibernateUtil;

/**
 *
 * @author ian
 */
public class VentaDAO {
    
    SessionFactory sf = null;
    @Transient
    Transaction tra = null;
    Session session = null;
    
    public boolean save(VentaEntity venta) {
        boolean success = false;
        try {
            sf = HibernateUtil.getSessionFactory();
            session = sf.getCurrentSession();
            tra = session.beginTransaction();
            session.save(venta);
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
    
    public boolean update(VentaEntity venta, String id) {
        boolean success = false;
        try {
            sf = HibernateUtil.getSessionFactory();
            session = sf.getCurrentSession();
            tra = session.beginTransaction();
            VentaEntity ven = session.get(VentaEntity.class, id);
            if (ven != null) {
                ven.setFecha(ven.getFecha());
                ven.setCliente(venta.getCliente());
                ven.setNombreCliente(venta.getNombreCliente());
                ven.setCorreoCliente(venta.getCorreoCliente());
                session.update(ven);
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
            VentaEntity ven = session.get(VentaEntity.class, id);
            if (ven != null) {
                session.delete(ven);
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
    
    public VentaEntity findByID(String id) {
        try {
            sf = HibernateUtil.getSessionFactory();
            session = sf.getCurrentSession();
            tra = session.beginTransaction();
            VentaEntity ven = session.get(VentaEntity.class, id);
            session.close();
            return ven;
        } catch (HibernateException e) {
            if (tra != null) {
                tra.rollback();
            }
            return null;
        }        
    }
    
    public List<VentaEntity> findByCliente(String id){
        try {
            sf = HibernateUtil.getSessionFactory();
            session = sf.getCurrentSession();
            tra = session.beginTransaction();
            ClienteEntity cliente = session.get(ClienteEntity.class, id);
            String hql = "FROM VentaEntity WHERE cliente = :cliente";
            Query<VentaEntity> query = session.createQuery(hql,VentaEntity.class);
            query.setParameter("cliente", cliente);
            List<VentaEntity> ventas = query.getResultList();
            session.close();
            return ventas;
        } catch (HibernateException e) {
            if (tra != null) {
                tra.rollback();
            }
            return null;
        }  
    }
    
    public List<VentaEntity> findAll() {
        try {
            sf = HibernateUtil.getSessionFactory();
            session = sf.getCurrentSession();
            tra = session.beginTransaction();
            List<VentaEntity> ventas;
            ventas = session.createQuery("From VentaEntity ventas order by ventas.id").list();
            tra.commit();
            session.close();
            return ventas;
        } catch (HibernateException e) {
            if (tra != null) {
                tra.rollback();
            }
            return null;
        }
    }
    
}

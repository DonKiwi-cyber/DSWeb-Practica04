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
import org.uv.dsweb.practica04.Entities.ClienteEntity;
import org.uv.dsweb.practica04.Hibernate.HibernateUtil;

/**
 *
 * @author ian
 */
public class ClienteDAO {

    SessionFactory sf = null;
    @Transient
    Transaction tra = null;
    Session session = null;

    public boolean save(ClienteEntity cliente) {
        boolean success = false;
        try {
            sf = HibernateUtil.getSessionFactory();
            session = sf.getCurrentSession();
            tra = session.beginTransaction();
            session.save(cliente);
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

    public boolean update(ClienteEntity cliente, String id) {
        boolean success = false;
        try {
            sf = HibernateUtil.getSessionFactory();
            session = sf.getCurrentSession();
            tra = session.beginTransaction();
            ClienteEntity cli = session.get(ClienteEntity.class, id);
            if (cli != null) {
                cli.setNombre(cliente.getNombre());
                cli.setCorreo(cliente.getCorreo());
                session.update(cli);
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
            ClienteEntity cli = session.get(ClienteEntity.class, id);
            if (cli != null) {
                session.delete(cli);
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

    public ClienteEntity findByID(String id) {
        try {
            sf = HibernateUtil.getSessionFactory();
            session = sf.getCurrentSession();
            tra = session.beginTransaction();
            ClienteEntity cliente = session.get(ClienteEntity.class, id);
            session.close();
            return cliente;
        } catch (HibernateException e) {
            if (tra != null) {
                tra.rollback();
            }
            return null;
        }
    }

    public List<ClienteEntity> findAll() {
        try {
            sf = HibernateUtil.getSessionFactory();
            session = sf.getCurrentSession();
            tra = session.beginTransaction();
            List<ClienteEntity> clientes;
            clientes = session.createQuery("From ClienteEntity clientes order by clientes.id").list();
            tra.commit();
            session.close();
            return clientes;
        } catch (HibernateException e) {
            if (tra != null) {
                tra.rollback();
            }
            return null;
        }
    }
}

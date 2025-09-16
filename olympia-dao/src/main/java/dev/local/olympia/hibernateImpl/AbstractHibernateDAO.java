package dev.local.olympia.hibernateImpl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Optional;

@Repository
public abstract class AbstractHibernateDAO<T, ID extends Serializable> {
    @Autowired
    protected SessionFactory sessionFactory;

    protected Class<T> entityClass;

    public AbstractHibernateDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}

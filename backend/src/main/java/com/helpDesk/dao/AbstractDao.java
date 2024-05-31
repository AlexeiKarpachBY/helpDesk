package com.helpDesk.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;


public abstract class AbstractDao<T> {

    private Class<T> clazz;

    @PersistenceContext
    private EntityManager entityManager;

    public void setClazz(Class<T> clazzToSet) {
        this.clazz = clazzToSet;
    }

    public Optional<T> findOne(Long id) {

        return Optional.of(entityManager.find(clazz, id));

    }

    public List<T> findAll() {

        return entityManager.createQuery("FROM " + clazz.getName(), clazz)
                .getResultList();

    }

    public void save(T entity) {

        entityManager.persist(entity);

    }

    public void update(T entity) {

        entityManager.merge(entity);

    }

    public void delete(T entity) {

        entityManager.remove(entity);

    }

    protected EntityManager getEntityManager() {

        return entityManager;

    }
}

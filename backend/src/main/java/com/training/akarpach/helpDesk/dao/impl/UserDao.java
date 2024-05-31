package com.training.akarpach.helpDesk.dao.impl;

import com.training.akarpach.helpDesk.dao.AbstractDao;
import com.training.akarpach.helpDesk.enums.Role;
import com.training.akarpach.helpDesk.exception.UserNotFoundException;
import com.training.akarpach.helpDesk.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDao extends AbstractDao<User> {

    public UserDao() {
        setClazz(User.class);
    }

    public Optional<User> findUserByEmail(String email) {

        try {
            return Optional.of(getEntityManager()
                    .createQuery("FROM User u where u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult());
        } catch (NoResultException e) {
            throw new UserNotFoundException("User not found");
        }

    }

    public List<User> getAllUsersByRole(Role role) {

        return getEntityManager()
                .createQuery("FROM User u WHERE u.role = :role", User.class)
                .setParameter("role", role)
                .getResultList();

    }

}

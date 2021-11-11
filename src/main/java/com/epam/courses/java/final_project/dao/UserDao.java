package com.epam.courses.java.final_project.dao;

import com.epam.courses.java.final_project.model.User;
import com.epam.courses.java.final_project.dao.impl.jdbc.JDBCException;

import java.util.List;
import java.util.Optional;

/**
 * DAO interface for User.
 *
 * @author Kostiantyn Kolchenko
 * */
public interface UserDao extends AbstractDao<User>{

    Optional<User> getByLogin(String login) throws JDBCException;

    Optional<User> getByEmail(String email) throws JDBCException;

    List<User> getUsersByRole(User.Role role) throws JDBCException;
}

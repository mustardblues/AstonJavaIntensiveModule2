package edu.aston.userservice.dao;

import edu.aston.userservice.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    User create(final User user) throws UserDAOException;
    List<User> read() throws UserDAOException;
    Optional<User> read(final Long id) throws UserDAOException;
    void update(final User user) throws UserDAOException;
    boolean delete(final Long id) throws UserDAOException;
}

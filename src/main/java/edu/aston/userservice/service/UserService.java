package edu.aston.userservice.service;

import edu.aston.userservice.model.User;

import java.util.List;

public interface UserService {
    User createUser(final String name, final String email, final int age) throws UserServiceException;
    List<User> findAll() throws UserServiceException;
    User findById(final long id) throws UserServiceException;
    void updateUser(final long id, final String name, final String email, final int age) throws UserServiceException;
    boolean deleteById(final long id) throws UserServiceException;
}

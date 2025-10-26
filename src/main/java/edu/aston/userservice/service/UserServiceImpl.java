package edu.aston.userservice.service;

import edu.aston.userservice.model.User;
import edu.aston.userservice.dao.UserDAO;
import edu.aston.userservice.dao.UserDAOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserDAO userDAO;

    private static class UserValidator {
        static final int MIN_AGE = 18;
        static final int MAX_AGE = 99;

        public static void validateData(final String name, final String email, final int age) throws UserServiceException {
            if(isInvalidUserName(name)) {
                throw new UserServiceException("The user's name is invalid");
            }

            if(isInvalidUserEmail(email)) {
                throw new UserServiceException("The user's email is invalid");
            }

            if(age < MIN_AGE || age > MAX_AGE) {
                throw new UserServiceException("The user's age is out of range");
            }
        }

        public static void validateId(final long id) throws UserServiceException {
            if(id < 0L) {
                throw new UserServiceException("The user's ID must be greater than 0L");
            }
        }

        private static boolean isInvalidUserName(final String name) {
            if(name == null || name.isBlank()) {
                return true;
            }

            return !name.chars().allMatch(Character::isLetter);
        }

        private static boolean isInvalidUserEmail(final String email) {
            if(email == null || email.isBlank()) {
                return true;
            }

            return !email.contains("@");
        }
    }

    public UserServiceImpl(final UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public User createUser(final String name, final String email, final int age) throws UserServiceException {
        logger.info("Start creating a new user: [name={}, email={}, age={}].", name, email, age);

        try {
            UserValidator.validateData(name, email, age);

            final User user = new User(name, email, age);

            userDAO.create(user);

            logger.info("The user has been created: {}", user.toString());

            return user;
        }
        catch (Exception exception) {
            logger.error("Failed to add a new user to the database.");
            throw new UserServiceException("Failed to add a new user to the database", exception);
        }
    }

    @Override
    public List<User> findAll() throws UserServiceException {
        logger.info("Start searching for all users in the database.");

        try {
            final List<User> list =  userDAO.read();

            logger.info("Found {} users in the database.", list.size());

            return list;
        }
        catch (UserDAOException exception) {
            logger.error("Failed to find all users in the database.");
            throw new UserServiceException("Failed to find all users in the database", exception);
        }
    }

    @Override
    public User findById(final long id) throws UserServiceException {
        logger.info("Start searching a user by ID: [id={}].", id);

        try {
            UserValidator.validateId(id);

            return userDAO.read(id).orElseThrow(() -> new UserServiceException("The user could not be found"));
        }
        catch (UserDAOException exception) {
            logger.error("Failed to find the user by ID in the database.");
            throw new UserServiceException("Failed to find the user by ID in the database", exception);
        }
    }

    @Override
    public void updateUser(final long id, final String name, final String email, final int age) throws UserServiceException {
        logger.info("Start updating user information: [id={}, name={}, email={}, age={}].", id, name, email, age);

        try {
            UserValidator.validateId(id);
            UserValidator.validateData(name, email, age);

            final User user = new User(id, name, email, age);

            this.userDAO.update(user);
        }
        catch(Exception exception) {
            logger.error("Failed to update user information in the database.");
            throw new UserServiceException("Failed to update user information in the database", exception);
        }
    }

    @Override
    public boolean deleteById(final long id) throws UserServiceException {
        logger.info("Deleting a user from the database: [id={}].", id);

        try {
            UserValidator.validateId(id);

            if(this.userDAO.delete(id)) {
                logger.info("The user with ID {} was deleted from the database.", id);

                return true;
            }
            else {
                logger.warn("The user with ID {} does not exist in the database.", id);

                return false;
            }
        }
        catch(Exception exception) {
            logger.error("Failed to delete a user information from the database.");
            throw new UserServiceException("Failed to delete a user information from the database", exception);
        }
    }
}

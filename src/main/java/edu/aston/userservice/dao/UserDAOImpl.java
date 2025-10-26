package edu.aston.userservice.dao;

import edu.aston.userservice.model.User;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class UserDAOImpl implements UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

    private final SessionFactory sessionFactory;

    public UserDAOImpl(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User create(final User user) throws UserDAOException{
        logger.info("Running session to create a new user...");

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            try {
                logger.info("Running the creation process for a new user in the database.");

                session.persist(user);
                transaction.commit();

                logger.info("Stopping the creation process for a new user in the database.");

                return user;
            } catch (HibernateException exception) {
                transaction.rollback();

                logger.error("Session could not add user to the database: {}.", exception.getMessage());
                throw new UserDAOException("Session could not add user to the database");
            }
        } catch (SessionException exception) {
            logger.error("Session could not run to add user to the database: {}.", exception.getMessage());
            throw new UserDAOException("Session could not run to add user to the database");
        }
    }

    @Override
    public List<User> read() throws UserDAOException {
        logger.info("Running session to find all users in the database...");

        try(Session session = sessionFactory.openSession()) {
            logger.info("Running the search process for all users in the database.");

            Query<User> query = session.createQuery("FROM User", User.class);
            List<User> list = query.list();

            logger.info("Stopping the search process for all users in the database.");

            return list;
        }
        catch(SessionException exception) {
            logger.error("Session could not run to read all users in the database: {}.", exception.getMessage());
            throw new UserDAOException("Session could not run to read all users in the database");
        }
        catch(HibernateException exception) {
            logger.error("Session could not read all users in the database: {}.", exception.getMessage());
            throw new UserDAOException("Session could not read all users in the database");
        }
    }

    @Override
    public Optional<User> read(final Long id) throws UserDAOException {
        logger.info("Running session to find the user by ID...");

        try(Session session = sessionFactory.openSession()) {
            logger.info("Running the search process for a user by ID.");

            final User user = session.find(User.class, id);

            logger.info("Stopping the search process for a user by ID.");

            return Optional.ofNullable(user);
        }
        catch(SessionException exception) {
            logger.error("Session could not run to read a user by ID in the database: {}.", exception.getMessage());
            throw new UserDAOException("Session could not run to read a user by ID in the database");
        }
    }

    @Override
    public void update(final User user) throws UserDAOException {
        logger.info("Running session to update user information...");

        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            try {
                logger.info("Running the update process for user in the database.");

                session.merge(user);
                transaction.commit();

                logger.info("Stopping the update process for user in the database.");
            }
            catch(HibernateException exception) {
                transaction.rollback();

                logger.error("Session could not update user information in the database: {}.", exception.getMessage());
                throw new UserDAOException("Session could not update user information in the database");
            }
        }
        catch(SessionException exception) {
            logger.error("Session could not run to update user information in the database: {}.", exception.getMessage());
            throw new UserDAOException("Session could not run to update user information in the database");
        }
    }

    @Override
    public boolean delete(final Long id) throws UserDAOException {
        logger.info("Running session to delete a user from the database...");

        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            try{
                logger.info("Running the delete process for a user in the database.");

                final User user = session.find(User.class, id);

                if(user != null) {
                    session.remove(user);
                    transaction.commit();
                }

                logger.info("Running the delete process for a user in the database.");

                return user != null;
            }
            catch(HibernateException exception) {
                transaction.rollback();

                logger.info("Session could not delete user from the database: {}.", exception.getMessage());
                throw new UserDAOException("Session could not delete user from the database");
            }

        }
        catch(SessionException exception) {
            logger.error("Session could not run to delete user from the database: {}.", exception.getMessage());
            throw new UserDAOException("Session could not run to delete user from the database");
        }
    }
}

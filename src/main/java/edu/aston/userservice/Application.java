package edu.aston.userservice;

import edu.aston.userservice.gui.Cli;

import edu.aston.userservice.dao.UserDAOImpl;
import edu.aston.userservice.service.UserServiceImpl;
import edu.aston.userservice.util.HibernateUtil;

public class Application {
    public static void main(String[] args) {
        try {
            final Cli cli = new Cli(new UserServiceImpl(new UserDAOImpl(HibernateUtil.getSessionFactory())));

            cli.run();
        }
        catch(Exception exception) {
            System.exit(1);
        }
        finally {
            HibernateUtil.shutdown();
        }
    }
}

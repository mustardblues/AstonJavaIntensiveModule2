package edu.aston.userservice.gui;

import edu.aston.userservice.model.User;
import edu.aston.userservice.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cli {
    private static final Logger logger = LoggerFactory.getLogger(Cli.class);

    private String[] userInput;

    private final List<String> commands = List.of("create", "read", "update", "delete", "help");

    private interface Method { String method(); }

    final Method[] consoleMethods = {this::create, this::read, this::update, this::delete, this::help};

    private final UserService userService;

    public Cli(final UserService userService) {
        this.userService = userService;
    }

    public void run() {
        final Scanner scanner = new Scanner(System.in);

        System.out.println(help());

        while(true) {
            System.out.print("-> ");

            this.userInput = scanner.nextLine().split(" ");

            if("exit".equals(this.userInput[0])) {
                break;
            }

            final int index = this.commands.indexOf(this.userInput[0]);

            if(index > -1 && index < this.consoleMethods.length) {
                logger.info("User input: {}", Arrays.toString(userInput));

                System.out.println(consoleMethods[index].method());
            }
        }
    }

    private String create() {
        if(userInput.length < 4) {
            logger.warn("Incorrect user input for creating a new user in the database.");
            return "Incorrect user input for creating a new user in the database";
        }

        try {
            final String name = userInput[1];
            final String email = userInput[2];
            final int age = Integer.parseInt(userInput[3]);

            this.userService.createUser(name, email, age);

            return "CREATED";
        }
        catch(NumberFormatException exception) {
            logger.info("Incorrect number format to create a new user: {}.", exception.getMessage());
            return "Incorrect number format to create a new user: " + exception.getMessage();
        }
        catch (Exception exception) {
            return exception.getMessage();
        }
    }

    private String read() {
        return userInput.length > 1 ? readById() : readAll();
    }

    private String readAll() {
        try {
            final List<User> list = this.userService.findAll();

            StringBuilder builder = new StringBuilder();

            for(final User user : list) {
                builder.append(user.toString()).append("\n");
            }

            return "READ\n" + builder;
        }
        catch (Exception exception) {
            return exception.getMessage();
        }
    }

    private String readById() {
        if(userInput.length < 2) {
            logger.warn("Incorrect user input for reading user information in the database.");
            return "Incorrect user input for reading user information in the database";
        }

        try {
            final long id = Long.parseLong(userInput[1]);

            final User user = this.userService.findById(id);

            return "READ\n" + user.toString();
        }
        catch(NumberFormatException exception) {
            logger.info("Incorrect number format to read by ID: {}.", exception.getMessage());
            return "Incorrect number format to read by ID: " + exception.getMessage();
        }
        catch (Exception exception) {
            return exception.getMessage();
        }
    }

    private String update() {
        if(userInput.length < 5) {
            logger.warn("Incorrect user input for updating user information in the database.");
            return "Incorrect user input for updating user information in the database";
        }

        try {
            final long id = Long.parseLong(userInput[1]);
            final String name = userInput[2];
            final String email = userInput[3];
            final int age = Integer.parseInt(userInput[4]);

            this.userService.updateUser(id, name, email, age);

            return "UPDATED";
        }
        catch(NumberFormatException exception) {
            logger.info("Incorrect number format to update user information: {}.", exception.getMessage());
            return "Incorrect number format to update user information: " + exception.getMessage();
        }
        catch (Exception exception) {
            return exception.getMessage();
        }
    }

    private String delete() {
        if(userInput.length < 2) {
            logger.warn("Incorrect user input for deleting user information from the database.");
            return "Incorrect user input for deleting user information from the database";
        }

        try {
            final long id = Long.parseLong(userInput[1]);

            return this.userService.deleteById(id) ? "DELETED" : "No user with ID " + id + " in the database";
        }
        catch(NumberFormatException exception) {
            logger.info("Incorrect number format to delete user information: {}.", exception.getMessage());
            return "Incorrect number format to delete user information: " + exception.getMessage();
        }
        catch (Exception exception) {
            return exception.getMessage();
        }
    }

    private String help() {
        return "COMMANDS:\n" +
                "* create <name> <email> <age>\n" +
                "* read (or) read <id>\n" +
                "* update <id> <name> <email> <age>\n" +
                "* delete <id>\n" +
                "* help\n" +
                "* exit";
    }
}

package Main;

import User.Menu;

import java.sql.*;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    private static final Logger log = LogManager.getLogger(Main.class);

    private static Scanner scanner = new Scanner(System.in);
    private static final String url = "jdbc:mysql://localhost:3306/pp_lab_4_8";
    private static final String username = "root";
    private static final String password = "PASSWORD";


    public static void main(String[] args) {
        System.setProperty("mail.smtp.starttls.enable", "true");
        log.info("Початок роботи");
        scanner = new Scanner(System.in);
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            //Class.forName("com.mysql.jdbc.Driver");
            int userID = -1;
            while (userID < 0) {
                userID = login(connection);
            }
            if (userID == 0) return;
            Menu userMenu = new Menu(connection, scanner, userID);
            userMenu.start();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            log.error(throwables);
        }
        scanner.close();
        log.info("Кінець роботи");
    }

    // return userID or -1 if invalid login
    private static int login(Connection connection) {
        System.out.println("""
                ***------------------------------------------------***
                Виберіть дію:
                0 - вийти з програми
                1 - увійти
                2 - зареєструватися""");
        int choice;
        String str = "Введіть цифру від 0 до 2 !";
        do  {
            System.out.println(str);
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                choice = -1;
            }
        } while (choice < 0 || choice > 2);
        if (choice == 1) return singIn(connection);
        if (choice == 2) return singUp(connection);
        return 0;
    }

    private static int singIn(Connection connection) {
        System.out.println("Введіть ім'я користувача: ");
        String userName = scanner.nextLine();
        String query = "select userID, password from users where userName = '" + userName + "';";
        try (Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                System.out.println("Введіть пароль: ");
                String password = scanner.nextLine();
                if (password.equals(resultSet.getString("password"))) {
                    log.info("Успішна спроба увійти");
                    return resultSet.getInt("userID");
                } else {
                    System.out.println("Неправильний логін або пароль! Спробуйте увійти ще раз.");
                }
            } else {
                System.out.println("Немає такого користувача! Введіть логін ще раз або зареєструйтеся як новий користувач.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        log.info("Невдала спроба увійти");
        return -1;
    }

    private static int singUp(Connection connection) {
        System.out.println("Введіть ім'я користувача: ");
        String userName = scanner.nextLine();
        String query1 = "select userID, password from users where userName = '" + userName + "';";
        try (Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(query1);
            if (!resultSet.next()) {
                String password = "password";
                String confirmPassword;
                boolean isPasswordConfirmed = false;
                while (!isPasswordConfirmed) {
                    System.out.println("Введіть пароль: ");
                    password = scanner.nextLine();
                    System.out.println("Введіть пароль ще раз: ");
                    confirmPassword = scanner.nextLine();
                    if (!password.equals(confirmPassword)) {
                        System.out.println("Паролі не збігаються! Введіть пароль ще раз!");
                    }
                    else if (password.length() < 8 || password.length() > 32){
                        System.out.println("Пароль має мати довжину від 8 до 32 символів! Введіть пароль ще раз");
                    }
                    else isPasswordConfirmed = true;
                }
                String query2 = "insert into users (userName, password) values ('" + userName + "', '" + password + "');";
                if (statement.executeUpdate(query2) == 1) {
                    resultSet = statement.executeQuery("select LAST_INSERT_ID() as last_id;");
                    if (resultSet.next()) {
                        System.out.println("Користувач успішно зареєстрований!");
                        log.info("Успішна спроба зареєструватися");
                        return resultSet.getInt("last_id");
                    } else {
                        System.out.println("Упс, щось пішло не так. Попробуйте зареєструватися ще раз.");
                    }
                } else {
                    System.out.println("Не вдалось зареєструвати користувача. Попробуйте зареєструватися ще раз.");
                }
            } else {
                System.out.println("Такий користувач уже існує! Введіть інше ім'я");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        log.info("Невдала спроба зареєструватися");
        return -1;
    }
}
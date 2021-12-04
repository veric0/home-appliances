package User;

import Appliances.*;
import Conditional.*;

import java.sql.Connection;
import java.util.Scanner;

public class Menu {

    private final Scanner scanner;
    private final User user;


    public Menu(Connection connection, Scanner scanner, int userID) {
        this.scanner = scanner;
        user = new User(connection, userID);
    }

    public void start() {
        int choice;

        while (true) {
            System.out.println("""
                ***------------------------------------------------***
                Виберіть дію:
                0 - вийти з програми
                1 - додати електроприлад
                2 - показати електроприлади
                3 - видалити електроприлад
                4 - порахувати загальну потужність
                5 - знайти електроприлади в заданому діапазоні потужності""");
            choice = inputInt(0, 5);
            switch (choice) {
                case 1 -> {
                    Appliance newAppliance = createAppliance();
                    if (newAppliance != null) {
                        user.addAppliance(newAppliance);
                    }
                }
                case 2 -> findAppliance();
                case 3 -> removeAppliance();
                case 4 -> user.calculatePower();
                case 5 -> findApplianceWithinPowerLimits();
                default -> {
                    return;
                }
            }
        }

    }

    private Appliance createAppliance() {
        System.out.println("""
                Виберіть який електроприлад додати:
                0 - скасувати додавання електроприлада
                1 - чайник
                2 - мікрохвильовка
                3 - холодильник
                4 - блендер""");
        int choice = inputInt(0, 5);
        return switch (choice) {
            case 1 -> new Kettle();
            case 2 -> new Microwave();
            case 3 -> new Fridge();
            case 4 -> new Blender();
            default -> null;
        };
    }

    private void findAppliance() {
        System.out.println("""
                Виберіть умову:
                0 - повернутися назад
                1 - усі електроприлади
                2 - електроприлади які потрібно ремонтувати
                3 - включені в розетку електроприлади
                4 - виключені електроприлади
                """);
        int choice = inputInt(0, 4);
        Condition condition;
        switch (choice) {
            case 1 -> user.findAppliances(new TrueCondition());
            case 2 -> {
                user.findAppliances(new IsNeedsRepair());
                System.out.print("Введіть ID електроприлада який потрібно поремонтувати (0 - скасувати): ");
                int id = inputPositiveInt();
                if (id == 0) break;
                user.repairAppliance(id);
            }
            case 3 -> {
                user.findAppliances(new IsPluggedIn());
                System.out.print("Введіть ID електроприлада який потрібно вимкнути (0 - скасувати): ");
                int id = inputPositiveInt();
                if (id == 0) break;
                user.unplugAppliance(id);
            }
            case 4 -> {
                user.findAppliances(new IsUnplugged());
                System.out.print("Введіть ID електроприлада який потрібно увімкнути (0 - скасувати): ");
                int id = inputPositiveInt();
                if (id == 0) break;
                user.plugInAppliance(id);
            }
            default -> {
            }
        }
    }

    private void removeAppliance() {
        System.out.print("Введіть ID електроприлада який потрібно видалити (0 - скасувати): ");
        int id = inputPositiveInt();
        if (id != 0) {
            user.removeAppliance(id);
        }
    }

    private void findApplianceWithinPowerLimits() {
        System.out.println("Ведіть мінімальну і максимальну потужність: ");
        int min = inputPositiveInt();
        int max = inputPositiveInt();
        user.findAppliances(new IsPowerWithinLimits(min, max));
    }


    public int inputPositiveInt() {
        int res;
        String str = "Введіть додатнє число!";
        do  {
            System.out.println(str);
            try {
                res = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                res = -1;
            }
        }
        while (res < 0);
        return res;
    }

    public int inputInt(int min, int max) {
        if (min > max) {
            int temp = min;
            min = max;
            max = temp;
        }
        int res;
        String str = "Введіть цифру від " + min + " до " + max +" !";
        do  {
            System.out.println(str);
            try {
                res = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                res = min - 1;
            }
        }
        while (res < min || res > max);
        return res;
    }
}

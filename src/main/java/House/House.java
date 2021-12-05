package House;

import Appliances.*;
import Conditional.Condition;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class House {
    private final Connection connection;
    private final int userID;

    public House(Connection connection, int userID) {
        this.connection = connection;
        this.userID = userID;
    }

    public void addAppliance(Appliance appliance) {
        if (appliance == null) return;
        try (Statement statement = connection.createStatement()) {
            String query1 = "insert into appliances (type, basicPower, power) values ('"
                    + appliance.getClass().getName() + "', "
                    + appliance.getPower() + ", "
                    + appliance.getPower() + ");";
            int rowcount = statement.executeUpdate(query1);
            if (rowcount == 1) {
                ResultSet resultSet = statement.executeQuery("select LAST_INSERT_ID() as last_id;");
                if (resultSet.next()) {
                    int appID = resultSet.getInt("last_id");
                    String query2 = "insert into houses (userID, applianceID) values (" + userID + ", " + appID + ")";
                    if (statement.executeUpdate(query2) == 1) {
                        System.out.println("Електроприлад успішно додано!");
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void calculateTotalPower() {
        try (Statement statement = connection.createStatement()){
            String query = "select sum(power) as sum from appliances where applianceID in (select applianceID from houses where userID = " + userID + ");";
            int totalPower;
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                totalPower = resultSet.getInt("sum");
                System.out.println("Загальна потужність = " + totalPower);
            } else {
                System.out.println("Не вдалося порахувати загальну потужність!");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void findAppliances(Condition condition) {
        List<Appliance> founded = new ArrayList<>();
        String query = "select * from appliances where " + condition.toSql() + " and applianceID in (select applianceID from houses where userID = " + userID + ") order by power desc;";
        try (Statement statement = connection.createStatement()) {
            ResultSet set = statement.executeQuery(query);
            Appliance appliance;
            String type;
            int id, basicPower, power;
            boolean isPlugged;
            while (set.next()) {
                type = set.getString("type");
                id = set.getInt("applianceID");
                basicPower = set.getInt("basicPower");
                power = set.getInt("power");
                isPlugged = set.getBoolean("isPlugged");
                appliance = createApplianceFromTable(id, type, basicPower, power, isPlugged);
                if (appliance != null) {
                    founded.add(appliance);
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        showListToConsole(founded, condition.getTitle());
    }

    public void removeAppliance(int applianceID) {
        String query1 = "delete from houses where userID = " + userID + " and applianceID = " + applianceID + ";";
        String query2 = "delete from appliances where applianceID = " + applianceID + ";";
        try (Statement statement = connection.createStatement()){
            if (statement.executeUpdate(query1) > 0 && statement.executeUpdate(query2) > 0) {
                System.out.println("Електроприлад успішло видалено!");
            } else {
                System.out.println("Не існує електроприладу з таким ID!");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void repairAppliance(int id) {
        String query1 = "select * from appliances where power < basicPower * 3 / 4 and applianceID = " + id + " and applianceID in (select applianceID from houses where userID = " + userID + ");";
        try (Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(query1);
            if (resultSet.next()) {
                Appliance appliance;
                String type;
                int appID, basicPower, power;
                boolean isPlugged;
                type = resultSet.getString("type");
                appID = resultSet.getInt("applianceID");
                basicPower = resultSet.getInt("basicPower");
                power = resultSet.getInt("power");
                isPlugged = resultSet.getBoolean("isPlugged");
                appliance = createApplianceFromTable(appID, type, basicPower, power, isPlugged);
                if (appliance == null) {
                    System.out.println("Немає електроприладу з таким ID для ввімкнення!");
                } else  {
                    String query2 = "update appliances set power = " + basicPower + " where applianceID = " + id + ";";
                    if (statement.executeUpdate(query2) == 1) {
                        appliance.repair();
                    } else {
                        System.out.println("Немає електроприладу з таким ID для ввімкнення!");
                    }
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void plugInAppliance(int id) {
        String query1 = "select * from appliances where isPlugged = false and applianceID = " + id + " and applianceID in (select applianceID from houses where userID = " + userID + ");";
        String query2 = "update appliances set isPlugged = true where applianceID = " + id + ";";
        try (Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(query1);
            if (resultSet.next()) {
                Appliance appliance;
                String type;
                int appID, basicPower, power;
                boolean isPlugged;
                type = resultSet.getString("type");
                appID = resultSet.getInt("applianceID");
                basicPower = resultSet.getInt("basicPower");
                power = resultSet.getInt("power");
                isPlugged = resultSet.getBoolean("isPlugged");
                appliance = createApplianceFromTable(appID, type, basicPower, power, isPlugged);
                if (appliance == null) {
                    System.out.println("Немає електроприладу з таким ID для ввімкнення!");
                } else  {
                    if (statement.executeUpdate(query2) == 1) {
                        appliance.plugIn();
                    } else {
                        System.out.println("Немає електроприладу з таким ID для ввімкнення!");
                    }
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void unplugAppliance(int id) {
        String query1 = "select * from appliances where isPlugged = false and applianceID = " + id + " and applianceID in (select applianceID from houses where userID = " + userID + ") order by power desc;";
        String query2 = "update appliances set isPlugged = false where applianceID = " + id + ";";
        try (Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(query1);
            if (resultSet.next()) {
                Appliance appliance;
                String type;
                int appID, basicPower, power;
                boolean isPlugged;
                type = resultSet.getString("type");
                appID = resultSet.getInt("applianceID");
                basicPower = resultSet.getInt("basicPower");
                power = resultSet.getInt("power");
                isPlugged = resultSet.getBoolean("isPlugged");
                appliance = createApplianceFromTable(appID, type, basicPower, power, isPlugged);
                if (appliance == null) {
                    System.out.println("Немає електроприладу з таким ID для вимкнення!");
                } else  {
                    if (statement.executeUpdate(query2) == 1) {
                        appliance.unplug();
                    } else {
                        System.out.println("Немає електроприладу з таким ID для вимкнення!");
                    }
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static void showListToConsole(List<Appliance> appliances, String title) {
        System.out.println(    "╔═══════════════════════════════════════════════════════╗");
        System.out.printf("║ %-53s ║\n", title);
        if (appliances.isEmpty()) {
            System.out.println("╠═══════════════════════════════════════════════════════╣");
            System.out.println("║ Електроприладів немає                                 ║");
            System.out.println("╚═══════════════════════════════════════════════════════╝");
        }
        else {
            System.out.println("╠══════╦═══════════╦═══════╦═══════════╦════════════════╣");
            System.out.println("║  ID  ║ Type      ║ Power ║ isPlugged ║ isNeedToRepair ║");
            System.out.println("╠══════╬═══════════╬═══════╬═══════════╬════════════════╣");
            for (Appliance appliance: appliances) {
                System.out.println(appliance);
            }
            System.out.println("╚══════╩═══════════╩═══════╩═══════════╩════════════════╝");
        }
    }

    private static Appliance createApplianceFromTable(int id, String type, int basicPower, int power, boolean isPlugged) {
        String blender = Blender.class.getName();
        String fridge = Fridge.class.getName();
        String kettle = Kettle.class.getName();
        String microwave = Microwave.class.getName();

        if (type.equals(blender)) {
            return new Blender(id, basicPower, power, isPlugged);
        } else if (type.equals(fridge)) {
            return new Fridge(id, basicPower, power, isPlugged);
        } else if (type.equals(kettle)) {
            return new Kettle(id, basicPower, power, isPlugged);
        } else if (type.equals(microwave)) {
            return new Microwave(id, basicPower, power, isPlugged);
        } else return null;
    }

}

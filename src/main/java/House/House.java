package House;

import Appliances.*;
import Conditional.Condition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class House {
    private static final Logger log = LogManager.getLogger(House.class);

    private final Connection connection;
    private final int userID;

    public House(Connection connection, int userID) {
        this.connection = connection;
        this.userID = userID;
    }

    public String addAppliance(Appliance appliance) {
        if (appliance == null) return null;
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
                        return "Електроприлад успішно додано!";
                    }
                }
            }
            log.error("Не вдалося додати користувача");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            log.error(throwables);
        }
        return null;
    }

    public String calculateTotalPower() {
        String res;
        try (Statement statement = connection.createStatement()){
            String query = "select sum(power) as sum from appliances where applianceID in (select applianceID from houses where userID = " + userID + ");";
            int totalPower;
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                totalPower = resultSet.getInt("sum");
                res = "Загальна потужність = " + totalPower;
            } else {
                log.error("Не вдалося порахувати загальну потужність");
                res = "Не вдалося порахувати загальну потужність!";
            }
            System.out.println(res);
            return res;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            log.error(throwables);
        }
        return null;
    }

    public String findAppliances(Condition condition) {
        if (condition == null) return null;
        List<Appliance> founded = new ArrayList<>();
        String query = "select * from appliances where " + condition.toSql() + " and applianceID in (select applianceID from houses where userID = " + userID + ") order by power desc;";
        try (Statement statement = connection.createStatement()) {
            ResultSet set = statement.executeQuery(query);
            Appliance appliance;
            String type;
            int id, basicPower, power;
            boolean isPlugged;
            int count = 0;
            while (set.next()) {
                type = set.getString("type");
                id = set.getInt("applianceID");
                basicPower = set.getInt("basicPower");
                power = set.getInt("power");
                isPlugged = set.getBoolean("isPlugged");
                appliance = createApplianceFromTable(id, type, basicPower, power, isPlugged);
                if (appliance != null) {
                    founded.add(appliance);
                    ++count;
                }
            }
            log.info("Знайдено " + count + " електроприладів");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            log.error(throwables);
        }
        String res = listToString(founded, condition.getTitle());
        System.out.println(res);
        return res;
    }

    public String removeAppliance(int applianceID) {
        String query1 = "delete from houses where userID = " + userID + " and applianceID = " + applianceID + ";";
        String query2 = "delete from appliances where applianceID = " + applianceID + ";";
        try (Statement statement = connection.createStatement()){
            String res;
            if (statement.executeUpdate(query1) > 0 && statement.executeUpdate(query2) > 0) {
                res = "Електроприлад успішло видалено!";
            } else {
                res = "Не існує електроприладу з таким ID!";
            }
            log.info(res);
            System.out.println(res);
            return res;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            log.error(throwables);
        }
        return null;
    }

    public String repairAppliance(int id) {
        String query1 = "select * from appliances where power <= basicPower * 3 / 4 and applianceID = " + id + " and applianceID in (select applianceID from houses where userID = " + userID + ");";
        try (Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(query1);
            String res;
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
                    res = "Немає електроприладу з таким ID для ремонту!";
                } else  {
                    String query2 = "update appliances set power = " + basicPower + " where applianceID = " + id + ";";
                    if (statement.executeUpdate(query2) == 1) {
                        res = appliance.repair();
                    } else {
                        res = "Немає електроприладу з таким ID для ремонту!";
                    }
                }
            } else {
                res = "Немає електроприладу з таким ID для ремонту!";
            }
            log.info(res);
            System.out.println(res);
            return res;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            log.error(throwables);
        }
        return null;
    }

    public String plugInAppliance(int id) {
        String query1 = "select * from appliances where isPlugged = false and applianceID = " + id + " and applianceID in (select applianceID from houses where userID = " + userID + ");";
        String query2 = "update appliances set isPlugged = true, power = power - (basicPower * 1 / 20) where applianceID = " + id + ";";
        try (Statement statement = connection.createStatement()) {
            String res;
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
                    res = "Немає електроприладу з таким ID для ввімкнення!";
                } else  {
                    if (statement.executeUpdate(query2) == 1) {
                        res = appliance.plugIn();
                    } else {
                        res = "Немає електроприладу з таким ID для ввімкнення!";
                    }
                }
            } else {
                res = "Немає електроприладу з таким ID для ввімкнення!";
            }
            log.info(res);
            System.out.println(res);
            return res;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            log.error(throwables);
        }
        return null;
    }

    public String unplugAppliance(int id) {
        String res;
        String query1 = "select * from appliances where isPlugged = true and applianceID = " + id + " and applianceID in (select applianceID from houses where userID = " + userID + ") order by power desc;";
        String query2 = "update appliances set isPlugged = false, power = power - (basicPower * 1 / 20) where applianceID = " + id + ";";
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
                    res = "Немає електроприладу з таким ID для вимкнення!";
                } else  {
                    if (statement.executeUpdate(query2) == 1) {
                        res = appliance.unplug();
                    } else {
                        res = "Немає електроприладу з таким ID для вимкнення!";
                    }
                }
            } else {
                res = "Немає електроприладу з таким ID для вимкнення!";
            }
            log.info(res);
            System.out.println(res);
            return res;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            log.error(throwables);
        }
        return null;
    }

    protected static String listToString(List<Appliance> appliances, String title) {
        if (appliances == null || title == null) {
            log.error("в методі listToString передали null");
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder(300);
        stringBuilder.append("╔═══════════════════════════════════════════════════════╗\n");
        stringBuilder.append(String.format("║ %-53s ║\n", title));
        if (appliances.isEmpty()) {
            stringBuilder.append("╠═══════════════════════════════════════════════════════╣\n");
            stringBuilder.append("║ Електроприладів немає                                 ║\n");
            stringBuilder.append("╚═══════════════════════════════════════════════════════╝\n");
        }
        else {
            stringBuilder.append("╠══════╦═══════════╦═══════╦═══════════╦════════════════╣\n");
            stringBuilder.append("║  ID  ║ Type      ║ Power ║ isPlugged ║ isNeedToRepair ║\n");
            stringBuilder.append("╠══════╬═══════════╬═══════╬═══════════╬════════════════╣\n");
            for (Appliance appliance: appliances) {
                stringBuilder.append(appliance);
                stringBuilder.append('\n');
            }
            stringBuilder.append("╚══════╩═══════════╩═══════╩═══════════╩════════════════╝\n");
        }
        return stringBuilder.toString();
    }

    protected static Appliance createApplianceFromTable(int id, String type, int basicPower, int power, boolean isPlugged) {
        if (type == null) return null;
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

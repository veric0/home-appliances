package User;

import Appliances.Appliance;
import Command.*;
import Conditional.Condition;
import House.House;

import java.io.Serializable;
import java.sql.Connection;

public class User implements Serializable {
    private final House house;

    public User(Connection connection, int userID) {
        this.house = new House(connection, userID);
    }

    public void addAppliance(Appliance appliance) {
        new AddCommand(house, appliance).execute();
    }

    public void findAppliances(Condition condition) {
        new FindCommand(house, condition).execute();
    }

    public void calculatePower() {
        new CalculatePowerCommand(house).execute();
    }

    public void removeAppliance(int id) {
        new RemoveCommand(house, id).execute();
    }

    public void repairAppliance(int id) {
        new RepairCommand(house, id).execute();
    }

    public void unplugAppliance(int id) {
        new UnplugCommand(house, id).execute();
    }

    public void plugInAppliance(int id) {
        new PlugInCommand(house, id).execute();
    }
}

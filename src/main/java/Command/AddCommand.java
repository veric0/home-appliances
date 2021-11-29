package Command;

import Appliances.Appliance;
import House.House;

public class AddCommand implements Command {
    private House house;
    private Appliance appliance;

    public AddCommand(House house, Appliance appliance) {
        this.house = house;
        this.appliance = appliance;
    }

    @Override
    public void execute() {
        house.addAppliance(appliance);
    }
}

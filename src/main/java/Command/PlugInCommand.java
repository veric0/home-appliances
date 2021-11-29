package Command;

import House.House;

public class PlugInCommand implements Command {
    private House house;
    private int id;

    public PlugInCommand(House house, int id) {
        this.house = house;
        this.id = id;
    }

    @Override
    public void execute() {
        house.plugInAppliance(id);
    }
}

package Command;

import House.House;

public class UnplugCommand implements Command {
    private House house;
    private int id;

    public UnplugCommand(House house, int id) {
        this.house = house;
        this.id = id;
    }

    @Override
    public void execute() {
        house.unplugAppliance(id);
    }
}

package Command;

import House.House;

public class RepairCommand implements Command {
    private House house;
    private int id;

    public RepairCommand(House house, int id) {
        this.house = house;
        this.id = id;
    }

    @Override
    public void execute() {
        house.repairAppliance(id);
    }
}

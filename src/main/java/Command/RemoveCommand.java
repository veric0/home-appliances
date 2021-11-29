package Command;

import House.House;

public class RemoveCommand implements Command {
    private House house;
    private int id;

    public RemoveCommand(House house, int id) {
        this.house = house;
        this.id = id;
    }

    @Override
    public void execute() {
        house.removeAppliance(id);
    }
}

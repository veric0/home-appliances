package Command;

import House.House;

public class CalculatePowerCommand implements Command {
    private House house;

    public CalculatePowerCommand(House house) {
        this.house = house;
    }

    @Override
    public void execute() {
        house.calculateTotalPower();
    }
}

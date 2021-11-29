package Command;

import Conditional.Condition;
import House.House;

public class FindCommand implements Command {
    private House house;
    private Condition condition;

    public FindCommand(House house, Condition condition) {
        this.house = house;
        this.condition = condition;
    }

    @Override
    public void execute() {
        house.findAppliances(condition);

    }
}

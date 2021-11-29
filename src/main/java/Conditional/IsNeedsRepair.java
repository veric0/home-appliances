package Conditional;

import Appliances.Appliance;

public class IsNeedsRepair implements Condition {

    @Override
    public boolean condition(Appliance appliance) {
        return appliance.isNeedsRepair();
    }

    @Override
    public String getTitle() {
        return "Електроприлади, які потребують ремонту";
    }

    @Override
    public String toSql() {
        return "power < (basicPower * 3 / 4)";
    }
}

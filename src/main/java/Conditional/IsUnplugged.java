package Conditional;

import Appliances.Appliance;

public class IsUnplugged implements Condition {

    @Override
    public boolean condition(Appliance appliance) {
        return !appliance.isPluggedIn();
    }

    @Override
    public String getTitle() {
        return "Електроприлади які не під'єднані до розетки";
    }

    @Override
    public String toSql() {
        return "isPlugged = 0";
    }
}

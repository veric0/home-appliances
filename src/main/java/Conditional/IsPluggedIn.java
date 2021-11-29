package Conditional;

import Appliances.Appliance;

public class IsPluggedIn implements Condition {
    @Override
    public boolean condition(Appliance appliance) {
        return appliance.isPluggedIn();
    }

    @Override
    public String getTitle() {
        return "Електроприлади які під'єднані до розетки";
    }

    @Override
    public String toSql() {
        return "isPlugged = 1";
    }
}

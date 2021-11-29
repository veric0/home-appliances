package Conditional;

import Appliances.Appliance;

public class TrueCondition implements Condition {
    @Override
    public boolean condition(Appliance appliance) {
        return true;
    }

    @Override
    public String getTitle() {
        return "Усі електроприлади";
    }

    @Override
    public String toSql() {
        return "applianceID is not null";
    }
}

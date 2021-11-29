package Conditional;

import Appliances.Appliance;

public class IsPowerWithinLimits implements Condition {
    int min;
    int max;

    public IsPowerWithinLimits(int min, int max) {
        if (min > max) {
            int temp = min;
            min = max;
            max = temp;
        }
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean condition(Appliance appliance) {
        return appliance.isPowerWithinLimits(min, max);
    }

    @Override
    public String getTitle() {
        return "Електроприлади з потужністю в межах [" + min + "; " + max + "]";
    }

    @Override
    public String toSql() {
        return "power between " + min + " and " + max;
    }
}

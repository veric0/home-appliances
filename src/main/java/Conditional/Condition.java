package Conditional;

import Appliances.Appliance;

public interface Condition {

    boolean condition(Appliance appliance);
    String getTitle();
    String toSql();
}

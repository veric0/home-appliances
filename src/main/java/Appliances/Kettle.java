package Appliances;

public class Kettle extends Appliance {
    public Kettle() {
        super();
        setPower(2000);
    }
    public Kettle(int id, int basicPower, int power, boolean isPlugged) {
        this.id = id;
        this.basicPower = basicPower;
        this.power = power;
        this.isPlugged = isPlugged;
    }

    @Override
    public void repair() {
        power = basicPower;
        System.out.println("Чайник №" + id + "відремонтований!");
    }

    @Override
    public void plugIn() {
        System.out.println("Чайник №" + id + "почав гріти воду");
    }

    @Override
    public void unplug() {
        System.out.println("Чайник №" + id + "закипів");
    }

    @Override
    public String toString() {
        return String.format("║ %4d ║ Kettle    ║ %5d ║ %9b ║ %14b ║", id, power, isPlugged, isNeedsRepair());
    }
}

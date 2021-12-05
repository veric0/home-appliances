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
    public String repair() {
        power = basicPower;
        System.out.println("Чайник №" + id + "відремонтований!");
        return "Чайник №" + id + "відремонтований!";
    }

    @Override
    public String plugIn() {
        System.out.println("Чайник №" + id + "почав гріти воду");
        isPlugged = true;
        return "Чайник №" + id + "почав гріти воду";
    }

    @Override
    public String unplug() {
        System.out.println("Чайник №" + id + "закипів");
        isPlugged = false;
        return "Чайник №" + id + "закипів";
    }

    @Override
    public String toString() {
        return String.format("║ %4d ║ Kettle    ║ %5d ║ %9b ║ %14b ║", id, power, isPlugged, isNeedsRepair());
    }
}

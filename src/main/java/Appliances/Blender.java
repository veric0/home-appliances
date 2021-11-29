package Appliances;

public class Blender extends Appliance{
    public Blender() {
        super();
        setPower(700);
    }

    public Blender(int id, int basicPower, int power, boolean isPlugged) {
        this.id = id;
        this.basicPower = basicPower;
        this.power = power;
        this.isPlugged = isPlugged;
    }

    @Override
    public void repair() {
        power = basicPower;
        System.out.println("Блендер №" + id + "відремонтований!");
    }

    @Override
    public void plugIn() {
        System.out.println("Блендер №" + id + "почав працювати");
    }

    @Override
    public void unplug() {
        System.out.println("Блендер №" + id + "вимкнули");
    }

    @Override
    public String toString() {
        return String.format("║ %4d ║ Blender   ║ %5d ║ %9b ║ %14b ║", id, power, isPlugged, isNeedsRepair());
    }
}

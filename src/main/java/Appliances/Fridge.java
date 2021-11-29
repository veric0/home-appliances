package Appliances;

public class Fridge extends Appliance {
    public Fridge() {
        super();
        setPower(300);
    }

    public Fridge(int id, int basicPower, int power, boolean isPlugged) {
        this.id = id;
        this.basicPower = basicPower;
        this.power = power;
        this.isPlugged = isPlugged;
    }

    @Override
    public void repair() {
        power = basicPower;
        System.out.println("Холодильник №" + id + "відремонтований!");
    }

    @Override
    public void plugIn() {
        System.out.println("Холодильник №" + id + "почав охолоджувати продукти");
    }

    @Override
    public void unplug() {
        System.out.println("Холодильник №" + id + "вимкнули");
    }

    @Override
    public String toString() {
        return String.format("║ %4d ║ Fridge    ║ %5d ║ %9b ║ %14b ║", id, power, isPlugged, isNeedsRepair());
    }

}

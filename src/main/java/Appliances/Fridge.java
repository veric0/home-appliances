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
    public String repair() {
        power = basicPower;
        System.out.println("Холодильник №" + id + "відремонтований!");
        return "Холодильник №" + id + "відремонтований!";
    }

    @Override
    public String plugIn() {
        System.out.println("Холодильник №" + id + "почав охолоджувати продукти");
        isPlugged = true;
        return "Холодильник №" + id + "почав охолоджувати продукти";
    }

    @Override
    public String unplug() {
        System.out.println("Холодильник №" + id + "вимкнули");
        isPlugged = false;
        return "Холодильник №" + id + "вимкнули";
    }

    @Override
    public String toString() {
        return String.format("║ %4d ║ Fridge    ║ %5d ║ %9b ║ %14b ║", id, power, isPlugged, isNeedsRepair());
    }

}

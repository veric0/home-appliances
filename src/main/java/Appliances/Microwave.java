package Appliances;

public class Microwave extends Appliance {
    public Microwave() {
        super();
        setPower(1200);
    }
    public Microwave(int id, int basicPower, int power, boolean isPlugged) {
        this.id = id;
        this.basicPower = basicPower;
        this.power = power;
        this.isPlugged = isPlugged;
    }

    @Override
    public String repair() {
        power = basicPower;
        System.out.println("Мікрохвильова піч №" + id + "відремонтована!");
        return "Мікрохвильова піч №" + id + "відремонтована!";
    }

    @Override
    public String plugIn() {
        System.out.println("Мікрохвильова піч №" + id + "почала нагрівати продукти всередині");
        isPlugged = true;
        return "Мікрохвильова піч №" + id + "почала нагрівати продукти всередині";
    }

    @Override
    public String unplug() {
        System.out.println("Мікрохвильова піч №" + id + "завершила нагрівати");
        isPlugged = false;
        return "Мікрохвильова піч №" + id + "завершила нагрівати";
    }

    @Override
    public String toString() {
        return String.format("║ %4d ║ Microwave ║ %5d ║ %9b ║ %14b ║", id, power, isPlugged, isNeedsRepair());
    }
}

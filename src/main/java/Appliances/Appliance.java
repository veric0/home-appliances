package Appliances;

public abstract class Appliance {

    protected int id;
    protected int basicPower;
    protected int power;
    protected boolean isPlugged;

    public Appliance() {
        this.basicPower = 0;
        power = 0;
        isPlugged = false;
    }

    public Appliance(int id, int basicPower, int power, boolean isPlugged) {
        this.id = id;
        this.basicPower = basicPower;
        this.power = power;
        this.isPlugged = isPlugged;
    }

    protected void setPower(int power) {
        basicPower = power;
        this.power = power;
    }

    public int getPower() {
        return power;
    }

    public boolean isNeedsRepair() {
        return power > basicPower * 3 / 4;
    }
    public boolean isPowerWithinLimits(int min, int max) {
        return (power >= min) && (power <= max);
    }
    public boolean isPluggedIn() {
        return isPlugged;
    }

    abstract public void repair();
    public abstract void plugIn();
    public abstract void unplug();

}

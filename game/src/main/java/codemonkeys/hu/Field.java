package codemonkeys.hu;

public class Field {
    private int maxCapacity;
    private int moveInFactor;
    private final Coordinate coord;
    private Zone zone;
    private UpgradeLevel upgradeLevel;
    private Building building;

    public Field(Coordinate c) {
        this.coord = c;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getMoveInFactor() {
        return moveInFactor;
    }

    public void setMoveInFactor(int moveInFactor) {
        this.moveInFactor = moveInFactor;
    }

    public Coordinate getCoord() {
        return coord;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public UpgradeLevel getUpgradeLevel() {
        return upgradeLevel;
    }

    public void setUpgradeLevel(UpgradeLevel upgradeLevel) {
        this.upgradeLevel = upgradeLevel;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public int upgrade(int budget) throws RuntimeException{
        if(upgradeLevel == null){throw new NullPointerException("Field can't be upgraded!");}
        if(zone == null){throw new NullPointerException("Field has no zone!");}
        switch(upgradeLevel){
            case TOWN -> {upgradeLevel = UpgradeLevel.CITY; budget -= 100;} //exact amount is TODO
            case CITY -> {upgradeLevel = UpgradeLevel.METROPOLIS; budget -= 500;} //exact amount is TODO
            case METROPOLIS -> throw new RuntimeException("UpgradeLevel is already max!");
        }
        return budget;
    }

    public int deleteZone(int budget){
        if(building != null){throw new RuntimeException("Can't delete zone, there is a building on the field!");}
        if(zone == null){throw new RuntimeException("Field is already empty!");}
        zone = null;
        budget += 100; //exact amount is TODO
        return budget;
    }

    public int demolishBuilding(int budget){
        if(building == null){throw new RuntimeException("There is no building on the field!");}
        if(zone != null){throw new RuntimeException("Can't demolish public buildings! (There is a zone on the field!)");}
        building = null;
        // call animation
        budget += 100; //exact amount is TODO
        return budget;
    }

    public int buildBuilding(String buildingType, int budget){
        if(building != null){throw new RuntimeException("There already is a building on this field!");}
        if(buildingType == null || buildingType.equals("")){
            switch(zone){
                case RESIDENTIAL_ZONE -> building = new ResidentialBuilding();
                case INDUSTRIAL_ZONE -> building = new IndustrialWorkplace();
                case SERVICE_ZONE -> building = new ServiceWorkplace();
                default -> throw new RuntimeException("Building type not specified! (Zone is null)");
            }
        }else{
            switch(buildingType){
                case "policestation" -> {building = new PoliceStation(); budget -= 100;} //exact amount is TODO
                case "stadium" -> {building = new Stadium(); budget -= 100;} //exact amount is TODO
                case "firedepartment" -> {building = new FireDepartment(); budget -= 100;} //exact amount is TODO
                case "forest" -> {building = new Forest(); budget -= 100;} //exact amount is TODO
                case "road" -> {building = new Road(); budget -= 100;} //exact amount is TODO
                default -> throw new RuntimeException("Unrecognized building type!");
            }
        }
        return budget;
    }

    public int getCurrentCapacity(){
        if(building == null){throw new RuntimeException("There is no building on the field!");}
        return 0;
        //return building.getPeople().size();
    }
}

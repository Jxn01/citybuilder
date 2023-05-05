package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.graph.MutableGraph;
import com.sun.source.tree.Tree;
import controller.catastrophies.Catastrophe;
import controller.catastrophies.Covid;
import controller.catastrophies.FinancialCrisis;
import controller.catastrophies.Firestorm;
import controller.interfaces.SaveManager;
import controller.interfaces.SpeedManager;
import model.Coordinate;
import model.GameData;
import model.Person;
import model.buildings.Building;
import model.buildings.generated.GeneratedBuilding;
import model.buildings.generated.IndustrialWorkplace;
import model.buildings.generated.ResidentialBuilding;
import model.buildings.generated.ServiceWorkplace;
import model.buildings.interfaces.Flammable;
import model.buildings.interfaces.FunctionalBuilding;
import model.buildings.playerbuilt.Forest;
import model.buildings.playerbuilt.RangedBuilding;
import model.enums.Effect;
import model.enums.UpgradeLevel;
import model.enums.Zone;
import model.field.Field;
import model.field.PlayableField;
import util.Date;
import util.GraphDeserializer;
import util.Logger;
import view.components.GameGUI;
import view.components.Panel;
import view.enums.MenuState;

import javax.swing.*;
import java.io.File;
import java.util.*;
import java.util.Timer;
import java.util.stream.Collectors;


/**
 * This class represents the game manager.
 */
public class GameManager implements SaveManager, SpeedManager {
    private static final int STADIUM_BUILD_COST = 5000; // It will cost 5000 * 4 = 20000, because it contains 4 fields
    private static final int POLICE_BUILD_COST = 10000;
    private static final int ROAD_BUILD_COST = 1000;
    private static final int FIRE_STATION_BUILD_COST = 1000;
    private static final int FOREST_BUILD_COST = 1000;
    private static final int STADIUM_MAINTENANCE_COST = 5000;
    private static final int POLICE_MAINTENANCE_COST = 1000;
    private static final int ROAD_MAINTENANCE_COST = 100;
    private static final int FIRE_STATION_MAINTENANCE_COST = 1000;
    private static final int FOREST_MAINTENANCE_COST = 100;
    private static final int STADIUM_RANGE = 10;
    private static final int POLICE_RANGE = 10;
    private static final int FIRE_STATION_RANGE = 10;
    private static final int FOREST_RANGE = 10;
    private static final int FOREST_GROWTH_TIME = 10;
    private static final int MARK_RESIDENTIAL_COST = 1000;
    private static final int MARK_SERVICE_COST = 1000;
    private static final int MARK_INDUSTRIAL_COST = 1000;
    private static final int LEVEL_ONE_MAX_CAPACITY = 100;
    private static final int LEVEL_TWO_MAX_CAPACITY = 200;
    private static final int LEVEL_THREE_MAX_CAPACITY = 300;
    private static final int LEVEL_TWO_UPGRADE_COST = 10000;
    private static final int LEVEL_THREE_UPGRADE_COST = 100000;
    private static final double REFUND_PERCENT = 0.5;
    private static final int STARTER_MAP_SIZE = 51;
    private static final int STARTER_PEOPLE = 50;
    private static final int STARTER_BUDGET = 100000;
    private static final int STARTER_TAXES = 1000;
    private static final double FIRE_POSSIBILITY = 0.001;
    private static final int MAX_FIRETRUCKS = 2;
    private static final int PENSION = 1000;
    private static final double MAX_DISTRIBUTION = 0.7;
    private static GameData gameData;
    private static final double CATASTROPHE_CHANCE = 0.0001;
    private static final double HOSPITAL_CHANCE = 0.1;
    private static final int MIN_POPULATION = 10; // for game over
    private static final int MIN_SATISFACTION = 20; // for game over
    private SimulationSpeed simulationSpeed;
    private final List<Catastrophe> catastrophes;
    private final String saveDirectory = System.getProperty("user.home") + File.separator + ".citybuilder" + File.separator + "saves";
    private List<File> saveFiles;
    private Timer timer;
    private int delay = 1000;
    private int period = 1000;

    public GameManager() {
        simulationSpeed = SimulationSpeed.NORMAL;
        catastrophes = new ArrayList<>();
        catastrophes.add(FinancialCrisis.getInstance());
        catastrophes.add(Covid.getInstance());
        catastrophes.add(Firestorm.getInstance());

        File directory = new File(saveDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        Logger.log("Game manager created.");
    }

    public void initGame(String cityName) {
        setGameData(new GameData(cityName, STARTER_BUDGET, STARTER_TAXES, STARTER_PEOPLE, STARTER_MAP_SIZE));
        Logger.log("Game initialized.");
        startSimulation();
    }

    public static GameData getGameData() {
        return gameData;
    }

    public void setGameData(GameData gameData) {
        GameManager.gameData = gameData;
        Logger.log("Game data set to " + gameData.getId());
    }


    /**
     * Getter for the hospital chance.
     *
     * @return the hospital chance
     */
    public static double getHospitalChance() {
        return HOSPITAL_CHANCE;
    }

    /**
     * Getter for the budget.
     *
     * @return the budget
     */
    public static int getBudget() {
        return gameData.getBudget();
    }

    /**
     * Getter for the stadium build cost.
     *
     * @return the stadium build cost
     */
    public static int getStadiumBuildCost() {
        return STADIUM_BUILD_COST;
    }

    /**
     * Getter for the police build cost.
     *
     * @return the police build cost
     */
    public static int getPoliceBuildCost() {
        return POLICE_BUILD_COST;
    }

    /**
     * Getter for the fire station build cost.
     *
     * @return the fire station build cost
     */
    public static int getFireStationBuildCost() {
        return FIRE_STATION_BUILD_COST;
    }

    /**
     * Getter for the forest build cost.
     *
     * @return the forest build cost
     */
    public static int getForestBuildCost() {
        return FOREST_BUILD_COST;
    }

    /**
     * Getter for the road build cost.
     *
     * @return the road build cost
     */
    public static int getRoadBuildCost() {
        return ROAD_BUILD_COST;
    }

    /**
     * Getter for the stadium maintenance cost.
     *
     * @return the stadium maintenance cost
     */
    public static int getStadiumMaintenanceCost() {
        return STADIUM_MAINTENANCE_COST;
    }

    /**
     * Getter for the police maintenance cost.
     *
     * @return the police maintenance cost
     */
    public static int getPoliceMaintenanceCost() {
        return POLICE_MAINTENANCE_COST;
    }

    /**
     * Getter for the fire station maintenance cost.
     *
     * @return the fire station maintenance cost
     */
    public static int getFireStationMaintenanceCost() {
        return FIRE_STATION_MAINTENANCE_COST;
    }

    /**
     * Getter for the forest maintenance cost.
     *
     * @return the forest maintenance cost
     */
    public static int getForestMaintenanceCost() {
        return FOREST_MAINTENANCE_COST;
    }

    /**
     * Getter for the road maintenance cost.
     *
     * @return the road maintenance cost
     */
    public static int getRoadMaintenanceCost() {
        return ROAD_MAINTENANCE_COST;
    }

    /**
     * Getter for the stadium range.
     *
     * @return the stadium range
     */
    public static int getStadiumRange() {
        return STADIUM_RANGE;
    }

    /**
     * Getter for the police range.
     *
     * @return the police range
     */
    public static int getPoliceRange() {
        return POLICE_RANGE;
    }

    /**
     * Getter for the fire station range.
     *
     * @return the fire station range
     */
    public static int getFireStationRange() {
        return FIRE_STATION_RANGE;
    }

    /**
     * Getter for the forest range.
     *
     * @return the forest range
     */
    public static int getForestRange() {
        return FOREST_RANGE;
    }

    /**
     * Getter for the forest growth time.
     *
     * @return the forest growth time
     */
    public static int getForestGrowthTime() {
        return FOREST_GROWTH_TIME;
    }

    /**
     * Getter for the residential zone's marking cost.
     *
     * @return the residential zone's marking cost
     */
    public static int getMarkResidentialCost() {
        return MARK_RESIDENTIAL_COST;
    }

    /**
     * Getter for the service zone's marking cost.
     *
     * @return the service zone's marking cost
     */
    public static int getMarkServiceCost() {
        return MARK_SERVICE_COST;
    }

    /**
     * Getter for the industrial zone's marking cost.
     *
     * @return the industrial zone's marking cost
     */
    public static int getMarkIndustrialCost() {
        return MARK_INDUSTRIAL_COST;
    }

    /**
     * Getter for the refund percent
     *
     * @return the refund percent
     */
    public static double getRefundPercent() {
        return REFUND_PERCENT;
    }

    /**
     * Getter for the max capacity at level one
     *
     * @return the max capacity at level one
     */
    public static int getLevelOneMaxCapacity() {
        return LEVEL_ONE_MAX_CAPACITY;
    }

    /**
     * Getter for the max capacity at level two
     *
     * @return the max capacity at level two
     */
    public static int getLevelTwoMaxCapacity() {
        return LEVEL_TWO_MAX_CAPACITY;
    }

    /**
     * Getter for the max capacity at level three
     *
     * @return the max capacity at level three
     */
    public static int getLevelThreeMaxCapacity() {
        return LEVEL_THREE_MAX_CAPACITY;
    }

    /**
     * Getter for the level two upgrade cost
     *
     * @return the level two upgrade cost
     */
    public static int getLevelTwoUpgradeCost() {
        return LEVEL_TWO_UPGRADE_COST;
    }

    /**
     * Getter for the level three upgrade cost
     *
     * @return the level three upgrade cost
     */
    public static int getLevelThreeUpgradeCost() {
        return LEVEL_THREE_UPGRADE_COST;
    }

    /**
     * Getter for the starter budget
     *
     * @return the starter budget
     */
    public static int getStarterBudget() {
        return STARTER_BUDGET;
    }

    /**
     * Getter for the starter people
     *
     * @return the starter people
     */
    public static int getStarterPeople() {
        return STARTER_PEOPLE;
    }

    /**
     * Getter for the starter map size
     *
     * @return the starter map size
     */
    public static int getStarterMapSize() {
        return STARTER_MAP_SIZE;
    }

    /**
     * Getter for the starter taxes
     *
     * @return the starter taxes
     */
    public static int getStarterTaxes() {
        return STARTER_TAXES;
    }

    /**
     * Getter for the pension
     *
     * @return the pension
     */
    public static int getPension() {
        return PENSION;
    }

    /**
     * This method counts the disconnected part-graphs of the main graph.
     * Useful for checking if the demolition of a road would disconnect the graph.
     *
     * @param graph The graph to check.
     * @return The number of disconnected part-graphs.
     */
    public static int countDisconnectedGraphs(MutableGraph<Coordinate> graph) {
        int count = 0;

        Set<Coordinate> visited = new HashSet<>();
        for (Coordinate node : graph.nodes()) {
            if (!visited.contains(node)) {
                count++;
                visited.add(node);
                Queue<Coordinate> queue = new LinkedList<>();
                queue.add(node);
                while (!queue.isEmpty()) {
                    Coordinate current = queue.poll();
                    for (Coordinate neighbor : graph.adjacentNodes(current)) {
                        if (!visited.contains(neighbor)) {
                            visited.add(neighbor);
                            queue.add(neighbor);
                        }
                    }
                }
            }
        }
        return count;
    }

    public static int getPopulation() {
        return gameData.getPopulation();
    }

    public static MutableGraph<Coordinate> getGraph() {
        return gameData.getGraph();
    }

    public void doFinancials() {
        for (Person p : gameData.getPeople()) {
            if (p.isRetired()) {
                gameData.subtractFromBudget(PENSION);
            } else {
                gameData.addToBudget(gameData.getYearlyTaxes());
            }
        }

        for (PlayableField field : gameData.getPlayableFieldsWithBuildings()) {
            gameData.subtractFromBudget(field.getBuilding().getMaintenanceCost());
        }
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }


    public void startSimulation() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                if (gameData.isGameOver()) {
                    stopSimulation();
                }
                gameData.increaseDays();
                gameData.setInGameCurrentDate(Date.nextDay(gameData.getInGameCurrentDate()));

                //daily functions:
                buildingEffects();
                buildingOnFire();
                evokeCatastrophe();
                houseHomeless();
                employUnemployed();
                workplaceDistrEffect();
                negativeBudgetEffect();
                newPeople();
                removePeopleFromBuildings();
                isGameOver();

                Logger.log("A day is passed: " + gameData.getDays() + ".day");

                // check if a year has passed
                if (gameData.getInGameCurrentDate().matches("\\d{4}-01-01")) {
                    //yearly functions:
                    growForests();
                    peopleAge();
                    peopleDie();
                    peopleMoveAway();
                    doFinancials();

                    Logger.log("A year passed");
                }
            }
        }, delay, period);
    }

    private void isGameOver(){
        if(gameData.getPopulation() <= MIN_POPULATION || gameData.getAverageSatisfaction() <= MIN_SATISFACTION){
            gameData.setGameOver(true);
            timer.cancel();
            JOptionPane.showMessageDialog(null, "Game Over! You lost all your citizens!");
            MenuState ms = Panel.getState();
            ms = MenuState.MAINMENU;
        }
    }

    private void negativeBudgetEffect() {
        if (gameData.getBudget() < 0) {
            gameData.getPeople().forEach(p -> p.addEffect(Effect.BAD_BUDGET));
        } else {
            gameData.getPeople().forEach(p -> p.removeEffect(Effect.BAD_BUDGET));
        }
    }

    private void workplaceDistrEffect() {
        double serviceWs = (double) gameData.getPlayableFieldsWithBuildings()
                .stream()
                .filter(f -> f.getBuilding() instanceof ServiceWorkplace)
                .count();

        double industrialWs = (double) gameData.getPlayableFieldsWithBuildings()
                .stream()
                .filter(f -> f.getBuilding() instanceof IndustrialWorkplace)
                .count();

        if (serviceWs >= industrialWs && serviceWs != 0) {
            if (industrialWs / serviceWs > MAX_DISTRIBUTION) {
                gameData.getPeople().forEach(p -> p.addEffect(Effect.BAD_WORKPLACE_DIST));
            } else {
                gameData.getPeople().forEach(p -> p.removeEffect(Effect.BAD_WORKPLACE_DIST));
            }
        } else if (serviceWs < industrialWs) {
            if (serviceWs / industrialWs > MAX_DISTRIBUTION) {
                gameData.getPeople().forEach(p -> p.addEffect(Effect.BAD_WORKPLACE_DIST));
            } else {
                gameData.getPeople().forEach(p -> p.removeEffect(Effect.BAD_WORKPLACE_DIST));
            }
        } else {
            gameData.getPeople().forEach(p -> p.removeEffect(Effect.BAD_WORKPLACE_DIST));
        }
    }

    private void newPeople() {

    }

    private void employUnemployed() {

    }

    private void houseHomeless() {
        Stack<Person> homeless = gameData.getPeople()
                .stream()
                .filter(p -> p.getHome() == null)
                .collect(Collectors.toCollection(Stack::new));

        Stack<ResidentialBuilding> residentialBuildings = gameData.getPlayableFieldsWithBuildings()
                .stream()
                .filter(f -> f.getBuilding() instanceof ResidentialBuilding)
                .map(f -> (ResidentialBuilding) f.getBuilding())
                .collect(Collectors.toCollection(Stack::new));

        Stack<PlayableField> residentialZonesWithNoBuildings = Arrays.stream(gameData.getFields())
                .flatMap(Arrays::stream)
                .filter(f -> f instanceof PlayableField)
                .map(f -> (PlayableField) f)
                .filter(f -> f.getZone() == Zone.RESIDENTIAL_ZONE && f.getBuilding() == null)
                .collect(Collectors.toCollection(Stack::new));

        while(homeless.size() != 0) {
            if(residentialBuildings.size() == 0){
                if(residentialZonesWithNoBuildings.size() != 0){
                    PlayableField pf = residentialZonesWithNoBuildings.pop();
                    ResidentialBuilding newRb = (ResidentialBuilding) pf.buildBuilding(null);
                    residentialBuildings.add(newRb);
                } else {
                    break;
                }
            }else{
                ResidentialBuilding rb = residentialBuildings.pop();
                while(rb.getPeople().size() < rb.getMaxCapacity() && homeless.size() != 0){
                    Person p = homeless.pop();
                    rb.addPerson(p);
                }
            }
        }
    }

    private void buildingOnFire() {
        gameData.getPlayableFieldsWithBuildings()
                .stream()
                .map(f -> (Flammable) f.getBuilding())
                .forEach(b -> {
                    if (((Building) b).isOnFire()) {
                        //todo
                    } else if (Math.random() <= ((Building) b).getFirePossibility()) {
                        b.setOnFire();
                    }
                });
    }

    private void evokeCatastrophe() {
        if (Math.random() <= CATASTROPHE_CHANCE) {
            int random = (int) (Math.random() * 3);
            switch (random) {
                case 0 -> catastrophes.get(0).effect(gameData);
                case 1 -> catastrophes.get(1).effect(gameData);
                case 2 -> catastrophes.get(2).effect(gameData);
            }
        }
    }

    private void buildingEffects() {
        gameData.getPeople().forEach(p -> {
            p.removeEffect(Effect.STADIUM);
            p.removeEffect(Effect.FOREST);
            p.removeEffect(Effect.POLICE_STATION);
        });
        gameData.getPlayableFieldsWithBuildings()
                .stream()
                .filter(f -> f.getBuilding() instanceof FunctionalBuilding)
                .map(f -> (FunctionalBuilding) f.getBuilding())
                .forEach(FunctionalBuilding::effect);
    }

    private void removePeopleFromBuildings(){
        gameData.getPlayableFieldsWithBuildings()
                .stream()
                .filter(f -> f.getBuilding() instanceof GeneratedBuilding)
                .map(f -> (GeneratedBuilding) f.getBuilding())
                .forEach(GeneratedBuilding::removePeople);
    }

    private void peopleMoveAway() {
        int people = gameData.getPeople().size();
        gameData.getPeople().forEach(p -> {
            if(p.calculateMoveAwayChance() >= Math.random()){
                p.moveAway();
            }
        });

        gameData.getPeople().removeIf(p -> p.getName().equals("Moved away"));
        Logger.log("People moved away: " + (people - gameData.getPeople().size()));
    }

    private void peopleDie() {
        int people = gameData.getPeople().size();
        gameData.getPeople().forEach(p -> {
            if(p.calculateDeceaseChance() >= Math.random()){
                p.decease();
            }
        });

        gameData.getPeople().removeIf(p -> p.getName().equals("Deceased"));
        Logger.log("People died: " + (people - gameData.getPeople().size()));
    }

    private void peopleAge() {
        gameData.getPeople().forEach(p -> p.setAge(p.getAge() + 1));
    }

    private void growForests() {
        gameData.getPlayableFieldsWithBuildings()
                .stream()
                .filter(field -> field.getBuilding() instanceof Forest)
                .map(f -> (Forest) f.getBuilding())
                .forEach(Forest::grow);
    }

    public void stopSimulation() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            Logger.log("Timer stopped");
        }
    }

    public SimulationSpeed getSimulationSpeed() {
        return simulationSpeed;
    }

    public void setSimulationSpeed(SimulationSpeed simulationSpeed) {
        this.simulationSpeed = simulationSpeed;
    }

    @Override
    public void setTimePaused() {
        simulationSpeed = SimulationSpeed.PAUSED;
        stopSimulation();
        Logger.log("SimulationSpeed is PAUSED");
    }

    @Override
    public void setTimeNormal() {
        simulationSpeed = SimulationSpeed.NORMAL;
        stopSimulation();
        setDelay(1000);
        setPeriod(1000);
        startSimulation();
        Logger.log("SimulationSpeed is NORMAL");
    }

    @Override
    public void setTimeFast() {
        simulationSpeed = SimulationSpeed.FAST;
        stopSimulation();
        setDelay(500);
        setPeriod(500);
        startSimulation();
        Logger.log("SimulationSpeed is FAST");
    }

    @Override
    public void setTimeFaster() {
        simulationSpeed = SimulationSpeed.FASTER;
        stopSimulation();
        setDelay(50);
        setPeriod(50);
        startSimulation();
        Logger.log("SimulationSpeed is FASTER");
    }

    /**
     * This method calculates the distance between two coordinates.
     *
     * @param a The first coordinate.
     * @param b The second coordinate.
     * @return The distance between the two coordinates.
     */
    private static int calculateDistance(Coordinate a, Coordinate b) {
        int dx = a.getX() - b.getX();
        int dy = a.getY() - b.getY();
        return (int) Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * This method calculates the shortest path between two coordinates in the graph.
     *
     * @param start The start coordinate.
     * @param end   The end coordinate.
     * @return The shortest path between the two coordinates or null if there is no path.
     */
    public static List<Coordinate> findShortestPath(Coordinate start, Coordinate end) {
        Map<Coordinate, Integer> distances = new HashMap<>();
        PriorityQueue<Coordinate> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));
        Map<Coordinate, Coordinate> previous = new HashMap<>();
        MutableGraph<Coordinate> graph = getGraph();
        for (Coordinate vertex : graph.nodes()) {
            if (vertex.equals(start)) {
                distances.put(vertex, 0);
                queue.offer(vertex);
            } else {
                distances.put(vertex, Integer.MAX_VALUE);
            }
            previous.put(vertex, null);
        }
        while (!queue.isEmpty()) {
            Coordinate current = queue.poll();
            if (current.equals(end)) {
                break;
            }
            int currentDistance = distances.get(current);
            if (currentDistance == Integer.MAX_VALUE) {
                break;
            }
            Set<Coordinate> neighbors = graph.adjacentNodes(current);
            for (Coordinate neighbor : neighbors) {
                int newDistance = currentDistance + calculateDistance(current, neighbor);
                if (newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                    previous.put(neighbor, current);
                    queue.offer(neighbor);
                }
            }
        }
        if (previous.get(end) == null) {
            return null; // No path exists between start and end
        }
        List<Coordinate> path = new ArrayList<>();
        Coordinate current = end;
        while (current != null) {
            path.add(0, current);
            current = previous.get(current);
        }
        return path;
    }

    /**
     * Adds the amount to the budget
     *
     * @param amount the amount to add
     */
    public static void addToBudget(int amount) {
        gameData.addToBudget(amount);
    }

    /**
     * Subtracts the amount from the budget
     *
     * @param amount the amount to subtract
     */
    public static void subtractFromBudget(int amount) {
        gameData.subtractFromBudget(amount);
    }

    /**
     * Getter for the fields
     *
     * @return the fields
     */
    public static Field[][] getFields() {
        return gameData.getFields();
    }

    /**
     * Getter for the base fire possibility
     *
     * @return the base fire possibility
     */
    public static double getFirePossibility() {
        return FIRE_POSSIBILITY;
    }

    /**
     * Getter for the maximum number of firetrucks a fire station can have
     *
     * @return the maximum number of firetrucks a fire station can have
     */
    public static int getMaxFiretrucks() {
        return MAX_FIRETRUCKS;
    }

    public void evokeFinancialCrisis() {
        Logger.log("Financial crisis evoked.");
        catastrophes.get(0).effect(gameData);
    }

    public void evokeCovid() {
        Logger.log("Covid evoked.");
        catastrophes.get(1).effect(gameData);
    }

    public void evokeFirestorm() {
        Logger.log("Firestorm evoked.");
        catastrophes.get(2).effect(gameData);
    }

    public List<File> getSaveFiles() {
        return saveFiles;
    }

    @Override
    public List<File> readSaveFiles() {
        Logger.log("Reading save files...");
        saveFiles = new ArrayList<>();
        File directory = new File(saveDirectory);
        if (directory.exists()) {
            for (File file : Objects.requireNonNull(directory.listFiles())) {
                if (file.getName().endsWith(".json")) {
                    saveFiles.add(file);
                }
            }
        }
        return saveFiles;
    }

    @Override
    public void deleteSaveFile(File file) {
        Logger.log("Deleting save file: " + file.getName());
        if (file.delete()) {
            if (file.delete()) {
                Logger.log("Save file deleted.");
            } else {
                Logger.log("Save file could not be deleted.");
            }
        }
    }

    @Override
    public void loadSaveFile(File file) {
        Logger.log("Loading save file: " + file.getName());
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(MutableGraph.class, new GraphDeserializer());
        objectMapper.registerModule(module);
        try {
            setGameData(objectMapper.readValue(file, GameData.class));
            Logger.log("Save file loaded.");
        } catch (Exception exc) {
            Logger.log("Save file could not be loaded.");
            exc.printStackTrace();
        }
    }

    @Override
    public void saveGame(GameData gameData) {
        if (gameData.getSaveFile() == null) {
            Logger.log("Game has not been saved yet, creating new save file...");
            File file = new File(saveDirectory + File.separator + gameData.getId() + ".json");
            try {
                if (file.createNewFile()) {
                    Logger.log("Save file created.");
                } else {
                    Logger.log("Save file already exists.");
                }
                gameData.setSaveFile(file);
                Logger.log("Save file created.");
            } catch (Exception exc) {
                Logger.log("Save file could not be created.");
                exc.printStackTrace();
            }
        }
        Logger.log("Saving game...");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(gameData.getSaveFile(), gameData);
            Logger.log("Game saved.");
        } catch (Exception exc) {
            Logger.log("Game could not be saved.");
            exc.printStackTrace();
        }
    }

}


package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.graph.MutableGraph;
import controller.catastrophies.Catastrophe;
import controller.catastrophies.Covid;
import controller.catastrophies.FinancialCrisis;
import controller.catastrophies.Firestorm;
import controller.interfaces.SaveManager;
import controller.interfaces.SpeedManager;
import model.Coordinate;
import model.GameData;
import model.Person;
import model.field.Field;
import util.GraphDeserializer;
import util.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;


/**
 * This class represents the game manager.
 */
public class GameManager implements SaveManager, SpeedManager {
    private static final int STADIUM_BUILD_COST = 5000; // It will cost 5000 * 4 = 20000, because it contains 4 fields
    private static final int POLICE_BUILD_COST = 10000;
    private static final int ROAD_BUILD_COST = 1000;
    private static final int FIRE_STATION_BUILD_COST = 10000;
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
    private static final double FIRE_POSSIBILITY = 0.1;
    private static final int MAX_FIRETRUCKS = 2;
    private static GameData gameData;
    private double catastropheChance;
    private double hospitalChance;
    private SimulationSpeed simulationSpeed;
    private final List<Catastrophe> catastrophes;
    private final String saveDirectory = System.getProperty("user.home") + File.separator + ".citybuilder" + File.separator + "saves";
    private List<File> saveFiles;

    public GameManager() {
        catastropheChance = 0.1;
        hospitalChance = 0.1;
        simulationSpeed = SimulationSpeed.PAUSED;
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
        simulate();
    }

    public static GameData getGameData() {
        return gameData;
    }

    public void setGameData(GameData gameData) {
        GameManager.gameData = gameData;
        Logger.log("Game data set to " + gameData.getId());
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
                gameData.subtractFromBudget(100);
            } else {
                gameData.addToBudget(100);
            }
        }
    }

    private void simulate() {
        Timer timer = new Timer();
        int delay = 1000; // delay for 1 second
        int period = 1000; // repeat every 1 second

        timer.scheduleAtFixedRate(new TimerTask() {
            int count = 0;

            public void run() {
                Logger.log("Timer: " + ++count);
                Logger.log("A day is passed: " + count + ".day");

                // check if a year has passed
                if (count % 365 == 0) {
                    //doFinancials(); // do taxes for the year
                    Logger.log("A year passed");
                }

                if (gameData.isGameOver() || getSimulationSpeed() == SimulationSpeed.PAUSED) {
                    timer.cancel();
                }
            }
        }, delay, period);
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
     * @return The shortest path between the two coordinates.
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
     * Getter for the population size.
     *
     * @return the population size
     */

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

    public double getCatastropheChance() {
        return catastropheChance;
    }

    public void setCatastropheChance(double catastropheChance) {
        this.catastropheChance = catastropheChance;
        Logger.log("Catastrophe chance set to " + catastropheChance);
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

    public void setTaxes(int taxes) {
        gameData.setYearlyTaxes(taxes);
        Logger.log("Taxes set to " + taxes);
    }

    public double getWorkplaceDistribution() {
        return 0.0;
    }

    public double getHospitalChance() {
        return hospitalChance;
    }

    public void setHospitalChance(double hospitalChance) {
        this.hospitalChance = hospitalChance;
        Logger.log("Hospital chance set to " + hospitalChance);
    }

    public SimulationSpeed getSimulationSpeed() {
        return simulationSpeed;
    }

    public void setSimulationSpeed(SimulationSpeed simulationSpeed) {
        this.simulationSpeed = simulationSpeed;
        Logger.log("Simulation speed set to " + simulationSpeed);
    }

    @Override
    public void timeStop() {
        simulationSpeed = SimulationSpeed.PAUSED;
        Logger.log("Time stopped");
    }

    @Override
    public void setTimeNormal() {
        simulationSpeed = SimulationSpeed.NORMAL;
        Logger.log("Time flows normally");
    }

    @Override
    public void setTimeFast() {
        simulationSpeed = SimulationSpeed.FAST;
        Logger.log("Time flows fast");
    }

    @Override
    public void setTimeFaster() {
        simulationSpeed = SimulationSpeed.FASTER;
        Logger.log("Time flows faster");
    }
}

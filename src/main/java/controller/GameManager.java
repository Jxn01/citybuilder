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
import java.util.*;

/**
 * This class represents the game manager.
 */
public class GameManager implements SaveManager, SpeedManager {
    private double catastropheChance;
    private double hospitalChance;
    private static GameData gameData;
    private SimulationSpeed simulationSpeed;
    private final List<Catastrophe> catastrophes;
    private List<File> saveFiles;
    private final String saveDirectory = System.getProperty("user.home") + File.separator + ".citybuilder" + File.separator + "saves";
    private static final int stadiumBuildCost = 100000;
    private static final int policeBuildCost = 10000;
    private static final int roadBuildCost = 1000;
    private static final int fireStationBuildCost = 10000;
    private static final int forestBuildCost = 1000;
    private static final int stadiumMaintenanceCost = 10000;
    private static final int policeMaintenanceCost = 1000;
    private static final int roadMaintenanceCost = 100;
    private static final int fireStationMaintenanceCost = 1000;
    private static final int forestMaintenanceCost = 100;
    private static final int stadiumRange = 10;
    private static final int policeRange = 10;
    private static final int fireStationRange = 10;
    private static final int forestRange = 10;
    private static final int forestGrowthTime = 10;
    private static final int markResidentialCost = 1000;
    private static final int markServiceCost = 1000;
    private static final int markIndustrialCost = 1000;
    private static final int levelOneMaxCapacity = 100;
    private static final int levelTwoMaxCapacity = 200;
    private static final int levelThreeMaxCapacity = 300;
    private static final int levelTwoUpgradeCost = 10000;
    private static final int levelThreeUpgradeCost = 100000;
    private static final double refundPercent = 0.5;
    private static final int starterMapSize = 51;
    private static final int starterPeople = 50;
    private static final int starterBudget = 100000;
    private static final int starterTaxes = 1000;
    private static final double firePossibility = 0.1;
    private static final int maxFiretrucks = 2;

    /**
     * Constructor of the game manager.
     */
    public GameManager() {
        catastropheChance = 0.1;
        hospitalChance = 0.1;
        catastrophes = new ArrayList<>();
        catastrophes.add(FinancialCrisis.getInstance());
        catastrophes.add(Covid.getInstance());
        catastrophes.add(Firestorm.getInstance());

        setSimulationSpeed(SimulationSpeed.PAUSED);
        Logger.log("Game manager created.");

        File directory = new File(saveDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    /**
     * This method initializes the game.
     */
    public void initGame(String cityName) {
        setGameData(new GameData(cityName, starterBudget, starterTaxes, starterPeople, starterMapSize));
        gameData.calculateAverageSatisfaction();
        Logger.log("Game initialized.");
    }

    /**
     * This method simulates.
     */
    public void nextDay() {
        Logger.log("A day passes...");
    }

    /**
     * This method counts the disconnected part-graphs of the main graph.
     * Useful for checking if the demolition of a road would disconnect the graph.
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

    /**
     * This method calculates the distance between two coordinates.
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
     * @param start The start coordinate.
     * @param end The end coordinate.
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
     * This method does the financials.
     */
    public void doFinancials(){
        for(Person p : gameData.getPeople()) {
            if(p.isRetired()) {
                gameData.subtractFromBudget(100);
            } else {
                gameData.addToBudget(100);
            }
        }
    }

    /**
     * Evokes a financial crisis.
     */
    public void evokeFinancialCrisis() {
        Logger.log("Financial crisis evoked.");
        catastrophes.get(0).effect(gameData);
    }

    /**
     * Evokes a covid pandemic.
     */
    public void evokeCovid() {
        Logger.log("Covid evoked.");
        catastrophes.get(1).effect(gameData);
    }

    /**
     * Evokes a firestorm.
     */
    public void evokeFirestorm() {
        Logger.log("Firestorm evoked.");
        catastrophes.get(2).effect(gameData);
    }

    @Override
    public List<File> readSaveFiles() {
        Logger.log("Reading save files...");
        saveFiles = new ArrayList<>();
        File directory = new File(saveDirectory);
        if(directory.exists()) {
            for(File file : directory.listFiles()) {
                if(file.getName().endsWith(".json")) {
                    saveFiles.add(file);
                }
            }
        }
        return saveFiles;
    }

    @Override
    public void deleteSaveFile(File file) {
        Logger.log("Deleting save file: " + file.getName());
        if(file.delete()) {
            Logger.log("Save file deleted.");
        } else {
            Logger.log("Save file could not be deleted.");
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
        } catch(Exception exc) {
            Logger.log("Save file could not be loaded.");
            exc.printStackTrace();
        }
    }

    @Override
    public void saveGame(GameData gameData) {
        if(gameData.getSaveFile() == null) {
            Logger.log("Game has not been saved yet, creating new save file...");
            File file = new File(saveDirectory + File.separator + gameData.getId() + ".json");
            try {
                if(file.createNewFile()) {
                    Logger.log("Save file created.");
                } else {
                    Logger.log("Save file already exists.");
                }
                gameData.setSaveFile(file);
                Logger.log("Save file created.");
            } catch(Exception exc) {
                Logger.log("Save file could not be created.");
                exc.printStackTrace();
            }
        }
        Logger.log("Saving game...");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(gameData.getSaveFile(), gameData);
            Logger.log("Game saved.");
        } catch(Exception exc) {
            Logger.log("Game could not be saved.");
            exc.printStackTrace();
        }
    }

    /**
     * This method sets the taxes.
     */
    public void setTaxes(int taxes) {
        Logger.log("Taxes set to " + taxes);
    }

    /**
     * Getter for workplace distribution. (Service/Industry)
     */
    public double getWorkplaceDistribution() {
        return 0.0;
    }

    /**
     * Getter for the catastrophe chance.
     * @return the catastrophe chance
     */
    public double getCatastropheChance() {
        return catastropheChance;
    }

    /**
     * Setter for the catastrophe chance.
     * @param catastropheChance the new catastrophe chance
     */
    public void setCatastropheChance(double catastropheChance) {
        this.catastropheChance = catastropheChance;
        Logger.log("Catastrophe chance set to " + catastropheChance);
    }

    /**
     * Getter for the hospital chance.
     * @return the hospital chance
     */
    public double getHospitalChance() {
        return hospitalChance;
    }

    /**
     * Setter for the hospital chance.
     * @param hospitalChance the new hospital chance
     */
    public void setHospitalChance(double hospitalChance) {
        this.hospitalChance = hospitalChance;
        Logger.log("Hospital chance set to " + hospitalChance);
    }

    /**
     * Getter for the game data.
     * @return the game data
     */
    public static GameData getGameData() {
        return gameData;
    }

    /**
     * Getter for the graph.
     * @return the graph
     */
    public static MutableGraph<Coordinate> getGraph() {
        return gameData.getGraph();
    }

    /**
     * Setter for the game data.
     * @param gameData the new game data
     */
    public void setGameData(GameData gameData) {
        GameManager.gameData = gameData;
        Logger.log("Game data set to " + gameData.getId());
    }

    /**
     * Getter for the simulation speed.
     * @return the simulation speed
     */
    public SimulationSpeed getSimulationSpeed() {
        return simulationSpeed;
    }

    /**
     * Setter for the simulation speed.
     * @param simulationSpeed the new simulation speed
     */
    public void setSimulationSpeed(SimulationSpeed simulationSpeed) {
        this.simulationSpeed = simulationSpeed;
        Logger.log("Simulation speed set to " + simulationSpeed);
    }

    /**
     * Getter for the population size.
     * @return the population size
     */
    public static int getPopulation() {
        return gameData.getPopulation();
    }

    /**
     * Getter for the budget.
     * @return the budget
     */
    public static int getBudget() {
        return gameData.getBudget();
    }

    /**
     * Getter for the stadium build cost.
     * @return the stadium build cost
     */
    public static int getStadiumBuildCost(){
        return stadiumBuildCost;
    }

    /**
     * Getter for the police build cost.
     * @return the police build cost
     */
    public static int getPoliceBuildCost(){
        return policeBuildCost;
    }

    /**
     * Getter for the fire station build cost.
     * @return the fire station build cost
     */
    public static int getFireStationBuildCost(){
        return fireStationBuildCost;
    }

    /**
     * Getter for the forest build cost.
     * @return the forest build cost
     */
    public static int getForestBuildCost(){
        return forestBuildCost;
    }

    /**
     * Getter for the road build cost.
     * @return the road build cost
     */
    public static int getRoadBuildCost(){
        return roadBuildCost;
    }

    /**
     * Getter for the stadium maintenance cost.
     * @return the stadium maintenance cost
     */
    public static int getStadiumMaintenanceCost(){
        return stadiumMaintenanceCost;
    }

    /**
     * Getter for the police maintenance cost.
     * @return the police maintenance cost
     */
    public static int getPoliceMaintenanceCost(){
        return policeMaintenanceCost;
    }

    /**
     * Getter for the fire station maintenance cost.
     * @return the fire station maintenance cost
     */
    public static int getFireStationMaintenanceCost(){
        return fireStationMaintenanceCost;
    }

    /**
     * Getter for the forest maintenance cost.
     * @return the forest maintenance cost
     */
    public static int getForestMaintenanceCost(){
        return forestMaintenanceCost;
    }

    /**
     * Getter for the road maintenance cost.
     * @return the road maintenance cost
     */
    public static int getRoadMaintenanceCost(){
        return roadMaintenanceCost;
    }

    /**
     * Getter for the stadium range.
     * @return the stadium range
     */
    public static int getStadiumRange(){
        return stadiumRange;
    }

    /**
     * Getter for the police range.
     * @return the police range
     */
    public static int getPoliceRange(){
        return policeRange;
    }

    /**
     * Getter for the fire station range.
     * @return the fire station range
     */
    public static int getFireStationRange(){
        return fireStationRange;
    }

    /**
     * Getter for the forest range.
     * @return the forest range
     */
    public static int getForestRange(){
        return forestRange;
    }

    /**
     * Getter for the forest growth time.
     * @return the forest growth time
     */
    public static int getForestGrowthTime(){
        return forestGrowthTime;
    }

    /**
     * Getter for the residential zone's marking cost.
     * @return the residential zone's marking cost
     */
    public static int getMarkResidentialCost(){
        return markResidentialCost;
    }

    /**
     * Getter for the service zone's marking cost.
     * @return the service zone's marking cost
     */
    public static int getMarkServiceCost(){
        return markServiceCost;
    }

    /**
     * Getter for the industrial zone's marking cost.
     * @return the industrial zone's marking cost
     */
    public static int getMarkIndustrialCost(){
        return markIndustrialCost;
    }

    /**
     * Getter for the refund percent
     * @return the refund percent
     */
    public static double getRefundPercent(){
        return refundPercent;
    }

    /**
     * Getter for the max capacity at level one
     * @return the max capacity at level one
     */
    public static int getLevelOneMaxCapacity(){
        return levelOneMaxCapacity;
    }

    /**
     * Getter for the max capacity at level two
     * @return the max capacity at level two
     */
    public static int getLevelTwoMaxCapacity(){
        return levelTwoMaxCapacity;
    }

    /**
     * Getter for the max capacity at level three
     * @return the max capacity at level three
     */
    public static int getLevelThreeMaxCapacity(){
        return levelThreeMaxCapacity;
    }

    /**
     * Getter for the level two upgrade cost
     * @return the level two upgrade cost
     */
    public static int getLevelTwoUpgradeCost(){
        return levelTwoUpgradeCost;
    }

    /**
     * Getter for the level three upgrade cost
     * @return the level three upgrade cost
     */
    public static int getLevelThreeUpgradeCost(){
        return levelThreeUpgradeCost;
    }

    /**
     * Getter for the starter budget
     * @return the starter budget
     */
    public static int getStarterBudget(){
        return starterBudget;
    }

    /**
     * Getter for the starter people
     * @return the starter people
     */
    public static int getStarterPeople(){
        return starterPeople;
    }

    /**
     * Getter for the starter map size
     * @return the starter map size
     */
    public static int getStarterMapSize(){
        return starterMapSize;
    }

    /**
     * Getter for the starter taxes
     * @return the starter taxes
     */
    public static int getStarterTaxes(){
        return starterTaxes;
    }

    /**
     * Adds the amount to the budget
     * @param amount the amount to add
     */
    public static void addToBudget(int amount) {
        gameData.addToBudget(amount);
    }

    /**
     * Subtracts the amount from the budget
     * @param amount the amount to subtract
     */
    public static void subtractFromBudget(int amount) {
        gameData.subtractFromBudget(amount);
    }

    /**
     * Getter for the fields
     * @return the fields
     */
    public static Field[][] getFields() {
        return gameData.getFields();
    }

    /**
     * Getter for the base fire possibility
     * @return the base fire possibility
     */
    public static double getFirePossibility(){
        return firePossibility;
    }

    /**
     * Getter for the maximum number of firetrucks a fire station can have
     * @return the maximum number of firetrucks a fire station can have
     */
    public static int getMaxFiretrucks(){
        return maxFiretrucks;
    }

    /**
     * Getter for the save files
     * @return the save files
     */
    public List<File> getSaveFiles() {
        return saveFiles;
    }

    @Override
    public void timeStop() {
        Logger.log("Time stopped");
        simulationSpeed = SimulationSpeed.PAUSED;
    }

    @Override
    public void timeNormal() {
        Logger.log("Time flows normally");
        simulationSpeed = SimulationSpeed.NORMAL;
    }

    @Override
    public void timeFast() {
        Logger.log("Time flows fast");
        simulationSpeed = SimulationSpeed.FAST;
    }

    @Override
    public void timeFaster() {
        Logger.log("Time flows faster");
        simulationSpeed = SimulationSpeed.FASTER;
    }
}

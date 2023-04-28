package controller;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.graph.Graph;
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
import org.javatuples.Pair;
import util.GraphDeserializer;
import util.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
        setGameData(new GameData(cityName));
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
     * This method calculates the shortest path between two coordinates.
     * @param start The start coordinate.
     * @param end The end coordinate.
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
     * Getter for the expenses.
     */
    public Pair<String, Integer> getExpenses() {
        return new Pair<>("", 0);
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

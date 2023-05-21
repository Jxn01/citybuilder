package util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import model.Coordinate;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class GraphDeserializer extends JsonDeserializer<MutableGraph<Coordinate>> {

    @Override
    public @NotNull MutableGraph<Coordinate> deserialize(@NotNull JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        MutableGraph<Coordinate> graph = GraphBuilder.undirected().allowsSelfLoops(false).build();
        JsonNode nodesNode = node.get("nodes");
        if (nodesNode != null) {
            for (JsonNode nodeNode : nodesNode) { //parse node into coordinate
                Coordinate coord = new Coordinate(nodeNode.get("x").asInt(), nodeNode.get("y").asInt());
                graph.addNode(coord);
            }
        }
        JsonNode edgesNode = node.get("edges");
        if (edgesNode != null) {
            for (JsonNode edgeNode : edgesNode) {
                Coordinate source = new Coordinate(edgeNode.get("source").get("x").asInt(), edgeNode.get("source").get("y").asInt()); //parse source into coordinate
                Coordinate target = new Coordinate(edgeNode.get("target").get("x").asInt(), edgeNode.get("target").get("y").asInt()); //parse target into coordinate
                graph.putEdge(source, target);
            }
        }
        Logger.log(graph.toString());
        return graph;
    }
}

package util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.MutableGraph;
import model.Coordinate;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class GraphSerializer extends JsonSerializer<MutableGraph<Coordinate>> {

    @Override
    public void serialize(@NotNull MutableGraph<Coordinate> graph, @NotNull JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeArrayFieldStart("nodes");
        for (Coordinate node : graph.nodes()) {
            gen.writeObject(node);
        }
        gen.writeEndArray();
        gen.writeArrayFieldStart("edges");
        for (EndpointPair<Coordinate> endpointPair : graph.edges()) {
            gen.writeStartObject();
            gen.writeObjectField("source", endpointPair.nodeU());
            gen.writeObjectField("target", endpointPair.nodeV());
            gen.writeEndObject();
        }
        gen.writeEndArray();
        gen.writeEndObject();
    }
}

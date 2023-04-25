package model.field;

import model.Coordinate;
import static util.ResourceLoader.*;
import java.io.IOException;

public class BorderField extends Field {
    public BorderField(Coordinate coord) {
        super(coord);

        try { this.texture = loadImage("PATHROCKS.png"); }
        catch(IOException exc) { exc.printStackTrace(); }
    }
}

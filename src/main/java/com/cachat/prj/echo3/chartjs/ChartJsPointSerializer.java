package com.cachat.prj.echo3.chartjs;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

/**
 *
 * @author scachat
 */
public class ChartJsPointSerializer implements JsonSerializer<ChartJsPoint> {

    @Override
    public JsonElement serialize(ChartJsPoint src, Type typeOfSrc, JsonSerializationContext context) {

        // Si label est non null -> array [label, y]
        if (src.getLabel() != null) {
            JsonArray array = new JsonArray();
            array.add(src.getLabel());
            array.add(src.getY());
            return array;
        } // Sinon si x est non null -> array [x, y]
        else if (src.getX() != null) {
            JsonArray array = new JsonArray();
            array.add(src.getX());
            array.add(src.getY());
            if (src.getR() != null) {
                array.add(src.getR());
            }
            return array;
        } // Sinon (si x et label sont null) -> renvoie le nombre (y)
        else {
            return new JsonPrimitive(src.getY());
        }
    }
}

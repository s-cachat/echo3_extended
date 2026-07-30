package com.cachat.prj.echo3.chartjs;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import nextapp.echo.app.Color;
import java.lang.reflect.Type;
import java.util.List;
/**
 *
 * @author scachat
 */
public class ChartJsColorArraySerializer implements JsonSerializer<List<Color>> {

    @Override
    public JsonElement serialize(List<Color> src, Type typeOfSrc, JsonSerializationContext context) {
        // Si la liste ne contient qu'un seul élément, on renvoie directement l'élément sérialisé
        if (src != null && src.size() == 1) {
            // context.serialize va appeler automatiquement votre ColorSerializer
            return context.serialize(src.get(0));
        }
        
        // S'il y a 0 ou plusieurs éléments, on crée un tableau (JsonArray)
        JsonArray array = new JsonArray();
        if (src != null) {
            for (Color color : src) {
                array.add(context.serialize(color));
            }
        }
        return array;
    }
}

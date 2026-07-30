package com.cachat.prj.echo3.chartjs;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import nextapp.echo.app.Color;
import java.lang.reflect.Type;
import java.util.Locale;

/**
 *
 * @author scachat
 */
public class ChartJsColorSerializer implements JsonSerializer<Color> {

    @Override
    public JsonElement serialize(Color src, Type typeOfSrc, JsonSerializationContext context) {
        String rgbString;
        if (src instanceof ChartAlphaColor srca && srca.getAlpha() < 1) {
            rgbString = String.format(Locale.ENGLISH, "rgba(%d,%d,%d,%1.1f)", src.getRed(), src.getGreen(), src.getBlue(), srca.getAlpha());
        } else {
            rgbString = String.format("rgb(%d,%d,%d)", src.getRed(), src.getGreen(), src.getBlue());

        }
        return new JsonPrimitive(rgbString);
    }
}

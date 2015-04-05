package w094j.ctrl8.parse.statement;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Gson Adaptor to distinguish the statement classes, adapted from
 * http://stackoverflow.com/questions/5800433/polymorphism-with-gson
 */
public class StatementGsonAdaptor implements JsonSerializer<Statement>,
JsonDeserializer<Statement> {

    private static final String CLASS_NAME = "CLASS_NAME";
    private static final String INSTANCE = "INSTANCE";

    @Override
    public Statement deserialize(JsonElement json, Type typeOfT,
            JsonDeserializationContext context) throws JsonParseException {

        JsonObject jsonObject = json.getAsJsonObject();
        JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASS_NAME);
        String className = prim.getAsString();

        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new JsonParseException(e.getMessage());
        }
        return context.deserialize(jsonObject.get(INSTANCE), clazz);
    }

    @Override
    public JsonElement serialize(Statement src, Type typeOfSrc,
            JsonSerializationContext context) {

        JsonObject retValue = new JsonObject();
        String className = src.getClass().getCanonicalName();
        retValue.addProperty(CLASS_NAME, className);
        JsonElement elem = context.serialize(src);
        retValue.add(INSTANCE, elem);
        return retValue;
    }

}
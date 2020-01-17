package src.mua;

import java.util.HashMap;

public class NameSpace {
    private HashMap<String, Value> valueMap;

    public NameSpace() {
        valueMap = new HashMap<String, Value>();
        bindValue("pi", new Value(1, "3.14159"));
//        bindValue("run", new Value(4, ""));
    }

    public void bindValue(String key, Value value) {
        valueMap.put(key, value);
    }

    public void unbindValue(String key) {
        if (valueMap.containsKey(key))
            valueMap.remove(key, valueMap.get(key));
    }

    public Value getValueOf(String key) {
        if (valueMap.get(key) != null)
            return valueMap.get(key);
        else return Main.nameSpace.valueMap.get(key);
    }

    public boolean isName(String key) {
        return valueMap.containsKey(key) || Main.nameSpace.valueMap.containsKey(key);
    }

    public void erall() {
        valueMap.clear();
    }

    public void poall() {
        for (String name : valueMap.keySet()) {
            System.out.println(name);
        }
    }

    public HashMap<String, Value> getValueMap() {
        return valueMap;
    }
}

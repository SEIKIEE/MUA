package src.mua;

public class Value {
    public static final Value MONVALUE = new Value(0, "null");
    public int type;    //1:number 2:word 3:boolean 4:list
    public String value;

    public Value(int type, String value) {
        this.type = type;
        this.value = value;
    }

    public Value(Value v) {
        type = v.type;
        value = v.value;
    }
}

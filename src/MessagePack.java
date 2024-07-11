import java.io.Serializable;

public class MessagePack implements Serializable {
    private String name;
    private String msg;

    public MessagePack(String name, String msg) {
        this.name = name;
        this.msg = msg;
    }

    public String getName() {
        return this.name;
    }

    public String getMsg() {
        return this.msg;
    }
}

package cn.weekdragon.superdic.bean;

public class PosAndAcc {
    private String pos;
    private String acceptation;

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getAcceptation() {
        return acceptation;
    }

    public void setAcceptation(String acceptation) {
        this.acceptation = acceptation;
    }

    @Override
    public String toString() {
        return pos + '\'' +
                ":" + acceptation + '\'';
    }
}

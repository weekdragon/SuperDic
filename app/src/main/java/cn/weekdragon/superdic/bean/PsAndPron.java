package cn.weekdragon.superdic.bean;

public class PsAndPron {
    private String ps;
    private String pron;

    public String getPs() {
        return ps;
    }

    public void setPs(String ps) {
        this.ps = ps;
    }

    public String getPron() {
        return pron;
    }

    public void setPron(String pron) {
        this.pron = pron;
    }

    @Override
    public String toString() {
        return "PsAndPron{" +
                "ps='" + ps + '\'' +
                ", pron='" + pron + '\'' +
                '}';
    }
}

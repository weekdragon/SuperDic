package cn.weekdragon.superdic.bean;

import java.io.Serializable;
import java.util.List;

public class Word implements Serializable {
    /**
     * 关键词
     */
    private String key;
    /**
     * 音标和发音
     */
    private List<PsAndPron> psandpron;
    /**
     * 词性和解释
     */
    private List<PosAndAcc> posandacc;
    /**
     * 例句
     */
    private List<Sent> sent;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<PsAndPron> getPsandpron() {
        return psandpron;
    }

    public void setPsandpron(List<PsAndPron> psandpron) {
        this.psandpron = psandpron;
    }

    public List<PosAndAcc> getPosandacc() {

        return posandacc;
    }

    public void setPosandacc(List<PosAndAcc> posandacc) {
        this.posandacc = posandacc;
    }

    public List<Sent> getSent() {
        return sent;
    }

    public void setSent(List<Sent> sent) {
        this.sent = sent;
    }

    public String getSimpleExp() {
        String exp = "";
        for (PosAndAcc item : posandacc) {
            exp += item.toString();
        }
        return exp;
    }

}

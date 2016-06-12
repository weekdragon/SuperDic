package cn.weekdragon.superdic.bean;

/**
 * Created by WeekDragon on 2016/3/29.
 */
public class WordInfo {
    private int id;
    private String word;
    private String exp;

    public WordInfo(String word) {
        this.word = word;
    }

    public WordInfo(int id, String word, String exp) {
        this.id = id;
        this.word = word;
        this.exp = exp;
    }

    public WordInfo(String word, String exp) {
        this.word = word;
        this.exp = exp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }
}

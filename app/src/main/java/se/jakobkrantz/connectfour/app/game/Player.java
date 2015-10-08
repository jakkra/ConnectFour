package se.jakobkrantz.connectfour.app.game;/*
 * Created by jakkra on 2015-09-28.
 */

public class Player implements Comparable<Player>{
    private String name;
    private int score;
    private byte color;

    public Player(String name, int score){
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public boolean equals(Object o) {
        Player p = (Player) o;
        return name.equals(p.getName());
    }

    public void increaseScore() {
        score += 1;
    }

    public int getScore(){
        return score;
    }

    @Override
    public int compareTo(Player another) {
        return another.getScore() - score;
    }
}

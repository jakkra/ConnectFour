package se.jakobkrantz.connectfour.app.game;
/*
 * Created by jakkra on 2015-09-28.
 */

import java.io.Serializable;

/**
 * Representation of a Player
 */
public class Player implements Comparable<Player>, Serializable {
    private String name;
    private int score;

    /**
     * @param name of the player
     * @param score score of the player
     */
    public Player(String name, int score) {
        this.name = name;
        this.score = score;
    }

    /**
     * @return the name of the player
     */
    public String getName() {
        return name;
    }

    public boolean equals(Object o) {
        Player p = (Player) o;
        return name.equals(p.getName());
    }

    /**
     * Increases the score of this player by one.
     */
    public void increaseScore() {
        score += 1;
    }

    /**
     * @return the score of this player object.
     */
    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(Player another) {
        return another.getScore() - score;
    }
}

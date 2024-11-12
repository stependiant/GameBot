package model;

import model.weapon.Katana;

public class Mongol {

    private int health;
    private int level;
    private Katana katana;

    public Mongol() {
        this.health = 100;
        this.level = 1;
        this.katana = new Katana();
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Katana getKatana() {
        return katana;
    }

    public void setKatana(Katana katana) {
        this.katana = katana;
    }

    public int attack() {
        return katana.getDamage();
    }
}

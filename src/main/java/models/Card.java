package models;

import java.lang.reflect.Type;

public class Card {
    private String id;
    private String name;
    private Element element;
    private int damage;

    public Card(String id, String name, Element element, int damage) {
        this.id = id;
        this.name = name;
        this.element = element;
        this.damage = damage;
    }

    public String getName() {
        return name;
    }

    public Element getElement() {
        return element;
    }

    public int getDamage() {
        return damage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}

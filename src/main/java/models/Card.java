package models;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.lang.reflect.Type;

public class Card {
    @JsonAlias({"Id"})
    private String id;
    @JsonAlias({"Name"})
    private String name;
    private int type;  //TYPE 1: Spell TYPE 2: MONSTER
    private Element element;
    @JsonAlias({"Damage"})
    private int damage;

    public Card(String id, String name, int type, Element element, int damage) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.element = element;
        this.damage = damage;
    }

    public Card(String id, String name, Element element, int damage) {
        this.id = id;
        this.name = name;
        this.element = element;
        this.damage = damage;
    }

    public Card(String id, String name, int damage) {
        this.id = id;
        this.name = name;
        this.damage = damage;
    }

    public Card(String id){
        this.id = id;
    }

    public Card() {
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

    public String getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

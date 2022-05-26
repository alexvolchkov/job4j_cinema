package ru.job4j.cinema.model;

import java.util.Objects;

public class Session {

    private int id;
    private String name;
    private int numberOfRows;
    private int numberOfCells;

    public Session() {
    }

    public Session(int id) {
        this.id = id;
    }

    public Session(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Session(int id, String name, int numberOfRows, int numberOfCells) {
        this.id = id;
        this.name = name;
        this.numberOfRows = numberOfRows;
        this.numberOfCells = numberOfCells;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public int getNumberOfCells() {
        return numberOfCells;
    }

    public void setNumberOfCells(int numberOfCells) {
        this.numberOfCells = numberOfCells;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Session session = (Session) o;
        return id == session.id && Objects.equals(name, session.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Session{"
                + "id=" + id
                + ", name='" + name + '\''
                + '}';
    }
}

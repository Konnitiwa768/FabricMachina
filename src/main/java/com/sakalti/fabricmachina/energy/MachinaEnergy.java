package com.sakalti.fabricmachina.energy;

public class MachinaEnergy {
    private int rf;
    private final int capacity;
    private final int maxInsert;
    private int tq;

    public MachinaEnergy(int capacity, int maxInsert, int initialTq) {
        this.capacity = capacity;
        this.maxInsert = maxInsert;
        this.rf = 0;
        this.tq = initialTq;
    }

    public int addRf(int amount) {
        int inserted = Math.min(amount, maxInsert);
        int space = capacity - rf;
        int actual = Math.min(inserted, space);
        rf += actual;
        return actual;
    }

    public boolean consumeRf(int amount) {
        if (rf >= amount) {
            rf -= amount;
            return true;
        }
        return false;
    }

    public int getRf() {
        return rf;
    }

    public int getTq() {
        return tq;
    }

    public void setTq(int tq) {
        this.tq = tq;
    }
}

package org.example.lomakyla;

public class Lasku {
    private int laskuId;
    private int asiakasId;
    private int varausId;
    private double summa;
    private double alv;
    private boolean maksettu;

    public Lasku(int laskuId, int asiakasId, int varausId, double summa, double alv, boolean maksettu) {
        this.laskuId = laskuId;
        this.asiakasId = asiakasId;
        this.varausId = varausId;
        this.summa = summa;
        this.alv = alv;
        this.maksettu = maksettu;
    }

    public int getLaskuId() {
        return laskuId;
    }

    public void setLaskuId(int laskuId) {
        this.laskuId = laskuId;
    }

    public int getAsiakasId() {
        return asiakasId;
    }

    public void setAsiakasId(int asiakasId) {
        this.asiakasId = asiakasId;
    }

    public int getVarausId() {
        return varausId;
    }

    public void setVarausId(int varausId) {
        this.varausId = varausId;
    }

    public double getSumma() {
        return summa;
    }

    public void setSumma(double summa) {
        this.summa = summa;
    }

    public double getAlv() {
        return alv;
    }

    public void setAlv(double alv) {
        this.alv = alv;
    }

    public boolean isMaksettu() {
        return maksettu;
    }

    public void setMaksettu(boolean maksettu) {
        this.maksettu = maksettu;
    }

    @Override
    public String toString() {
        return "Lasku " +
                "ID=" + laskuId + " asiakasID: " + asiakasId + ' ' +
                "varausID: " + varausId + ' ' +
                "summa: " + summa + "€ " +
                "alv: " + alv + "% " +
                "maksettu: " + (maksettu ? "kyllä" : "ei") + ' ';
    }
}




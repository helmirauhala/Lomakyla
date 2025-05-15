package org.example.lomakyla;

public class Mokki {
    private int mokkiId;
    private String mokinNumero;
    private int kapasiteetti;
    private String varustelu;
    private String osoite;
    private String postinumero;
    private String kunta;

    // Konstruktori
    public Mokki(int mokkiId, String mokinNumero, int kapasiteetti, String varustelu, String osoite, String postinumero, String kunta) {
        this.mokkiId = mokkiId;
        this.mokinNumero = mokinNumero;
        this.kapasiteetti = kapasiteetti;
        this.varustelu = varustelu;
        this.osoite = osoite;
        this.postinumero = postinumero;
        this.kunta = kunta;
    }

    // Getterit ja setterit
    public int getMokkiId() {
        return mokkiId;
    }

    public void setMokkiId(int mokkiId) {
        this.mokkiId = mokkiId;
    }

    public String getMokinNumero() {
        return mokinNumero;
    }

    public void setMokinNumero(String mokinNumero) {
        this.mokinNumero = mokinNumero;
    }

    public int getKapasiteetti() {
        return kapasiteetti;
    }

    public void setKapasiteetti(int kapasiteetti) {
        this.kapasiteetti = kapasiteetti;
    }

    public String getVarustelu() {
        return varustelu;
    }

    public void setVarustelu(String varustelu) {
        this.varustelu = varustelu;
    }

    public String getOsoite() {
        return osoite;
    }

    public void setOsoite(String osoite) {
        this.osoite = osoite;
    }

    public String getPostinumero() {
        return postinumero;
    }

    public void setPostinumero(String postinumero) {
        this.postinumero = postinumero;
    }

    public String getKunta() {
        return kunta;
    }

    public void setKunta(String kunta) {
        this.kunta = kunta;
    }

    // toString-metodi helpompaan tulostukseen
    @Override
    public String toString() {
        return "Mökki " +
                "ID=" + mokkiId + " numero: " + mokinNumero + ' ' +  " kapasiteetti: " + kapasiteetti +
                " varustelu: " + varustelu + ' ' +  " osoite: " + osoite + ' ' +
                " postinumero: " + postinumero + ' ' +  " kunta: " + kunta + ' ';
    }
}



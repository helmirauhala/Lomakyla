package org.example.lomakyla;

public class Asiakas {
    private int asiakasId;
    private String nimi;
    private String puhelinnumero;
    private String sahkoposti;
    private String osoite;
    private String postinumero;
    private String kunta;

    // Konstruktori
    public Asiakas(int asiakasId, String nimi, String puhelinnumero, String sahkoposti,
                   String osoite, String postinumero, String kunta) {
        this.asiakasId = asiakasId;
        this.nimi = nimi;
        this.puhelinnumero = puhelinnumero;
        this.sahkoposti = sahkoposti;
        this.osoite = osoite;
        this.postinumero = postinumero;
        this.kunta = kunta;
    }

    // Getterit ja setterit
    public int getAsiakasId() {
        return asiakasId;
    }

    public void setAsiakasId(int asiakasId) {
        this.asiakasId = asiakasId;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getPuhelinnumero() {
        return puhelinnumero;
    }

    public void setPuhelinnumero(String puhelinnumero) {
        this.puhelinnumero = puhelinnumero;
    }

    public String getSahkoposti() {
        return sahkoposti;
    }

    public void setSahkoposti(String sahkoposti) {
        this.sahkoposti = sahkoposti;
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

    @Override
    public String toString() {
        return "Asiakas " +
                "ID=" + asiakasId + " nimi: " + nimi + ' ' +
                "puhelin: " + puhelinnumero + ' ' +
                "sähköposti: " + sahkoposti + ' ' +
                "osoite: " + osoite + ' ' +
                "postinumero: " + postinumero + ' ' +
                "kunta: " + kunta + ' ';
    }

}


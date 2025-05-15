package org.example.lomakyla;
import java.time.LocalDate;

public class Varaus {
    private int varausId;
    private int asiakasId;
    private int mokkiId;
    private LocalDate varattuPvm;
    private LocalDate vahvistusPvm;
    private LocalDate varauksenAlkuPvm;
    private LocalDate varauksenLoppuPvm;

    public Varaus(int varausId, int asiakasId, int mokkiId, LocalDate varattuPvm, LocalDate vahvistusPvm,
                  LocalDate varauksenAlkuPvm, LocalDate varauksenLoppuPvm) {
        this.varausId = varausId;
        this.asiakasId = asiakasId;
        this.mokkiId = mokkiId;
        this.varattuPvm = varattuPvm;
        this.vahvistusPvm = vahvistusPvm;
        this.varauksenAlkuPvm = varauksenAlkuPvm;
        this.varauksenLoppuPvm = varauksenLoppuPvm;
    }

    public int getVarausId() {
        return varausId;
    }

    public void setVarausId(int varausId) {
        this.varausId = varausId;
    }

    public int getAsiakasId() {
        return asiakasId;
    }

    public void setAsiakasId(int asiakasId) {
        this.asiakasId = asiakasId;
    }

    public int getMokkiId() {
        return mokkiId;
    }

    public void setMokkiId(int mokkiId) {
        this.mokkiId = mokkiId;
    }

    public LocalDate getVarattuPvm() {
        return varattuPvm;
    }

    public void setVarattuPvm(LocalDate varattuPvm) {
        this.varattuPvm = varattuPvm;
    }

    public LocalDate getVahvistusPvm() {
        return vahvistusPvm;
    }

    public void setVahvistusPvm(LocalDate vahvistusPvm) {
        this.vahvistusPvm = vahvistusPvm;
    }

    public LocalDate getVarauksenAlkuPvm() {
        return varauksenAlkuPvm;
    }

    public void setVarauksenAlkuPvm(LocalDate varauksenAlkuPvm) {
        this.varauksenAlkuPvm = varauksenAlkuPvm;
    }

    public LocalDate getVarauksenLoppuPvm() {
        return varauksenLoppuPvm;
    }

    public void setVarauksenLoppuPvm(LocalDate varauksenLoppuPvm) {
        this.varauksenLoppuPvm = varauksenLoppuPvm;
    }

    @Override
    public String toString() {
        return "Varaus " +
                "ID=" + varausId + " asiakasID: " + asiakasId + ' ' +
                "mökkiID: " + mokkiId + ' ' +
                "varattu: " + varattuPvm + ' ' +
                "vahvistettu: " + vahvistusPvm + ' ' +
                "alku: " + varauksenAlkuPvm + ' ' +
                "loppu: " + varauksenLoppuPvm + ' ';
    }
}


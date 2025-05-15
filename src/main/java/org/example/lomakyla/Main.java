package org.example.lomakyla;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Mokki mokki1 = new Mokki(1, "123", 6, "Sauna, Takka, WiFi, Palju, Vene", "Metsäpolku 10", "80140", "Joensuu");
        System.out.println(mokki1);

        // Luodaan asiakas
        Asiakas asiakas = new Asiakas(100, "Maija Meikäläinen", "0401234567", "maija@example.com",
                "Keskuskatu 1", "54321", "Helsinki");
        System.out.println(asiakas);

        // Luodaan varaus
        Varaus varaus = new Varaus(
                200,
                asiakas.getAsiakasId(),
                mokki1.getMokkiId(),
                LocalDate.of(2025, 4, 20),
                LocalDate.of(2025, 4, 21),
                LocalDate.of(2025, 5, 1),
                LocalDate.of(2025, 5, 7)
        );
        System.out.println(varaus);

        // Luodaan lasku
        Lasku lasku = new Lasku(
                300,
                asiakas.getAsiakasId(),
                varaus.getVarausId(),
                750.00,
                24.0,
                false
        );
        System.out.println(lasku);
    }
}

package org.example.lomakyla;
import java.sql.*;
import java.time.LocalDate;

public class MokkivarausSQL {

    // ==== ASIAKAS ====

    public static void lisaaAsiakas(Connection conn, int id, String nimi, String sahkoposti,
                                    String puhelin, String osoite, String postinumero, String kunta) throws SQLException {
        String sql = """
            INSERT INTO asiakas (
              asiakas_id, nimi, sahkoposti, puhelinnumero,
              osoite, postinumero, kunta
            ) VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, nimi);
            stmt.setString(3, sahkoposti);
            stmt.setString(4, puhelin);
            stmt.setString(5, osoite);
            stmt.setString(6, postinumero);
            stmt.setString(7, kunta);
            stmt.executeUpdate();
        }
    }

    public static void muokkaaAsiakasNimi(Connection conn, int asiakasId, String uusiNimi) throws SQLException {
        String sql = "UPDATE asiakas SET nimi = ? WHERE asiakas_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, uusiNimi);
            stmt.setInt(2, asiakasId);
            stmt.executeUpdate();
        }
    }

    public static void poistaAsiakas(Connection conn, int asiakasId) throws SQLException {
        String sql = "DELETE FROM asiakas WHERE asiakas_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, asiakasId);
            stmt.executeUpdate();
        }
    }

    // ==== MOKKI ====

    public static void lisaaMokki(Connection conn, int id, String numero, int kapasiteetti,
                                  String varustelu, String osoite, String postinumero, String kunta) throws SQLException {
        String sql = """
            INSERT INTO mokki (
              mokki_id, mokkinumero, kapasiteetti,
              varustelu, osoite, postinumero, kunta
            ) VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, numero);
            stmt.setInt(3, kapasiteetti);
            stmt.setString(4, varustelu);
            stmt.setString(5, osoite);
            stmt.setString(6, postinumero);
            stmt.setString(7, kunta);
            stmt.executeUpdate();
        }
    }

    public static void muokkaaMokkiVarustelu(Connection conn, int mokkiId, String uusiVarustelu) throws SQLException {
        String sql = "UPDATE mokki SET varustelu = ? WHERE mokki_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, uusiVarustelu);
            stmt.setInt(2, mokkiId);
            stmt.executeUpdate();
        }
    }

    public static void poistaMokki(Connection conn, int mokkiId) throws SQLException {
        String sql = "DELETE FROM mokki WHERE mokki_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, mokkiId);
            stmt.executeUpdate();
        }
    }

    // ==== LASKU ====

    public static void lisaaLasku(Connection conn, int id, boolean maksettu, double summa,
                                  double alv, int varausId, int asiakasId) throws SQLException {
        String sql = """
            INSERT INTO laskujen_hallinta (
              lasku_id, tila, summa, alv,
              varaus_id, asiakas_id
            ) VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setBoolean(2, maksettu);
            stmt.setDouble(3, summa);
            stmt.setDouble(4, alv);
            stmt.setInt(5, varausId);
            stmt.setInt(6, asiakasId);
            stmt.executeUpdate();
        }
    }

    public static void merkitseLaskuMaksetuksi(Connection conn, int laskuId) throws SQLException {
        String sql = "UPDATE laskujen_hallinta SET tila = TRUE WHERE lasku_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, laskuId);
            stmt.executeUpdate();
        }
    }

    public static void poistaLasku(Connection conn, int laskuId) throws SQLException {
        String sql = "DELETE FROM laskujen_hallinta WHERE lasku_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, laskuId);
            stmt.executeUpdate();
        }
    }

    // ==== VARAUS ====

    public static void lisaaVaraus(Connection conn, int id, int asiakasId, int mokkiId,
                                   LocalDate alku, LocalDate loppu, LocalDate varattu, LocalDate vahvistus) throws SQLException {
        String sql = """
            INSERT INTO varaus (
              varaus_id, asiakas_id, mokki_id,
              aloitus, paattyminen,
              varattu_pvm, vahvistus_pvm
            ) VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setInt(2, asiakasId);
            stmt.setInt(3, mokkiId);
            stmt.setDate(4, Date.valueOf(alku));
            stmt.setDate(5, Date.valueOf(loppu));
            stmt.setDate(6, Date.valueOf(varattu));
            if (vahvistus != null) {
                stmt.setDate(7, Date.valueOf(vahvistus));
            } else {
                stmt.setNull(7, Types.DATE);
            }
            stmt.executeUpdate();
        }
    }

    public static void muokkaaVarausVahvistus(Connection conn, int varausId, LocalDate uusiPvm) throws SQLException {
        String sql = "UPDATE varaus SET vahvistus_pvm = ? WHERE varaus_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(uusiPvm));
            stmt.setInt(2, varausId);
            stmt.executeUpdate();
        }
    }

    public static void poistaVaraus(Connection conn, int varausId) throws SQLException {
        String sql = "DELETE FROM varaus WHERE varaus_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, varausId);
            stmt.executeUpdate();
        }
    }

    // ==== APUMETODIT ====

    public static boolean existsAsiakas(Connection conn, int asiakasId) throws SQLException {
        String sql = "SELECT 1 FROM asiakas WHERE asiakas_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, asiakasId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public static boolean existsMokki(Connection conn, int mokkiId) throws SQLException {
        String sql = "SELECT 1 FROM mokki WHERE mokki_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, mokkiId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public static boolean existsVaraus(Connection conn, int varausId) throws SQLException {
        String sql = "SELECT 1 FROM varaus WHERE varaus_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, varausId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    // ==== RAPORTOINTI ====

    public static String myyntiraportti(Connection conn, LocalDate alku, LocalDate loppu) throws SQLException {
        String sql = """
            SELECT
              COUNT(l.lasku_id) AS invoiceCount,
              COALESCE(SUM(l.summa),0) AS totalSum
            FROM laskujen_hallinta l
            JOIN varaus v ON l.varaus_id = v.varaus_id
            WHERE v.varattu_pvm BETWEEN ? AND ?
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(alku));
            stmt.setDate(2, Date.valueOf(loppu));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("invoiceCount");
                    double sum = rs.getDouble("totalSum");
                    return String.format("Myyntiraportti %s – %s:\n  • Laskuja: %d\n  • Kokonaissumma: %.2f €",
                            alku, loppu, count, sum);
                }
            }
        }
        return "Ei tietoja annetulta ajalta.";
    }
}

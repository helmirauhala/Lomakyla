package org.example.lomakyla;
import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

public class Workbench {

    private static final String URL = "jdbc:mysql://localhost:3306/Lomaparatiisi"
            + "?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "mysql123456789";

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in);
             Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

            while (true) {
                System.out.println("\nValitse toiminto:");
                System.out.println("1.  Lisää asiakas");
                System.out.println("2.  Hae kaikki asiakkaat");
                System.out.println("3.  Muokkaa asiakkaan nimeä");
                System.out.println("4.  Poista asiakas");
                System.out.println("5.  Lisää mökki");
                System.out.println("6.  Hae kaikki mökit");
                System.out.println("7.  Muokkaa mökin varustelua");
                System.out.println("8.  Poista mökki");
                System.out.println("9.  Lisää lasku");
                System.out.println("10. Hae kaikki laskut");
                System.out.println("11. Merkitse lasku maksetuksi");
                System.out.println("12. Poista lasku");
                System.out.println("13. Lisää varaus");
                System.out.println("14. Hae kaikki varaukset");
                System.out.println("15. Muokkaa varauksen vahvistuspäivää");
                System.out.println("16. Poista varaus");
                System.out.println("17. Myyntiraportti");
                System.out.println("18. Lopeta");
                System.out.print("Valinta: ");

                int valinta = scanner.nextInt();
                scanner.nextLine();

                switch (valinta) {
                    case 1  -> lisaaAsiakas(conn, scanner);
                    case 2  -> haeAsiakkaat(conn);
                    case 3  -> muokkaaAsiakas(conn, scanner);
                    case 4  -> poistaAsiakas(conn, scanner);
                    case 5  -> lisaaMokki(conn, scanner);
                    case 6  -> haeMokit(conn);
                    case 7  -> muokkaaMokkiVarustelu(conn, scanner);
                    case 8  -> poistaMokki(conn, scanner);
                    case 9  -> lisaaLasku(conn, scanner);
                    case 10 -> haeLaskut(conn);
                    case 11 -> merkitseLaskuMaksetuksi(conn, scanner);
                    case 12 -> poistaLasku(conn, scanner);
                    case 13 -> lisaaVaraus(conn, scanner);
                    case 14 -> haeVaraukset(conn);
                    case 15 -> muokkaaVarausVahvistus(conn, scanner);
                    case 16 -> poistaVaraus(conn, scanner);
                    case 17 -> myyntiraportti(conn, scanner);
                    case 18 -> {
                        System.out.println("Lopetetaan...");
                        return;
                    }
                    default -> System.out.println("⚠️ Virheellinen valinta!");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==== Asiakas CRUD ====

    private static void lisaaAsiakas(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Anna asiakkaan ID: ");
        int id = scanner.nextInt(); scanner.nextLine();
        System.out.print("Anna nimi: ");
        String nimi = scanner.nextLine();
        System.out.print("Anna sähköposti: ");
        String sahkoposti = scanner.nextLine();
        System.out.print("Anna puhelinnumero: ");
        String puhelin = scanner.nextLine();
        System.out.print("Anna osoite: ");
        String osoite = scanner.nextLine();
        System.out.print("Anna postinumero: ");
        String postinumero = scanner.nextLine();
        System.out.print("Anna kunta: ");
        String kunta = scanner.nextLine();

        Asiakas a = new Asiakas(id, nimi, sahkoposti, puhelin, osoite, postinumero, kunta);

        String sql = """
            INSERT INTO asiakas (
              asiakas_id, nimi, sahkoposti, puhelinnumero,
              osoite, postinumero, kunta
            ) VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, a.getAsiakasId());
            stmt.setString(2, a.getNimi());
            stmt.setString(3, a.getSahkoposti());
            stmt.setString(4, a.getPuhelinnumero());
            stmt.setString(5, a.getOsoite());
            stmt.setString(6, a.getPostinumero());
            stmt.setString(7, a.getKunta());
            System.out.println(stmt.executeUpdate() > 0
                    ? "✅ Asiakas lisätty: " + a
                    : "⚠️ Asiakkaan lisäys epäonnistui.");
        }
    }

    private static void haeAsiakkaat(Connection conn) throws SQLException {
        String sql = "SELECT asiakas_id, nimi, sahkoposti, puhelinnumero, osoite, postinumero, kunta FROM asiakas";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\nAsiakkaat:");
            while (rs.next()) {
                Asiakas a = new Asiakas(
                        rs.getInt("asiakas_id"),
                        rs.getString("nimi"),
                        rs.getString("sahkoposti"),
                        rs.getString("puhelinnumero"),
                        rs.getString("osoite"),
                        rs.getString("postinumero"),
                        rs.getString("kunta")
                );
                System.out.println(a);
            }
        }
    }

    private static void muokkaaAsiakas(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Anna muokattavan asiakkaan ID: ");
        int id = scanner.nextInt(); scanner.nextLine();
        System.out.print("Anna uusi nimi: ");
        String uusi = scanner.nextLine();

        String sql = "UPDATE asiakas SET nimi = ? WHERE asiakas_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, uusi);
            stmt.setInt(2, id);
            System.out.println(stmt.executeUpdate() > 0
                    ? "✅ Asiakkaan nimi päivitetty."
                    : "⚠️ Asiakasta ei löytynyt.");
        }
    }

    private static void poistaAsiakas(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Anna poistettavan asiakkaan ID: ");
        int id = scanner.nextInt(); scanner.nextLine();

        String sql = "DELETE FROM asiakas WHERE asiakas_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            System.out.println(stmt.executeUpdate() > 0
                    ? "✅ Asiakas poistettu."
                    : "⚠️ Asiakasta ei löytynyt.");
        }
    }

    // ==== Mokki CRUD ====

    private static void lisaaMokki(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Anna mökin ID: ");
        int id = scanner.nextInt(); scanner.nextLine();
        System.out.print("Anna mökin numero: ");
        String numero = scanner.nextLine();
        System.out.print("Anna kapasiteetti: ");
        int kapas = scanner.nextInt(); scanner.nextLine();
        System.out.print("Anna varustelu: ");
        String varustelu = scanner.nextLine();
        System.out.print("Anna osoite: ");
        String osoite = scanner.nextLine();
        System.out.print("Anna postinumero: ");
        String postinumero = scanner.nextLine();
        System.out.print("Anna kunta: ");
        String kunta = scanner.nextLine();

        Mokki m = new Mokki(id, numero, kapas, varustelu, osoite, postinumero, kunta);

        String sql = """
            INSERT INTO mokki (
              mokki_id, mokkinumero, kapasiteetti,
              varustelu, osoite, postinumero, kunta
            ) VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, m.getMokkiId());
            stmt.setString(2, m.getMokinNumero());
            stmt.setInt(3, m.getKapasiteetti());
            stmt.setString(4, m.getVarustelu());
            stmt.setString(5, m.getOsoite());
            stmt.setString(6, m.getPostinumero());
            stmt.setString(7, m.getKunta());
            System.out.println(stmt.executeUpdate() > 0
                    ? "✅ Mökki lisätty: " + m
                    : "⚠️ Mökki-lisäys epäonnistui.");
        }
    }

    private static void haeMokit(Connection conn) throws SQLException {
        String sql = "SELECT mokki_id, mokkinumero, kapasiteetti, varustelu, osoite, postinumero, kunta FROM mokki";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\nMökit:");
            while (rs.next()) {
                Mokki m = new Mokki(
                        rs.getInt("mokki_id"),
                        rs.getString("mokkinumero"),
                        rs.getInt("kapasiteetti"),
                        rs.getString("varustelu"),
                        rs.getString("osoite"),
                        rs.getString("postinumero"),
                        rs.getString("kunta")
                );
                System.out.println(m);
            }
        }
    }

    private static void muokkaaMokkiVarustelu(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Anna muokattavan mökin ID: ");
        int id = scanner.nextInt(); scanner.nextLine();
        System.out.print("Anna uusi varustelu: ");
        String uusi = scanner.nextLine();

        String sql = "UPDATE mokki SET varustelu = ? WHERE mokki_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, uusi);
            stmt.setInt(2, id);
            System.out.println(stmt.executeUpdate() > 0
                    ? "✅ Mökin varustelu päivitetty."
                    : "⚠️ Mökin ID ei löytynyt.");
        }
    }

    private static void poistaMokki(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Anna poistettavan mökin ID: ");
        int id = scanner.nextInt(); scanner.nextLine();

        String sql = "DELETE FROM mokki WHERE mokki_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            System.out.println(stmt.executeUpdate() > 0
                    ? "✅ Mökki poistettu."
                    : "⚠️ Mökin ID ei löytynyt.");
        }
    }

    // ==== Lasku CRUD ====

    private static void lisaaLasku(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Anna laskun ID: ");
        int id = scanner.nextInt(); scanner.nextLine();

        System.out.print("Anna tila (0 = ei maksettu, 1 = maksettu): ");
        int tilaInt = scanner.nextInt(); scanner.nextLine();
        boolean maksettu = (tilaInt == 1);

        System.out.print("Anna summa: ");
        double summa = Double.parseDouble(scanner.nextLine().replace(',', '.'));

        System.out.print("Anna ALV: ");
        double alv = Double.parseDouble(scanner.nextLine().replace(',', '.'));

        System.out.print("Anna varauksen ID: ");
        int varausId = scanner.nextInt(); scanner.nextLine();
        if (!existsVaraus(conn, varausId)) {
            System.out.println("⚠️ Virhe: Varausta ID=" + varausId + " ei löydy!");
            return;
        }

        System.out.print("Anna asiakkaan ID: ");
        int asiakasId = scanner.nextInt(); scanner.nextLine();
        if (!existsAsiakas(conn, asiakasId)) {
            System.out.println("⚠️ Virhe: Asiakasta ID=" + asiakasId + " ei löydy!");
            return;
        }

        Lasku l = new Lasku(id, asiakasId, varausId, summa, alv, maksettu);

        String sql = """
            INSERT INTO laskujen_hallinta (
              lasku_id, tila, summa, alv,
              varaus_id, asiakas_id
            ) VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, l.getLaskuId());
            stmt.setBoolean(2, l.isMaksettu());
            stmt.setDouble(3, l.getSumma());
            stmt.setDouble(4, l.getAlv());
            stmt.setInt(5, l.getVarausId());
            stmt.setInt(6, l.getAsiakasId());
            System.out.println(stmt.executeUpdate() > 0
                    ? "✅ Lasku lisätty: " + l
                    : "⚠️ Laskun lisäys epäonnistui.");
        }
    }

    private static void haeLaskut(Connection conn) throws SQLException {
        String sql = "SELECT lasku_id, tila, summa, alv, varaus_id, asiakas_id FROM laskujen_hallinta";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\nLaskut:");
            while (rs.next()) {
                Lasku l = new Lasku(
                        rs.getInt("lasku_id"),
                        rs.getInt("asiakas_id"),
                        rs.getInt("varaus_id"),
                        rs.getDouble("summa"),
                        rs.getDouble("alv"),
                        rs.getBoolean("tila")
                );
                System.out.println(l);
            }
        }
    }

    private static void merkitseLaskuMaksetuksi(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Anna maksettavan laskun ID: ");
        int id = scanner.nextInt(); scanner.nextLine();

        String sql = "UPDATE laskujen_hallinta SET tila = TRUE WHERE lasku_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            System.out.println(stmt.executeUpdate() > 0
                    ? "✅ Lasku merkitty maksetuksi."
                    : "⚠️ Laskua ei löytynyt.");
        }
    }

    private static void poistaLasku(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Anna poistettavan laskun ID: ");
        int id = scanner.nextInt(); scanner.nextLine();

        String sql = "DELETE FROM laskujen_hallinta WHERE lasku_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            System.out.println(stmt.executeUpdate() > 0
                    ? "✅ Lasku poistettu."
                    : "⚠️ Laskua ei löytynyt.");
        }
    }

    // ==== Varaus CRUD ====

    // ===== Apumetodi mokkien tarkistukseen =====
    private static boolean existsMokki(Connection conn, int givenId) throws SQLException {
        String sql = "SELECT 1 FROM mokki WHERE mokki_id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, givenId);
            try (ResultSet rs = st.executeQuery()) {
                return rs.next();
            }
        }
    }

    // ===== Päivitetty lisaaVaraus =====
    private static void lisaaVaraus(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Anna varauksen ID: ");
        int id = scanner.nextInt(); scanner.nextLine();

        System.out.print("Anna asiakkaan ID: ");
        int asiakasId = scanner.nextInt(); scanner.nextLine();
        if (!existsAsiakas(conn, asiakasId)) {
            System.out.println("⚠️ Virhe: Asiakasta ID=" + asiakasId + " ei löydy. Luo ensin asiakas!");
            return;
        }

        System.out.print("Anna mökin ID: ");
        int mokkiId = scanner.nextInt(); scanner.nextLine();
        if (!existsMokki(conn, mokkiId)) {
            System.out.println("⚠️ Virhe: Mökkiä ID=" + mokkiId + " ei löydy. Luo ensin mökki!");
            return;
        }

        System.out.print("Anna aloituspäivä (yyyy-MM-dd): ");
        LocalDate alku = LocalDate.parse(scanner.nextLine());
        System.out.print("Anna päättymispäivä (yyyy-MM-dd): ");
        LocalDate loppu = LocalDate.parse(scanner.nextLine());
        System.out.print("Anna varauksen luonti pvm (yyyy-MM-dd): ");
        LocalDate varattu = LocalDate.parse(scanner.nextLine());
        System.out.print("Anna  varauksen vahvistus pvm kun lasku on maksettu (yyyy-MM-dd) tai jätä tyhjäksi: ");
        String vahv = scanner.nextLine();
        LocalDate vahvistus = vahv.isBlank() ? null : LocalDate.parse(vahv);

        Varaus v = new Varaus(id, asiakasId, mokkiId, varattu, vahvistus, alku, loppu);

        String sql = """
        INSERT INTO varaus (
          varaus_id, asiakas_id, mokki_id,
          aloitus, paattyminen,
          varattu_pvm, vahvistus_pvm
        ) VALUES (?, ?, ?, ?, ?, ?, ?)
    """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, v.getVarausId());
            stmt.setInt(2, v.getAsiakasId());
            stmt.setInt(3, v.getMokkiId());
            stmt.setDate(4, Date.valueOf(v.getVarauksenAlkuPvm()));
            stmt.setDate(5, Date.valueOf(v.getVarauksenLoppuPvm()));
            stmt.setDate(6, Date.valueOf(v.getVarattuPvm()));
            if (v.getVahvistusPvm() != null) {
                stmt.setDate(7, Date.valueOf(v.getVahvistusPvm()));
            } else {
                stmt.setNull(7, Types.DATE);
            }
            System.out.println(stmt.executeUpdate() > 0
                    ? "✅ Varaus lisätty: " + v
                    : "⚠️ Varauksen lisäys epäonnistui.");
        }
    }


    private static void haeVaraukset(Connection conn) throws SQLException {
        String sql = "SELECT varaus_id, asiakas_id, mokki_id, aloitus, paattyminen, varattu_pvm, vahvistus_pvm FROM varaus";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\nVaraukset:");
            while (rs.next()) {
                Varaus v = new Varaus(
                        rs.getInt("varaus_id"),
                        rs.getInt("asiakas_id"),
                        rs.getInt("mokki_id"),
                        rs.getDate("varattu_pvm").toLocalDate(),
                        rs.getDate("vahvistus_pvm") != null
                                ? rs.getDate("vahvistus_pvm").toLocalDate() : null,
                        rs.getDate("aloitus").toLocalDate(),
                        rs.getDate("paattyminen").toLocalDate()
                );
                System.out.println(v);
            }
        }
    }

    private static void muokkaaVarausVahvistus(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Anna varauksen ID: ");
        int id = scanner.nextInt(); scanner.nextLine();
        System.out.print("Anna uusi vahvistus pvm (yyyy-MM-dd): ");
        LocalDate uusi = LocalDate.parse(scanner.nextLine());

        String sql = "UPDATE varaus SET vahvistus_pvm = ? WHERE varaus_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(uusi));
            stmt.setInt(2, id);
            System.out.println(stmt.executeUpdate() > 0
                    ? "✅ Varauksen vahvistuspvm päivitetty."
                    : "⚠️ Varausta ei löytynyt.");
        }
    }

    private static void poistaVaraus(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Anna poistettavan varauksen ID: ");
        int id = scanner.nextInt(); scanner.nextLine();

        String sql = "DELETE FROM varaus WHERE varaus_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            System.out.println(stmt.executeUpdate() > 0
                    ? "✅ Varaus poistettu."
                    : "⚠️ Varausta ei löytynyt.");
        }
    }

    // myyntiraportti
    private static void myyntiraportti(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Anna raportin alkupäivä (yyyy-MM-dd): ");
        LocalDate alku = LocalDate.parse(scanner.nextLine());

        System.out.print("Anna raportin loppupäivä (yyyy-MM-dd): ");
        LocalDate loppu = LocalDate.parse(scanner.nextLine());

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
                    System.out.printf("\nMyyntiraportti %s – %s:%n", alku, loppu);
                    System.out.printf("  • Laskuja: %d%n", count);
                    System.out.printf("  • Kokonaissumma: %.2f €%n", sum);
                } else {
                    System.out.println("Ei raportoituja myyntejä valitulta ajalta.");
                }
            }
        }
    }

    // ==== Exist-check methods ====

    private static boolean existsVaraus(Connection conn, int givenId) throws SQLException {
        String sql = "SELECT 1 FROM varaus WHERE varaus_id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, givenId);
            try (ResultSet rs = st.executeQuery()) {
                return rs.next();
            }
        }
    }

    private static boolean existsAsiakas(Connection conn, int givenId) throws SQLException {
        String sql = "SELECT 1 FROM asiakas WHERE asiakas_id = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, givenId);
            try (ResultSet rs = st.executeQuery()) {
                return rs.next();
            }
        }
    }
}

//tästä alakaa muokkaus

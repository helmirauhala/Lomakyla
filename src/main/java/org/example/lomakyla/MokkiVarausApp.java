package org.example.lomakyla;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.util.Scanner;

public class MokkiVarausApp extends Application {

    private static final String URL = "jdbc:mysql://localhost:3306/lomaparatiisi"
            + "?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "helmi";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Mökkivarausjärjestelmä");

        TabPane tabPane = new TabPane();

        // Jokainen Workbenchin toiminto saa oman tabin tai osan graafisessa muodossa
        tabPane.getTabs().add(new Tab("Lisää asiakas", createLisaaAsiakasPane()));
        tabPane.getTabs().add(new Tab("Hae asiakkaat", createHaeAsiakkaatPane()));
        tabPane.getTabs().add(new Tab("Muokkaa asiakasta", createMuokkaaAsiakasPane()));
        tabPane.getTabs().add(new Tab("Poista asiakas", createPoistaAsiakasPane()));
        tabPane.getTabs().add(new Tab("Lisää mökki", createLisaaMokkiPane()));
        tabPane.getTabs().add(new Tab("Hae mökit", createHaeMokitPane()));
        tabPane.getTabs().add(new Tab("Muokkaa mökkiä", createMuokkaaMokkiPane()));
        tabPane.getTabs().add(new Tab("Poista mökki", createPoistaMokkiPane()));
        tabPane.getTabs().add(new Tab("Lisää varaus", createLisaaVarausPane()));
        tabPane.getTabs().add(new Tab("Hae varaukset", createHaeVarauksetPane()));
        tabPane.getTabs().add(new Tab("Muokkaa varausta", createMuokkaaVarausPane()));
        tabPane.getTabs().add(new Tab("Poista varaus", createPoistaVarausPane()));
        tabPane.getTabs().add(new Tab("Lisää lasku", createLisaaLaskuPane()));
        tabPane.getTabs().add(new Tab("Hae laskut", createHaeLaskutPane()));
        tabPane.getTabs().add(new Tab("Merkitse maksetuksi", createMerkitseMaksetuksiPane()));
        tabPane.getTabs().add(new Tab("Poista lasku", createPoistaLaskuPane()));

        Scene scene = new Scene(tabPane, 700, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Esimerkit pane-metodeista (voit tehdä saman kaikille Workbenchin metodeille)
    private Pane createLisaaAsiakasPane() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        TextField idField = new TextField();
        idField.setPromptText("Asiakas ID");

        TextField nimiField = new TextField();
        nimiField.setPromptText("Nimi");

        TextField sahkopostiField = new TextField();
        sahkopostiField.setPromptText("Sähköposti");

        TextField puhelinField = new TextField();
        puhelinField.setPromptText("Puhelin");

        TextField osoiteField = new TextField();
        osoiteField.setPromptText("Osoite");

        TextField postinumeroField = new TextField();
        postinumeroField.setPromptText("Postinumero");

        TextField kuntaField = new TextField();
        kuntaField.setPromptText("Kunta");

        Button lisaaButton = new Button("Lisää asiakas");

        lisaaButton.setOnAction(e -> {
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                MokkivarausSQL.lisaaAsiakas(conn,
                        Integer.parseInt(idField.getText()),
                        nimiField.getText(),
                        sahkopostiField.getText(),
                        puhelinField.getText(),
                        osoiteField.getText(),
                        postinumeroField.getText(),
                        kuntaField.getText());

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Asiakas lisätty!");
                alert.show();
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Virhe: " + ex.getMessage());
                alert.show();
            }
        });


        vbox.getChildren().addAll(idField, nimiField, sahkopostiField, puhelinField,
                osoiteField, postinumeroField, kuntaField, lisaaButton);

        return vbox;
    }

    private Pane createHaeAsiakkaatPane() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);

        Button haeButton = new Button("Hae asiakkaat");

        haeButton.setOnAction(e -> {
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                StringBuilder sb = new StringBuilder();
                var stmt = conn.createStatement();
                var rs = stmt.executeQuery("SELECT * FROM asiakas");
                while (rs.next()) {
                    sb.append(String.format("%d %s, %s\n", rs.getInt("asiakas_id"),
                            rs.getString("nimi"), rs.getString("sahkoposti")));
                }
                resultArea.setText(sb.toString());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        vbox.getChildren().addAll(haeButton, resultArea);
        return vbox;
    }

    private Pane createMuokkaaAsiakasPane() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        TextField idField = new TextField();
        idField.setPromptText("Asiakas ID");
        TextField uusiNimiField = new TextField();
        uusiNimiField.setPromptText("Uusi nimi");

        Button muokkaaButton = new Button("Päivitä nimi");

        muokkaaButton.setOnAction(e -> {
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                var stmt = conn.prepareStatement("UPDATE asiakas SET nimi = ? WHERE asiakas_id = ?");
                stmt.setString(1, uusiNimiField.getText());
                stmt.setInt(2, Integer.parseInt(idField.getText()));
                stmt.executeUpdate();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Nimi päivitetty.");
                alert.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        vbox.getChildren().addAll(idField, uusiNimiField, muokkaaButton);
        return vbox;
    }

    private Pane createPoistaAsiakasPane() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        TextField idField = new TextField();
        idField.setPromptText("Asiakas ID");
        Button poistaButton = new Button("Poista asiakas");

        poistaButton.setOnAction(e -> {
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                var stmt = conn.prepareStatement("DELETE FROM asiakas WHERE asiakas_id = ?");
                stmt.setInt(1, Integer.parseInt(idField.getText()));
                stmt.executeUpdate();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Asiakas poistettu.");
                alert.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        vbox.getChildren().addAll(idField, poistaButton);
        return vbox;
    }

    private Pane createLisaaMokkiPane() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        TextField idField = new TextField();
        idField.setPromptText("Mökki ID");

        TextField numeroField = new TextField();
        numeroField.setPromptText("Mökkinumero");

        TextField kapasiteettiField = new TextField();
        kapasiteettiField.setPromptText("Kapasiteetti");

        TextField varusteluField = new TextField();
        varusteluField.setPromptText("Varustelu");

        TextField osoiteField = new TextField();
        osoiteField.setPromptText("Osoite");

        TextField postinumeroField = new TextField();
        postinumeroField.setPromptText("Postinumero");

        TextField kuntaField = new TextField();
        kuntaField.setPromptText("Kunta");

        Button lisaaButton = new Button("Lisää mökki");

        lisaaButton.setOnAction(e -> {
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                MokkivarausSQL.lisaaMokki(conn,
                        Integer.parseInt(idField.getText()),
                        numeroField.getText(),
                        Integer.parseInt(kapasiteettiField.getText()),
                        varusteluField.getText(),
                        osoiteField.getText(),
                        postinumeroField.getText(),
                        kuntaField.getText());

                new Alert(Alert.AlertType.INFORMATION, " Mökki lisätty!").show();
            } catch (Exception ex) {
                ex.printStackTrace();
                new Alert(Alert.AlertType.ERROR, " Virhe: " + ex.getMessage()).show();
            }
        });

        vbox.getChildren().addAll(idField, numeroField, kapasiteettiField, varusteluField,
                osoiteField, postinumeroField, kuntaField, lisaaButton);

        return vbox;
    }
    private Pane createHaeMokitPane() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);
        Button haeButton = new Button("Hae mökit");

        haeButton.setOnAction(e -> {
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                var stmt = conn.createStatement();
                var rs = stmt.executeQuery("SELECT * FROM mokki");
                StringBuilder sb = new StringBuilder();
                while (rs.next()) {
                    sb.append(String.format("ID: %d | Numero: %s | Kunta: %s\n",
                            rs.getInt("mokki_id"),
                            rs.getString("mokkinumero"),
                            rs.getString("kunta")));
                }
                resultArea.setText(sb.toString());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        vbox.getChildren().addAll(haeButton, resultArea);
        return vbox;
    }




    private Pane createMuokkaaMokkiPane() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        TextField idField = new TextField();
        idField.setPromptText("Mökki ID");

        TextField uusiVarusteluField = new TextField();
        uusiVarusteluField.setPromptText("Uusi varustelu");

        Button paivitaButton = new Button("Päivitä varustelu");

        paivitaButton.setOnAction(e -> {
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                MokkivarausSQL.muokkaaMokkiVarustelu(conn,
                        Integer.parseInt(idField.getText()),
                        uusiVarusteluField.getText());
                new Alert(Alert.AlertType.INFORMATION, "Varustelu päivitetty!").show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        vbox.getChildren().addAll(idField, uusiVarusteluField, paivitaButton);
        return vbox;
    }


    private Pane createPoistaMokkiPane() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        TextField idField = new TextField();
        idField.setPromptText("Mökki ID");

        Button poistaButton = new Button("Poista mökki");

        poistaButton.setOnAction(e -> {
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                MokkivarausSQL.poistaMokki(conn, Integer.parseInt(idField.getText()));
                new Alert(Alert.AlertType.INFORMATION, "Mökki poistettu!").show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        vbox.getChildren().addAll(idField, poistaButton);
        return vbox;
    }


    private Pane createLisaaVarausPane() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        TextField idField = new TextField();
        idField.setPromptText("Varaus ID");

        TextField asiakasIdField = new TextField();
        asiakasIdField.setPromptText("Asiakas ID");

        TextField mokkiIdField = new TextField();
        mokkiIdField.setPromptText("Mökki ID");

        DatePicker alkuPvm = new DatePicker();
        alkuPvm.setPromptText("Alkupäivä");

        DatePicker loppuPvm = new DatePicker();
        loppuPvm.setPromptText("Loppupäivä");

        DatePicker varattuPvm = new DatePicker();
        varattuPvm.setPromptText("Varattu pvm");

        DatePicker vahvistusPvm = new DatePicker();
        vahvistusPvm.setPromptText("Vahvistus pvm (valinnainen)");

        Button lisaaButton = new Button("Lisää varaus");

        lisaaButton.setOnAction(e -> {
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                MokkivarausSQL.lisaaVaraus(conn,
                        Integer.parseInt(idField.getText()),
                        Integer.parseInt(asiakasIdField.getText()),
                        Integer.parseInt(mokkiIdField.getText()),
                        alkuPvm.getValue(),
                        loppuPvm.getValue(),
                        varattuPvm.getValue(),
                        vahvistusPvm.getValue());

                new Alert(Alert.AlertType.INFORMATION, " Varaus lisätty!").show();
            } catch (Exception ex) {
                ex.printStackTrace();
                new Alert(Alert.AlertType.ERROR, " Virhe: " + ex.getMessage()).show();
            }
        });

        vbox.getChildren().addAll(idField, asiakasIdField, mokkiIdField,
                alkuPvm, loppuPvm, varattuPvm, vahvistusPvm, lisaaButton);
        return vbox;
    }


    private Pane createHaeVarauksetPane() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);

        Button haeButton = new Button("Hae varaukset");

        haeButton.setOnAction(e -> {
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                var rs = conn.createStatement().executeQuery("SELECT * FROM varaus");
                StringBuilder sb = new StringBuilder();
                while (rs.next()) {
                    sb.append(String.format("Varaus ID: %d | Asiakas ID: %d | Mökki ID: %d | Alku: %s | Loppu: %s\n",
                            rs.getInt("varaus_id"),
                            rs.getInt("asiakas_id"),
                            rs.getInt("mokki_id"),
                            rs.getDate("aloitus"),
                            rs.getDate("paattyminen")));
                }
                resultArea.setText(sb.toString());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        vbox.getChildren().addAll(haeButton, resultArea);
        return vbox;
    }


    private Pane createMuokkaaVarausPane() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        TextField varausIdField = new TextField();
        varausIdField.setPromptText("Varaus ID");

        DatePicker uusiPvmField = new DatePicker();
        uusiPvmField.setPromptText("Uusi vahvistus pvm");

        Button paivitaButton = new Button("Päivitä");

        paivitaButton.setOnAction(e -> {
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                MokkivarausSQL.muokkaaVarausVahvistus(conn,
                        Integer.parseInt(varausIdField.getText()),
                        uusiPvmField.getValue());

                new Alert(Alert.AlertType.INFORMATION, " Varaus päivitetty!").show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        vbox.getChildren().addAll(varausIdField, uusiPvmField, paivitaButton);
        return vbox;
    }


    private Pane createPoistaVarausPane() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        TextField idField = new TextField();
        idField.setPromptText("Varaus ID");

        Button poistaButton = new Button("Poista varaus");

        poistaButton.setOnAction(e -> {
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                MokkivarausSQL.poistaVaraus(conn, Integer.parseInt(idField.getText()));
                new Alert(Alert.AlertType.INFORMATION, " Varaus poistettu!").show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        vbox.getChildren().addAll(idField, poistaButton);
        return vbox;
    }


    private Pane createLisaaLaskuPane() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        TextField idField = new TextField(); idField.setPromptText("Lasku ID");
        TextField maksettuField = new TextField(); maksettuField.setPromptText("Maksettu (true/false)");
        TextField summaField = new TextField(); summaField.setPromptText("Summa");
        TextField alvField = new TextField(); alvField.setPromptText("ALV");
        TextField varausIdField = new TextField(); varausIdField.setPromptText("Varaus ID");
        TextField asiakasIdField = new TextField(); asiakasIdField.setPromptText("Asiakas ID");

        Button lisaaButton = new Button("Lisää lasku");

        lisaaButton.setOnAction(e -> {
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                MokkivarausSQL.lisaaLasku(conn,
                        Integer.parseInt(idField.getText()),
                        Boolean.parseBoolean(maksettuField.getText()),
                        Double.parseDouble(summaField.getText()),
                        Double.parseDouble(alvField.getText()),
                        Integer.parseInt(varausIdField.getText()),
                        Integer.parseInt(asiakasIdField.getText()));
                new Alert(Alert.AlertType.INFORMATION, "✅ Lasku lisätty!").show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        vbox.getChildren().addAll(idField, maksettuField, summaField, alvField,
                varausIdField, asiakasIdField, lisaaButton);
        return vbox;
    }


    private Pane createHaeLaskutPane() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        TextArea resultArea = new TextArea();
        resultArea.setEditable(false);

        Button haeButton = new Button("Hae laskut");

        haeButton.setOnAction(e -> {
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                var rs = conn.createStatement().executeQuery("SELECT * FROM laskujen_hallinta");
                StringBuilder sb = new StringBuilder();
                while (rs.next()) {
                    sb.append(String.format("Lasku ID: %d | Summa: %.2f € | Maksettu: %s\n",
                            rs.getInt("lasku_id"),
                            rs.getDouble("summa"),
                            rs.getBoolean("tila")));
                }
                resultArea.setText(sb.toString());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        vbox.getChildren().addAll(haeButton, resultArea);
        return vbox;
    }

    private Pane createMerkitseMaksetuksiPane() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        TextField idField = new TextField();
        idField.setPromptText("Lasku ID");

        Button merkitseButton = new Button("Merkitse maksetuksi");

        merkitseButton.setOnAction(e -> {
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                MokkivarausSQL.merkitseLaskuMaksetuksi(conn, Integer.parseInt(idField.getText()));
                new Alert(Alert.AlertType.INFORMATION, " Lasku merkitty maksetuksi!").show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        vbox.getChildren().addAll(idField, merkitseButton);
        return vbox;
    }


    private Pane createPoistaLaskuPane() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        TextField idField = new TextField();
        idField.setPromptText("Lasku ID");

        Button poistaButton = new Button("Poista lasku");

        poistaButton.setOnAction(e -> {
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                MokkivarausSQL.poistaLasku(conn, Integer.parseInt(idField.getText()));
                new Alert(Alert.AlertType.INFORMATION, "Lasku poistettu!").show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        vbox.getChildren().addAll(idField, poistaButton);
        return vbox;
    }











    // === Samalla tyylillä toteuta createLisaaMokkiPane(), createHaeMokitPane(), jne. ===
    // Käytä Workbenchin metodeja suoraan tai kopioi SQL-logiikka tähän luokkaan.

    public static void main(String[] args) {
        launch(args);
    }
}


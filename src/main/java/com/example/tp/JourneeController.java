package com.example.tp;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class JourneeController extends VBox {

    @FXML
    private VBox creneaucontainer;

    @FXML
    private Label date;

    public String getDate() {
        return this.date.getText();
    }

    public void setDate(LocalDate date) {
        System.out.println("AVANT LE SETDATE CONTROLLER JOURNEE LOCAL GIVEN DATE = "+date.toString());
        this.date.setText(date.format(DateTimeFormatter.ofPattern("EEE")) + " " + date.getDayOfMonth());
        System.out.println("SETDATE CONTROLLER JOURNEE DATE = "+this.date.getText());
    }

    public void setCreneau(Journee jour) {
        List<Creneau> listetemp = new ArrayList<>();
        listetemp.addAll(jour.getCreneauxLibres());
        listetemp.addAll(jour.getCreneauxPris());

        listetemp.sort(Comparator.comparing(Creneau::getHeureDebut));
        String texteBouton = null;
        if (listetemp.isEmpty())
            System.out.println("LISTE CRENEAUX VIDE");

        for (Creneau creneau : listetemp) {
            System.out.println("creneau i TEST TEST");
            if (jour.getCreneauxLibres().contains(creneau)) {
                texteBouton = creneau.getHeureDebut().toString() + " - " + creneau.getHeureFin().toString();
                Button boutonCreneau = new Button(texteBouton);
                boutonCreneau.getStyleClass().add("creneaulibre");

                // Ajouter le bouton à la vbox creneaucontainer
                creneaucontainer.getChildren().add(boutonCreneau);
            } else
            {   //c'est automatiquement un créneau pris
                List<Taches> taches = jour.getTacheList();
                for (Taches tache : taches) {
                    if (tache.getCreneau() == creneau) {
                        texteBouton = tache.getName();
                        break;
                    }
                }
                Button boutonTache = new Button(texteBouton);
                boutonTache.getStyleClass().add("creneaupris");
                // Ajouter le bouton à la vbox creneaucontainer
                creneaucontainer.getChildren().add(boutonTache);
            }
        }
    }

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }
}




// Crée une instance de JourneeController et appelle la méthode setJournee avec la journée correspondante
            /*FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("Journee.fxml"));
            VBox jour = fxmlLoader.load();
            JourneeController journeeController = fxmlLoader.getController();
            //JourneeController journeeController = new JourneeController();
            journeeController.setDate(journee.getDate());
            System.out.println("get date dans ................"+journeeController.getDate());
            journeeController.setCreneau(journee);*/

// Ajoute la journée à la grille en respectant les critères de positionnement
//Button btn = new Button("test "+i);

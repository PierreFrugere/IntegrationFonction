package main;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import main.java.net.objecthunter.exp4j.Expression;
import main.java.net.objecthunter.exp4j.ExpressionBuilder;

import java.net.URL;
import java.util.ResourceBundle;

public class VueIntegrationController implements Initializable {

    @FXML //  fx:id="cb_methode"
    private ChoiceBox<String> methodes; // Value injected by FXMLLoader

    @FXML //  fx:id="btn_integrer"
    private Button btn_integrer; // Value injected by FXMLLoader

    @FXML //  fx:id="tf_fonction"
    private TextField tf_fonction; // Value injected by FXMLLoader

    @FXML //  fx:id="tf_borneInf"
    private TextField tf_borneInf; // Value injected by FXMLLoader

    @FXML //  fx:id="tf_borneSup"
    private TextField tf_borneSup; // Value injected by FXMLLoader

    @FXML //  fx:id="tf_nbPas"
    private TextField tf_nbPas; // Value injected by FXMLLoader

    @FXML //  fx:id="tf_precision"
    private TextField tf_precision; // Value injected by FXMLLoader

    @FXML //  fx:id="ta_resultats"
    private TextArea ta_resultats; // Value injected by FXMLLoader

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO intégrer les élémnents dans le ChoiceBox

    }

    @FXML
    private void handleIntegrerAction(ActionEvent event) {
        // Button was clicked, do something...
        // Déclaration des variables
        double borneInf,
                borneSup,
                nbPas,
                precision,
                resultatMethodeRectangle,
                resultatMethodeTrapeze,
                resultatMethodeSimpson,
                resultatMethodeRomberg;

        // Récupération des variables
        // TODO - Tester si less textField sont bien des doubles
        borneInf = Double.parseDouble(tf_borneInf.getText());
        borneSup = Double.parseDouble(tf_borneSup.getText());
        nbPas = Double.parseDouble(tf_nbPas.getText());
        precision = Double.parseDouble(tf_precision.getText());

        resultatMethodeRectangle = methodeRectangle(borneInf, borneSup, nbPas);
        resultatMethodeTrapeze = methodeTrapeze(borneInf, borneSup, nbPas);
        resultatMethodeSimpson = methodeSimpson(borneInf, borneSup, nbPas);
        resultatMethodeRomberg = methodeRomberg(borneInf, borneSup, precision);

        // Affichage des résultats
        ta_resultats.appendText("Résultat méthode des rectangles : " + resultatMethodeRectangle + "\n");
        ta_resultats.appendText("Résultat méthode des trapèzes : " + resultatMethodeTrapeze + "\n");
        ta_resultats.appendText("Résultat méthode de Simpson : " + resultatMethodeSimpson + "\n");
        ta_resultats.appendText("Résultat méthode de Romberg : " + resultatMethodeRomberg + "\n");
        ta_resultats.appendText("Fonction : " + fonction(5) + "\n");
;
    }

    private double methodeRectangle(double borneInf, double borneSup, double nbPas) {
        double aire;
        double x;
        double hauteur;

        // Initialisation des variables
        aire = 0;
        x = borneInf;
        hauteur = (borneSup-borneInf)/nbPas;

        // Boucle de calcul
        while (x < borneSup) {
            aire = aire + hauteur * (fonction(x+hauteur)+fonction(x))/2;
            x = x+hauteur;
        }

        return aire;
    }

    private double methodeTrapeze(double borneInf, double borneSup, double nbPas) {
        double aire;
        double aireApproximative;
        double hauteur;
        int i;

        // Initialisation des variables
        aire = 0;
        hauteur = (borneSup-borneInf)/nbPas;

        // Calcul de l'aire approximative du trapeze f(a) f(b)
        aireApproximative = (hauteur/2)*(fonction(borneInf)+fonction(borneSup));

        // Boucle de calcul
        for (i=0 ; i < nbPas-1 ; i++) {
            aire = aire + fonction(borneInf+i*hauteur);
        }
        // Calcul final de l'aire
        aire = aireApproximative + aire * hauteur;

        return aire;

    }
    private double methodeSimpson(double borneInf, double borneSup, double nbPas) {
        double aire;
        double hauteur;
        double sommePaire;
        double sommeImpaire;
        int i;

        // Initialisation des variables
        aire = 0;
        hauteur = (borneSup-borneInf)/(nbPas*2);
        sommePaire = 0;
        sommeImpaire = 0;

        // Calcul de la somme des indices impaires
        for (i=1 ; i < nbPas-1 ; i++) {
            sommeImpaire = sommeImpaire + fonction(borneInf+hauteur*2*i);
        }

        // Calcul de la somme des indices paires
        for (i=0 ; i < nbPas ; i++) {
            sommePaire = sommePaire + fonction(borneInf+hauteur*(2*i-1));
        }
        // Calcul final de l'aire
        aire = hauteur * (fonction(borneInf) + fonction(borneSup) + 2*sommePaire + 4*sommeImpaire)/3;

        return aire;

    }
    private double methodeRomberg(double borneInf, double borneSup, double nbPas) {
        // TODO - A implémenter
        return 0.0;

    }

    private double fonction(double x) {
        double result;
        Expression e = new ExpressionBuilder(tf_fonction.getText())
                .variables("x")
                .build()
                .setVariable("x", x);
        result = e.evaluate();
        return result;
    }
}

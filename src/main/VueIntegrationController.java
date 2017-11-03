package main;

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

    private final int NB_PAS_RECTANGLES  = 400;

    private final int NB_PAS_TRAPEZES  = 4096;

    private final int NB_PAS_SIMPSON  = 8096;

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

    @FXML //  fx:id="ta_resultats"
    private TextArea ta_resultats; // Value injected by FXMLLoader

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO intégrer les élémnents dans le ChoiceBox

    }

    @FXML
    private void handleIntegrerAction(ActionEvent event) {
        // Cette fonction se déclenche lors d'un clic sur le bouton

        // Déclaration des variables
        double borneInf,
                borneSup,
                nbPas,
                precision,
                resultatMethodeRectangle1,
                resultatMethodeRectangle,
                resultatMethodeTrapeze,
                resultatMethodeSimpson;

        // Récupération des variables
        // TODO - Tester si less textField sont bien des doubles
        borneInf = Double.parseDouble(tf_borneInf.getText());
        borneSup = Double.parseDouble(tf_borneSup.getText());
        nbPas = NB_PAS_TRAPEZES;
//        precision = Double.parseDouble(tf_precision.getText());

        // Calcul méthode rectangle
        resultatMethodeRectangle1 = methodeRectangles(borneInf, borneSup, NB_PAS_RECTANGLES);
//        resultatMethodeRectangle = resultatMethodeRectangle1 + 2 * precision;
//        while (Math.abs(resultatMethodeRectangle-resultatMethodeRectangle1) >= precision) {
//            nbPas = nbPas *2;
//            resultatMethodeRectangle = methodeRectangles(borneInf, borneSup, NB_PAS_TRAPEZES);
//        }
//        resultatMethodeRectangle = methodeRectangles(borneInf, borneSup, NB_PAS_TRAPEZES);
//        System.nanoTime();
        resultatMethodeTrapeze = methodeTrapezes(borneInf, borneSup, NB_PAS_TRAPEZES);
        resultatMethodeSimpson = methodeSimpson(borneInf, borneSup, NB_PAS_SIMPSON);
        // resultatMethodeRomberg = methodeRomberg(borneInf, borneSup, precision);

        // Affichage des résultats
        ta_resultats.clear();
//        ta_resultats.appendText("Résultat méthode des rectangles : " + resultatMethodeRectangle + "\n");
        ta_resultats.appendText("Résultat méthode des rectangles : " + resultatMethodeRectangle1 + "\n");
        ta_resultats.appendText("**********************************************************\n");
        ta_resultats.appendText("Résultat méthode des trapèzes : " + resultatMethodeTrapeze + "\n");
        ta_resultats.appendText("**********************************************************\n");
        ta_resultats.appendText("Résultat méthode de Simpson : " + resultatMethodeSimpson + "\n");
        // ta_resultats.appendText("Résultat méthode de Romberg : " + resultatMethodeRomberg + "\n");

    }

    /**
     * Calcul de l'intégrale de la fonction saisie par l'utilisateur suivant la méthode des rectangles (point milieu)
     * @param borneInf borne inferieure d'intégration
     * @param borneSup : borne supérieure d'intégration
     * @param nbPas nombre de pas
     * @return la valeur de cette intégrale
     */
    private double methodeRectangles(double borneInf, double borneSup, double nbPas) {
        double aire;        // surface retournée
        double x;           // variable de la fonction f(x)
        double hauteur;     // hauteur du rectangle

        // On initialise les variables
        aire = 0;
        x = borneInf;
        hauteur = (borneSup - borneInf) / nbPas;

        // On effectue la boucle de calcul
        while (x < borneSup) {
            aire = aire + hauteur * (fonction(x + hauteur) + fonction(x)) / 2;
            x = x + hauteur;
        }

        return aire;
    }

    /**
     * Calcul de l'intégrale de la fonction saisie par l'utilisateur suivant la méthode des trapèzes
     * @param borneInf borne inferieure d'intégration
     * @param borneSup : borne supérieure d'intégration
     * @param nbPas nombre de pas
     * @return la valeur de cette intégrale
     */
    private double methodeTrapezes(double borneInf, double borneSup, double nbPas) {
        double aire;
        double aireApproximative;
        double hauteur;
        int i;

        // On initialise les variables
        aire = 0;
        hauteur = (borneSup-borneInf)/nbPas;

        // On alcule l’aire approximative du trapèze
        aireApproximative = (hauteur/2)*(fonction(borneInf)+fonction(borneSup));

        // On effectue la boucle de calcul
        for (i=1 ; i < nbPas-1 ; i++) {
            aire = aire + fonction(borneInf+i*hauteur);
        }
        // On calcule l’aire finale
        aire = aireApproximative + aire * hauteur;

        return aire;
    }

    /**
     * Calcul de l'intégrale de la fonction saisie par l'utilisateur suivant la méthode de Simpson
     * @param borneInf borne inferieure d'intégration
     * @param borneSup : borne supérieure d'intégration
     * @param nbPas nombre de pas
     * @return la valeur de cette intégrale
     */
    private double methodeSimpson(double borneInf, double borneSup, double nbPas) {
        double aire;
        double hauteur;
        double sommePair;
        double sommeImpair;
        int i;

        // On initialise les variables
        aire = 0;
        hauteur = (borneSup-borneInf)/(nbPas*2);
        sommePair = 0;
        sommeImpair = 0;

        // On calcule la somme des indices impairs
        for (i=1 ; i < nbPas-1 ; i++) {
            sommeImpair = sommeImpair + fonction(borneInf+hauteur*2*i);
        }

        // On calcule la somme des indices pairs
        for (i=1 ; i < nbPas ; i++) {
            sommePair = sommePair + fonction(borneInf+hauteur*(2*i-1));
    }

        // On calcule l’aire finale
        aire = hauteur * (fonction(borneInf) + fonction(borneSup) + 2 * sommePair + 4 * sommeImpair) / 3;

        return aire;
    }

    /**
     * Permet de calculer f(x) de la fonction saisie par l'utilisateur
     * @param x la valeur de x
     * @return la valeur de f(x) calculée
     */
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

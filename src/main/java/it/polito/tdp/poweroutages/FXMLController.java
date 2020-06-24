package it.polito.tdp.poweroutages;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.poweroutages.model.Arco;
import it.polito.tdp.poweroutages.model.Model;
import it.polito.tdp.poweroutages.model.Nerc;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea txtResult;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private ComboBox<Nerc> cmbBoxNerc;

    @FXML
    private Button btnVisualizzaVicini;

    @FXML
    private TextField txtK;

    @FXML
    private Button btnSimula;

    @FXML
    void doCreaGrafo(ActionEvent event) {

    	this.txtResult.clear();
    	this.model.creaGrafo();
    	this.txtResult.appendText(String.format("Grafo creato!\n#vertici: %d\n#archi: %d\n",model.nVertici(), model.nArchi()));
    	
    	this.cmbBoxNerc.getItems().clear(); 
    	this.cmbBoxNerc.getItems().addAll(model.tendina());
    	this.cmbBoxNerc.setValue(model.tendina().get(0));
    
    }

    @FXML
    void doSimula(ActionEvent event) {

    	this.txtResult.clear();
    	
    	try {
    		Integer k = Integer.valueOf(this.txtK.getText());
    		Integer nCatastrofi = model.simula(k);
    		this.txtResult.appendText("Totale bonus per NERC:\n");
    		
    		for(Nerc n : model.tendina()) {
    			this.txtResult.appendText(n.getValue()+" "+n.getBonus()+"\n");
    		}
    		
    		this.txtResult.appendText("\nNumero catastrofi: "+nCatastrofi);
    	
    	}catch(NumberFormatException nfe) {
    		this.txtResult.appendText("Inserisci valore corretto");
    	}
    }

    @FXML
    void doVisualizzaVicini(ActionEvent event) {
    	this.txtResult.clear();
    	
    	Nerc n = this.cmbBoxNerc.getValue();
    	
    	List<Arco> vicini = model.vicini(n);  
    	if(vicini.size()>0) {
	    	this.txtResult.appendText("Vicini di: "+n+"\n");
	    	for(Arco a : vicini) {
	    		this.txtResult.appendText(a.toString()+"\n");
	    	}
    	}else {
    		this.txtResult.appendText("Nessun vicino");
    	}
    }

    @FXML
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'PowerOutages.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'PowerOutages.fxml'.";
        assert cmbBoxNerc != null : "fx:id=\"cmbBoxNerc\" was not injected: check your FXML file 'PowerOutages.fxml'.";
        assert btnVisualizzaVicini != null : "fx:id=\"btnVisualizzaVicini\" was not injected: check your FXML file 'PowerOutages.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'PowerOutages.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'PowerOutages.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
	}
}

package br.edu.ufu.doutorado.pca.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import br.edu.ufu.doutorado.pca.modelo.Acionador;
import br.edu.ufu.doutorado.pca.modelo.AcionadorEstado;
import br.edu.ufu.doutorado.pca.modelo.Comando;
import br.edu.ufu.doutorado.pca.modelo.Pictograma;
import br.edu.ufu.doutorado.pca.view.fxml.ISceneAcionador;

public class SimuladorView extends Application {
	
	private static ISceneAcionador controllerAtual, controllerPrincipal, controllerBusca;
	private static Scene scenePrincipal, sceneBusca;

	private static Stage stage;

	public static final Integer TAMANHO_PALAVRA = 3;
	private static List<Comando> comando = new ArrayList<Comando>();
	private static long tempo;
	private static long tempoDwell;
	private static boolean esperandoDwell = false;
	private static boolean tocouLongo = false;
	private static boolean tocouCurto = false;
	private static boolean tocouCancelar = false;

	private static AcionadorEstado estado = AcionadorEstado.OFF;

	public static void main(String[] args) {
		launch();
	}

	private void criarScenePrincipal() {
		FXMLLoader loader = getScreen("fxml/Principal.fxml", controllerPrincipal);
		try {
			scenePrincipal = new Scene((VBox)loader.load());
			controllerPrincipal = loader.getController();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void criarSceneBusca() {
		FXMLLoader loader = getScreen("fxml/BuscaPictograma.fxml", controllerBusca);
		try {
			sceneBusca = new Scene((VBox)loader.load());
			controllerBusca = loader.getController();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private FXMLLoader getScreen(String caminho, ISceneAcionador controller) {
	    try
	    {
	    	FXMLLoader fxmlLoader = new FXMLLoader();
	    	fxmlLoader.setLocation(getClass().getResource("/" + caminho));
	    	return fxmlLoader;
	    }
	    catch ( Exception ex )
	    {
	        ex.printStackTrace();
	        return null;
	    } 
	}

	@Override
	public void start(Stage stage) throws Exception {
		SimuladorView.stage = stage;

		criarScenePrincipal();
		criarSceneBusca();

		initComponents();

		stage.setScene(scenePrincipal);
		stage.show();

		controllerAtual = controllerPrincipal;
	}

	private void tocarSom(Comando comando) {
		String musicFile = getClass().getResource(comando.getCaminho()).toExternalForm() ;     // For example

		Media sound = new Media(musicFile);
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
	}

	private void initComponents() {

		for (Comando comando : Comando.values()) {
			tocarSom(comando);
		}

		Timeline timer = new Timeline(new KeyFrame(Duration.millis(20), new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent event) {
		    	if (estado.equals(AcionadorEstado.ON)) {
		    		long tempo = System.currentTimeMillis() - SimuladorView.tempo;
		    		
		    		List<Comando> comandoTemp = new ArrayList<Comando>();
		    		comandoTemp.addAll(comando);
		    		
			    	if (Acionador.classificar(tempo).equals(Comando.CURTO) && ! tocouCurto) {
			    		tocarSom(Comando.CURTO);
			    		comandoTemp.add(Comando.CURTO);
			    		processarCaractere(Acionador.palavra(comandoTemp));
			    		tocouCurto = true;
			    	} else if (Acionador.classificar(tempo).equals(Comando.LONGO) && ! tocouLongo) {
			    		tocarSom(Comando.LONGO);
			    		comandoTemp.add(Comando.LONGO);
			    		processarCaractere(Acionador.palavra(comandoTemp));
			    		tocouLongo = true;
			    	} else if (Acionador.classificar(tempo).equals(Comando.CANCELAR) && ! tocouCancelar) {
			    		tocarSom(Comando.CANCELAR);
			    		processarCaractere(Acionador.palavra(comandoTemp));
			    		tocouCancelar = true;
			    	}
		    	}
		    	if (esperandoDwell) {
		    		long tempo = System.currentTimeMillis() - SimuladorView.tempoDwell;
		    		if (Acionador.isDwellTime(tempo)) {
		    			processarPalavra();
		    		}
		    	}
		    }
		}));
		timer.setCycleCount(Timeline.INDEFINITE);
		timer.play();

		stage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent arg0) {
				if (estado.equals(AcionadorEstado.OFF)) {
					tocouCancelar = false;
					tocouCurto = false;
					tocouLongo = false;
					esperandoDwell = false;

					tempo = System.currentTimeMillis();
					estado = AcionadorEstado.ON;
				}
			}
		});

		stage.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent arg0) {
				tempo = System.currentTimeMillis() - tempo;
				estado = AcionadorEstado.OFF;
				Comando comando = Acionador.classificar(tempo);

				if (comando.equals(Comando.CANCELAR)) {
					SimuladorView.comando = new ArrayList<Comando>();
					processarCancelar();
				} else {
					SimuladorView.comando.add(comando);

					processarCaractere(Acionador.palavra(SimuladorView.comando));

					if (SimuladorView.comando.size() == TAMANHO_PALAVRA) {
						processarPalavra();
					} else {
						tempoDwell = System.currentTimeMillis();
						esperandoDwell = true;
					}
				}
			}
		});

	}

	private void processarCancelar() {
		controllerAtual.processarCancelar();
	}

	private void processarPalavra() {
		try {
			tocarSom(Comando.DWELL);
			esperandoDwell = false;

			controllerAtual.processarPalavra(Acionador.palavra(comando));
			comando = new ArrayList<Comando>();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void processarCaractere(String comando) {
		controllerAtual.processarCaractere(comando);
	}

	public static Stage getStage() {
		return stage;
	}

	public static void carregarPesquisa() {
		try {
			controllerAtual = controllerBusca;
			controllerAtual.setParametro(controllerPrincipal.getParametro());
			controllerAtual.abrir();
			getStage().setScene(sceneBusca);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void carregarPrincipal() {
		controllerAtual = controllerPrincipal;
		getStage().setScene(scenePrincipal);
	}

	public static void carregarPrincipal(Pictograma pictograma) {
		try {
			List<Object> params = new ArrayList<Object>();
			params.add(pictograma);
			controllerAtual = controllerPrincipal;
			controllerAtual.setParametro(params);
			controllerAtual.abrir();
			getStage().setScene(scenePrincipal);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

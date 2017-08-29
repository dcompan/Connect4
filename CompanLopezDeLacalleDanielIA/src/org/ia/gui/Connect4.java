package org.ia.gui;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import org.ia.core.BC;
import org.ia.core.BCA;
import org.ia.core.Player;
import org.ia.core.PlayerMinimax;
import org.ia.core.PlayerRandom;
import org.ia.core.PlayerRules;

public class Connect4 extends JFrame {
	
	/** Si se va a empaquetar en un archivo .JAR ha de ponerse a <code>true</code> */
	public static final boolean TOJAR=false;
	
	private static final long serialVersionUID = 1L;
	private static final String WINDOW_ICON="recursos/smallblue.png";
	private static final String P1_ICON="recursos/smallyellow.png";
	private static final String P2_ICON="recursos/smallred.png";
	
	private int p1Type;
	private int p2Type;
	private int p1Level;
	private int p2Level;
	private String p1RulesFileName;
	private String p1RulesFilePath;
	private String p1HeuristicFileName;
	private String p1HeuristicFilePath;
	private String p2RulesFileName;
	private String p2RulesFilePath;
	private String p2HeuristicFileName;
	private String p2HeuristicFilePath;
	
	private JPanel contentPane;
	
	/**
	 *  ComboBox Elements.
	 */
	private class ComboItem {
		
	    public String key;
	    public int value;

	    public ComboItem(String key, int value) {
	        this.key = key;
	        this.value = value;
	    }

	    @Override
	    public String toString() {
	        return key;
	    }
	    
	}
	
	/**
	 *  ComboBox Player Select Listener.
	 */
	private class PlayerSelectComboBoxActionListener implements ActionListener {
		
		private int playerNumber;
		private JComboBox<ComboItem> comboBox;
		private ComboItem itemSelected;
		private JSpinner spinner;
		private JButton button;
		
		PlayerSelectComboBoxActionListener(int pn, JComboBox<ComboItem> cb, JSpinner s, JButton b) {
			this.playerNumber=pn;
			this.comboBox=cb;
			this.spinner=s;
			this.button=b;
		}
		
		public void actionPerformed(ActionEvent e) {
			this.itemSelected=(ComboItem) comboBox.getSelectedItem();
			switch(itemSelected.value){
				case Player.HUMAN_PLAYER:
					if(playerNumber==1) p1Type=Player.HUMAN_PLAYER;
					else if(playerNumber==2) p2Type=Player.HUMAN_PLAYER;
					String[] numbersH = {"1"};
				    spinner.setModel(new SpinnerListModel(numbersH));
				    spinner.setValue("1");
				    spinner.setEnabled(false);
				    if(playerNumber==1) p1Level=1;
					else if(playerNumber==2) p2Level=1;
				    button.setEnabled(false);
					break;
				case Player.RANDOM_PLAYER:
					if(playerNumber==1) p1Type=Player.RANDOM_PLAYER;
					else if(playerNumber==2) p2Type=Player.RANDOM_PLAYER;
					String[] numbersD = {"1"};
				    spinner.setModel(new SpinnerListModel(numbersD));
				    spinner.setValue("1");
				    spinner.setEnabled(false);
				    if(playerNumber==1) p1Level=1;
					else if(playerNumber==2) p2Level=1;
				    button.setEnabled(false);
					break;
				case Player.RULES_PLAYER:
					if(playerNumber==1) p1Type=Player.RULES_PLAYER;
					else if(playerNumber==2) p2Type=Player.RULES_PLAYER;
					String[] numbersR = {"1","2","3"};
					spinner.setModel(new SpinnerListModel(numbersR));
					spinner.setValue("1");
					spinner.setEnabled(true);
					if(playerNumber==1) p1Level=1;
					else if(playerNumber==2) p2Level=1;
					button.setEnabled(true);
					break;
				case Player.MINMAX_PLAYER:
					if(playerNumber==1) p1Type=Player.MINMAX_PLAYER;
					else if(playerNumber==2) p2Type=Player.MINMAX_PLAYER;
					String[] numbersMM = {"1","2","3"};
					spinner.setModel(new SpinnerListModel(numbersMM));
					spinner.setValue("1");
					spinner.setEnabled(true);
					if(playerNumber==1) p1Level=1;
					else if(playerNumber==2) p2Level=1;
					button.setEnabled(true);
					break;
			}
		}
		
	}
	
	/**
	 *  Spinner Level Listener.
	 */
	private class LevelSelectChangeListener implements ChangeListener {
		
		private int playerNumber;
		private JSpinner spinner;
		
		LevelSelectChangeListener(int pn, JSpinner s) {
			this.playerNumber = pn;
			this.spinner=s;
		}
		
		public void stateChanged(ChangeEvent e) {
			int level=Integer.parseInt(spinner.getValue().toString());
			if(playerNumber==1){
				p1Level=level;
		    }
		    else if(playerNumber==2){
		    	p2Level=level;
		    }
		}
	}
	
	/**
	 *  File Chooser Button Listener.
	 */
	private class FileChooserActionListener implements ActionListener {
		
		private int playerNumber;
		
		FileChooserActionListener(int pn) {
			this.playerNumber = pn;
		}
		
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
			int result = fileChooser.showOpenDialog(contentPane);
			if (result == JFileChooser.APPROVE_OPTION) {
			    File selectedFile = fileChooser.getSelectedFile();
			    String absolutePath = selectedFile.getAbsolutePath();
			    if(selectedFile.exists() && selectedFile.isFile()){
				    if(playerNumber==1){
				    	if(p1Type==Player.RULES_PLAYER){
					    	p1RulesFileName=selectedFile.getName();
					    	p1RulesFilePath=absolutePath.substring(0,absolutePath.lastIndexOf(File.separator));
				    	}else if(p1Type==Player.MINMAX_PLAYER){
				    		p1HeuristicFileName=selectedFile.getName();
					    	p1HeuristicFilePath=absolutePath.substring(0,absolutePath.lastIndexOf(File.separator));
				    	}
				    }
				    else if(playerNumber==2){
				    	if(p2Type==Player.RULES_PLAYER){
					    	p2RulesFileName=selectedFile.getName();
					    	p2RulesFilePath=absolutePath.substring(0,absolutePath.lastIndexOf(File.separator));
				    	}else if(p2Type==Player.MINMAX_PLAYER){
				    		p2HeuristicFileName=selectedFile.getName();
					    	p2HeuristicFilePath=absolutePath.substring(0,absolutePath.lastIndexOf(File.separator));
				    	}
				    }
			    }
			}
		}
		
	}
	
	/**
	 * Start Button Listener.
	 */
	private class StartButtonActionListener implements ActionListener {
		
		StartButtonActionListener() {
			super();
		}
		
		public void actionPerformed(ActionEvent e) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					BC bc=new BC();
					BCA bca=new BCA();
					Player p1=createPlayer(1,bc,bca);
					Player p2=createPlayer(2,bc,bca);
					try {
						WindowsBoard frame = new WindowsBoard(p1,p2);
						frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
	
	/**
	 * Crea un jugador nuevo.
	 */
	private Player createPlayer(int playerNumber, BC bcBoard, BCA bcaBoard){
		int type=0;
		int level=1;
		char piece='\0';
		String rulesFileName=null;
		String rulesFilePath=null;
		String heuristicFileName=null;
		String heuristicFilePath=null;
		if(playerNumber==1){
			type=p1Type;
			piece='O';
			level=p1Level;
			rulesFileName=p1RulesFileName;
			rulesFilePath=p1RulesFilePath;
			heuristicFileName=p1HeuristicFileName;
			heuristicFilePath=p1HeuristicFilePath;
		}
		else if(playerNumber==2){
			type=p2Type;
			piece='X';
			level=p2Level;
			rulesFileName=p2RulesFileName;
			rulesFilePath=p2RulesFilePath;
			heuristicFileName=p2HeuristicFileName;
			heuristicFilePath=p2HeuristicFilePath;
		}
		switch(type){
			case Player.HUMAN_PLAYER:
				PlayerEcho pe=new PlayerEcho(bcBoard,piece);;
				return pe;
			case Player.RANDOM_PLAYER:
				PlayerRandom pd=new PlayerRandom(bcBoard,piece);
				pd.setWaitTime(0L);
				return pd;
			case Player.RULES_PLAYER:
				PlayerRules pr=new PlayerRules(bcaBoard,piece);
				pr.setLevel(level);
				pr.setWaitTime(0L);
				pr.setShowInfo(false);
				if(rulesFileName!=null && rulesFileName.length()!=0) pr.choseRulesFile(rulesFilePath,rulesFileName);
				pr.openRulesFile();
				return pr;
			case Player.MINMAX_PLAYER:
				PlayerMinimax pm=new PlayerMinimax(bcaBoard,piece);
				pm.setLevel(level);
				pm.setWaitTime(0L);
				if(heuristicFileName!=null && heuristicFileName.length()!=0) pm.choseRulesFile(heuristicFilePath,heuristicFileName);
				return pm;
			default:
				return null;
		}
	}
	
	/**
	 * Create the frame.
	 */
	public Connect4() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 330, 330);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// Icono de la Ventana
		ImageIcon windowIcon=null;
		if(!TOJAR) windowIcon=new ImageIcon(WINDOW_ICON);
		else windowIcon=new ImageIcon(Connect4.class.getClassLoader().getResource(WINDOW_ICON));	
		setIconImage(windowIcon.getImage());
		
		// Titulo de la ventana
		this.setTitle("Connect 4");
		
		// JUGADOR 1
		
		// Icono del Jugador 1
		JLabel lbPlayer1Icon = new JLabel("");
		lbPlayer1Icon.setBounds(30, 30, 16, 26);
		BufferedImage imgp1 = null;
		try {
			if(!TOJAR) imgp1 = ImageIO.read(new File(P1_ICON));
			else imgp1 = ImageIO.read(Connect4.class.getClassLoader().getResourceAsStream(P1_ICON));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		Image dimgp1 = imgp1.getScaledInstance(lbPlayer1Icon.getHeight()-10,lbPlayer1Icon.getHeight()-10,Image.SCALE_SMOOTH);
		lbPlayer1Icon.setIcon(new ImageIcon(dimgp1));
		contentPane.add(lbPlayer1Icon);
		
		// Label: "Player 1"
		JLabel lbPlayer1Text = new JLabel("  Player 1");
		lbPlayer1Text.setFont(new Font("Calibri", Font.PLAIN, 16));
		lbPlayer1Text.setBounds(46, 30, 80, 26);
		contentPane.add(lbPlayer1Text);
		
		// Label: "Level" (Player 1)
		JLabel lbPlayer1Level = new JLabel("Level");
		lbPlayer1Level.setFont(new Font("Arial", Font.PLAIN, 14));
		lbPlayer1Level.setBounds(126, 80, 44, 20);
		contentPane.add(lbPlayer1Level);
		
		// Spinner: Elegir nivel del jugador 1
		JSpinner spPlayer1level = new JSpinner();
		spPlayer1level.addChangeListener(new LevelSelectChangeListener(1,spPlayer1level));
		spPlayer1level.setFont(new Font("Arial", Font.PLAIN, 14));
		spPlayer1level.setBounds(170, 80, 44, 20);
		String[] numbersp1 = {"1"};
		spPlayer1level.setModel(new SpinnerListModel(numbersp1));
		spPlayer1level.setValue("1");
	    spPlayer1level.setEnabled(false);
		contentPane.add(spPlayer1level);
		p1Level=1;
		
		// Button: Elegir archivo del jugador 1
		JButton btnPlayer1fc = new JButton("");
		btnPlayer1fc.addActionListener(new FileChooserActionListener(1));
		btnPlayer1fc.setIcon(new ImageIcon(Connect4.class.getResource("/com/sun/java/swing/plaf/windows/icons/FloppyDrive.gif")));
		btnPlayer1fc.setBounds(233, 75, 33, 25);
		btnPlayer1fc.setEnabled(false);
		contentPane.add(btnPlayer1fc);
		
		// ComboBox: Elegir al jugador 1
		JComboBox<ComboItem> comboBoxPlayer1 = new JComboBox<ComboItem>();
		comboBoxPlayer1.addActionListener(new PlayerSelectComboBoxActionListener(1,comboBoxPlayer1,spPlayer1level,btnPlayer1fc));
		comboBoxPlayer1.setFont(new Font("Calibri", Font.PLAIN, 16));
		comboBoxPlayer1.setBounds(126, 30, 140, 26);
		comboBoxPlayer1.addItem(new ComboItem(" "+Player.PLAYERS[Player.HUMAN_PLAYER],Player.HUMAN_PLAYER));
		comboBoxPlayer1.addItem(new ComboItem(" "+Player.PLAYERS[Player.RANDOM_PLAYER],Player.RANDOM_PLAYER));
		comboBoxPlayer1.addItem(new ComboItem(" "+Player.PLAYERS[Player.RULES_PLAYER],Player.RULES_PLAYER));
		comboBoxPlayer1.addItem(new ComboItem(" "+Player.PLAYERS[Player.MINMAX_PLAYER],Player.MINMAX_PLAYER));
		contentPane.add(comboBoxPlayer1);
		p1Type=Player.HUMAN_PLAYER;
		
		// JUGADOR 2
		
		// Icono del Jugador 2
		JLabel lbPlayer2Icon = new JLabel("");
		lbPlayer2Icon.setBounds(30, 143, 16, 26);
		contentPane.add(lbPlayer2Icon);
		BufferedImage imgp2 = null;
		try {
			if(!TOJAR) imgp2 = ImageIO.read(new File(P2_ICON));
			else imgp2 = ImageIO.read(Connect4.class.getClassLoader().getResourceAsStream(P2_ICON));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		Image dimgp2 = imgp2.getScaledInstance(lbPlayer2Icon.getHeight()-10,lbPlayer2Icon.getHeight()-10,Image.SCALE_SMOOTH);
		lbPlayer2Icon.setIcon(new ImageIcon(dimgp2));
		contentPane.add(lbPlayer2Icon);
		
		// Label: "Player 1"
		JLabel lbPlayer2Text = new JLabel("  Player 2");
		lbPlayer2Text.setFont(new Font("Calibri", Font.PLAIN, 16));
		lbPlayer2Text.setBounds(46, 143, 80, 26);
		contentPane.add(lbPlayer2Text);
		
		// Label: "Level" (Player 2)
		JLabel lbPlayer2Level = new JLabel("Level");
		lbPlayer2Level.setFont(new Font("Arial", Font.PLAIN, 14));
		lbPlayer2Level.setBounds(126, 193, 44, 20);
		contentPane.add(lbPlayer2Level);
		
		// Spinner: Elegir nivel del jugador 2
		JSpinner spPlayer2level = new JSpinner();
		spPlayer2level.addChangeListener(new LevelSelectChangeListener(2,spPlayer2level));
		spPlayer2level.setFont(new Font("Arial", Font.PLAIN, 14));
		spPlayer2level.setBounds(170, 193, 44, 20);
		String[] numbersp2 = {"1"};
		spPlayer2level.setModel(new SpinnerListModel(numbersp2));
		spPlayer2level.setValue("1");
		spPlayer2level.setEnabled(false);
		contentPane.add(spPlayer2level);
		p2Level=1;
		
		// Button: Elegir archivo del jugador 1
		JButton btnPlayer2fc = new JButton("");
		btnPlayer2fc.addActionListener(new FileChooserActionListener(2));
		btnPlayer2fc.setIcon(new ImageIcon(Connect4.class.getResource("/com/sun/java/swing/plaf/windows/icons/FloppyDrive.gif")));
		btnPlayer2fc.setBounds(233, 188, 33, 25);
		btnPlayer2fc.setEnabled(false);
		contentPane.add(btnPlayer2fc);
				
		// ComboBox: Elegir al jugador 2
		JComboBox<ComboItem> comboBoxPlayer2 = new JComboBox<ComboItem>();
		comboBoxPlayer2.addActionListener(new PlayerSelectComboBoxActionListener(2,comboBoxPlayer2,spPlayer2level,btnPlayer2fc));
		comboBoxPlayer2.setFont(new Font("Calibri", Font.PLAIN, 16));
		comboBoxPlayer2.setBounds(126, 143, 140, 26);
		comboBoxPlayer2.addItem(new ComboItem(" "+Player.PLAYERS[Player.HUMAN_PLAYER],Player.HUMAN_PLAYER));
		comboBoxPlayer2.addItem(new ComboItem(" "+Player.PLAYERS[Player.RANDOM_PLAYER],Player.RANDOM_PLAYER));
		comboBoxPlayer2.addItem(new ComboItem(" "+Player.PLAYERS[Player.RULES_PLAYER],Player.RULES_PLAYER));
		comboBoxPlayer2.addItem(new ComboItem(" "+Player.PLAYERS[Player.MINMAX_PLAYER],Player.MINMAX_PLAYER));
		contentPane.add(comboBoxPlayer2);
		p2Type=Player.HUMAN_PLAYER;
		
		// START
		
		// Button: Comenzar la partida
		JButton btnStart = new JButton("START");
		btnStart.addActionListener(new StartButtonActionListener());
		btnStart.setFont(new Font("Calibri", Font.PLAIN, 16));
		btnStart.setBounds(115, 245, 80, 29);
		contentPane.add(btnStart);
		
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Connect4 frame = new Connect4();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}

package org.ia.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.ia.core.BC;
import org.ia.core.Player;

public class WindowsBoard extends JFrame {
	
	/** Si se va a empaquetar en un archivo .JAR ha de ponerse a <code>true</code> */
	public static final boolean TOJAR=false;
	
	private static final long serialVersionUID = 1L;
	public static final int ROWS=6;
	public static final int COLS=7;
	private static final String WINDOW_ICON="recursos/smallblue.png";
	private static final String HOLE="recursos/hole.png";
	private static final String OPIECE="recursos/yellow.png";
	private static final String XPIECE="recursos/red.png";
	
	private BC bc;
	private Player player1;
	private Player player2;
	private Player playerTurn;
	private int numPlayerTurn;
	private int playerTurnType;
	private Game game;
	private int imageWidth;
	private int imageHeight;
	private ImageIcon hole;
	private ImageIcon pieceO;
	private ImageIcon pieceX;
	private JLabel[][] jlBoard;
	
	private JPanel contentPane;
	
	/**
	 * Retarda la ejecucion para evitar la espera activa
	 * @param mseg Milisegundos.
	 */
	public static void sleepFor(long mseg){
		try{
			Thread.sleep(mseg);
		}
		catch(InterruptedException ie){ 
			// Si el hilo fue interrumpido por otro hilo
			ie.printStackTrace();
		 }
	}
	
	/**
	 * Pinta el tablero.
	 */
	private void updateBoard(){
		char e;
		String sRow;
		for(int r=0;r<ROWS;r++){
			sRow=bc.getRow(r);
			for(int c=0;c<COLS;c++){
				e=sRow.charAt(c);
				switch(e){
					case 'E':
						jlBoard[r][c].setIcon(hole);
						break;
					case 'O':
						jlBoard[r][c].setIcon(pieceO);
						break;
					case 'X':
						jlBoard[r][c].setIcon(pieceX);
						break;
				}
			}
		}
		contentPane.updateUI();
	}
	
	/**
	 * Establece a que jugador le toca mover (playerTurn).
	 */
	private void SetNextTurn(){
		numPlayerTurn=game.getNextTurn();
		if(numPlayerTurn==1){
			playerTurn=player1;
			playerTurnType=player1.getType();
			setIconImage(pieceO.getImage());
		}
		else if(numPlayerTurn==2){
			playerTurn=player2;
			playerTurnType=player2.getType();
			setIconImage(pieceX.getImage());
		}
	}
	
	/**
	 * Hace el movimiento de un jugador no humano.
	 */
	private void machineMove(){
		game.turn();
		updateBoard();
		SetNextTurn();
		endGameCheck();
	}
	
	/**
	 * Espera un tiempo antes de pedirle a un jugador no humano que mueva.
	 */
	private void machineMoveDelayed(){
		new Thread() {
		    @Override
		    public void run() {
		        sleepFor(Player.DEF_WAIT_TIME);
		        machineMove();
		    }
		}.start();
	}
	
	/**
	 * Juega la partida completa cuando no hay jugadores humanos.
	 */
	private void machinePlay(){
		new Thread() {
		    @Override
		    public void run() {
		        while(!game.isEnd()){
		        	sleepFor(Player.DEF_WAIT_TIME);
		        	machineMove();
				}
		    }
		}.start();
	}
	
	private void endGameCheck(){
		new Thread() {
		    @Override
		    public void run() {
		    	if(game.isEnd()) endGame();
		    }
		}.start();
	}
	
	/**
	 * Mouse Listener. Coloca la ficha cuando mueve un jugador huano.
	 */
	private class BoardMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			int x=e.getX();
			if(playerTurnType==Player.HUMAN_PLAYER){
				int column=(x/imageWidth)+1;
				((PlayerEcho) playerTurn).setColumn(column);
				if(playerTurn.isReady()){
					game.turn();
					updateBoard();
					SetNextTurn();
					endGameCheck();
				}
				if(!game.isEnd() && playerTurnType!=Player.HUMAN_PLAYER) machineMoveDelayed();
			}
		}
	}
	
	/**
	 * Indica el final de la partida y pregunta si se desea volver a jugar.
	 */
	public void endGame(){
		String message="";
		if(game.isError()){
			JOptionPane.showConfirmDialog(null,game.getError(),"Error",JOptionPane.CLOSED_OPTION,JOptionPane.ERROR_MESSAGE);
			dispose();
		}
		else{
			if(game.isVictory()){
				int numWinnerPlayer = (numPlayerTurn==1) ? 2 : 1 ;
				message=message+"¡Victoria del jugador "+numWinnerPlayer;
				if(numWinnerPlayer==1) message=message+" ("+player1.getName()+")";
				else if(numWinnerPlayer==1) message=message+" ("+player2.getName()+")";
				message=message+"!";
			}
			else if(game.isDraw()) message=message+"¡Empate!";
			int nGames=game.getNumGames();
			int[] score=game.getScore();
			message=message+"\nMARCADOR:";
			message=message+"\n   Victorias P1 ("+player1.getName()+") : "+score[1]+"/"+nGames+".";
			message=message+"\n   Victorias P2 ("+player2.getName()+") : "+score[2]+"/"+nGames+".";
			message=message+"\n   Empates: "+score[0]+"/"+nGames+".";
			message=message+"\n¿Volver a jugar?";
			int option=JOptionPane.showConfirmDialog(null,message,"Partida terminada",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
			if(option==1) dispose();
			else{
				game.restart();
				updateBoard();
				SetNextTurn();			
				if(player1.getType()!=Player.HUMAN_PLAYER && player2.getType()!=Player.HUMAN_PLAYER) machinePlay();
				else{
					if(playerTurn.getType()!=Player.HUMAN_PLAYER) machineMoveDelayed();
				}
			}
		}
	}
	
	/**
	 * Create the frame.
	 */
	public WindowsBoard(Player p1, Player p2) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.addMouseListener(new BoardMouseListener());
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0};
		gbl_contentPane.rowHeights = new int[]{0};
		gbl_contentPane.columnWeights = new double[]{Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		// Icono de la Ventana
		ImageIcon windowIcon=null;
		if(!TOJAR) windowIcon=new ImageIcon(WINDOW_ICON);
		else windowIcon=new ImageIcon(WindowsBoard.class.getClassLoader().getResource(WINDOW_ICON));
		setIconImage(windowIcon.getImage());
		// Titulo de la ventana
		this.setTitle("Board");
		// Color de fondo
		contentPane.setBackground(new Color(236,233,216));
		// Inicializar BC y tomar Jugadores 
		bc=new BC();
		player1=p1;
		player2=p2;
		// Imagenes de fichas
		if(!TOJAR){
			hole=new ImageIcon(HOLE);
			pieceO=new ImageIcon(OPIECE);
			pieceX=new ImageIcon(XPIECE);
		}
		else{
			hole=new ImageIcon(WindowsBoard.class.getClassLoader().getResource(HOLE));
			pieceO=new ImageIcon(WindowsBoard.class.getClassLoader().getResource(OPIECE));
			pieceX=new ImageIcon(WindowsBoard.class.getClassLoader().getResource(XPIECE));
		}
		imageWidth=hole.getIconWidth();
		imageHeight=hole.getIconHeight();
		// Añadir JLabels para las imagenes
		jlBoard=new JLabel[ROWS][COLS];
		GridBagConstraints gbc=new GridBagConstraints();
		for(int r=0;r<ROWS;r++){
			for(int c=0;c<COLS;c++){
				jlBoard[r][c]=new JLabel();
				jlBoard[r][c].setPreferredSize(new Dimension(imageWidth,imageHeight));
				gbc.gridx=c;
				gbc.gridy=r;
				contentPane.add(jlBoard[r][c],gbc);
			}
		}
		pack();
		setResizable(false);
		// Pintar tablero
		updateBoard();
		// Crea una partida
		game=new Game(p1,p2);
		game.setBoard(bc);
		// Juega la partida
		SetNextTurn();
		if(player1.getType()!=Player.HUMAN_PLAYER && player2.getType()!=Player.HUMAN_PLAYER) machinePlay();
		else{
			if(playerTurn.getType()!=Player.HUMAN_PLAYER) machineMoveDelayed();
		}
	}

}

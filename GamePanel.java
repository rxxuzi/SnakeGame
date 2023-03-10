package Snake;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class GamePanel extends JPanel implements ActionListener{
	
	static final int SCREEN_WIDTH = 700;
	static final int SCREEN_HEIGHT = 700;
	static final int UNIT_SIZE = 50;//マスの大きさ(SCREEN_WIDTHの約数)
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	static final int DELAY = 120;//小さいほど早くなる
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int pose = 0;
	int bodyParts = 5; 
	int applesEaten;
	int appleX; int appleY;
	
	char direction = 'R';
	
	boolean running = true;
	boolean stop = false;
	
	Timer timer;
	Random random;
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH , SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdpter());
	}
	
	public void startGame() {
		newApples();
		running =true;
		timer = new Timer(DELAY, this);
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g) {
		for(int i = 0 ; i <SCREEN_HEIGHT/UNIT_SIZE ; i ++) {
			g.drawLine(i*UNIT_SIZE, 0 , i*UNIT_SIZE , SCREEN_HEIGHT);//縦線を描画
			g.drawLine(0 , i*UNIT_SIZE , SCREEN_WIDTH , i*UNIT_SIZE);//横線を描画
		}
		
		g.setColor(Color.red);
		g.fillOval(appleX,appleY,UNIT_SIZE,UNIT_SIZE);
		
		/*snake*/
		for(int i = 0; i < bodyParts; i++) {
			if( i != 0 ) {
				g.setColor(Color.green);
				g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
				FontMetrics metrics = getFontMetrics(g.getFont());
				if(!running) {
					g.setColor(Color.red);
					g.drawString("Game Over",(SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2,SCREEN_HEIGHT/2);
					g.drawString("Score : " + applesEaten,(SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2,SCREEN_HEIGHT/2 - 15);
				}
			    
			}
			else {
				g.setColor(new Color(45,190,0));
			}
			
			
		}
		
	}
	public void newApples() {
		appleX = SCREEN_WIDTH/UNIT_SIZE * UNIT_SIZE / 2;
		appleY = SCREEN_HEIGHT/UNIT_SIZE*UNIT_SIZE/2;
		
		appleX = random.nextInt((int)SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
		appleY = random.nextInt((int)SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
		
	}
	public void move() {
		
		for(int i= bodyParts; i > 0 ; i --) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		switch(direction) {
		case 'U' : 
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D' : 
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L' : 
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R' : 
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
	}
	
	public void checkApples() {
		if((x[0] == appleX) && (y[0] ==appleY)) {
			bodyParts++;
			applesEaten++;
			newApples();
		}
	}
	public void checkCollisions() {
		//check if head collides with body
		for(int i = bodyParts; i>0 ; i--) {
			if((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		//左側に壁を作る
		if(x[0] < 0) {
			running = false;
		}
		//右
		if(x[0] >=  SCREEN_WIDTH) {
			running = false;
		}
		//上
		if(y[0] < 0) {
			running = false;
		}
		//下
		if(y[0] >=  SCREEN_HEIGHT) {
			running = false;
		}
		
		if(!running) {
			timer.stop();
		}
	}
	public void gameover(Graphics g) {
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(running) {
			move();
			checkApples();
			checkCollisions();
		}
		repaint();
	}
	public class MyKeyAdpter extends KeyAdapter{
		
		@Override
		public void keyPressed(KeyEvent e) {
			
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			
			case KeyEvent.VK_SPACE:
				++pose;
				System.out.println(pose);
				if(pose == 1) {
					startGame();
				}
				
			}
		}
	}
	

}

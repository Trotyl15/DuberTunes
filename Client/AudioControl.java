package Client;

import java.awt.Image;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.ImageIcon;

public class AudioControl{
	
	Clip clip;
	AudioInputStream audioInput;
	long clipTimePosition;

	AudioControl(){
		try {
			clip=AudioSystem.getClip();
			clipTimePosition=0;
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	//select the music for audio system
	public void selectMusic(String musicLocation) {
		try {
			File musicPath=new File(musicLocation);
			if(musicPath.exists()) {
				audioInput=AudioSystem.getAudioInputStream(musicPath);
				String substring = musicLocation.substring(0, musicLocation.length() - 4);
				File coverPath=new File(substring +".jpg");
				if(coverPath.exists()) {
					//setting the cover adn size it
					ImageIcon temp=new ImageIcon(substring +".jpg");
					Image image = temp.getImage(); // transform it 
					Image newimg = image.getScaledInstance(286, 286,  java.awt.Image.SCALE_SMOOTH);
					ImageIcon imgc= new ImageIcon(newimg);
					PlayerGUI.cover.setIcon(imgc);
					Image newimg2 = image.getScaledInstance(60, 60,  java.awt.Image.SCALE_SMOOTH);
					ImageIcon imgc2= new ImageIcon(newimg2); 
					MiniGUI.cover.setIcon(imgc2);
				}else {
					File coverPath2=new File(substring +".png");
					if(coverPath2.exists()) {
						ImageIcon temp=new ImageIcon(substring +".png");
						Image image = temp.getImage(); // transform it 
						Image newimg = image.getScaledInstance(286, 286,  java.awt.Image.SCALE_SMOOTH);
						ImageIcon imgc= new ImageIcon(newimg); 
						PlayerGUI.cover.setIcon(imgc);
						Image newimg2 = image.getScaledInstance(60, 60,  java.awt.Image.SCALE_SMOOTH);
						ImageIcon imgc2= new ImageIcon(newimg2); 
						MiniGUI.cover.setIcon(imgc2);
					}else {
						//if no cover found, use the default image
						PlayerGUI.cover.setIcon(new ImageIcon(PlayerGUI.class.getResource("/Client/icon2.png")));
						MiniGUI.cover.setIcon(new ImageIcon(PlayerGUI.class.getResource("/Client/icon3.png")));
					}
				}

				if(PlayerGUI.playButton.getToolTipText().equals("stop")) {
					ProgressBar.clipTimeCur=0;
					clip.stop();
					clip.close();
					clip.open(audioInput);
					play();
					ProgressBar.setLength(clip.getMicrosecondLength());
				}else{
					clip.close();
					clip.open(audioInput);
				}
			}else {
				System.out.println("can't find file");
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}

	}
	public void play() {
		clipTimePosition=ProgressBar.clipTimeCur;
		clip.setMicrosecondPosition(clipTimePosition);
		clip.start();
	}
	
	public void stop() {
		clipTimePosition=clip.getMicrosecondPosition();
		clip.stop();

	}
	
	
}
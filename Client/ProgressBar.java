package Client;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class ProgressBar implements Runnable {
	
	public AudioControl control;
	public static boolean run=true;
	public static long clipTimeLength;
	public static long clipTimeCur;
	public static boolean isHold=false;
	public static String songLength;
	
	ProgressBar(AudioControl control){
		this.control=control;
	}
	//this will be ran when new thread starts
	public void run() {
		while(run) {
			setBar();
			if(clipTimeLength!=0&&clipTimeLength==clipTimeCur) {
				PlayerGUI.next();
			}
		}
	}

	public static void setLength(long length) {
		clipTimeLength=length;
		songLength=convert(clipTimeLength);
	}

	//convert the microsecond to min:sec
	public static String convert(long micro) {
		int total=(int)micro/1000000;
		int min=total/60;
		int sec=total%60;
		if(sec<10) {
			return min+":0"+sec;
		}else {
			return min+":"+sec;
		}
	}

	public void setBar() {
		System.out.print("");//I don't know why, but if I delete this line, it won't work
		//set the progress bar
		if(!isHold) {
			int p;
			if(clipTimeLength==0) {
				 p=0;
			}else {
				clipTimeCur=control.clip.getMicrosecondPosition();
				p=(int)(clipTimeCur*1.0/clipTimeLength*100);
			}
			PlayerGUI.progressBar.setValue(p);
			PlayerGUI.progressBar.setString(convert(clipTimeCur)+"-"+songLength);
		}

	}
	
}

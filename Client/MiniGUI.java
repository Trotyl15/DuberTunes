package Client;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Window.Type;

public class MiniGUI {

	public static JFrame frame;
	private static int x0;
	private static int y0;
	private static int y3;
	private static int x3;
	public static JLabel cover;
	public static JLabel songLabel;
	public static JLabel play;

	/**
	 * Create the application.
	 */
	public MiniGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setType(Type.POPUP);
		frame.setResizable(false);
		frame.setAlwaysOnTop(true);
		frame.setBounds(100, 100, 240, 60);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(PlayerGUI.class.getResource("/Client/icon.png")));
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		cover = new JLabel("");
		cover.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		cover.setBounds(0, 0, 60, 60);
		panel.setLayout(null);
		songLabel = new JLabel("no song selected");
		songLabel.setHorizontalAlignment(SwingConstants.CENTER);
		songLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		songLabel.setBackground(Color.WHITE);
		songLabel.setBounds(60, 0, 180, 60);
		JPanel panelBg = new JPanel();
		panelBg.setBackground(Color.WHITE);
		panelBg.setBounds(60, 0, 180, 60);
		panel.add(panelBg);
		panelBg.setLayout(new GridLayout(0, 1, 0, 0));
		panelBg.add(songLabel);
		panelBg.setVisible(false);
		cover.setHorizontalAlignment(SwingConstants.CENTER);
		cover.setIcon(new ImageIcon(MiniGUI.class.getResource("/Client/icon3.png")));
		panel.add(cover);
		JLabel prev = new JLabel("");
		prev.setToolTipText("previous");
		prev.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		prev.setBounds(60, 0, 60, 60);
		prev.setHorizontalAlignment(SwingConstants.CENTER);
		prev.setIcon(new ImageIcon(MiniGUI.class.getResource("/Client/prev.png")));
		panel.add(prev);
		play = new JLabel("");
		play.setToolTipText("stop");
		play.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		play.setBounds(120, 0, 60, 60);
		play.setHorizontalAlignment(SwingConstants.CENTER);
		play.setIcon(new ImageIcon(MiniGUI.class.getResource("/Client/stop.png")));
		panel.add(play);
		JLabel next = new JLabel("");
		next.setToolTipText("next");
		next.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		next.setBounds(180, 0, 60, 60);
		next.setHorizontalAlignment(SwingConstants.CENTER);
		next.setIcon(new ImageIcon(MiniGUI.class.getResource("/Client/next.png")));
		panel.add(next);

		cover.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				x3 = e.getX();
				y3 = e.getY();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				panelBg.setVisible(false);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				panelBg.setVisible(true);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.setVisible(false);
				PlayerGUI.frmDubertunes.setVisible(true);
			}
			
		});
		cover.addMouseMotionListener(new MouseMotionAdapter() {
			

			@Override
			public void mouseDragged(MouseEvent e) {
				x0 = e.getX();
				y0 = e.getY();
				int x1 = frame.getBounds().x;
				int y1 = frame.getBounds().y;
				frame.setLocation(x0 + x1 - x3, y0 + y1 - y3);
			}
		});
		prev.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				x3 = e.getX();
				y3 = e.getY();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				panelBg.setVisible(false);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				panelBg.setVisible(true);
			}
			public void mouseClicked(MouseEvent e) {
				if(PlayerGUI.songs.size()!=0) {
					PlayerGUI.prev();
				}
			}
		});
		prev.addMouseMotionListener(new MouseMotionAdapter() {
			

			@Override
			public void mouseDragged(MouseEvent e) {
				x0 = e.getX();
				y0 = e.getY();
				int x1 = frame.getBounds().x;
				int y1 = frame.getBounds().y;
				frame.setLocation(x0 + x1 - x3, y0 + y1 - y3);
			}
		});
		play.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				x3 = e.getX();
				y3 = e.getY();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				panelBg.setVisible(false);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				panelBg.setVisible(true);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				if(play.getToolTipText().equals("play")){
					PlayerGUI.control.play();
					PlayerGUI.playButton.setIcon(new ImageIcon(PlayerGUI.class.getResource("/Client/stop.png")));
					PlayerGUI.playButton.setToolTipText("stop");
					play.setIcon(new ImageIcon(PlayerGUI.class.getResource("/Client/stop.png")));
					play.setToolTipText("stop");
				}else {
					PlayerGUI.control.stop();
					PlayerGUI.playButton.setIcon(new ImageIcon(PlayerGUI.class.getResource("/Client/play.png")));
					PlayerGUI.playButton.setToolTipText("play");
					play.setIcon(new ImageIcon(PlayerGUI.class.getResource("/Client/play.png")));
					play.setToolTipText("play");
				}
			}
			
		});
		play.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				x0 = e.getX();
				y0 = e.getY();
				int x1 = frame.getBounds().x;
				int y1 = frame.getBounds().y;
				frame.setLocation(x0 + x1 - x3, y0 + y1 - y3);
			}
		});
		next.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				x3 = e.getX();
				y3 = e.getY();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				panelBg.setVisible(false);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				panelBg.setVisible(true);
			}
			public void mouseClicked(MouseEvent e) {
				if(PlayerGUI.songs.size()!=0) {
					PlayerGUI.next();
				}
			}
		});
		next.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				x0 = e.getX();
				y0 = e.getY();
				int x1 = frame.getBounds().x;
				int y1 = frame.getBounds().y;
				frame.setLocation(x0 + x1 - x3, y0 + y1 - y3);
			}
		});
	}
}

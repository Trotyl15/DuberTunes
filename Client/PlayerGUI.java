package Client;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

/**
 * PlayerGUI.java
 * @version 1.0
 * @author Trotyl
 * @since 2022 Jan.11
 * The class the contains the main function, a music player that supports .wav
 */
public class PlayerGUI {
	//variables
    public static JFrame frmDubertunes;
    public static JButton playButton;
    public static JLabel cover;
    public static DefaultListModel songs;
    public static AudioControl control = new AudioControl();
    public static JProgressBar progressBar;
    public static int songIndex;
    public static JLabel songInfo;
    public static ConnectGUI connect;
    private static JMenuItem mntmNewMenuItem_1;
    private static JList<String> list;
    private static SyncGUI sync;
    private final Color defColor = new Color(111, 229, 229);
    private File[] songFile;
    private int x3, y3;
    private int x0, y0;


    /**
     * Create the application.
     */
    public PlayerGUI() {
        initialize();
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    UIManager.put("ToolTip.background", Color.WHITE);
                    PlayerGUI window = new PlayerGUI();
                    frmDubertunes.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
	//select next song
    public static void next() {
        songIndex = (songIndex + 1) % songs.size();
        control.selectMusic(((Music) songs.getElementAt(songIndex)).getFilePath());
        songInfo.setText(((Music) songs.getElementAt(songIndex)).getName());
        MiniGUI.songLabel.setText(((Music) songs.getElementAt(songIndex)).getName());
    }
	//select previous song
    public static void prev() {
        songIndex = (songIndex - 1 + songs.size()) % songs.size();
        control.selectMusic(((Music) songs.getElementAt(songIndex)).getFilePath());
        songInfo.setText(((Music) songs.getElementAt(songIndex)).getName());
        MiniGUI.songLabel.setText(((Music) songs.getElementAt(songIndex)).getName());
    }
	//pop op menu
    private static void addPopup(Component component, final JPopupMenu popup) {
        component.addMouseListener(new MouseAdapter() {
            private void showMenu(MouseEvent e) {
                popup.show(e.getComponent(), e.getX(), e.getY());
                mntmNewMenuItem_1.setText("(" + list.getSelectedValuesList().size() + ") items selected");
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    showMenu(e);
                } else {
                    if (e.getClickCount() == 2) {    //When double click JList
                        control.selectMusic(((Music) songs.getElementAt(list.getSelectedIndex())).getFilePath());
                        songIndex = list.getSelectedIndex();
                        songInfo.setText(((Music) songs.getElementAt(songIndex)).getName());
                        MiniGUI.songLabel.setText(((Music) songs.getElementAt(songIndex)).getName());
                    }

                }
            }
        });
    }

    //start the syncing window
    public static void syncStart() {
        sync.setVisible();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        MiniGUI mini = new MiniGUI();
        frmDubertunes = new JFrame();
        frmDubertunes.getContentPane().setBackground(Color.WHITE);
        frmDubertunes.setIconImage(Toolkit.getDefaultToolkit().getImage(PlayerGUI.class.getResource("/Client/icon.png")));
        frmDubertunes.setResizable(false);
        frmDubertunes.setTitle("DuberTunes");
        frmDubertunes.setBounds(100, 100, 300, 471);
        frmDubertunes.setLocationRelativeTo(null);
        frmDubertunes.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
        JPanel overAllPanel = new JPanel();
        overAllPanel.setBackground(Color.WHITE);
        frmDubertunes.getContentPane().add(overAllPanel);
        overAllPanel.setLayout(null);
        JPanel ImgPanel = new JPanel();
        ImgPanel.setBackground(Color.WHITE);
        JLabel nameLabel = new JLabel("DuberTunes");
        nameLabel.setForeground(Color.PINK);
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 35));
        nameLabel.setBounds(8, 0, 236, 55);
        ImgPanel.add(nameLabel);
        nameLabel.setVisible(false);
        //when drag the image panel, the frame follows the cursor
        ImgPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                x0 = e.getX();
                y0 = e.getY();
                int x1 = frmDubertunes.getBounds().x;
                int y1 = frmDubertunes.getBounds().y;
                frmDubertunes.setLocation(x0 + x1 - x3, y0 + y1 - y3);
            }
        });
        ImgPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x3 = e.getX();
                y3 = e.getY();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                nameLabel.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                nameLabel.setVisible(false);
            }
        });

        ImgPanel.setBounds(0, 0, 286, 286);
        overAllPanel.add(ImgPanel);
        ImgPanel.setLayout(null);
        cover = new JLabel("New label");
        cover.setName("");
        cover.setHorizontalAlignment(SwingConstants.CENTER);
        //the default cover
        cover.setIcon(new ImageIcon(PlayerGUI.class.getResource("/Client/icon2.png")));
        cover.setBounds(0, 0, 286, 286);
        ImgPanel.add(cover);
        JPanel objectsPanel = new JPanel();
        objectsPanel.setBackground(Color.WHITE);
        objectsPanel.setBounds(0, 286, 286, 127);
        overAllPanel.add(objectsPanel);
        objectsPanel.setLayout(new GridLayout(0, 1, 0, 0));
        JPanel progressPanel = new JPanel();
        progressPanel.setBackground(Color.WHITE);
        objectsPanel.add(progressPanel);
        progressPanel.setLayout(new GridLayout(0, 1, 0, 0));
        //the progress bar for music
        progressBar = new JProgressBar();
        progressBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (ProgressBar.clipTimeLength != 0) {
                    ProgressBar.clipTimeCur = PlayerGUI.progressBar.getValue() * ProgressBar.clipTimeLength / 100;
                    control.clip.setMicrosecondPosition(ProgressBar.clipTimeCur);
                }
                ProgressBar.isHold = false;
            }
        });
        progressBar.setBackground(Color.WHITE);
        progressBar.setStringPainted(true);
        progressBar.setForeground(defColor);
        progressPanel.add(progressBar);
        //song name
        JPanel songInfoPanel = new JPanel();
        songInfoPanel.setBackground(Color.WHITE);
        objectsPanel.add(songInfoPanel);
        songInfoPanel.setLayout(new GridLayout(0, 1, 0, 0));
        songInfo = new JLabel("no song selected");
        songInfo.setFont(new Font("Tahoma", Font.PLAIN, 14));
        songInfoPanel.add(songInfo);
        //operations are like play/stop, previous/next song
        JPanel operationsPanel = new JPanel();
        operationsPanel.setBackground(Color.WHITE);
        objectsPanel.add(operationsPanel);
        operationsPanel.setLayout(new GridLayout(1, 0, 0, 0));
        JLabel miniButton = new JLabel("");
        miniButton.setName("miniButton");
        miniButton.setToolTipText("minimize");
        miniButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        miniButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frmDubertunes.setVisible(false);
                MiniGUI.frame.setVisible(true);
            }
        });
        miniButton.setHorizontalAlignment(SwingConstants.CENTER);
        miniButton.setIcon(new ImageIcon("C:\\Users\\amyhu\\Desktop\\mini.png"));
        miniButton.setBackground(Color.WHITE);
        operationsPanel.add(miniButton);
        JButton prevButton = new JButton();
        prevButton.setIcon(new ImageIcon(PlayerGUI.class.getResource("/Client/prev.png")));
        prevButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (songs.size() != 0) {
                    prev();
                }
            }
        });
        prevButton.setFocusPainted(false);
        prevButton.setFocusPainted(false);
        prevButton.setFocusPainted(false);
        prevButton.setMargin(new Insets(0, 0, 0, 0));
        prevButton.setIconTextGap(0);
        prevButton.setBorderPainted(false);
        prevButton.setBorder(null);
        prevButton.setToolTipText("previous");
        prevButton.setContentAreaFilled(false);
        prevButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        operationsPanel.add(prevButton);
        playButton = new JButton("");
        playButton.setIcon(new ImageIcon(PlayerGUI.class.getResource("/Client/stop.png")));
        playButton.setFocusPainted(false);
        playButton.setMargin(new Insets(0, 0, 0, 0));
        playButton.setIconTextGap(0);
        playButton.setBorderPainted(false);
        playButton.setBorder(null);
        playButton.setContentAreaFilled(false);
        playButton.setToolTipText("stop");
        playButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (playButton.getToolTipText().equals("play")) {
                    control.play();
                    playButton.setIcon(new ImageIcon(PlayerGUI.class.getResource("/Client/stop.png")));
                    MiniGUI.play.setIcon(new ImageIcon(PlayerGUI.class.getResource("/Client/stop.png")));
                    MiniGUI.play.setToolTipText("stop");
                    playButton.setToolTipText("stop");
                } else {
                	if(!songInfo.getText().equals("no song selected")) {
						control.stop();
                        MiniGUI.play.setIcon(new ImageIcon(PlayerGUI.class.getResource("/Client/play.png")));
                        MiniGUI.play.setToolTipText("play");
						playButton.setIcon(new ImageIcon(PlayerGUI.class.getResource("/Client/play.png")));
						playButton.setToolTipText("play");
					}
                }
            }
        });
        operationsPanel.add(playButton);

        JButton nextButton = new JButton();
        nextButton.setIcon(new ImageIcon(PlayerGUI.class.getResource("/Client/next.png")));
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (songs.size() != 0) {
                    next();
                }
            }
        });
        nextButton.setFocusPainted(false);
        nextButton.setFocusPainted(false);
        nextButton.setMargin(new Insets(0, 0, 0, 0));
        nextButton.setIconTextGap(0);
        nextButton.setBorderPainted(false);
        nextButton.setBorder(null);
        nextButton.setToolTipText("next");
        nextButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        nextButton.setContentAreaFilled(false);
        operationsPanel.add(nextButton);
        JButton listButton = new JButton();
        listButton.setIcon(new ImageIcon(PlayerGUI.class.getResource("/Client/list.png")));
        listButton.setFocusPainted(false);
        listButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        listButton.setFocusPainted(false);
        listButton.setFocusPainted(false);
        listButton.setMargin(new Insets(0, 0, 0, 0));
        listButton.setIconTextGap(0);
        listButton.setBorderPainted(false);
        listButton.setBorder(null);
        listButton.setContentAreaFilled(false);
        listButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (frmDubertunes.getWidth() == 300) {
                    frmDubertunes.setSize(500, 471);
                } else {
                    frmDubertunes.setSize(300, 471);
                }
            }
        });
        operationsPanel.add(listButton);
        JPanel eastPanel = new JPanel();
        eastPanel.setBackground(Color.WHITE);
        eastPanel.setBounds(286, 0, 199, 413);
        overAllPanel.add(eastPanel);
        songs = new DefaultListModel<Music>();
        JScrollPane jsp = new JScrollPane();
        jsp.setBorder(null);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        eastPanel.setLayout(new BorderLayout(0, 0));
        jsp.getVerticalScrollBar().setUnitIncrement(20);
        jsp.getHorizontalScrollBar().setUnitIncrement(20);
        JPanel items = new JPanel();
        items.setLayout(new BorderLayout(0, 0));
        JPanel listOperPanel = new JPanel();
        items.add(listOperPanel, BorderLayout.SOUTH);
        listOperPanel.setLayout(new GridLayout(0, 2, 0, 0));
        JComboBox wayOfList = new JComboBox();
        wayOfList.setFocusable(false);
        listOperPanel.add(wayOfList);
        wayOfList.addItem("by date");
        wayOfList.addItem("by name");
        JButton turn = new JButton("filp (+)");
        turn.setFocusPainted(false);
        listOperPanel.add(turn);
        JComboBox albumList = new JComboBox();
        albumList.setFocusable(false);
        JButton deleteAlbum = new JButton("delete album");
        deleteAlbum.setFocusPainted(false);
        deleteAlbum.setForeground(Color.RED);
        deleteAlbum.setBackground(Color.RED);
        JPanel choosePanel = new JPanel();
        items.add(choosePanel, BorderLayout.CENTER);
        choosePanel.setLayout(new BorderLayout(0, 0));
        eastPanel.add(items, BorderLayout.NORTH);
        JPanel listPanel = new JPanel();
        jsp.setViewportView(listPanel);
        listPanel.setLayout(new BorderLayout(0, 0));
        list = new JList<String>(songs);
        list.setFocusable(false);
        listPanel.add(list, BorderLayout.NORTH);
        list.setSelectionBackground(defColor);
        list.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));
        JPopupMenu popupMenu = new JPopupMenu();
        addPopup(list, popupMenu);

        mntmNewMenuItem_1 = new JMenuItem("12");
        popupMenu.add(mntmNewMenuItem_1);
        JMenuItem deleteSong = new JMenuItem("Delete");
        deleteSong.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[] temp = list.getSelectedIndices();
                //remove from the tail of the array because if remove from front, the later element will replace the removed index
                for (int i = temp.length - 1; i >= 0; i--) {
                    int index = temp[i];
                     ((Album) albumList.getSelectedItem()).getAlbumByName().remove(songs.elementAt(index));
                    songs.remove(index);
                }
            }
        });
        deleteSong.setForeground(Color.RED);
        popupMenu.add(deleteSong);
        JPanel blankPanel = new JPanel();
        blankPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                list.clearSelection();
            }
        });
        blankPanel.setBackground(Color.WHITE);
        listPanel.add(blankPanel, BorderLayout.CENTER);
        JTextField txtSongPath = new JTextField();
        choosePanel.add(txtSongPath);
        txtSongPath.setEnabled(false);
        txtSongPath.setEditable(false);
        txtSongPath.setText("choose music file(s)");
        txtSongPath.setColumns(10);
        JButton btnNewButton = new JButton("add");
        btnNewButton.setFocusPainted(false);
        choosePanel.add(btnNewButton, BorderLayout.EAST);
        JPanel scrollPanel = new JPanel();
        scrollPanel.setLayout(new BorderLayout(0, 0));
        scrollPanel.add(jsp);
        eastPanel.add(scrollPanel);
        Album all = new Album("All");
        albumList.addItem(all);
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setDialogTitle("choose file");
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("Music File (*.wav)", "wav");
                    chooser.setFileFilter(filter);
                    chooser.setMultiSelectionEnabled(true);
                    chooser.showOpenDialog(null);
                    songFile = chooser.getSelectedFiles();
                    for (File file : songFile) {
                        Music music = new Music();
                        music.setFilePath(file.getPath());
                        music.setName(file.getName().substring(0, file.getName().length() - 4));
                        all.addSong(music);
                    }
                    refreshList(all, true, true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        turn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (turn.getText().equals("filp (+)")) {
                    refreshList((Album) albumList.getSelectedItem(), wayOfList.getSelectedIndex() == 0, false);
                    turn.setText("filp (-)");
                } else if (turn.getText().equals("filp (-)")) {
                    refreshList((Album) albumList.getSelectedItem(), wayOfList.getSelectedIndex() == 0, true);
                    turn.setText("filp (+)");
                }
            }
        });
        wayOfList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshList((Album) albumList.getSelectedItem(), wayOfList.getSelectedIndex() == 0, turn.getText() == "filp (+)");
            }
        });
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBorderPainted(false);
        frmDubertunes.setJMenuBar(menuBar);
        JMenu mnM = new JMenu("options");
        menuBar.add(mnM);
        JMenuItem mntmNewMenuItem = new JMenuItem("sync");
        mntmNewMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sync = new SyncGUI(songs);
                connect = new ConnectGUI();
            }
        });
        mnM.add(mntmNewMenuItem);
        Runnable progress = new ProgressBar(control);
        Thread t = new Thread(progress);
        t.start();
        frmDubertunes.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                ProgressBar.run = false;
                if (connect != null) {
                    connect.close();
                }
                System.exit(0);
            }
        });

        progressBar.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                ProgressBar.isHold = true;
                if (ProgressBar.clipTimeLength != 0) {
                    int progress = (int) (e.getX() / 286.0 * 100);
                    PlayerGUI.progressBar.setValue(progress);
                }
            }
        });
    }

    protected void refreshList(Album album, boolean isByDate, boolean poOrder) {
        songs.clear();
        if (isByDate) {
            ArrayList<Music> temp = album.getAlbumByDate();
            if (poOrder) {
                for (int i = temp.size() - 1; i >= 0; i--) {
                    songs.addElement(temp.get(i));
                }
            } else {

                for (Music m : temp) {
                    songs.addElement(m);
                }
            }
        } else {
            ArrayList<Music> temp = album.getAlbumByName();
            if (poOrder) {
                for (int i = temp.size() - 1; i >= 0; i--) {
                    songs.addElement(temp.get(i));
                }
            } else {
                for (Music m : temp) {
                    songs.addElement(m);
                }
            }
        }

    }
}

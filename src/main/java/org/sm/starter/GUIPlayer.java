package org.sm.starter;

import org.sm.math.RangeMapper;
import org.sm.sound.FYMSong;
import org.sm.sound.PausableSongProcessor;
import org.sm.sound.Song;
import org.sm.sound.SongProcessorThread;
import org.sm.ui.SongAnimator;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUIPlayer {

    private static final String OPEN_FOLDER = "OpenFolderIcon";
    private static final String PLAY = "PlayTransparent";
    private static final String PAUSE = "PauseTransparent";
    private static final String FF = "FFTransparent";
    private static final String BB = "BBTransparent";
    private static final String[] ICON_NAMES = {OPEN_FOLDER, PLAY, PAUSE, FF, BB};
    private static final String[] COLUMN_NAMES = {"File", "Author", "Track"};
    private static final int ABS_FILE_LOCATION_INDEX = 3;
    private static final int SLIDER_SCALE = 1000;

    private Map<String, ImageIcon> icons;

    private PausableSongProcessor songProcessor;
    private SongProcessorThread songProcessorThread;

    private RangeMapper sliderMapper;
    private RangeMapper reverseSliderMapper;

    private Song song;

    private boolean sliderMouseDown = false;
    private boolean onPlay = true;

    private JFrame mainFrame;
    private JButton openDirectory;
    private JTextField directoryPath;
    private JSlider songSlider;
    private JLabel playedDurationInfo;
    private JButton playPauseButton;
    private JButton prevSongButton;
    private JButton nextSongButton;

    private JTable filesTable;
    private List<List<Object>> filesTableData;
    private int songIndex = -1;

    private JFileChooser folderChooser;

    public GUIPlayer() {
        loadIcons();
        initSongProcessor();
        initGUI();
    }

    private void initSongProcessor() {
        songProcessorThread = new SongProcessorThread();
        songProcessor = songProcessorThread.getSongProcessor();
        songProcessorThread.freeze();
        Thread realSongProcessorThread = new Thread(songProcessorThread);
        realSongProcessorThread.start();
    }

    private void initGUI() {
        mainFrame = new JFrame();
        folderChooser = new JFileChooser();
        folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        folderChooser.setCurrentDirectory(new File("./"));
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainFrame.add(mainPanel);
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0;
        c.weighty = 0.5;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        JPanel topSpring = new JPanel();
        topSpring.setBackground(Color.DARK_GRAY);
        mainPanel.add(topSpring, c);

        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        openDirectory = new JButton();
        openDirectory.setIcon(icons.get(OPEN_FOLDER));
        openDirectory.addActionListener(this::onOpenDirectory);
        mainPanel.add(openDirectory, c);

        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.BOTH;
        directoryPath = new JTextField();
        Font font = new Font("SansSerif", Font.BOLD, 32);
        directoryPath.setFont(font);
//        directoryPath.setColumns(20);
        directoryPath.addActionListener(this::onTextActionPerformed);
        mainPanel.add(directoryPath,c);

        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 2;
        //c.fill = GridBagConstraints.BOTH;
        songProcessor.setFrameNotifier(this::onSongFrameChanged);
        mainPanel.add(new SongAnimator(800, 600, songProcessor), c);

        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        JPanel songControls = new JPanel();
        songControls.setLayout(new GridBagLayout());
        mainPanel.add(songControls,c);

        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0;
        c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        songSlider = new JSlider(0, SLIDER_SCALE);
        songSlider.setValue(0);
        songSlider.addMouseListener(new SliderMouseListener());
        songSlider.addChangeListener(this::sliderChanged);
        songControls.add(songSlider, c);

        c = new GridBagConstraints();
        c.gridx = 1; c.gridy = 0;
        playedDurationInfo = new JLabel("0:00/0:00");
        songControls.add(playedDurationInfo, c);

        c = new GridBagConstraints();
        c.gridx = 2; c.gridy = 0;
        prevSongButton = new JButton();
        prevSongButton.setIcon(icons.get(BB));
        prevSongButton.addActionListener(this::onPrevSongClicked);
        songControls.add(prevSongButton, c);

        c = new GridBagConstraints();
        c.gridx = 3; c.gridy = 0;
        playPauseButton = new JButton();
        playPauseButton.setIcon(icons.get(PLAY));
        playPauseButton.addActionListener(this::onPlayButtonClick);
        songControls.add(playPauseButton, c);

        c = new GridBagConstraints();
        c.gridx = 4; c.gridy = 0;
        nextSongButton = new JButton();
        nextSongButton.setIcon(icons.get(FF));
        nextSongButton.addActionListener(this::onNextSongClicked);
        songControls.add(nextSongButton, c);

        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 2;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        Object[][] data = {};
        filesTable = new JTable(data, COLUMN_NAMES);
        filesTable.addMouseListener(new TableMouseAdapter());
        JScrollPane scrollPane = new JScrollPane(filesTable);
        filesTable.setFillsViewportHeight(true);
        scrollPane.setPreferredSize(new Dimension(250, 100));
        mainPanel.add(scrollPane, c);

        c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 1000;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 2;
        JPanel bottomSpring = new JPanel();
        bottomSpring.setBackground(Color.DARK_GRAY);
        mainPanel.add(bottomSpring, c);

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
        mainFrame.setMinimumSize(mainFrame.getSize());
    }

    private void loadIcons() {
        icons = new HashMap<>();
        for (String iconName : ICON_NAMES) {
            Image img;
            String fullIconName = "/" + iconName + ".png";
            try {
                img = ImageIO.read(getClass().getResource(fullIconName));
                icons.put(iconName, new ImageIcon(img));
            } catch (IOException ioe) {
                throw new RuntimeException("Cannot load icon: " + fullIconName);
            }
        }
    }

    private void sliderChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (!source.getValueIsAdjusting() && sliderMouseDown) {
            int sliderValue = source.getValue();
            // System.out.println(sliderValue + " here?");
            if (reverseSliderMapper != null) {
                int frame = (int)reverseSliderMapper.map(sliderValue);
                song.setFrame(frame);
            }
        }
    }

    private void onTextActionPerformed(ActionEvent e) {
        JTextField source = (JTextField) e.getSource();
        String songLocationPath = source.getText();
        File songLocation = new File(songLocationPath);
        if (songLocation.isDirectory()) {
            loadSelectedDirectory(songLocation);
        }
    }

    private void loadSong(String songLocation) {
        song = new FYMSong(songLocation);
        songProcessor.setSong(song);
        sliderMapper = new RangeMapper(0, song.getFrameCount(), 0, SLIDER_SCALE);
        reverseSliderMapper = new RangeMapper(0, SLIDER_SCALE, 0, song.getFrameCount());
        if (onPlay) {
            playPauseButton.setIcon(icons.get(PAUSE));
            songProcessorThread.unfreeze();
        }
        double songDurationInSeconds = (double)song.getFrameCount() / song.getFrameRate();
        updateDuration(0d, songDurationInSeconds);
    }

    private void updateDuration(double nowSec, double totalSec) {
        String durationInfo = convertSeconds(nowSec) + "/" + convertSeconds(totalSec);
        playedDurationInfo.setText(durationInfo);
    }

    private String convertSeconds(double seconds) {
        int floorSeconds = (int)Math.floor(seconds);
        int minutesPart = floorSeconds / 60;
        int secondsPart = floorSeconds % 60;
        String additionalZero = "";
        if (secondsPart < 10) {
            additionalZero = "0";
        }
        return minutesPart + ":" + additionalZero + secondsPart;
    }

    private void onSongFrameChanged(int frameNum) {
        if (!sliderMouseDown) {
            SwingUtilities.invokeLater(() -> {
                songSlider.setValue((int)sliderMapper.map(frameNum));
                int frameCount = song.getFrameCount();
                double songDuration = (double) frameCount / song.getFrameRate();
                double nowTime = RangeMapper.map(frameNum, 0, frameCount, 0, songDuration);
                updateDuration(nowTime, songDuration);
            });
        }
    }

    private void onPlayButtonClick(ActionEvent e) {
        if (song == null) {
            if (filesTableData == null || filesTableData.isEmpty()) return;
            int[] selectedRows = filesTable.getSelectedRows();
            songIndex = 0;
            if (selectedRows != null && selectedRows.length != 0) {
                songIndex = selectedRows[0];
            }
            filesTable.setRowSelectionInterval(songIndex, songIndex);
            String absSongPath = (String)filesTableData.get(songIndex).get(ABS_FILE_LOCATION_INDEX);
            loadSong(absSongPath);
            playPauseButton.setIcon(icons.get(PAUSE));
            songProcessorThread.unfreeze();
            return;
        }
        if (onPlay) {
            playPauseButton.setIcon(icons.get(PLAY));
            songProcessorThread.freeze();
        } else {
            playPauseButton.setIcon(icons.get(PAUSE));
            songProcessorThread.unfreeze();
        }
        onPlay = !onPlay;
    }

    private void onNextSongClicked(ActionEvent e) {
        if (songIndex == -1) return;
        songIndex = (songIndex + 1) % filesTableData.size();
        filesTable.setRowSelectionInterval(songIndex, songIndex);
        loadSong((String)filesTableData.get(songIndex).get(ABS_FILE_LOCATION_INDEX));
    }

    private void onPrevSongClicked(ActionEvent e) {
        if (songIndex == -1) return;
        songIndex--;
        if (songIndex < 0) {
            songIndex = filesTableData.size() - 1;
        }
        filesTable.setRowSelectionInterval(songIndex, songIndex);
        loadSong((String)filesTableData.get(songIndex).get(ABS_FILE_LOCATION_INDEX));
    }

    private void onOpenDirectory(ActionEvent e) {
        int retVal = folderChooser.showOpenDialog(mainFrame);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = folderChooser.getSelectedFile();
            directoryPath.setText(selectedDirectory.getAbsolutePath());
            loadSelectedDirectory(selectedDirectory);
        }
    }

    private void loadSelectedDirectory(File directory) {
        File[] files = directory.listFiles(this::filesToAccept);
        if (files == null || files.length == 0) return;
        filesTableData = new ArrayList<>();
        for (File f : files) {
            List<Object> row = new ArrayList<>();
            row.add(f.getName());
            try {
                Song s = new FYMSong(f.getAbsolutePath(), true);
                row.add(s.getAuthor());
                row.add(s.getTrack());
                row.add(f.getAbsolutePath());
                row.add(s);
                filesTableData.add(row);
            } catch (Exception ignore) {}
        }

        FileListTableModel tableModel = new FileListTableModel(COLUMN_NAMES, 0);
        for (List<Object> row : filesTableData) {
            tableModel.addRow(row.toArray());
        }
        filesTable.setModel(tableModel);
        songIndex = -1;
    }

    private boolean filesToAccept(File f, String name) {
        return name.toLowerCase().endsWith(".fym");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUIPlayer::new);
    }


    private class SliderMouseListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            sliderMouseDown = true;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            sliderMouseDown = false;
        }

    }

    private static class FileListTableModel extends DefaultTableModel {

        public FileListTableModel(Object[] columnNames, int rowCount) {
            super(columnNames, rowCount);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

    private class TableMouseAdapter extends MouseAdapter {
        public void mousePressed(MouseEvent mouseEvent) {
            JTable table =(JTable) mouseEvent.getSource();
            Point point = mouseEvent.getPoint();
            int row = table.rowAtPoint(point);
            if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1 && row != -1) {
                songIndex = row;
                String absFileLocation = (String)filesTableData.get(row).get(ABS_FILE_LOCATION_INDEX);
                loadSong(absFileLocation);
            }
        }
    }
}

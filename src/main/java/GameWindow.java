import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWindow extends JFrame {

    private static final int WINDOW_HEIGHT = 555;
    private static final int WINDOW_WIDTH = 507;
    private static final int WINDOW_POS_X = 800;
    private static final int WINDOW_POS_Y = 300;

    static public final String WINDOW_NAME = "Tic-Tac-Toe";

    JButton buttonStart = new JButton("New Game");
    JButton buttonExit = new JButton("Exit");

    Map map;
    SettingsWindow settings;

    GameWindow() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(WINDOW_POS_X, WINDOW_POS_Y);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setTitle(WINDOW_NAME);
        setResizable(false);

        map = new Map();
        settings = new SettingsWindow(this);

        buttonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.setVisible(true);
            }
        });

        JPanel panelBottom = new JPanel(new GridLayout(1, 2));
        panelBottom.add(buttonStart);
        panelBottom.add(buttonExit);
        add(panelBottom, BorderLayout.SOUTH);

        add(map);
        setVisible(true);
    }

    void startNewGame (boolean mode, int fieldSizeX, int fieldSizeY, int winLength) {
        map.startNewGame(mode, fieldSizeX, fieldSizeY, winLength);

    }


}

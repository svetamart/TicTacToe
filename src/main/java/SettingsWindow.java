import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsWindow extends JFrame {

    private static final int WINDOW_HEIGHT = 230;
    private static final int WINDOW_WIDTH = 350;
    static public int sliderSizeValue = 3;
    static public int sliderWinValue = 3;
    static public boolean modeValue = false;


    JLabel labelMode = new JLabel("Choose game mode");
    JRadioButton humanVshumanButton = new JRadioButton("Player vs Player", true);
    JRadioButton humanVsAI = new JRadioButton("Player vs Computer", false);

    ButtonGroup topButtonGroup = new ButtonGroup();

    JLabel labelSize = new JLabel(String.format("Set field size: %d", sliderSizeValue));
    JSlider sliderSize = new JSlider(3, 5, sliderSizeValue);

    JLabel labelWin = new JLabel(String.format("Set win combo length: %d", sliderWinValue));
    JSlider sliderWin = new JSlider(3, 5, sliderWinValue);

    JPanel grid = new JPanel(new GridLayout(4, 1));

    JPanel top = new JPanel(new GridLayout(2,1));
    JPanel topButton = new JPanel(new GridLayout(2, 1));
    JPanel middle = new JPanel(new GridLayout(2,1));
    JPanel bottom = new JPanel(new GridLayout(2,1));


    JButton buttonStart = new JButton("Start new game");

    SettingsWindow(GameWindow gameWindow ) {
        setTitle("Game Settings");

        setLocationRelativeTo(gameWindow);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        topButtonGroup.add(humanVshumanButton);
        topButtonGroup.add(humanVsAI);
        topButton.add(humanVshumanButton);
        topButton.add(humanVsAI);

        top.add(labelMode);
        top.add(topButton);

        sliderSize.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e){
                sliderSizeValue = sliderSize.getValue();
                labelSize.setText(String.format("Set field size: %d", sliderSizeValue));
            }
        });
        middle.add(labelSize);
        middle.add(sliderSize);

        sliderWin.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e){
                sliderWinValue = sliderWin.getValue();
                labelWin.setText(String.format("Set win combo length: %d", sliderWinValue));
            }
        });
        bottom.add(labelWin);
        bottom.add(sliderWin);

        grid.add(top);
        grid.add(middle);
        grid.add(bottom);
        grid.add(buttonStart);

        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modeValue = humanVshumanButton.isSelected();
                gameWindow.startNewGame(modeValue, sliderSizeValue, sliderSizeValue, sliderWinValue);
                setVisible(false);
            }
        });

        add(grid);

    }
}

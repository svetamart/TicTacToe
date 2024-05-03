import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class Map extends JPanel {
    private static final Random RANDOM = new Random();

    private final Image crossImage;
    private final Image circleImage;

    private boolean isInitialized;
    private int gameOverType;
    private boolean isGameOver;
    private static final int STATE_DRAW = 0;
    private static final int STATE_WIN_HUMAN = 1;
    private static final int STATE_WIN_AI = 2;

    private static final String MSG_WIN_HUMAN = "Победил игрок!";
    private static final String MSG_WIN_AI = "Победил компьютер!";
    private static final String MSG_DRAW = "Ничья!";

    private final int HUMAN_DOT = 1;
    private final int AI_DOT = 2;
    private final int EMPTY_DOT = 0;

    private int fieldSizeX;
    private int fieldSizeY;
    private char[][] field;
    private int winLength;

    private boolean mode = false;


    private int panelWidth;
    private int panelHeight;
    private int cellHeight;
    private int cellWidth;


    Map() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                update(e);
            }
        });
        isInitialized = false;

        crossImage = new ImageIcon("cross.png").getImage();
        circleImage = new ImageIcon("circle.png").getImage();
    }

    private void update (MouseEvent e) {
        if (isGameOver || !isInitialized) return;

        int cellX = e.getX() / cellWidth;
        int cellY = e.getY() / cellHeight;

        if (!isValidCell(cellX, cellY) || !isEmptyCell(cellX, cellY)) return;
        field[cellY][cellX] = HUMAN_DOT;

        if(checkEndGame(HUMAN_DOT, STATE_WIN_HUMAN)) return;
        aiTurn();
        repaint();

        if(checkEndGame(AI_DOT, STATE_WIN_AI)) return;
    }

    private boolean checkEndGame (int dot, int gameOverType) {
        if (checkWin(dot)) {
            this.gameOverType = gameOverType;
            isGameOver = true;
            repaint();
            return true;
        }
        if (isMapFull()) {
            this.gameOverType = STATE_DRAW;
            isGameOver = true;
            repaint();
            return true;
        }
        return false;
    }

    void startNewGame (boolean mode, int fieldSizeX, int fieldSizeY, int winLength) {
        this.fieldSizeY = fieldSizeY;
        this.fieldSizeX = fieldSizeX;
        this.mode = mode;
        this.winLength = winLength;

        initMap(fieldSizeX, fieldSizeY);
        isGameOver = false;
        isInitialized = true;

        repaint();

    }

    @Override
    protected void paintComponent (Graphics g) {
        super.paintComponent(g);
        render(g);
    }

    private void render (Graphics g) {
        if (!isInitialized) return;

        panelWidth = getWidth();
        panelHeight = getHeight();
        cellHeight = panelHeight / fieldSizeY;
        cellWidth = panelWidth / fieldSizeX;

        g.setColor(Color.BLACK);
        for (int h = 0; h < fieldSizeY; h++) {
            int y = h * cellHeight;
            g.drawLine(0, y, panelWidth, y);
        }

        for (int w = 0; w < fieldSizeX; w++) {
            int x = w * cellWidth;
            g.drawLine(x, 0, x, panelHeight);
        }

        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (field[y][x] == EMPTY_DOT) continue;

                int imageSize = (int) (Math.min(cellWidth, cellHeight) * 0.8); // Adjusted size
                int xPos = x * cellWidth + (cellWidth - imageSize) / 2;
                int yPos = y * cellHeight + (cellHeight - imageSize) / 2;

                if (field[y][x] == HUMAN_DOT) {
                    g.drawImage(crossImage, xPos, yPos, imageSize, imageSize, null);
                } else if (field[y][x] == AI_DOT) {
                    g.drawImage(circleImage, xPos, yPos, imageSize, imageSize, null);
                } else {
                    throw new RuntimeException("Unexpected value: " + field[y][x] +
                            " in cell: x = " + x + " y = " + y);
                }
            }
        }

        if (isGameOver) showMessageGameOver(g);

    }

    private void showMessageGameOver (Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 200, getWidth(), 70);
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Times New Roman", Font.BOLD, 48));

        switch (gameOverType) {
            case STATE_DRAW:
                g.drawString(MSG_DRAW, 180, getHeight() / 2);
                break;
            case STATE_WIN_AI:
                g.drawString(MSG_WIN_AI, 20, getHeight() / 2);
                break;
            case STATE_WIN_HUMAN:
                g.drawString(MSG_WIN_HUMAN, 70, getHeight() / 2);
                break;
            default:
                throw new RuntimeException("Unexpected gameOver state: " + gameOverType);
        }
    }

    private void initMap (int fieldSizeX, int fieldSizeY) {
        field = new char[fieldSizeY][fieldSizeX];
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                field[i][j] = EMPTY_DOT;
            }
        }
    }

    private boolean isValidCell (int x, int y) {
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    private boolean isEmptyCell (int x, int y) {
        return field[y][x] == EMPTY_DOT;
    }

    private void aiTurn() {
        if (makeWinningMove()) {
            return;
        }
        if (makeBlockingMove()) {
            return;
        }
        makeRandomMove();
    }

    private boolean makeWinningMove() {
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (isEmptyCell(j, i)) {
                    field[i][j] = AI_DOT;
                    if (checkWin(AI_DOT)) {
                        return true;
                    }
                    field[i][j] = EMPTY_DOT;
                }
            }
        }
        return false;
    }
    private boolean makeBlockingMove() {
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (isEmptyCell(j, i)) {
                    field[i][j] = HUMAN_DOT;
                    if (checkWin(HUMAN_DOT)) {
                        field[i][j] = AI_DOT;
                        return true;
                    }
                    field[i][j] = EMPTY_DOT;
                }
            }
        }
        return false;
    }
    private void makeRandomMove() {
        int x, y;
        do {
            x = RANDOM.nextInt(fieldSizeX);
            y = RANDOM.nextInt(fieldSizeY);
        } while (!isEmptyCell(x, y));
        field[y][x] = AI_DOT;
    }


    private boolean isMapFull() {
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (field[i][j] == EMPTY_DOT) return false;
            }
        }
        return true;
    }

    private boolean checkWin(int c) {
        for (int i = 0; i < fieldSizeY; i++) {
            if (checkRow(i, c) || checkColumn(i, c)) {
                return true;
            }
        }
        return checkDiagonal(c) || checkAntiDiagonal(c);
    }

    private boolean checkRow(int row, int c) {
        for (int j = 0; j <= fieldSizeX - winLength; j++) {
            boolean win = true;
            for (int k = 0; k < winLength; k++) {
                if (field[row][j + k] != c) {
                    win = false;
                    break;
                }
            }
            if (win) {
                return true;
            }
        }
        return false;
    }

    private boolean checkColumn(int col, int c) {
        for (int i = 0; i <= fieldSizeY - winLength; i++) {
            boolean win = true;
            for (int k = 0; k < winLength; k++) {
                if (field[i + k][col] != c) {
                    win = false;
                    break;
                }
            }
            if (win) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDiagonal(int c) {
        for (int i = 0; i <= fieldSizeY - winLength; i++) {
            for (int j = 0; j <= fieldSizeX - winLength; j++) {
                boolean win = true;
                for (int k = 0; k < winLength; k++) {
                    if (field[i + k][j + k] != c) {
                        win = false;
                        break;
                    }
                }
                if (win) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkAntiDiagonal(int c) {
        for (int i = 0; i <= fieldSizeY - winLength; i++) {
            for (int j = winLength - 1; j < fieldSizeX; j++) {
                boolean win = true;
                for (int k = 0; k < winLength; k++) {
                    if (field[i + k][j - k] != c) {
                        win = false;
                        break;
                    }
                }
                if (win) {
                    return true;
                }
            }
        }
        return false;
    }
}

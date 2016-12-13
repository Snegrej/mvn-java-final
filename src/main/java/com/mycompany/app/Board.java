package com.mycompany.app;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Board extends JPanel {


    private int blockDimensions = 15;

    private int hiddenBlock = 10;
    private int flaggedBlock = 11;
    private int emptyBlock = 0;
    private int mineBlock = 9;
    private int hiddenMineBlock = mineBlock + hiddenBlock;
    private int flaggedMineBlock = hiddenMineBlock + flaggedBlock;
    private int incorrectFlag = 12;

    private int numMines = 40;
    private int numRows = 16;
    private int numColumns = 16;

    private int[] grid;
    private boolean playing;
    private int minesLeft;
    private Image[] img;

    private int numBlocks;
    private JLabel infoPanel;


    public Board(JLabel infoPanel) {

        this.infoPanel = infoPanel;

        img = new Image[13];

        for (int i = 0; i < 13; i++) {
            img[i] = (new ImageIcon(i + ".png")).getImage();
        }

        setDoubleBuffered(true);

        addMouseListener(new MinesAdapter());
        newGame();
    }


    private void newGame() {

        Random random;
        int current_col;

        int i = 0;
        int position = 0;
        int cell = 0;

        random = new Random();
        playing = true;
        minesLeft = numMines;

        numBlocks = numRows * numColumns;
        grid = new int[numBlocks];

        for (i = 0; i < numBlocks; i++)
            grid[i] = hiddenBlock;

        infoPanel.setText(Integer.toString(minesLeft));


        i = 0;
        while (i < numMines) {

            position = (int) (numBlocks * random.nextDouble());

            if ((position < numBlocks) &&
                (grid[position] != hiddenMineBlock)) {


                current_col = position % numColumns;
                grid[position] = hiddenMineBlock;
                i++;

                if (current_col > 0) {
                    cell = position - 1 - numColumns;
                    if (cell >= 0)
                        if (grid[cell] != hiddenMineBlock)
                            grid[cell] += 1;
                    cell = position - 1;
                    if (cell >= 0)
                        if (grid[cell] != hiddenMineBlock)
                            grid[cell] += 1;

                    cell = position + numColumns - 1;
                    if (cell < numBlocks)
                        if (grid[cell] != hiddenMineBlock)
                            grid[cell] += 1;
                }

                cell = position - numColumns;
                if (cell >= 0)
                    if (grid[cell] != hiddenMineBlock)
                        grid[cell] += 1;
                cell = position + numColumns;
                if (cell < numBlocks)
                    if (grid[cell] != hiddenMineBlock)
                        grid[cell] += 1;

                if (current_col < (numColumns - 1)) {
                    cell = position - numColumns + 1;
                    if (cell >= 0)
                        if (grid[cell] != hiddenMineBlock)
                            grid[cell] += 1;
                    cell = position + numColumns + 1;
                    if (cell < numBlocks)
                        if (grid[cell] != hiddenMineBlock)
                            grid[cell] += 1;
                    cell = position + 1;
                    if (cell < numBlocks)
                        if (grid[cell] != hiddenMineBlock)
                            grid[cell] += 1;
                }
            }
        }
    }


    public void find_emptyBlocks(int j) {

        int current_col = j % numColumns;
        int cell;

        if (current_col > 0) {
            cell = j - numColumns - 1;
            if (cell >= 0)
                if (grid[cell] > mineBlock) {
                    grid[cell] -= hiddenBlock;
                    if (grid[cell] == emptyBlock)
                        find_emptyBlocks(cell);
                }

            cell = j - 1;
            if (cell >= 0)
                if (grid[cell] > mineBlock) {
                    grid[cell] -= hiddenBlock;
                    if (grid[cell] == emptyBlock)
                        find_emptyBlocks(cell);
                }

            cell = j + numColumns - 1;
            if (cell < numBlocks)
                if (grid[cell] > mineBlock) {
                    grid[cell] -= hiddenBlock;
                    if (grid[cell] == emptyBlock)
                        find_emptyBlocks(cell);
                }
        }

        cell = j - numColumns;
        if (cell >= 0)
            if (grid[cell] > mineBlock) {
                grid[cell] -= hiddenBlock;
                if (grid[cell] == emptyBlock)
                    find_emptyBlocks(cell);
            }

        cell = j + numColumns;
        if (cell < numBlocks)
            if (grid[cell] > mineBlock) {
                grid[cell] -= hiddenBlock;
                if (grid[cell] == emptyBlock)
                    find_emptyBlocks(cell);
            }

        if (current_col < (numColumns - 1)) {
            cell = j - numColumns + 1;
            if (cell >= 0)
                if (grid[cell] > mineBlock) {
                    grid[cell] -= hiddenBlock;
                    if (grid[cell] == emptyBlock)
                        find_emptyBlocks(cell);
                }

            cell = j + numColumns + 1;
            if (cell < numBlocks)
                if (grid[cell] > mineBlock) {
                    grid[cell] -= hiddenBlock;
                    if (grid[cell] == emptyBlock)
                        find_emptyBlocks(cell);
                }

            cell = j + 1;
            if (cell < numBlocks)
                if (grid[cell] > mineBlock) {
                    grid[cell] -= hiddenBlock;
                    if (grid[cell] == emptyBlock)
                        find_emptyBlocks(cell);
                }
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        int cell = 0;
        int uncover = 0;

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {

                cell = grid[(i * numColumns) + j];

                if (playing && cell == mineBlock)
                    playing = false;

                if (!playing) {
                    if (cell == hiddenMineBlock) {
                        cell = mineBlock;
                    } else if (cell == flaggedMineBlock) {
                        cell = flaggedBlock;
                    } else if (cell > hiddenMineBlock) {
                        cell = incorrectFlag;
                    } else if (cell > mineBlock) {
                        cell = hiddenBlock;
                    }


                } else {
                    if (cell > hiddenMineBlock)
                        cell = flaggedBlock;
                    else if (cell > mineBlock) {
                        cell = hiddenBlock;
                        uncover++;
                    }
                }

                g.drawImage(img[cell], (j * blockDimensions),
                    (i * blockDimensions), this);
            }
        }

        if (uncover == 0 && playing) {
            playing = false;
            infoPanel.setText("Game won");
        } else if (!playing)
            infoPanel.setText("Game lost");
    }


    class MinesAdapter extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {

            int x = e.getX();
            int y = e.getY();

            int cCol = x / blockDimensions;
            int cRow = y / blockDimensions;

            boolean rep = false;


            if (!playing) {
                newGame();
                repaint();
            }


            if ((x < numColumns * blockDimensions) && (y < numRows * blockDimensions)) {

                if (e.getButton() == MouseEvent.BUTTON3) {

                    if (grid[(cRow * numColumns) + cCol] > mineBlock) {
                        rep = true;

                        if (grid[(cRow * numColumns) + cCol] <= hiddenMineBlock) {
                            if (minesLeft > 0) {
                                grid[(cRow * numColumns) + cCol] += flaggedBlock;
                                minesLeft--;
                                infoPanel.setText(Integer.toString(minesLeft));
                            } else
                                infoPanel.setText("No marks left");
                        } else {

                            grid[(cRow * numColumns) + cCol] -= flaggedBlock;
                            minesLeft++;
                            infoPanel.setText(Integer.toString(minesLeft));
                        }
                    }

                } else {

                    if (grid[(cRow * numColumns) + cCol] > hiddenMineBlock) {
                        return;
                    }

                    if ((grid[(cRow * numColumns) + cCol] > mineBlock) &&
                        (grid[(cRow * numColumns) + cCol] < flaggedMineBlock)) {

                        grid[(cRow * numColumns) + cCol] -= hiddenBlock;
                        rep = true;

                        if (grid[(cRow * numColumns) + cCol] == mineBlock)
                            playing = false;
                        if (grid[(cRow * numColumns) + cCol] == emptyBlock)
                            find_emptyBlocks((cRow * numColumns) + cCol);
                    }
                }

                if (rep)
                    repaint();

            }
        }
    }
}

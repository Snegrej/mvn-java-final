package com.mycompany.app;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Minefield extends JPanel {


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
    private Image[]  img;

    private int numBlocks;
    private JLabel infoPanel;


    public Minefield(JLabel infoPanel) {
        this.infoPanel = infoPanel;
        img = new Image[13];

        for (int i = 0; i < 13; i++) {
            img[i] = (new ImageIcon("src/main/resources/imgFiles/" + i + ".png")).getImage();
        }


        addMouseListener(new MinesAdapter());
        newGame();
    }


    private void newGame() {

        Random random;
        int column;

        int i = 0;
        int position = 0;
        int block = 0;

        random = new Random();
        playing = true;
        minesLeft = numMines;

        numBlocks = numRows * numColumns;
        grid = new int[numBlocks];

        for (i = 0; i < numBlocks; i++)
            grid[i] = hiddenBlock;

        infoPanel.setText(Integer.toString(minesLeft));


        i = 0;
        //@object random, generates mines
        while (i < numMines) {

            position = (int) (numBlocks * random.nextDouble());

            if ((position < numBlocks) &&
                (grid[position] != hiddenMineBlock)) {
                column = position % numColumns;
                grid[position] = hiddenMineBlock;
                i++;

                if (column > 0) {
                    block = position - 1 - numColumns;
                    if (block >= 0)
                        if (grid[block] != hiddenMineBlock)
                            grid[block] += 1;
                    block = position - 1;
                    if (block >= 0)
                        if (grid[block] != hiddenMineBlock)
                            grid[block] += 1;

                    block = position + numColumns - 1;
                    if (block < numBlocks)
                        if (grid[block] != hiddenMineBlock)
                            grid[block] += 1;
                }

                block = position - numColumns;
                if (block >= 0)
                    if (grid[block] != hiddenMineBlock)
                        grid[block] += 1;
                block = position + numColumns;
                if (block < numBlocks)
                    if (grid[block] != hiddenMineBlock)
                        grid[block] += 1;

                if (column < (numColumns - 1)) {
                    block = position - numColumns + 1;
                    if (block >= 0)
                        if (grid[block] != hiddenMineBlock)
                            grid[block] += 1;
                    block = position + numColumns + 1;
                    if (block < numBlocks)
                        if (grid[block] != hiddenMineBlock)
                            grid[block] += 1;
                    block = position + 1;
                    if (block < numBlocks)
                        if (grid[block] != hiddenMineBlock)
                            grid[block] += 1;
                }
            }
        }
    }

    //@functionCall recurShowEmpty
    //For each adjacent block, if not hiddenMineBlock
    //change value to respective emptyBlock(black value - 10) (0-8)
    //Then calls itself with new block
    public void recurShowEmpty(int a) {

        int column = a % numColumns;
        int block;

        if (column > 0) {

            //top left block
            block = a - numColumns - 1;
            if (block >= 0)
                if (grid[block] > mineBlock) {
                    grid[block] -= hiddenBlock;
                    if (grid[block] == emptyBlock)
                        recurShowEmpty(block);
                }

            //left middle block
            block = a - 1;
            if (block >= 0)
                if (grid[block] > mineBlock) {
                    grid[block] -= hiddenBlock;
                    if (grid[block] == emptyBlock)
                        recurShowEmpty(block);
                }

            //bottom left block
            block = a + numColumns - 1;
            if (block < numBlocks)
                if (grid[block] > mineBlock) {
                    grid[block] -= hiddenBlock;
                    if (grid[block] == emptyBlock)
                        recurShowEmpty(block);
                }
        }

        //top middle block
        block = a - numColumns;
        if (block >= 0)
            if (grid[block] > mineBlock) {
                grid[block] -= hiddenBlock;
                if (grid[block] == emptyBlock)
                    recurShowEmpty(block);
            }

        //bottom middle block
        block = a + numColumns;
        if (block < numBlocks)
            if (grid[block] > mineBlock) {
                grid[block] -= hiddenBlock;
                if (grid[block] == emptyBlock)
                    recurShowEmpty(block);
            }

        if (column < (numColumns - 1)) {
            //bottom right block
            block = a - numColumns + 1;
            if (block >= 0)
                if (grid[block] > mineBlock) {
                    grid[block] -= hiddenBlock;
                    if (grid[block] == emptyBlock)
                        recurShowEmpty(block);
                }

            //top right block
            block = a + numColumns + 1;
            if (block < numBlocks)
                if (grid[block] > mineBlock) {
                    grid[block] -= hiddenBlock;
                    if (grid[block] == emptyBlock)
                        recurShowEmpty(block);
                }

            //right middle block
            block = a + 1;
            if (block < numBlocks)
                if (grid[block] > mineBlock) {
                    grid[block] -= hiddenBlock;
                    if (grid[block] == emptyBlock)
                        recurShowEmpty(block);
                }
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        int block = 0;
        int uncover = 0;

        for (int i = 0; i < numRows; i++) {
            for (int a = 0; a < numColumns; a++) {

                block = grid[(i * numColumns) + a];

                if (playing && block == mineBlock)
                    playing = false;

                if (!playing) {
                    if (block == hiddenMineBlock) {
                        block = mineBlock;
                    } else if (block == flaggedMineBlock) {
                        block = flaggedBlock;
                    } else if (block > hiddenMineBlock) {
                        block = incorrectFlag;
                    } else if (block > mineBlock) {
                        block = hiddenBlock;
                    }


                } else {
                    if (block > hiddenMineBlock)
                        block = flaggedBlock;
                    else if (block > mineBlock) {
                        block = hiddenBlock;
                        uncover++;
                    }
                }

                g.drawImage(img[block], (a * blockDimensions),
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
        public void mousePressed(MouseEvent click) {

            int x = click.getX();
            int y = click.getY();
            int cCol = x / blockDimensions;
            int cRow = y / blockDimensions;
            boolean rep = false;


            if (!playing) {
                newGame();
                repaint();
            }


            if ((x < numColumns * blockDimensions) && (y < numRows * blockDimensions)) {

                if (click.getButton() == MouseEvent.BUTTON3) {

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
                            recurShowEmpty((cRow * numColumns) + cCol);
                    }
                }

                if (rep)
                    repaint();

            }
        }
    }
}

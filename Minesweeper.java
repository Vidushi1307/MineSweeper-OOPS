// Importing the libraries

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Minesweeper {
    private class MineTile extends JButton {
        int row;
        int col;

        public MineTile(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }
    int block_size = 70;

    // General size of board 8x8
    int numRows = 8;
    int numCols = numRows;

    //Setting the board width and height
    int board_w = numCols * block_size;
    int board_h = numRows * block_size;
    
    JFrame frame = new JFrame("Minesweeper");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();

    // Initializing the total count of mines
    int mineCount = 10;
    MineTile[][] board = new MineTile[numRows][numCols];
    ArrayList<MineTile> mineList;
    Random random = new Random();

    // Storing the count of tiles clicked
    int tilesClicked = 0; 
    boolean gameOver = false;

    Minesweeper() {
        // frame.setVisible(true);
        frame.setSize(board_w, board_h);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textLabel.setFont(new Font("Arial", Font.BOLD, 25));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Minesweeper: " + Integer.toString(mineCount));
        textLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(numRows, numCols)); //8x8
        // boardPanel.setBackground(Color.green);
        frame.add(boardPanel);

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                MineTile tile = new MineTile(row, col);
                board[row][col] = tile;

                tile.setFocusable(false);
                tile.setMargin(new Insets(0, 0, 0, 0));
                tile.setFont(new Font("Arial Unicode MS", Font.PLAIN, 45));
                // tile.setText("💣");
                tile.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (gameOver) {
                            return;
                        }
                        MineTile tile = (MineTile) e.getSource();

                        //left click
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            if (tile.getText() == "") {
                                if (mineList.contains(tile)) {
                                    Display_Mines();
                                }
                                else {
                                    checker(tile.row, tile.col);
                                }
                            }
                        }
                        //right click
                        else if (e.getButton() == MouseEvent.BUTTON3) {
                            if (tile.getText() == "" && tile.isEnabled()) {
                                tile.setText("🚩");
                            }
                            else if (tile.getText() == "🚩") {
                                tile.setText("");
                            }
                        }
                    } 
                });

                boardPanel.add(tile);
                
            }
        }

        frame.setVisible(true);

        Set_Mines();
    }

    void Set_Mines() {
        mineList = new ArrayList<MineTile>();

        int mineLeft = mineCount;
        while (mineLeft > 0) {
            int row = random.nextInt(numRows); //0-7
            int col = random.nextInt(numCols);

            MineTile tile = board[row][col]; 
            if (!mineList.contains(tile)) {
                mineList.add(tile);
                mineLeft -= 1;
            }
        }
    }

    void Display_Mines() {
        for (int i = 0; i < mineList.size(); i++) {
            MineTile tile = mineList.get(i);
            tile.setText("💣");
        }

        gameOver = true;
        textLabel.setText("Game Over!");
    }

    void checker(int row, int col) {
        if (row < 0 || row >= numRows || col < 0 || col >= numCols) {
            return;
        }

        MineTile tile = board[row][col];
        if (!tile.isEnabled()) {
            return;
        }
        tile.setEnabled(false);
        tilesClicked += 1;

        int minesFound = 0;

        // Checking mines at top

        minesFound += countMine(row-1, col-1);  //top left
        minesFound += countMine(row-1, col);    //top
        minesFound += countMine(row-1, col+1);  //top right

        // Checking mines at left and right

        minesFound += countMine(row, col-1);    //left
        minesFound += countMine(row, col+1);    //right

        // Checking mines at bottom

        minesFound += countMine(row+1, col-1);  //bottom left
        minesFound += countMine(row+1, col);    //bottom
        minesFound += countMine(row+1, col+1);  //bottom right

        if (minesFound > 0) {
            tile.setText(Integer.toString(minesFound));
        }
        else {
            tile.setText("");
            
            // Checking mines at top

            checker(row-1, col-1);    //top left
            checker(row-1, col);      //top
            checker(row-1, col+1);    //top right

            // Checking mines at left and right

            checker(row, col-1);      //left
            checker(row, col+1);      //right

            // Checking mines at bottom

            checker(row+1, col-1);    //bottom left
            checker(row+1, col);      //bottom
            checker(row+1, col+1);    //bottom right
        }

        if (tilesClicked == numRows * numCols - mineList.size()) {
            gameOver = true;
            textLabel.setText("Mines Cleared!");
        }
    }

    int countMine(int row, int col) {
        if (row < 0 || row >= numRows || col < 0 || col >= numCols) {
            return 0;
        }
        if (mineList.contains(board[row][col])) {
            return 1;
        }
        return 0;
    }
}


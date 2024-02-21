import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class GameGUI extends JFrame {
    private Game game;
    private JButton[][] buttons;
    private ImageIcon arrowUp, arrowLeft, arrowDown, arrowRight;
    private Spaceship selectedShip = null;
    private ImageIcon plane1Resized;
    private ImageIcon plane2Resized;
    private ImageIcon blackhole;
    private boolean player1turn = true;
    private JLabel turnLabel;
    public GameGUI(Game game) {
        this.game = game;
        arrowUp = new ImageIcon("up.png");
        arrowLeft = new ImageIcon("left.png");
        arrowDown = new ImageIcon("down.png");
        arrowRight = new ImageIcon("right.png");
        initializeGUI();
    }

    private void displayArrows(int x,int y){
        resetOtherButtons(x,y);
        if (x > 0 && game.table[x-1][y]==null) {
            buttons[x - 1][y].setIcon(arrowUp);
        }// Up
        if (y > 0 &&game.table[x][y-1]==null ){
            buttons[x][y - 1].setIcon(arrowLeft);
        }
        if (x < game.meret - 1 && game.table[x+1][y]==null){
            buttons[x + 1][y].setIcon(arrowDown);} // Down
        if (y < game.meret - 1 && game.table[x][y+1]==null) buttons[x][y + 1].setIcon(arrowRight);

    }
    private void resetOtherButtons(int x, int y) {

        ImageIcon[] arrowIcons = { arrowUp, arrowLeft, arrowDown, arrowRight };

        for (int i = 0; i < game.meret; i++) {
            for (int j = 0; j < game.meret; j++) {
                Icon currentIcon = buttons[i][j].getIcon();
                if (currentIcon == arrowUp || currentIcon == arrowLeft ||
                        currentIcon == arrowDown || currentIcon == arrowRight) {
                    buttons[i][j].setIcon(null);
                }

            }
        }

    }


    private void initializeGUI() {
        setTitle("Space Game");
        setSize(660, 690);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(game.meret, game.meret));
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(660, 670));

        // Background label
        JLabel backgroundLabel = null;
        try {
            backgroundLabel = new JLabel(new ImageIcon(ImageIO.read(new File("background.jpg"))));
            backgroundLabel.setBounds(0, 0, 660, 670);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JPanel buttonPanel = new JPanel(new GridLayout(game.meret, game.meret));
        buttonPanel.setOpaque(false);
        buttonPanel.setBounds(0, 0, 660, 600);
        buttons = new JButton[game.meret][game.meret];
        ImageIcon[] arrowIcons = { arrowUp, arrowLeft, arrowDown, arrowRight };
        for (int i = 0; i < game.meret; i++) {
            for (int j = 0; j < game.meret; j++) {
                buttons[i][j] = new JButton();
                final int x = i;
                final int y = j;

                buttons[i][j].addActionListener(e ->{
                    if(!game.isGameOver()){
                        if (game.table[x][y] instanceof Spaceship )  {
                            selectedShip = (Spaceship) game.table[x][y];
                            if((selectedShip.owner.playernum==1 && player1turn) || !player1turn && selectedShip.owner.playernum==2){
                                updateBoard();
                                displayArrows(x, y);
                            }
                        } else {
                            Icon currentIcon = buttons[x][y].getIcon();
                            if (selectedShip != null && currentIcon != null && ((selectedShip.owner.playernum == 1 && player1turn)||selectedShip.owner.playernum == 2 && !player1turn) ) {
                                if (currentIcon.equals(arrowUp)) {
                                    move(selectedShip, x - 1, y,"up");
                                } else if (currentIcon.equals(arrowDown)) {
                                    move(selectedShip, x + 1, y,"down");
                                } else if (currentIcon.equals(arrowLeft)) {
                                    move(selectedShip, x, y - 1,"left");
                                } else if (currentIcon.equals(arrowRight)) {
                                    move(selectedShip, x, y + 1,"right");
                                }
                                player1turn = !player1turn;
                                resetOtherButtons(x,y);
                                selectedShip = null;
                                updateBoard();
                            }
                        }

                    }


                });
                buttons[i][j].setOpaque(false);
                buttons[i][j].setContentAreaFilled(false);
                buttons[i][j].setBorderPainted(true);
                buttons[i][j].setBorder(BorderFactory.createLineBorder(Color.GRAY));
                buttonPanel.add(buttons[i][j]);


            }

        }

        // Add components to layered pane
        if (backgroundLabel != null) {
            layeredPane.add(backgroundLabel, Integer.valueOf(1)); // Background on lowest layer
        }
        layeredPane.add(buttonPanel, Integer.valueOf(2)); // Button panel on top layer

        // Set layered pane as content pane
        setContentPane(layeredPane);
        turnLabel = new JLabel("Player 1's Turn", SwingConstants.CENTER);
        turnLabel.setPreferredSize(new Dimension(turnLabel.getWidth(), 30));
        add(turnLabel, BorderLayout.SOUTH);
        turnLabel.setPreferredSize(new Dimension(turnLabel.getWidth(), 30));
        turnLabel.setForeground(Color.WHITE);
        turnLabel.setBackground(Color.DARK_GRAY); // Choose a color that contrasts well with your text color

        revalidate();
        repaint();
        pack();
        setVisible(true);
        updateBoard();
        SwingUtilities.invokeLater(() -> {
            int buttonWidth = buttonPanel.getWidth() / game.meret;
            int buttonHeight = buttonPanel.getHeight() / game.meret;
            resizeAndSetButtonIcons(buttonWidth, buttonHeight);
        });

    }
    private void resizeAndSetButtonIcons(int width, int height) {
        this.plane1Resized = resizeIcon(new ImageIcon("plane11.png"), width, height);
        this.plane2Resized = resizeIcon(new ImageIcon("plane22.png"), width, height);
        this.blackhole = resizeIcon(new ImageIcon("blackhole.png"), width, height);

    }

    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    private void move(Spaceship ship,int x,int y,String dir){
        if (ship.owner.playernum == 1){
            game.player1.moveShip(ship,dir,game);
        }
        else{
            game.player2.moveShip(ship,dir,game);
        }
    }
    private void updateBoard(){
        turnLabel.setText(player1turn ? "Player 1's Turn" : "Player 2's Turn");

        for (int i = 0; i < game.meret; i++) {
            for (int j = 0; j < game.meret; j++) {
                if (game.table[i][j] instanceof Spaceship) {
                    int ownernum = ((Spaceship) game.table[i][j]).owner.playernum;
                    if (ownernum == 1) {
                        buttons[i][j].setIcon(plane1Resized);

                    }
                    else {
                        buttons[i][j].setIcon(plane2Resized);

                    }
                    buttons[i][j].setBorderPainted(false);
                    buttons[i][j].setContentAreaFilled(false);
                    buttons[i][j].setFocusPainted(false);
                    buttons[i][j].setFocusable(false);

                }
                else if (game.table[i][j] == null) {
                    buttons[i][j].setIcon(null);
                }
                else if (game.table[i][j] instanceof Blackhole) {
                    buttons[i][j].setIcon(blackhole);
                }
            }
        }
        if (game.isGameOver())
        {
            ImageIcon customIcon = new ImageIcon("logo.png");
            customIcon = resizeIcon(customIcon,70,70);
            JOptionPane.showMessageDialog(this,"Player " + game.winner().playernum + " wins!", "Game Over",JOptionPane.INFORMATION_MESSAGE,customIcon);
        }

    }


}

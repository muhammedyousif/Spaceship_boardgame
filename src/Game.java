import java.util.Scanner;

public class Game {
    public int meret;
    public Blackhole blackhole;
    public Objects[][] table;
    public Player player1;
    public Player player2;
    public boolean isPlayer1Turn = true;
    public  Game(int meret){
        this.meret = meret;
        this.table = new Objects[meret][meret];
        this.blackhole = new Blackhole(meret / 2, meret / 2);
        this.table[meret / 2][meret / 2] = this.blackhole;
        this.player1= new Player(this,1);
        this.player2= new Player(this,2);
        int posx;
        int posy;
        for (Spaceship ship : player1.spaceships) {
            this.table[ship.x][ship.y] = ship;
        }

        // Place Player 2's spaceships
        for (Spaceship ship : player2.spaceships) {
            this.table[ship.x][ship.y] = ship;
        }

        /*
        for (int i = 0;i<meret-1;i++){
            posx = player1.spaceships.get(i).x;
            posy = player1.spaceships.get(i).y;
            System.out.print(posx);
            System.out.print(posy+" ");

            this.table[posx][posy] = player1.spaceships.get(i);
        }
        for (int i = 0;i<meret-1;i++){
            posx = player2.spaceships.get(i).x;
            posy = player2.spaceships.get(i).y;
            this.table[posx][posy] = player2.spaceships.get(i);
        }*/
    }
    public void displayBoard() {
        for (int i = 0; i < meret; i++) {
            for (int j = 0; j < meret; j++) {
                if (this.table[i][j] instanceof Spaceship) {
                    int ownernum = ((Spaceship) this.table[i][j]).owner.playernum;
                    if (ownernum == 1) {
                        System.out.print("1 ");
                    }
                    else {
                        System.out.print("2 ");
                    }
                } else if (this.table[i][j] instanceof Blackhole) {
                    System.out.print("B ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }
    public Boolean isGameOver(){
        return player1.spaceships.size() == this.meret / 2 || player2.spaceships.size() == this.meret / 2;
    }
    public Player winner(){
        if(player1.spaceships.size() == this.meret / 2){
            return player1;
        } else if (player2.spaceships.size() == this.meret / 2) {
            return player2;
        }
        return null;
    }

    public void run_game(Game game) {
        Scanner scanner = new Scanner(System.in);


        while (player1.spaceships.size() != game.meret / 2 && player2.spaceships.size() != game.meret / 2) {
            // Display current game state
            //game.displayBoard();

            // Determine which player's turn it is
            Player currentPlayer = isPlayer1Turn ? game.player1 : game.player2;
            //System.out.println("Player " + (isPlayer1Turn ? "1" : "2") + "'s turn.");

            //System.out.println("Enter your move (format: spaceshipIndex direction): ");
            int spaceshipIndex = scanner.nextInt();
            String direction = scanner.next();

            // Assuming a method to move a spaceship exists
            //currentPlayer.moveShip(currentPlayer.spaceships.get(spaceshipIndex), direction, game);

            // Check for game end condition
            if (game.isGameOver()) {
                break;
            }

            // Switch turns
            isPlayer1Turn = !isPlayer1Turn;
        }

        // Game has ended
        System.out.println("Game over!");
        if (player1.spaceships.size() == game.meret / 2) {
            System.out.println("Player 1 wins!");
        } else if (player2.spaceships.size() == game.meret / 2) {
            System.out.println("Player 2 wins!");
        }
    }

}

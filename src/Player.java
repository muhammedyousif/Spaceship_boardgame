import java.util.ArrayList;
import java.util.List;
public class Player {
    int playernum;
    List<Spaceship> spaceships;

    public Player(Game game, int playernum) {
        this.playernum = playernum;
        this.spaceships = new ArrayList<>();
        int i =0;  // Middle of the board

        if (playernum == 1) {

            int t = game.meret - 1;
            while (i != Math.floor((double) game.meret / 2)) {
                Spaceship ship = new Spaceship(t, i, this);
                this.spaceships.add(ship);
                i++;
                t--;
            }
            i=0;
            t =game.meret - 1;
            while (i != Math.floor((double) game.meret / 2)) {
                Spaceship ship = new Spaceship(t, t, this);
                this.spaceships.add(ship);
                i++;
                t--;
            }

        }
        if (playernum == 2) {
            int t = game.meret - 1;
            while (i != Math.floor((double) game.meret / 2)) {
                Spaceship ship1 = new Spaceship(i, i, this);
                this.spaceships.add(ship1);
                i++;
            }

            i = 0;
            t = game.meret - 1;
            while (i != Math.floor((double) game.meret / 2)) {
                Spaceship ship = new Spaceship(i, t, this);
                this.spaceships.add(ship);
                i++;
                t--;
            }

        }

    }


    public void moveShip(Spaceship ship,String direction,Game game){
        int x = ship.x;
        int y = ship.y;

        switch (direction.toLowerCase()){
            case "up":
                while (x > 0 && game.table[x-1][y] == null) {
                    x--;
                }
                break;
            case "down":
                while (x < game.meret - 1 && game.table[x+1][y] == null) {
                    x++;
                }
                break;
            case "left":
                while (y > 0 && game.table[x][y-1] == null ) {
                    y--;
                }
                break;
            case "right":
                while (y < game.meret - 1 && game.table[x][y+1] == null) {
                    y++;
                }
                break;

            default:
                System.out.println("Invalid direction");
                return;
        }

        game.table[ship.x][ship.y] = null;
        ship.x = x;
        ship.y = y;
        game.table[x][y] = ship;
        boolean blackholeNearby = (y + 1 < game.meret && game.table[x][y + 1] instanceof Blackhole) ||
                (x + 1 < game.meret && game.table[x + 1][y] instanceof Blackhole) ||
                (y - 1 >= 0 && game.table[x][y - 1] instanceof Blackhole) ||
                (x - 1 >= 0 && game.table[x - 1][y] instanceof Blackhole);

        if (blackholeNearby) {
            spaceships.remove(ship);
            game.table[x][y] = null;
        }

    }
}

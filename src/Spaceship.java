public class Spaceship extends Objects{
    public Player owner;

    public Spaceship(int x,int y,Player owner){
        super(x,y);
        this.owner=owner;
    }
}

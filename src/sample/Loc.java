package sample;
public class Loc implements Comparable<Loc>{
    private int x;
    private int y;

    public Loc(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    @Override
    public int compareTo(Loc other) {
        if(((other.getX() == this.x) && (other.getY() == this.y))){
            return 0;
        }
        else{
            return -1;
        }
    }

    @Override
    public String toString() {
        return "["+this.x+", "+this.y+"]";
    }
}
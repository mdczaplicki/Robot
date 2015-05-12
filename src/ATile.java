public class ATile
{
    public double g; // from start to this tile
    public double h; // heuristic to finish
    public double f; // sum of both
    public int i;
    public int j;
    public boolean available;
    public ATile parent;
    public ATile(double g, double h, boolean a, ATile p, int i, int j)
    {
        this.g = g;
        this.h = h;
        this.available = a;
        this.parent = p;
        this.i = i;
        this.j = j;
        this.f = this.g + this.h;
    }
}

public class ATile
{
    public double g; // from start to this tile
    public double h; // heuristic to finish
    public double f; // sum of both
    public boolean available;
    public ATile parent;
    public ATile(double g, double h, boolean a, ATile p)
    {
        this.g = g;
        this.h = h;
        this.available = a;
        this.parent = p;
        this.f = this.g + this.h;
    }
}

public class ATile
{
    public double g; // from start to this tile
    public double h; // heuristic to finish
    public double f; // sum of both
    public ATile(double g, double h)
    {
        this.g = g;
        this.h = h;
        this.f = this.g + this.h;
    }
}

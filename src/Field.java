public class Field
{
    int i, j;
    double f, g, h;
    boolean available;
    Field parent;

    public Field getParent() {
        return parent;
    }

    public void setParent(Field parent) {
        this.parent = parent;
    }

    public boolean isAvailable() {

        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getJ() {

        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public int getI() {

        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public double getH() {

        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public double getG() {

        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public double getF() {

        return f;
    }

    public void setF() {
        this.f = this.g + this.h;
    }
}

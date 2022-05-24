package Core.Helpers;

public class Dimensions2D
{
    //region PROPERTIES

    private double width;
    private double height;

    //endregion



    //region CONSTRUCTORS

    public Dimensions2D(double width, double height)
    {
        this.width = width;
        this.height = height;
    }

    //endregion



    //region PUBLIC METHODS

    public double getWidth()
    {
        return width;
    }
    public void setWidth(double width)
    {
        this.width = width;
    }

    public double getHeight()
    {
        return height;
    }
    public void setHeight(double height)
    {
        this.height = height;
    }

    public double getArea()
    {
        return width * height;
    }

    //endregion
}

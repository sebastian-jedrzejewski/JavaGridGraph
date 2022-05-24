package Core.Helpers;

public class Range
{
    //region PROPERTIES

    private double low;
    private double high;

    //endregion



    //region CONSTRUCTOR

    public Range(double low, double high)
    {
        this.low = low;
        this.high = high;
    }

    //endregion



    //region PUBLIC METHODS

    public double getLow()
    {
        return low;
    }
    public void setLow(double low)
    {
        this.low = low;
    }

    public double getHigh()
    {
        return high;
    }
    public void setHigh(double high)
    {
        this.high = high;
    }

    public double getDifference()
    {
        return high - low;
    }

    //endregion
}

package Core.Helpers;

public class Range<Number>
{
    //region PROPERTIES

    private Number _low;
    private Number _high;

    //endregion



    //region CONSTRUCTOR

    public Range(Number low, Number high)
    {
        _low = low;
        _high = high;
    }

    //endregion



    //region PUBLIC METHODS

    public Number getLow()
    {
        return _low;
    }
    public void setLow(Number low)
    {
        _low = low;
    }

    public Number getHigh()
    {
        return _high;
    }
    public void setHigh(Number high)
    {
        _high = high;
    }

    //endregion
}

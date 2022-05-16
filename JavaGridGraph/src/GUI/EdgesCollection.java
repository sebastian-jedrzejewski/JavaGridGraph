package GUI;

import java.io.IOException;
import java.util.ArrayList;

public class EdgesCollection
{
    //region PARAMETERS

    private Arrow _left = null;
    private int _leftNumber = -1;
    private Arrow _right = null;
    private int _rightNumber = -1;
    private Arrow _top = null;
    private int _topNumber = -1;
    private Arrow _bottom = null;
    private int _bottomNumber = -1;

    //endregion



    //region PUBLIC METHODS

    public void setLeft(int number, Arrow edge)
    {
        _leftNumber = number;
        _left = edge;
    }

    public void setRight(int number, Arrow edge)
    {
        _rightNumber = number;
        _right = edge;
    }

    public void setTop(int number, Arrow edge)
    {
        _topNumber = number;
        _top = edge;
    }

    public void setBottom(int number, Arrow edge)
    {
        _bottomNumber = number;
        _bottom = edge;
    }

    public Arrow getByNumber(int number)
    {
        if (_leftNumber == number)
        {
            return _left;
        }
        else if (_rightNumber == number)
        {
            return _right;
        }
        else if (_topNumber == number)
        {
            return _top;
        }
        else if (_bottomNumber == number)
        {
            return _bottom;
        }
        else
        {
            throw new IllegalArgumentException("Wrong vertex number");
        }
    }

    public ArrayList<Arrow> getAllEdges()
    {
        ArrayList<Arrow> list = new ArrayList<>();
        if (_left != null)
        {
            list.add(_left);
        }
        if (_right != null)
        {
            list.add(_right);
        }
        if (_top != null)
        {
            list.add(_top);
        }
        if (_bottom != null)
        {
            list.add(_bottom);
        }
        return list;
    }

    //endregion
}

package App.Controls;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class NumericTextField extends TextField
{
    //region PROPERTIES

    private final Type _type;

    private double _minValue;
    private double _maxValue;

    //endregion



    //region CONSTRUCTORS

    public NumericTextField(Type type)
    {
        this(type, "");
    }

    public NumericTextField(Type type, String text)
    {
        _type = type;
        _minValue = Double.NaN;
        _maxValue = Double.NaN;

        super.textProperty().addListener(new textChanged());
        super.setText(text);
    }

    //endregion



    //region PUBLIC METHODS

    public void setMinValue(double value)
    {
        _minValue = value;
    }
    public double getMinValue()
    {
        return _minValue;
    }

    public void setMaxValue(double value)
    {
        _maxValue = value;
    }
    public double getMaxValue()
    {
        return _maxValue;
    }

    //endregion



    //region EVENT HANDLERS

    private class textChanged implements ChangeListener<String>
    {
        @Override
        public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue)
        {
            String copyValue = newValue;
            if (!copyValue.isEmpty() && copyValue.charAt(0) == '-' && (_type == Type.Integer || _type == Type.Double))
            {
                copyValue = copyValue.substring(1);
            }
            copyValue = copyValue.replaceAll("[^[\\d|[.]]]", "");
            StringBuilder text = new StringBuilder(copyValue);
            boolean dot = false;
            for (int i = 0; i < text.length(); i++)
            {
                if (text.charAt(i) == '.')
                {
                    if (dot || _type == Type.PositiveInteger)
                    {
                        text.deleteCharAt(i);
                    }
                    else
                    {
                        dot = true;
                    }
                }
            }
            copyValue = text.toString();
            if (!newValue.isEmpty() && newValue.charAt(0) == '-' && (_type == Type.Integer || _type == Type.Double))
            {
                newValue = '-' + copyValue;
            }
            else
            {
                newValue = copyValue;
            }

            if ((!Double.isNaN(_minValue) || !Double.isNaN(_maxValue)) && !newValue.isEmpty())
            {
                if (_type == Type.PositiveDouble)
                {
                    double value = Double.parseDouble(newValue);
                    if (_minValue > value || _maxValue < value)
                    {
                        newValue = oldValue;
                    }
                }
                else
                {
                    int value = Integer.parseInt(newValue);
                    if (_minValue > value || _maxValue < value)
                    {
                        newValue = oldValue;
                    }
                }
            }
            NumericTextField.this.setText(newValue);
        }
    }

    //endregion



    //region ENUMS

    public enum Type
    {
        PositiveInteger,
        Integer,
        PositiveDouble,
        Double,
    }

    //endregion
}

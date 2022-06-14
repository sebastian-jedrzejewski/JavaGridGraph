package app.controls;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class NumericTextField extends TextField
{
    //region PROPERTIES

    private final Type type;

    private double minValue;
    private double maxValue;

    //endregion



    //region CONSTRUCTORS

    public NumericTextField(Type type)
    {
        this(type, "");
    }

    public NumericTextField(Type type, String text)
    {
        this.type = type;
        this.minValue = Double.NaN;
        this.maxValue = Double.NaN;

        this.textProperty().addListener(new TextChanged());

        this.setText(text);
    }

    //endregion



    //region PUBLIC METHODS

    public double getMinValue()
    {
        return minValue;
    }
    public void setMinValue(double value)
    {
        minValue = value;
    }

    public double getMaxValue()
    {
        return maxValue;
    }
    public void setMaxValue(double value)
    {
        maxValue = value;
    }

    //endregion



    //region EVENT HANDLERS

    private class TextChanged implements ChangeListener<String>
    {
        @Override
        public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue)
        {
            String copyValue = newValue;
            if (!copyValue.isEmpty() && copyValue.charAt(0) == '-' && (type == Type.Integer || type == Type.Double))
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
                    if (dot || type == Type.PositiveInteger)
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
            if (!newValue.isEmpty() && newValue.charAt(0) == '-' && (type == Type.Integer || type == Type.Double))
            {
                newValue = '-' + copyValue;
            }
            else
            {
                newValue = copyValue;
            }

            if ((!Double.isNaN(minValue) || !Double.isNaN(maxValue)) && !newValue.isEmpty())
            {
                if (type == Type.PositiveDouble)
                {
                    double value = Double.parseDouble(newValue);
                    if (minValue > value || maxValue < value)
                    {
                        newValue = oldValue;
                    }
                }
                else
                {
                    int value = Integer.parseInt(newValue);
                    if (minValue > value || maxValue < value)
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

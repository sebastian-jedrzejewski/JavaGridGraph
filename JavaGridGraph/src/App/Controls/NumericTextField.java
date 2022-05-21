package App.Controls;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class NumericTextField extends TextField
{
    //region PROPERTIES

    private final Type _type;

    //endregion



    //region CONSTRUCTORS

    public NumericTextField(Type type)
    {
        this(type, "");
    }

    public NumericTextField(Type type, String text)
    {
        _type = type;
        super.textProperty().addListener(new removeUnwantedChars());
        super.setText(text);
    }

    //endregion



    //region LISTENERS

    private class removeUnwantedChars implements ChangeListener<String>
    {
        @Override
        public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue)
        {
            newValue = newValue.replaceAll("[^[\\d|[.]]]", "");
            StringBuilder text = new StringBuilder(newValue);
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
            NumericTextField.this.setText(text.toString());
        }
    }

    //endregion



    //region ENUMS

    public enum Type
    {
        PositiveInteger,
        PositiveDouble,
    }

    //endregion
}

package components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class PlaceholderPasswordField extends JPasswordField {
    private String placeholderText;
    public PlaceholderPasswordField(String placeholderText) {
        this.placeholderText = placeholderText;

        setEchoChar((char) 0);  
        setText(placeholderText);
        setForeground(Color.GRAY);

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (getText().equals(placeholderText)) {
                    setText("");  
                    setForeground(Color.WHITE);  
                    setEchoChar('*');  
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setText(placeholderText); 
                    setForeground(Color.GRAY);
                    setEchoChar((char) 0);  
                }
            }
        });
    }

    public boolean isEmpty() {
        return getText().equals(placeholderText) || super.getText().isEmpty();
    }
}

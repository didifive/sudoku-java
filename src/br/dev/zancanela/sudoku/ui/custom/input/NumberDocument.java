package br.dev.zancanela.sudoku.ui.custom.input;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class NumberDocument extends PlainDocument {
    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if (str == null) return;
        if (getLength() == 0 && str.matches("[1-9]")) {
            super.insertString(offs, str, a);
        }
    }
}

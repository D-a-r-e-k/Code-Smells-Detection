//end constructor 
public void actionPerformed(ActionEvent evt) {
    JextTextArea textArea = getTextArea(evt);
    int start = textArea.getSelectionStart();
    if (action == DELETE) {
        if (start != textArea.getSelectionEnd()) {
            textArea.setSelectedText("");
            return;
        }
    }
    //end if action == DELETE 
    int caret = textArea.getCaretPosition();
    int line = textArea.getCaretLine();
    int lineStart = textArea.getLineStartOffset(line);
    caret -= lineStart;
    String lineText = textArea.getLineText(textArea.getCaretLine());
    caret += direction;
    try {
        int origCaret = caret;
        char origChar = lineText.charAt(caret);
        if (direction == TextUtilities.FORWARD) {
            char checkChar = lineText.charAt(caret - direction);
            if (!(Character.isLetterOrDigit(checkChar))) {
                caret -= direction;
                origChar = checkChar;
            }
        }
        //end if FORWARD and not letter or digit 
        caret = TextUtilities.findTypeChange(lineText, caret, direction);
        if (origCaret != caret) {
            char caretChar = lineText.charAt(caret);
            if ((!(Character.isLetterOrDigit(origChar) && Character.isLetterOrDigit(caretChar)) || (Character.isUpperCase(origChar) && Character.isLowerCase(caretChar))) && (direction == TextUtilities.BACKWARD)) //and this 
            {
                caret -= direction;
            }
            //end big fat if statement 
            if ((Character.isLetterOrDigit(origChar) && Character.isLetterOrDigit(lineText.charAt(caret))) && (caret + 1 == lineText.length()) && direction == TextUtilities.FORWARD) {
                caret += direction;
            }
            //end 2nd big fat if statement 
            if (Character.isWhitespace(origChar) && Character.isWhitespace(caretChar)) {
                try {
                    while (Character.isWhitespace(lineText.charAt(caret))) {
                        caret += direction;
                    }
                } catch (IndexOutOfBoundsException oobe_wan_kenoobi) {
                    caret -= direction;
                }
            }
        }
    } catch (IndexOutOfBoundsException oobe) {
        try {
            textArea.getText().charAt(lineStart + caret);
        } catch (IndexOutOfBoundsException oobeII) {
            textArea.getToolkit().beep();
            return;
        }
    }
    //end catch 
    if (action == SELECT) {
        textArea.select(textArea.getMarkPosition(), lineStart + caret);
    } else {
        if (action == DELETE) {
            try {
                int documentPosition = caret + lineStart;
                int length = Math.abs(start - documentPosition);
                textArea.getDocument().remove(((direction == TextUtilities.FORWARD) ? start : documentPosition), length);
            } catch (BadLocationException bl) {
                bl.printStackTrace();
            }
        } else {
            textArea.setCaretPosition(lineStart + caret);
        }
    }
}

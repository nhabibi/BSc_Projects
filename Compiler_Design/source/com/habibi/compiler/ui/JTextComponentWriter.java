/**
 * JTextComponentWriter.java
 *
 * @author Created by Omnicore CodeGuide
 */

package com.habibi.compiler.ui;

import java.io.*;
import javax.swing.text.*;

public class JTextComponentWriter extends Writer
{
    private JTextComponent target;

    public JTextComponentWriter(JTextComponent target) {
        this.target = target;
    }

    public void flush() throws IOException {
    }

    public void write(char[] cbuf, int off, int len) throws IOException {
        String text = new String(cbuf, off, len);
        Document doc = target.getDocument();
        try {
            doc.insertString(doc.getLength(), text, null);
        } catch (BadLocationException ble) {
            throw new IOException("Error in writing to the target text document: " + ble.getMessage());
        }
    }

    public void close() throws IOException {
    }


}


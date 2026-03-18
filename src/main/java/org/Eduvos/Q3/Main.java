package org.Eduvos.Q3;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // lots of stuff from here
        // https://github.com/JetBrains/build-UI-using-Swing-sample-project
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                UI ui = new UI();
                ui.setVisible(true);
            }
        });
    }
}
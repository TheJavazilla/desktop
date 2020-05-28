package com.fungus_soft.programs;

import java.awt.Dimension;
import java.awt.Insets;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;

import javax.swing.BoxLayout;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.fungus_soft.desktop.Main;
import com.fungus_soft.desktop.api.JProgram;
import com.fungus_soft.desktop.api.JWebApp;
import com.fungus_soft.desktop.api.ProgramInfo;
import com.fungus_soft.desktop.api.Toast;
import com.fungus_soft.desktop.theme.IconPack;
import com.fungus_soft.ui.ModernScrollPane;

@ProgramInfo(name = "NotePad", version="1.1")
public class Notepad extends JProgram {

    private static final long serialVersionUID = 1L;
    private File file;

    public Notepad(File fil) {
        this.setFrameIcon(IconPack.get().text);
        this.file = fil;
        String text = "";
        int i = 0;

        if (file != null && file.exists()) {
            try {
                for (String s : Files.readAllLines(file.toPath())) {
                    if (i == 1) text += "\n" + s;
                    if (i == 0) {
                        text += s;
                        i++;
                    }
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        JPanel pa = new JPanel();
        JTextArea a = new JTextArea(text);
        a.setMargin(new Insets(5, 8, 5, 8));
        pa.setLayout(new BoxLayout(pa, BoxLayout.Y_AXIS));
        a.setWrapStyleWord(true);
        a.setSize(200, 300);
        JMenuBar m = new JMenuBar();
        JMenu mf = new JMenu("File");
        mf.add("Save").addActionListener(l -> {
            if (null == file)
                file = new File( JOptionPane.showInternalInputDialog(Main.p, "Path to Save as...", "Save as", 0) );

            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"))) {
                file.createNewFile();
                for (String line : a.getText().split("\n")) {
                    writer.write(line);
                    writer.newLine();
                }
            } catch (IOException e) { Toast.show(e.getMessage(), 3500); e.printStackTrace(); }
        });

        mf.add("Open as JWebApp").addActionListener(l -> {
            JWebApp w = new JWebApp(a.getText());
            w.setVisible(true);
            Main.p.add(w);
        });

        pa.add(new ModernScrollPane(a));
        m.add(mf);
        this.setContentPane(pa);
        this.setClosable(true);
        this.setJMenuBar(m);
        this.setSize(new Dimension(520,500));
        this.setVisible(true);
    }

}
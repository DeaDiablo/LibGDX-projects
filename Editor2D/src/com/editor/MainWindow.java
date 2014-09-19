package com.editor;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;
import com.games.leveleditor.LevelEditor;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class MainWindow
{
  private JFrame frame;
  private LwjglCanvas canvas;

  /**
   * Launch the application.
   */
  public static void main(String[] args)
  {
    EventQueue.invokeLater(new Runnable()
    {
      public void run()
      {
        try
        {
          MainWindow window = new MainWindow();
          window.frame.setVisible(true);
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the application.
   */
  public MainWindow()
  {
    initialize();
  }

  /**
   * Initialize the contents of the frame.
   */
  private void initialize()
  {
    frame = new JFrame(java.util.Locale.getDefault().toString().compareTo("ru_RU") == 0 ? "Редактор уровней" : "Level editor");
    frame.setBounds(100, 100, 881, 539);
    frame.addWindowListener(new WindowAdapter() {
      
      @Override
      public void windowClosing(WindowEvent e) {
        canvas.stop();
        canvas.getApplicationListener().dispose();
        System.exit(0);
      }
    });
    
    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    config.title = "";
    config.useGL30 = false;
    config.width = 800;
    config.height = 480;
    config.fullscreen = false;
    config.vSyncEnabled = false;
    config.foregroundFPS = 0;
    
    Container container = frame.getContentPane();
    canvas = new LwjglCanvas(new LevelEditor(), config);
    container.add(canvas.getCanvas(), BorderLayout.CENTER);
    
    JMenuBar menuBar = new JMenuBar();
    frame.setJMenuBar(menuBar);
    
    JMenu mnFile = new JMenu("File");
    menuBar.add(mnFile);
    
    JMenuItem mntmNew = new JMenuItem("New");
    mnFile.add(mntmNew);
    
    JMenuItem mntmLoad = new JMenuItem("Load");
    mnFile.add(mntmLoad);
    
    JMenuItem mntmSave = new JMenuItem("Save");
    mnFile.add(mntmSave);
    
    JMenuItem mntmSaveAs = new JMenuItem("Save as ...");
    mnFile.add(mntmSaveAs);
    
    JMenuItem mntmExit = new JMenuItem("Exit");
    mnFile.add(mntmExit);
    
    JMenu mnEdit = new JMenu("Edit");
    menuBar.add(mnEdit);
    
    JMenuItem mntmCopy = new JMenuItem("Copy");
    mnEdit.add(mntmCopy);
    
    JMenuItem mntmCut = new JMenuItem("Cut");
    mnEdit.add(mntmCut);
    
    JMenuItem mntmPaste = new JMenuItem("Paste");
    mnEdit.add(mntmPaste);
  }
}

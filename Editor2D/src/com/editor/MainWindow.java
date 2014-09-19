package com.editor;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;

import javax.swing.JFrame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;
import com.games.leveleditor.LevelEditor;
import com.shellGDX.GameInstance;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class MainWindow extends JFrame
{
  /**
   * Launch the application.
   */
  public static void main(String[] args)
  {
    EventQueue.invokeLater(new Runnable()
    {
      public void run()
      {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "";
        config.useGL30 = false;
        config.width = 800;
        config.height = 480;
        config.fullscreen = false;
        config.vSyncEnabled = false;
        config.foregroundFPS = 0;

        new MainWindow(new LevelEditor(), config);
      }
    });
  }

  private static final long serialVersionUID = 1L;
  private LwjglCanvas       canvas;

  /**
   * Create the application.
   */
  public MainWindow(GameInstance listener, LwjglApplicationConfiguration config)
  {
    super(config.title);
    
    JMenuBar menuBar = new JMenuBar();
    setJMenuBar(menuBar);
    
    JMenu mnFile = new JMenu("File");
    menuBar.add(mnFile);
    
    JMenuItem mntmNew = new JMenuItem("New");
    mnFile.add(mntmNew);
    
    JMenuItem mntmLoad = new JMenuItem("Load");
    mnFile.add(mntmLoad);
    
    JMenuItem mntmSave = new JMenuItem("Save");
    mnFile.add(mntmSave);
    
    JMenuItem mntmSaveAs = new JMenuItem("Save as...");
    mnFile.add(mntmSaveAs);
    
    JMenuItem mntmExit = new JMenuItem("Exit");
    mnFile.add(mntmExit);
    
    JMenu mnEdit = new JMenu("Edit");
    menuBar.add(mnEdit);

    construct(listener, config);
    setBounds(100, 100, config.width, config.height);
  }

  private void construct(final GameInstance listener, LwjglApplicationConfiguration config)
  {
    canvas = new LwjglCanvas(listener, config)
    {
      protected void stopped()
      {
        MainWindow.this.dispose();
      }

      protected void setTitle(String title)
      {
        MainWindow.this.setTitle(title);
      }

      protected void setDisplayMode(int width, int height)
      {
        MainWindow.this.getContentPane().setPreferredSize(new Dimension(width, height));
        MainWindow.this.getContentPane().invalidate();
        MainWindow.this.pack();
        MainWindow.this.setLocationRelativeTo(null);
        updateSize(width, height);
      }

      protected void resize(int width, int height)
      {
        updateSize(width, height);
      }

      protected void start()
      {
        MainWindow.this.start();
      }

      protected void exception(Throwable t)
      {
        MainWindow.this.exception(t);
      }

      protected int getFrameRate()
      {
        int frameRate = MainWindow.this.getFrameRate();
        return frameRate == 0 ? super.getFrameRate() : frameRate;
      }
    };

    Runtime.getRuntime().addShutdownHook(new Thread()
    {
      public void run()
      {
        Runtime.getRuntime().halt(0); // Because fuck you, deadlock causing
                                      // Swing shutdown hooks.
      }
    });

    setDefaultCloseOperation(EXIT_ON_CLOSE);
    getContentPane().setPreferredSize(new Dimension(config.width, config.height));

    Point location = getLocation();
    if (location.x == 0 && location.y == 0)
      setLocationRelativeTo(null);
    canvas.getCanvas().setSize(getSize());

    // Finish with invokeLater so any LwjglFrame super constructor has a chance
    // to initialize.
    EventQueue.invokeLater(new Runnable()
    {
      public void run()
      {
        addCanvas();
        setVisible(true);
        canvas.getCanvas().requestFocus();
      }
    });
  }

  protected int getFrameRate()
  {
    return 0;
  }

  protected void exception(Throwable ex)
  {
    ex.printStackTrace();
    canvas.stop();
  }

  protected void addCanvas()
  {
    getContentPane().add(canvas.getCanvas());
  }

  /**
   * Called after {@link ApplicationListener} create and resize, but before the
   * game loop iteration.
   */
  protected void start()
  {
  }

  /** Called when the canvas size changes. */
  public void updateSize(int width, int height)
  {
  }

  public LwjglCanvas getCanvas()
  {
    return canvas;
  }
  
  @Override
  public void dispose()
  {
    super.dispose();
  }
}

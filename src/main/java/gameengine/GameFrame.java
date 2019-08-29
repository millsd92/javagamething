package gameengine;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame
{
    public static int SCREEN_HEIGHT, SCREEN_WIDTH;

    GameFrame(DisplayMode displayMode, GraphicsDevice device, boolean isFullscreen)
    {
        //---------- Setting Up Screen ----------//
        setTitle("Here's the game!");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        SCREEN_HEIGHT = displayMode.getHeight();
        SCREEN_WIDTH = displayMode.getWidth();

        if (isFullscreen)
        {
            device.setFullScreenWindow(this);
            device.setDisplayMode(displayMode);
        }
        else
            setSize(displayMode.getWidth(), displayMode.getHeight());

        //---------- Setting Up Controls ----------//
        addKeyListener(new MainControls());

        //---------- Add the Game ----------//
        add(new Game());
    }
}

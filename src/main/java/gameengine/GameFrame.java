package gameengine;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;

class GameFrame extends JFrame
{

    GameFrame(DisplayMode displayMode, GraphicsDevice device, boolean isFullscreen)
    {
        //---------- Setting Up Screen ----------//
        setTitle("Here's the game!");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        if (isFullscreen)
        {
            device.setFullScreenWindow(this);
            device.setDisplayMode(displayMode);
        }
        else
            setSize(displayMode.getWidth(), displayMode.getHeight());

        //---------- Setting Up Controls ----------//
        addKeyListener(new GameKeyListener());

        //---------- Add the Game ----------//
        add(new Game(this, isFullscreen));
    }
}

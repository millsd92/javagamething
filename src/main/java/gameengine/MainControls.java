package gameengine;

import gameengine.abstractions.Direction;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MainControls implements KeyListener
{

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            Game.setIsPaused(!Game.isPaused());
        if (e.getKeyCode() == KeyEvent.VK_D)
        {
            if (Game.getHero().getCurrentDirection() == Direction.LEFT)
                Game.getHero().changeDirection();
            else
            {
                Game.getHero().setStartMoving(true);
                Game.getHero().setStopping(false);
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_A)
        {
            if (Game.getHero().getCurrentDirection() == Direction.RIGHT)
                Game.getHero().changeDirection();
            else
            {
                Game.getHero().setStartMoving(true);
                Game.getHero().setStopping(false);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_A)
            Game.getHero().setStopping(true);
    }
}

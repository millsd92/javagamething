package gameengine;

import gameengine.abstractions.AnimationState;
import gameengine.abstractions.Direction;
import org.jetbrains.annotations.NotNull;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameKeyListener implements KeyListener
{

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(@NotNull KeyEvent e)
    {
        InputManager.keysPressed.add(e.getKeyCode());
        /*
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            Game.setIsPaused(!Game.isPaused());
        }
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
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            if (Game.getHero().isGrounded())
                Game.getHero().jump();
        }*/
    }

    @Override
    public void keyReleased(@NotNull KeyEvent e)
    {
        InputManager.keysPressed.remove(e.getKeyCode());
        /*
        if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_A)
        {
            if ((Game.getHero().isMoving() || Game.getHero().getCurrentState() == AnimationState.START_MOVING
                    || Game.getHero().getCurrentState() == AnimationState.MOVING)
                    && !Game.getHero().changingDirection())
                Game.getHero().setStopping(true);
        }*/
    }
}

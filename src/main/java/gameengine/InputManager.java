package gameengine;

import gameengine.abstractions.Direction;

import java.awt.event.KeyEvent;
import java.util.HashSet;

class InputManager
{
    static HashSet<Integer> keysPressed = new HashSet<>();

    private static final int RIGHT = KeyEvent.VK_D;
    private static final int LEFT = KeyEvent.VK_A;
    private static final int JUMP = KeyEvent.VK_SPACE;

    static void processLatestInputs()
    {
        //---------- Horizontal Movements ----------//
        if (keysPressed.contains(RIGHT) && !keysPressed.contains(LEFT))
        {
            if (Game.getHero().getDirection() == Direction.LEFT)
                Game.getHero().changeDirection();
            else
                Game.getHero().run();
        }
        else if (keysPressed.contains(LEFT) && !keysPressed.contains(RIGHT))
        {
            if (Game.getHero().getDirection() == Direction.RIGHT)
                Game.getHero().changeDirection();
            else
                Game.getHero().run();
        }

        //---------- Vertical Movements ----------//
        if (keysPressed.contains(JUMP))
            if (Game.getHero().isGrounded())
                Game.getHero().jump(Game.getHero().getJumpHeight());

        //---------- No Movement ----------//
        if (keysPressed.isEmpty())
            Game.getHero().idle();
    }
}

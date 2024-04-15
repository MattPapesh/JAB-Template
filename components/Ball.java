package components;

import fundamentals.animation.Animation;
import fundamentals.component.ComponentBase;

public class Ball extends ComponentBase 
{
    private Animation icon = null;
    public int x_velo = 0;
    public int y_velo = 0;
    public int deg_velo = 1;
    public Ball() {
        icon = new Animation("ball.png");
        addRequirements(500, 500, 0, icon);
    }
    
    @Override
    public void periodic() {
        setCoordinates(getCoordinates().getX() + x_velo, getCoordinates().getY() + y_velo, getCoordinates().getDegrees() + deg_velo);
    }
}

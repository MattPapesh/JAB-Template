package mechanics;

import components.Ball;
import fundamentals.Constants;
import fundamentals.appbase.AppBase;
import fundamentals.mechanic.MechanicBase;

public class MoveBall extends MechanicBase
{
    private Ball ball = null;
    private int speed = 0;
    private boolean cool_down = false;
    private int elapsed_cool_down_millis = 0;
    private int initial_cool_down_millis = 0;
    public MoveBall(Ball ball, int speed) {
        this.ball = ball;
        this.speed = Math.max(speed, 0);
        addRequirements(ball);
    }

    @Override
    public void initialize() {
        ball.x_velo = speed;
        ball.y_velo = speed;
    }

    @Override
    public void execute() {
        if((ball.getCoordinates().getX() < ball.getWidth() / 2 
        || ball.getCoordinates().getX() > Constants.WINDOW_CHARACTERISTICS.WINDOW_WIDTH - (ball.getWidth() / 2)) && !cool_down) {
            ball.x_velo = -ball.x_velo;
            cool_down = true;
            initial_cool_down_millis = AppBase.getMillis();
        }
        if((ball.getCoordinates().getY() < ball.getHeight() / 2 
        || ball.getCoordinates().getY() > Constants.WINDOW_CHARACTERISTICS.WINDOW_HEIGHT - (ball.getHeight() / 2)) && !cool_down) {
            ball.y_velo = -ball.y_velo;
            cool_down = true;
            initial_cool_down_millis = AppBase.getMillis();
        }
        if(cool_down && AppBase.getMillis() - initial_cool_down_millis >= elapsed_cool_down_millis) {
            cool_down = false;
        }
    }

    @Override
    public void end(boolean interrupted) {
        ball.x_velo = 0;
        ball.y_velo = 0;
    }

    @Override
    public boolean isFinished() {
        return false; 
    }
}

import components.Ball;
import fundamentals.appbase.AppBase;
import fundamentals.mechanic.MechanicScheduler;
import mechanics.MoveBall;

public class App extends AppBase
{
    private AppContainer app_container = new AppContainer(); 
    private Ball ball = new Ball(); 

    // Use the status initiation methods here to transition between the status methods below
    @Override // This method is always periodically called, regardless of the app's current status
    public void determineAppStatus() {   
        
    }

    @Override 
    public void appInit() {
        app_container.configureButtonBindings();
        // Schedule mechanic manually:
        MechanicScheduler.getInstance().scheduleMechanic(new MoveBall(ball, 2));
    }

    @Override 
    public void appPeriodic() { 
        
    }
}

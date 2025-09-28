package SimulatedAnneling;

public final class Constants {
    public final double TEMPERATURE_INITIAL = 1E-3;
    public final double TEMPERATURE_RESTART = 1E-4;
    public final double TEMPERATURE_RELOAD = 1E-6;
    public final double TEMPERATURE_CHANGE = 0.9999995;
    public final double GAMMA_CHANGE = 0.99;
    public final int MAX_TIMEOUT = 1_000_000;
    public final int MAX_MUTATION = 50;

    public final double HARD_PENALIZATION_FLAT = 0.004;
    public final double HARD_PENALIZATION_RATE = 1.1;
    public final double HARD_PENALIZATION_PRESSURE = 0.0;
    public final double HARD_PENALIZATION_DECAY = 0.9;

    public final double SOFT_PENALIZATION_RATE = 1.1;
    public final double SOFT_PENALIZATION_FLAT = 1E-3;
    public final double SOFT_PENALIZATION_SPECIFIC = 0.05;
    public final double SOFT_PENALIZATION_CONFLICTS = 1E-2;
    public final double SOFT_PENALIZATION_STUDENTS_TIME = 1E-2;
    public final double SOFT_PENALIZATION_STUDENTS_ROOM = 1E-3;
    public final double SOFT_PENALIZATION_ASSIGNMENT = 1E-2;
    public final double SOFT_PENALIZATION_DECAY_FLAT = 1E-3;
    public final double SOFT_PENALIZATION_DECAY_RATE = 0.9;

    public final double BETA = 6E-3;
    public final double BETA_UNFEASIBLE = 3E-3;
    
    
    public final double C1 = 1.0;
    public final double C2 = 1.0;
    
    public Constants() {
 
    }//FinMethod
    
}//FinMethod

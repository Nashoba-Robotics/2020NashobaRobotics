package edu.nr.robotics.subsystems.turret;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.units.Angle;
import edu.nr.lib.units.AngularSpeed;
import edu.nr.lib.units.Time;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.nr.robotics.subsystems.sensors.EnabledSensors;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
public class Turret extends NRSubsystem
{
   private static Turret singleton;

   private TalonSRX turretTalon;

   public static final double ENCODER_TICKS_PER_DEGREE_TURRET = (2048.0 / 360) / 30.0;
   private static double F_POS_TURRET = 10;
   private static double P_POS_TURRET = 01;
   private static double I_POS_TURRET = 0; 
   private static double D_POS_TURRET = 1;

   public static double turretMinOutput = -1;
   private static double turretMaxOutput = 1;
   private static Angle setAngle = Angle.ZERO;
   public static Angle goalAngle = Angle.ZERO;
   public static Angle deltaAngle = Angle.ZERO;

   public static Angle LEFT_LIMIT = new Angle(- 118,Angle.Unit.DEGREE);
   public static Angle RIGHT_LIMIT = new Angle( 118,Angle.Unit.DEGREE);


   public static final NeutralMode NEUTRAL_MODE = NeutralMode.Brake;
   //Type of PID. 0 = primary. 1 = cascade
   public static final int PID_TYPE = 0;

   public static final int POS_SLOT = 0;

   public static final int DEFAULT_TIMEOUT = 0;

   private Turret()
   {
       if(EnabledSubsystems.TURRET_ENABLED) {
           turretTalon = new TalonSRX(RobotMap.TURRET_TALON);

           turretTalon.config_kF(POS_SLOT, F_POS_TURRET);
           turretTalon.config_kP(POS_SLOT, P_POS_TURRET);
           turretTalon.config_kI(POS_SLOT, I_POS_TURRET);
           turretTalon.config_kD(POS_SLOT, D_POS_TURRET);

           turretTalon.setNeutralMode(NEUTRAL_MODE);

           turretTalon.setInverted(false);

           turretTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, DEFAULT_TIMEOUT);

           if(EnabledSubsystems.TURRET_DUMB_ENABLED) {
               turretTalon.set(ControlMode.PercentOutput, 0);
           }
           else{
               turretTalon.set(ControlMode.Position, getAngle().get(Angle.Unit.TURRET_ENCODER_TICK));
           }
           smartDashboardInit();
       }
   }
   public synchronized static void init()
   {
       if(singleton == null)
           singleton = new Turret();
           singleton.setDefaultCommand(new SetTurretLimelightCommand());
   }
   public static Turret getInstance()
   {
       if(singleton == null)
           init();
       return singleton;
   }
   public void disable()
   {
       if(turretTalon != null)
       {
           turretTalon.set(ControlMode.PercentOutput, 0);
       }
   }
   public void smartDashboardInit()
   {
       if(EnabledSubsystems.TURRET_SMARTDASHBOARD_DEBUG_ENABLED)
       {
           SmartDashboard.putNumber("P_POS_TURRET", 0);
           SmartDashboard.putNumber("I_POS_TURRET", 0);
           SmartDashboard.putNumber("D_POS_TURRET", 0);
           SmartDashboard.putNumber("F_POS_TURRET", 0);

           SmartDashboard.putNumber("Turret Current", getCurrent());

           SmartDashboard.putNumber("Set Angle Turret", setAngle.get(Angle.Unit.DEGREE));
           SmartDashboard.putNumber("Turret Current Angle", getAngle().get(Angle.Unit.DEGREE));
           SmartDashboard.putNumber("Turret Goal Angle", goalAngle.get(Angle.Unit.DEGREE));
           //SmartDashboard.putNumber("SET TURRET PERCENT", 0);
       }
   }
   public void smartDashboardInfo()
   {
       if(turretTalon != null)
       {
           if(EnabledSubsystems.TURRET_SMARTDASHBOARD_DEBUG_ENABLED)
           {
               setMotorSpeedInPercent(0.10);
               P_POS_TURRET = SmartDashboard.getNumber("P_POS_TURRET", 0);
               I_POS_TURRET = SmartDashboard.getNumber("I_POS_TURRET", 0);
               D_POS_TURRET = SmartDashboard.getNumber("D_POS_TURRET", 0);
               F_POS_TURRET = SmartDashboard.getNumber("F_POS_TURRET", 0);

               turretTalon.config_kF(POS_SLOT, F_POS_TURRET);
               turretTalon.config_kP(POS_SLOT, P_POS_TURRET);
               turretTalon.config_kI(POS_SLOT, I_POS_TURRET);
               turretTalon.config_kD(POS_SLOT, D_POS_TURRET);

               SmartDashboard.putNumber("Turret Current", getCurrent());

               goalAngle = new Angle(SmartDashboard.getNumber("Turret Goal Angle", goalAngle.get(Angle.Unit.DEGREE)), Angle.Unit.DEGREE);
               SmartDashboard.putNumber("Set Angle Turret" , setAngle.get(Angle.Unit.DEGREE));
               SmartDashboard.putNumber("Turret Current Angle", getAngle().get(Angle.Unit.TURRET_ENCODER_TICK));
               //setMotorSpeedInPercent(SmartDashboard.getNumber("SET TURRET PERCENT", 0));
           }
       }
       //System.out.println("Turret angle" + goalAngle.get(Angle.Unit.ROTATION));
       //turretPIDController.setReference(5, ControlType.kPosition);
   }
   public Angle getAngle()
   {
       if(turretTalon != null)
       {
           return new Angle(turretTalon.getSelectedSensorPosition(), Angle.Unit.TURRET_ENCODER_TICK);
       }
       return Angle.ZERO;
   }
   public AngularSpeed getActualSpeed(){
       if(turretTalon != null)
       {
           return new AngularSpeed(turretTalon.getSelectedSensorVelocity(), Angle.Unit.TURRET_ENCODER_TICK, Time.Unit.HUNDRED_MILLISECOND);
       }

       return AngularSpeed.ZERO;
   }
   public void setAngle(Angle targetAngle){
       if(turretTalon != null){
           setAngle = targetAngle;
        //   System.out.println("setAngle() is being called");
           turretTalon.selectProfileSlot(POS_SLOT, PID_TYPE);
           turretTalon.set(ControlMode.Position, setAngle.get(Angle.Unit.TURRET_ENCODER_TICK));
       }
   }

   public void periodic()
   {
       if(EnabledSubsystems.TURRET_ENABLED){
      //System.out.println(setAngle.get(Angle.Unit.DEGREE));
        if(EnabledSensors.getInstance().LimTurretLeft.get()){
            if(getActualSpeed().get(Angle.Unit.DEGREE, Time.Unit.SECOND) < 0){
                setMotorSpeedInPercent(0); // might not need this
                setAngle(LEFT_LIMIT);
            }
        }

        if(EnabledSensors.getInstance().LimTurretRight.get()){
            if(getActualSpeed().get(Angle.Unit.DEGREE, Time.Unit.SECOND) > 0){
                setMotorSpeedInPercent(0); // might not need this
                setAngle(RIGHT_LIMIT);
            }
        }
    }
   }

   public double getCurrent(){
       if(turretTalon != null)
       {
           return turretTalon.getStatorCurrent();
       }
       return 0;
   }

   public void setMotorSpeedInPercent(double percent)
   {
       if(turretTalon != null)
       {
           turretTalon.set(ControlMode.PercentOutput, percent);
       }
   }
}
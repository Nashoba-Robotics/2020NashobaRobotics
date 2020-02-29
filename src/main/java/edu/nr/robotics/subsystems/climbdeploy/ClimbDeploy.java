package edu.nr.robotics.subsystems.climbdeploy;
 
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
 
import edu.nr.lib.commandbased.NRSubsystem;
import edu.nr.lib.motorcontrollers.CTRECreator;
import edu.nr.lib.units.Distance;
import edu.nr.lib.units.Time;
import edu.nr.lib.units.Distance.Unit;
import edu.nr.robotics.RobotMap;
import edu.nr.robotics.subsystems.EnabledSubsystems;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
 
public class ClimbDeploy extends NRSubsystem{
 
   private static ClimbDeploy singleton;
 
   //private TalonSRX climbDeployTalon;
   private VictorSPX climbDeployVictor;

   public static final double ENCODER_TICKS_PER_INCH_CLIMB_DEPLOY = 50000 / 82; // test for old robot

   private PowerDistributionPanel pdp;
 
   public static double F_POS_CLIMB_DEPLOY = 0;
   public static double P_POS_CLIMB_DEPLOY = 0;
   public static double I_POS_CLIMB_DEPLOY = 0;
   public static double D_POS_CLIMB_DEPLOY = 0;
 
   public static Time VOLTAGE_RAMP_RATE_CLIMB_DEPLOY = new Time(0.05, Time.Unit.SECOND);
   public static final int VOLTAGE_COMPENSATION_LEVEL = 12;
   public static final double MIN_MOVE_VOLTAGE = 0.0;
   public static final int DEFAULT_TIMEOUT = 0;
 
   public static final int PEAK_CURRENT_CLIMB_DEPLOY = 60;
   public static final int CONTINUOUS_CURRENT_LIMIT_CLIMB_DEPLOY = 40;
 
   public static final Distance MINIMUM = Distance.ZERO;
   public static final Distance MAXIMUM = new Distance(24, Distance.Unit.INCH);
 
   NeutralMode NEUTRAL_MODE_CLIMB_DEPLOY = NeutralMode.Brake;
 
   public static final int POS_SLOT = 0;
   public static final int VEL_SLOT = 1;
 
   public static Distance setPositionClimb = Distance.ZERO;
   public static Distance goalPositionClimb = Distance.ZERO;
 
   public static final Distance DEPLOY_DISTANCE = Distance.ZERO;
   public static final double RETRACT_PERCENT = 0.0;
 
 
   private ClimbDeploy(){
       if(EnabledSubsystems.CLIMB_DEPLOY_ENABLED){
            climbDeployVictor = new VictorSPX(RobotMap.CLIMB_DEPLOY_VICTOR);
           //climbDeployTalon = CTRECreator.createMasterTalon(RobotMap.CLIMB_DEPLOY_TALON); // 4? rn
 
           //climbDeployTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
           climbDeployVictor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);

           pdp = new PowerDistributionPanel(RobotMap.PDP_ID);
 
           //climbDeployTalon.config_kF(POS_SLOT, F_POS_CLIMB_DEPLOY);
           //climbDeployTalon.config_kP(POS_SLOT, P_POS_CLIMB_DEPLOY);
           //climbDeployTalon.config_kI(POS_SLOT, I_POS_CLIMB_DEPLOY);
           //climbDeployTalon.config_kD(POS_SLOT, D_POS_CLIMB_DEPLOY);

           climbDeployVictor.config_kF(POS_SLOT, F_POS_CLIMB_DEPLOY);
           climbDeployVictor.config_kP(POS_SLOT, P_POS_CLIMB_DEPLOY);
           climbDeployVictor.config_kI(POS_SLOT, I_POS_CLIMB_DEPLOY);
           climbDeployVictor.config_kD(POS_SLOT, D_POS_CLIMB_DEPLOY);
 
           //climbDeployTalon.setNeutralMode(NEUTRAL_MODE_CLIMB_DEPLOY);
           //climbFollow1.setNeutralMode(NEUTRAL_MODE_CLIMB_DEPLOY);
           //climbFollow2.setNeutralMode(NEUTRAL_MODE_CLIMB_DEPLOY);
           //climbDeployTalon.setSensorPhase(false);

           climbDeployVictor.setNeutralMode(NEUTRAL_MODE_CLIMB_DEPLOY);
           climbDeployVictor.setSensorPhase(false);
 
           //climbDeployTalon.enableCurrentLimit(true);
           //climbDeployTalon.configContinuousCurrentLimit(CONTINUOUS_CURRENT_LIMIT_CLIMB_DEPLOY);
           //climbDeployTalon.configPeakCurrentLimit(PEAK_CURRENT_CLIMB_DEPLOY);
 
           //climbDeployTalon.setInverted(true);
           //climbFollow1.setInverted(true);
           //climbFollow2.setInverted(true);

           climbDeployVictor.setInverted(true);
 
           //climbDeployTalon.enableVoltageCompensation(true);
           //climbDeployTalon.configVoltageCompSaturation(VOLTAGE_COMPENSATION_LEVEL, DEFAULT_TIMEOUT);

           climbDeployVictor.enableVoltageCompensation(true);
           climbDeployVictor.configVoltageCompSaturation(VOLTAGE_COMPENSATION_LEVEL, DEFAULT_TIMEOUT);
 
           //climbDeployTalon.configClosedloopRamp(VOLTAGE_RAMP_RATE_CLIMB_DEPLOY.get(Time.Unit.SECOND));
           //climbDeployTalon.configOpenloopRamp(VOLTAGE_RAMP_RATE_CLIMB_DEPLOY.get(Time.Unit.SECOND));

           climbDeployVictor.configOpenloopRamp(VOLTAGE_RAMP_RATE_CLIMB_DEPLOY.get(Time.Unit.SECOND));
 
           //climbDeployTalon.getSensorCollection().setQuadraturePosition(0, DEFAULT_TIMEOUT);
 
           climbDeployVictor.setSelectedSensorPosition(0, 0, DEFAULT_TIMEOUT);

           if(EnabledSubsystems.CLIMB_DEPLOY_DUMB_ENABLED){
               //climbDeployTalon.set(ControlMode.PercentOutput, 0);
               climbDeployVictor.set(ControlMode.PercentOutput, 0);
           }else{
               //climbDeployTalon.set(ControlMode.Velocity, 0);
               climbDeployVictor.set(ControlMode.Velocity, 0);
           }
       }
       SmartDashboardInit();
 
   }
 
   public synchronized static void init(){
       if(singleton == null){
           singleton = new ClimbDeploy();
           //singleton.setDefaultCommand(new ClimbDeployJoystickCommand());
       }
   }
 
   public static ClimbDeploy getInstance(){
       if(singleton == null){
           init();
       }
       return singleton;
   }
 
   public void disable(){
       /*
       if(climbDeployTalon != null){
           climbDeployTalon.set(ControlMode.PercentOutput, 0);
       }
       */
      if(climbDeployVictor != null)
      {
          climbDeployVictor.set(ControlMode.PercentOutput, 0);
      }
   }
 
   public void SmartDashboardInit(){
       if(EnabledSubsystems.CLIMB_DEPLOY_SMARTDASHBOARD_DEBUG_ENABLED){
           SmartDashboard.putNumber("F_POS_CLIMB_DEPLOY", F_POS_CLIMB_DEPLOY);
           SmartDashboard.putNumber("P_POS_CLIMB_DEPLOY", P_POS_CLIMB_DEPLOY);
           SmartDashboard.putNumber("I_POS_CLIMB_DEPLOY", I_POS_CLIMB_DEPLOY);
           SmartDashboard.putNumber("D_POS_CLIMB_DEPLOY", D_POS_CLIMB_DEPLOY);

           SmartDashboard.putNumber("CLIMB DEPLOY PERCENT", 0);
 
       //    SmartDashboard.putNumber("run percent", 0);
 
           SmartDashboard.putNumber("Set Distance Climb Deploy", DEPLOY_DISTANCE.get(Distance.Unit.INCH));

           SmartDashboard.putNumber("Climb Deploy Encoder", climbDeployVictor.getSelectedSensorPosition());
 
           SmartDashboard.putNumber("Climb Deploy Position", getPosition().get(Distance.Unit.INCH));
           //SmartDashboard.putNumber("Climb Deploy Current", climbDeployTalon.getStatorCurrent());
           //Don't know if Victor has a .getStatorCurrent() equivalent
           //SmartDashboard.putNumber("Climb Deploy Current", pdp.getCurrent(RobotMap.CLIMB_DEPLOY_CHANNEL));
           SmartDashboard.putNumber("Goal Position Climb Deploy: ", ClimbDeploy.goalPositionClimb.get(Distance.Unit.INCH));
       }
   }
 
   public Distance getPosition(){
       /*
       if(climbDeployTalon != null){
           return new Distance(climbDeployTalon.getSelectedSensorPosition(), Distance.Unit.MAGNETIC_ENCODER_TICK_CLIMB_DEPLOY);
       }
       */
      if(climbDeployVictor != null)
      {
          return new Distance(climbDeployVictor.getSelectedSensorPosition(), Distance.Unit.MAGNETIC_ENCODER_TICK_CLIMB_DEPLOY);
      }
       return Distance.ZERO;
   }
 
   public void setPosition(Distance distance)
   {
       /*
       if(climbDeployTalon != null)
       {
           climbDeployTalon.selectProfileSlot(POS_SLOT, DEFAULT_TIMEOUT);
           climbDeployTalon.set(ControlMode.Position, distance.get(Unit.MAGNETIC_ENCODER_TICK_CLIMB_DEPLOY));
       }
       */
       if(climbDeployVictor != null)
       {
           climbDeployVictor.selectProfileSlot(POS_SLOT, DEFAULT_TIMEOUT);
           climbDeployVictor.set(ControlMode.Position, distance.get(Unit.MAGNETIC_ENCODER_TICK_CLIMB_DEPLOY));
       }
   }
 
   public void Deploy()
   {
       /*
       if(climbDeployTalon != null)
       {
           climbDeployTalon.selectProfileSlot(POS_SLOT, DEFAULT_TIMEOUT);
           climbDeployTalon.set(ControlMode.Position, DEPLOY_DISTANCE.get(Unit.MAGNETIC_ENCODER_TICK_CLIMB_DEPLOY));
       }
       */
      if(climbDeployVictor != null)
      {
          climbDeployVictor.selectProfileSlot(POS_SLOT, DEFAULT_TIMEOUT);
          climbDeployVictor.set(ControlMode.Position, DEPLOY_DISTANCE.get(Unit.MAGNETIC_ENCODER_TICK_CLIMB_DEPLOY));
      }
   }
 
   public void setMotorSpeedRaw(double percent)
   {
       /*
       if(climbDeployTalon != null)
       {
           //climbDeployTalon.selectProfileSlot(VEL_SLOT, DEFAULT_TIMEOUT);
           climbDeployTalon.set(ControlMode.PercentOutput, percent);
           //climbFollow1.set(ControlMode.PercentOutput, percent);
           //climbFollow2.set(ControlMode.PercentOutput, percent);
       }
       */
      if(climbDeployVictor != null)
      {
          climbDeployVictor.set(ControlMode.PercentOutput, percent);
      }
   }
 
   public void smartDashboardInfo(){
       /*
       if(climbDeployVictor != null){
 
           if(EnabledSubsystems.CLIMB_DEPLOY_SMARTDASHBOARD_DEBUG_ENABLED){
               F_POS_CLIMB_DEPLOY = SmartDashboard.getNumber("F_POS_CLIMB_DEPLOY", F_POS_CLIMB_DEPLOY);
               P_POS_CLIMB_DEPLOY = SmartDashboard.getNumber("P_POS_CLIMB_DEPLOY", P_POS_CLIMB_DEPLOY);
               I_POS_CLIMB_DEPLOY = SmartDashboard.getNumber("I_POS_CLIMB_DEPLOY", I_POS_CLIMB_DEPLOY);
               D_POS_CLIMB_DEPLOY = SmartDashboard.getNumber("D_POS_CLIMB_DEPLOY", D_POS_CLIMB_DEPLOY);
 
               //climbDeployTalon.config_kF(POS_SLOT, F_POS_CLIMB_DEPLOY);
               //climbDeployTalon.config_kP(POS_SLOT, P_POS_CLIMB_DEPLOY);
               //climbDeployTalon.config_kI(POS_SLOT, I_POS_CLIMB_DEPLOY);
               //climbDeployTalon.config_kD(POS_SLOT, D_POS_CLIMB_DEPLOY);
 
               SmartDashboard.putNumber("Set Distance Climb Deploy", DEPLOY_DISTANCE.get(Distance.Unit.INCH));
 
               //SmartDashboard.putNumber("Climb Deploy Current", climbDeployTalon.getStatorCurrent());
 
               goalPositionClimb = new Distance(SmartDashboard.getNumber("Goal Position Climb Deploy: ", goalPositionClimb.get(Distance.Unit.INCH)), Distance.Unit.INCH);
           }
       }
       */
      if(climbDeployVictor != null)
      {
        if(EnabledSubsystems.CLIMB_DEPLOY_SMARTDASHBOARD_DEBUG_ENABLED){
            F_POS_CLIMB_DEPLOY = SmartDashboard.getNumber("F_POS_CLIMB_DEPLOY", F_POS_CLIMB_DEPLOY);
            P_POS_CLIMB_DEPLOY = SmartDashboard.getNumber("P_POS_CLIMB_DEPLOY", P_POS_CLIMB_DEPLOY);
            I_POS_CLIMB_DEPLOY = SmartDashboard.getNumber("I_POS_CLIMB_DEPLOY", I_POS_CLIMB_DEPLOY);
            D_POS_CLIMB_DEPLOY = SmartDashboard.getNumber("D_POS_CLIMB_DEPLOY", D_POS_CLIMB_DEPLOY);

            //climbDeployTalon.config_kF(POS_SLOT, F_POS_CLIMB_DEPLOY);
            //climbDeployTalon.config_kP(POS_SLOT, P_POS_CLIMB_DEPLOY);
            //climbDeployTalon.config_kI(POS_SLOT, I_POS_CLIMB_DEPLOY);
            //climbDeployTalon.config_kD(POS_SLOT, D_POS_CLIMB_DEPLOY);
            climbDeployVictor.config_kF(POS_SLOT, F_POS_CLIMB_DEPLOY);
            climbDeployVictor.config_kP(POS_SLOT, P_POS_CLIMB_DEPLOY);
            climbDeployVictor.config_kI(POS_SLOT, I_POS_CLIMB_DEPLOY);
            climbDeployVictor.config_kD(POS_SLOT, D_POS_CLIMB_DEPLOY);

            SmartDashboard.putNumber("Set Distance Climb Deploy", DEPLOY_DISTANCE.get(Distance.Unit.INCH));

        //    setMotorSpeedRaw(SmartDashboard.getNumber("run percent", 0));

        setMotorSpeedRaw(SmartDashboard.getNumber("CLIMB DEPLOY PERCENT", 0));

        SmartDashboard.putNumber("Climb Deploy Encoder", climbDeployVictor.getSelectedSensorPosition());

       SmartDashboard.putNumber("Climb Deploy Position", getPosition().get(Distance.Unit.INCH));

            SmartDashboard.putNumber("Climb Deploy Position", getPosition().get(Distance.Unit.INCH));
            //SmartDashboard.putNumber("Climb Deploy Current", climbDeployTalon.getStatorCurrent());
            //SmartDashboard.putNumber("Climb Deploy Current", pdp.getCurrent(RobotMap.CLIMB_DEPLOY_CHANNEL));

            goalPositionClimb = new Distance(SmartDashboard.getNumber("Goal Position Climb Deploy: ", goalPositionClimb.get(Distance.Unit.INCH)), Distance.Unit.INCH);
        }   
      }
   }
}
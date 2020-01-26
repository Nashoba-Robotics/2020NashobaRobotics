    package edu.nr.robotics.subsystems.hood;


    import com.ctre.phoenix.motorcontrol.ControlMode;
    import com.ctre.phoenix.motorcontrol.FeedbackDevice;
    import com.ctre.phoenix.motorcontrol.NeutralMode;
    import com.ctre.phoenix.motorcontrol.can.TalonSRX;
    import com.revrobotics.CANSparkMax;
    import com.revrobotics.ControlType;
    import com.revrobotics.CANSparkMax.IdleMode;
    import com.revrobotics.CANSparkMaxLowLevel.*;
    import edu.nr.lib.commandbased.NRSubsystem;
    import edu.nr.lib.motorcontrollers.CTRECreator;
    import edu.nr.lib.motorcontrollers.SparkMax;
    import edu.nr.lib.units.Distance;
    import edu.nr.lib.units.Speed;
    import edu.nr.lib.units.Acceleration;
    import edu.nr.lib.units.Angle;
    import edu.nr.lib.units.AngularAcceleration;
    import edu.nr.lib.units.AngularSpeed;
    import edu.nr.lib.units.Time;
    import edu.nr.lib.units.Angle.Unit;
    import edu.nr.robotics.RobotMap;
    import edu.nr.robotics.subsystems.EnabledSubsystems;
    import edu.nr.robotics.subsystems.sensors.DigitalSensor;
    import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

    public class Hood extends NRSubsystem{

        private static Hood Singleton;

        private static TalonSRX hoodTalon;
        private static CANSparkMax hoodSpark;

        public static final double ENCODER_TICKS_PER_DEGREE_HOOD = 2048 / 360; // unknown for sparkmax

        public static final int VOLTAGE_COMPENSATION_LEVEL = 12;
        public static final double MIN_MOVE_VOLTAGE = 0.0; //unknown for sparkmax
        public static final int DEFAULT_TIMEOUT = 0;
        public static Time VOLTAGE_RAMP_RATE_HOOD = new Time(0.05, Time.Unit.SECOND);
        public static final double PROFILE_VEL_PERCENT_HOOD = 0.6; //change
        public static final double PROFILE_ACCEL_PERCENT_HOOD = 0.6;

        public static final double MOTION_MAGIC_PERCENT = 0.8; // n/a for sparkmax

        public static final AngularSpeed MAX_SPEED_HOOD = new AngularSpeed(45, Angle.Unit.DEGREE, Time.Unit.SECOND);
        public static final AngularAcceleration MAX_ACCELERATION_HOOD = new AngularAcceleration(200, Angle.Unit.DEGREE, Time.Unit.SECOND, Time.Unit.SECOND);
        public DigitalSensor limSwitchTop = new DigitalSensor(RobotMap.LIM_HOOD_UPPER); // change IDs
        public DigitalSensor limSwitchBottom = new DigitalSensor(RobotMap.LIM_HOOD_LOWER);

        //Change this

        public final Angle uppermost = new Angle(45, Angle.Unit.DEGREE);
        public final Angle lowermost = new Angle(0, Angle.Unit.DEGREE);

        public static double F_POS_HOOD = 0;
        public static double P_POS_HOOD = 0;
        public static double I_POS_HOOD = 0;
        public static double D_POS_HOOD = 0;

        public static double F_VEL_HOOD = 0;
        public static double P_VEL_HOOD = 0;
        public static double I_VEL_HOOD = 0;
        public static double D_VEL_HOOD = 0;

        public static final int PEAK_CURRENT_HOOD = 60;
        public static final int CONTINUOUS_CURRENT_LIMIT_HOOD = 40;

        public static final NeutralMode NEUTRAL_MODE_HOOD = NeutralMode.Brake;

        public static final IdleMode IDLE_MODE_HOOD = IdleMode.kBrake; // for spark

        private AngularSpeed speedSetPointHood = AngularSpeed.ZERO;

        public static Angle setAngleHood = Angle.ZERO;
        public static Angle deltaAngleHood = Angle.ZERO;
        public static Angle goalAngleHood = Angle.ZERO;


        public static final int MOTION_MAGIC_SLOT = 2; // n/a for sparkmax
        public static final int MOTION_MAGIC_MULTIPLIER = 3; // Garrison Calculation Constant # 4

        public static final AngularAcceleration MAX_ACCEL = new AngularAcceleration(580, Angle.Unit.DEGREE, Time.Unit.SECOND, Time.Unit.SECOND);
        public static final AngularSpeed MAX_SPEED = new AngularSpeed(80, Angle.Unit.DEGREE, Time.Unit.SECOND);

        public static final int PID_TYPE = 0;

        public static final int VEL_SLOT = 0;
        public static final int POS_SLOT = 1;



        private Hood(){
            //n in northeastern stands for "nah". Y in emory stands for "y would anyone go there?"
            if(EnabledSubsystems.HOOD_ENABLED){

            hoodSpark = SparkMax.createSpark(RobotMap.HOOD_TALON, MotorType.kBrushless);

            hoodSpark.getPIDController().setFF(F_POS_HOOD, 0);
            hoodSpark.getPIDController().setP(P_POS_HOOD, 0);
            hoodSpark.getPIDController().setI(I_POS_HOOD, 0);
            hoodSpark.getPIDController().setD(D_POS_HOOD, 0);

            hoodSpark.setIdleMode(IDLE_MODE_HOOD);

            hoodSpark.setInverted(true);

            hoodSpark.setSmartCurrentLimit(CONTINUOUS_CURRENT_LIMIT_HOOD);
            hoodSpark.setSecondaryCurrentLimit(PEAK_CURRENT_HOOD);

            hoodSpark.enableVoltageCompensation(VOLTAGE_COMPENSATION_LEVEL);

            hoodSpark.setClosedLoopRampRate(VOLTAGE_RAMP_RATE_HOOD.get(Time.Unit.SECOND));
            hoodSpark.setOpenLoopRampRate(VOLTAGE_RAMP_RATE_HOOD.get(Time.Unit.SECOND));

            hoodSpark.getPIDController().setOutputRange(-1, 1, VEL_SLOT);
            hoodSpark.getPIDController().setOutputRange(-1, 1, POS_SLOT);

            hoodTalon = CTRECreator.createMasterTalon(RobotMap.HOOD_TALON);

            hoodTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, PID_TYPE, DEFAULT_TIMEOUT);


            hoodTalon.config_kF(VEL_SLOT, 0, DEFAULT_TIMEOUT);
            hoodTalon.config_kP(VEL_SLOT, P_VEL_HOOD, DEFAULT_TIMEOUT);
            hoodTalon.config_kI(VEL_SLOT, I_VEL_HOOD, DEFAULT_TIMEOUT);
            hoodTalon.config_kD(VEL_SLOT, D_VEL_HOOD, DEFAULT_TIMEOUT);

            hoodTalon.config_kF(MOTION_MAGIC_SLOT, F_POS_HOOD, DEFAULT_TIMEOUT);
            hoodTalon.config_kP(MOTION_MAGIC_SLOT, P_POS_HOOD, DEFAULT_TIMEOUT);
            hoodTalon.config_kI(MOTION_MAGIC_SLOT, I_POS_HOOD, DEFAULT_TIMEOUT);
            hoodTalon.config_kD(MOTION_MAGIC_SLOT, D_POS_HOOD, DEFAULT_TIMEOUT);

            hoodTalon.config_kF(POS_SLOT, F_POS_HOOD, DEFAULT_TIMEOUT);
            hoodTalon.config_kP(POS_SLOT, P_POS_HOOD, DEFAULT_TIMEOUT);
            hoodTalon.config_kI(POS_SLOT, I_POS_HOOD, DEFAULT_TIMEOUT);
            hoodTalon.config_kD(POS_SLOT, D_POS_HOOD, DEFAULT_TIMEOUT);

            hoodTalon.setNeutralMode(NEUTRAL_MODE_HOOD);

            hoodTalon.setInverted(false);
            //Change to Talon Version
            hoodTalon.setSensorPhase(false);

            hoodTalon.configContinuousCurrentLimit(CONTINUOUS_CURRENT_LIMIT_HOOD);

            hoodTalon.enableVoltageCompensation(true);
            hoodTalon.configVoltageCompSaturation(VOLTAGE_COMPENSATION_LEVEL, DEFAULT_TIMEOUT);
            
            hoodTalon.enableCurrentLimit(true);
            hoodTalon.configPeakCurrentLimit(PEAK_CURRENT_HOOD, DEFAULT_TIMEOUT);
            hoodTalon.configPeakCurrentDuration(CONTINUOUS_CURRENT_LIMIT_HOOD, DEFAULT_TIMEOUT);

            hoodTalon.configClosedloopRamp(VOLTAGE_RAMP_RATE_HOOD.get(Time.Unit.SECOND), DEFAULT_TIMEOUT);
            hoodTalon.configOpenloopRamp(VOLTAGE_RAMP_RATE_HOOD.get(Time.Unit.SECOND), DEFAULT_TIMEOUT);

            //Change Magnetic_encoder_tick_aux_drive to angle stuff, angular acc and velocity
            hoodTalon.configMotionCruiseVelocity((int) MAX_SPEED.mul(PROFILE_VEL_PERCENT_HOOD).get(Angle.Unit.HOOD_ENCODER_TICK, Time.Unit.HUNDRED_MILLISECOND), DEFAULT_TIMEOUT);
            hoodTalon.configMotionAcceleration((int) MAX_ACCEL.mul(PROFILE_ACCEL_PERCENT_HOOD).get(Angle.Unit.HOOD_ENCODER_TICK, Time.Unit.HUNDRED_MILLISECOND, Time.Unit.HUNDRED_MILLISECOND), DEFAULT_TIMEOUT);

            hoodTalon.getSensorCollection().setQuadraturePosition(0, DEFAULT_TIMEOUT);

            }
            smartDashboardInit();
        }

        public synchronized static void init()
        {
            if(Singleton == null)
                Singleton = new Hood();
        }

        public static Hood getInstance()
        {
            if(Singleton == null)
            {
                init();
            }
            return Singleton;
        }

        public static Angle getAngle()
        {
            if(hoodTalon != null)
            {
                return new Angle(hoodTalon.getSelectedSensorPosition() / ENCODER_TICKS_PER_DEGREE_HOOD, Angle.Unit.DEGREE);
            //    return new Angle(hoodSpark.getEncoder().getPosition() / ENCODER_TICKS_PER_DEGREE_HOOD, Angle.Unit.DEGREE); for sparkmax
            } 
            return Angle.ZERO;
        }

        public static void setAngle(Angle target)
        {
            setAngleHood = target;

            if(hoodSpark != null){
                hoodSpark.getPIDController().setReference(setAngleHood.get(Angle.Unit.HOOD_ENCODER_TICK), ControlType.kPosition, POS_SLOT);
            }
            //Motion Magic
            /*
            hoodTalon.selectProfileSlot(MOTION_MAGIC_SLOT, DEFAULT_TIMEOUT);

            hoodTalon.configMotionCruiseVelocity(MOTION_MAGIC_MULTIPLIER * (int) MAX_SPEED_HOOD.mul(MOTION_MAGIC_PERCENT).get(Angle.Unit.DEGREE, Time.Unit.HUNDRED_MILLISECOND), DEFAULT_TIMEOUT);
            hoodTalon.configMotionAcceleration(MOTION_MAGIC_MULTIPLIER * (int) MAX_ACCELERATION_HOOD.mul(MOTION_MAGIC_PERCENT).get(Angle.Unit.DEGREE, Time.Unit.HUNDRED_MILLISECOND, Time.Unit.HUNDRED_MILLISECOND), DEFAULT_TIMEOUT);

            hoodTalon.set(ControlMode.MotionMagic, setAngleHood.get(Angle.Unit.HOOD_ENCODER_TICK));*/

            /*hoodTalon.selectProfileSlot(MOTION_MAGIC_SLOT, DEFAULT_TIMEOUT);
            
            hoodTalon.configMotionCruiseVelocity(MOTION_MAGIC_MULTIPLIER*(int) MAX_SPEED.mul(MOTION_MAGIC_PERCENT).get(
                    Angle.Unit.HOOD_ENCODER_TICK, Time.Unit.HUNDRED_MILLISECOND),
                            DEFAULT_TIMEOUT);
            System.out.println("Motion cruise velocity: " + MOTION_MAGIC_MULTIPLIER*(int) MAX_SPEED.mul(MOTION_MAGIC_PERCENT).get(
                Angle.Unit.HOOD_ENCODER_TICK, Time.Unit.HUNDRED_MILLISECOND));

            hoodTalon.configMotionAcceleration(MOTION_MAGIC_MULTIPLIER*(int) MAX_ACCEL.mul(MOTION_MAGIC_PERCENT).get(
                    Angle.Unit.HOOD_ENCODER_TICK, Time.Unit.HUNDRED_MILLISECOND, Time.Unit.HUNDRED_MILLISECOND),
                    DEFAULT_TIMEOUT);
                System.out.println("Motion magic acceleration" + MOTION_MAGIC_MULTIPLIER*(int) MAX_ACCEL.mul(MOTION_MAGIC_PERCENT).get(
                    Angle.Unit.HOOD_ENCODER_TICK, Time.Unit.HUNDRED_MILLISECOND, Time.Unit.HUNDRED_MILLISECOND));
            
            hoodTalon.set(ControlMode.MotionMagic, setAngleHood.get(Angle.Unit.HOOD_ENCODER_TICK));*/

            if(hoodTalon != null){
                hoodTalon.selectProfileSlot(POS_SLOT, DEFAULT_TIMEOUT);
                hoodTalon.set(ControlMode.Position, setAngleHood.get(Angle.Unit.HOOD_ENCODER_TICK));
            }
        }

        public void smartDashboardInit(){
            if(EnabledSubsystems.HOOD_SMARTDASHBOARD_BASIC_ENABLED){
            //SmartDashboard.putNumber("Spark Encoder Position Hood", hoodSpark.getEncoder().getPosition()); for spark
            //SmartDashboard.putNumber("Spark Hood Current", hoodSpark.getOutputCurrent());for spark
            
            SmartDashboard.putNumber("Testing encoder position", hoodTalon.getSelectedSensorPosition());
            SmartDashboard.putNumber("Hood Current", hoodTalon.getStatorCurrent());

            SmartDashboard.putNumber("F_POS_HOOD", F_POS_HOOD);
            SmartDashboard.putNumber("P_POS_HOOD", P_POS_HOOD);
            SmartDashboard.putNumber("I_POS_HOOD", I_POS_HOOD);
            SmartDashboard.putNumber("D_POS_HOOD", D_POS_HOOD);

            SmartDashboard.putNumber("F_VEL_HOOD", F_VEL_HOOD);
            SmartDashboard.putNumber("P_VEL_HOOD", P_VEL_HOOD);
            SmartDashboard.putNumber("I_VEL_HOOD", I_VEL_HOOD);
            SmartDashboard.putNumber("D_VEL_HOOD", D_VEL_HOOD);

            SmartDashboard.putNumber("Hood Goal Angle", goalAngleHood.get(Angle.Unit.DEGREE));
            }

        }

        public void disable()
        {
            if(hoodTalon != null){
                hoodTalon.set(ControlMode.PercentOutput, 0);
            }
            if(hoodSpark != null){
                hoodSpark.set(0);
            }
        }

        public AngularSpeed getSpeed(){
            if(hoodTalon != null){
                return new AngularSpeed(hoodTalon.getSelectedSensorVelocity(), Angle.Unit.HOOD_ENCODER_TICK, Time.Unit.HUNDRED_MILLISECOND);
            }

            if(hoodSpark != null){
                return new AngularSpeed(hoodSpark.getEncoder().getVelocity(), Angle.Unit.ROTATION, Time.Unit.MINUTE);
            }
            return AngularSpeed.ZERO;
        }

        public void setMotorSpeedRaw(double percent){
            if(hoodTalon != null){
                hoodTalon.set(ControlMode.PercentOutput, percent);
            }
            if(hoodSpark != null){
                hoodSpark.set(percent);
            }
        }

        public void smartDashboardInfo()
        {
            if(hoodSpark != null){

                if(EnabledSubsystems.HOOD_SMARTDASHBOARD_BASIC_ENABLED){
                    SmartDashboard.putNumber("Spark Encoder Position Hood", hoodSpark.getEncoder().getPosition());
                    SmartDashboard.putNumber("Spark Hood Current", hoodSpark.getOutputCurrent());

                    SmartDashboard.putNumber("Hood Spark Angular Speed", hoodSpark.getEncoder().getVelocity());
                    SmartDashboard.putNumber("Hood Spark Angle", getAngle().get(Angle.Unit.DEGREE));
                    SmartDashboard.putNumber("Hood setAngle", setAngleHood.get(Angle.Unit.DEGREE));

                    F_POS_HOOD = SmartDashboard.getNumber("F_POS_HOOD", 0);
                    P_POS_HOOD = SmartDashboard.getNumber("P_POS_HOOD", 0);
                    I_POS_HOOD = SmartDashboard.getNumber("I_POS_HOOD", 0);
                    D_POS_HOOD = SmartDashboard.getNumber("D_POS_HOOD", 0);

                    F_VEL_HOOD = SmartDashboard.getNumber("F_VEL_HOOD", 0);
                    P_VEL_HOOD = SmartDashboard.getNumber("P_VEL_HOOD", 0);
                    I_VEL_HOOD = SmartDashboard.getNumber("I_VEL_HOOD", 0);
                    D_VEL_HOOD = SmartDashboard.getNumber("D_VEL_HOOD", 0);

                }
                goalAngleHood = new Angle(SmartDashboard.getNumber("Hood Goal Angle", goalAngleHood.get(Angle.Unit.DEGREE)), Angle.Unit.DEGREE);

            }

            if(hoodTalon != null){

                if(EnabledSubsystems.HOOD_SMARTDASHBOARD_BASIC_ENABLED){
                    SmartDashboard.putNumber("Testing encoder position", hoodTalon.getSelectedSensorPosition());
                    //setMotorSpeedRaw(.7);
                    SmartDashboard.putNumber("Hood Angular Speed", getSpeed().get(Angle.Unit.DEGREE,Time.Unit.SECOND));
                    SmartDashboard.putNumber("Hood Current", hoodTalon.getStatorCurrent());
                    SmartDashboard.putString("Hood Angle", getAngle().get(Angle.Unit.DEGREE) + " : " + setAngleHood.get(Angle.Unit.DEGREE));	

                    F_POS_HOOD = SmartDashboard.getNumber("F_POS_HOOD", 0);
                    P_POS_HOOD = SmartDashboard.getNumber("P_POS_HOOD", 0);
                    I_POS_HOOD = SmartDashboard.getNumber("I_POS_HOOD", 0);
                    D_POS_HOOD = SmartDashboard.getNumber("D_POS_HOOD", 0);

                    F_VEL_HOOD = SmartDashboard.getNumber("F_VEL_HOOD", 0);
                    P_VEL_HOOD = SmartDashboard.getNumber("P_VEL_HOOD", 0);
                    I_VEL_HOOD = SmartDashboard.getNumber("I_VEL_HOOD", 0);
                    D_VEL_HOOD = SmartDashboard.getNumber("D_VEL_HOOD", 0);

                    hoodTalon.config_kF(POS_SLOT, F_POS_HOOD, DEFAULT_TIMEOUT);
                    hoodTalon.config_kP(POS_SLOT, P_POS_HOOD, DEFAULT_TIMEOUT);
                    hoodTalon.config_kI(POS_SLOT, I_POS_HOOD, DEFAULT_TIMEOUT);
                    hoodTalon.config_kD(POS_SLOT, D_POS_HOOD, DEFAULT_TIMEOUT);

                    hoodTalon.config_kF(VEL_SLOT, F_VEL_HOOD, DEFAULT_TIMEOUT);
                    hoodTalon.config_kP(VEL_SLOT, P_VEL_HOOD, DEFAULT_TIMEOUT);
                    hoodTalon.config_kI(VEL_SLOT, I_VEL_HOOD, DEFAULT_TIMEOUT);
                    hoodTalon.config_kD(VEL_SLOT, D_VEL_HOOD, DEFAULT_TIMEOUT);
                }
                if(EnabledSubsystems.HOOD_SMARTDASHBOARD_DEBUG_ENABLED){
                    SmartDashboard.putString("Hood Control Mode", hoodTalon.getControlMode().toString());
                    SmartDashboard.putNumber("Hood Voltage", hoodTalon.getMotorOutputVoltage());
                    SmartDashboard.putNumber("Hood Raw Position Ticks", hoodTalon.getSelectedSensorPosition());
                }
                goalAngleHood = new Angle(SmartDashboard.getNumber("Hood Goal Angle", goalAngleHood.get(Angle.Unit.DEGREE)), Angle.Unit.DEGREE);
            }
        //    System.out.println("Hood angle" + goalAngleHood.get(Angle.Unit.DEGREE));
        }

        public double getCurrent()
        {
            if(hoodTalon != null)  
                return hoodTalon.getStatorCurrent();
            if(hoodSpark != null){
                return hoodSpark.getOutputCurrent();
            }
            return 0;
        }

        public void periodic()
        {
            //check if limits are triggered, set to max / min hood spot if applicable

        }
}
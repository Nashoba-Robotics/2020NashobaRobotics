package edu.nr.lib.units;

import edu.nr.lib.Units;
import edu.nr.robotics.subsystems.hood.Hood;
import edu.nr.robotics.subsystems.shooter.Shooter;
import edu.nr.robotics.subsystems.transfer.Transfer;
import edu.nr.robotics.subsystems.turret.Turret;

public class Angle {
	
	public static final Angle ZERO = new Angle(0, Unit.DEGREE);
	private double val;
	private Unit type;
	
	public enum Unit implements GenericUnit {
		DEGREE, ROTATION, RADIAN, MAGNETIC_ENCODER_NATIVE_UNITS, 
		MAGNETIC_ENCODER_TICK, TURRET_ENCODER_TICK, SHOOTER_ENCODER_TICK, HOOD_ENCODER_TICK, TRANSFER_ENCODER_TICK;
		
		public static final Unit defaultUnit = DEGREE;
		
		private static final double ROTATIONS_PER_DEGREE = 1/360.0;
		private static final double MAGNETIC_ENCODER_NATIVE_UNITS_PER_ROTATION = Units.MAGNETIC_NATIVE_UNITS_PER_REV;
		private static final double MAGNETIC_ENCODER_NATIVE_UNITS_PER_DEGREE = MAGNETIC_ENCODER_NATIVE_UNITS_PER_ROTATION * ROTATIONS_PER_DEGREE; 
		private static final double RADIANS_PER_DEGREE = Math.PI / 180.0;
		private static final double MAGNETIC_ENCODER_TICKS_PER_DEGREE = MAGNETIC_ENCODER_NATIVE_UNITS_PER_ROTATION * ROTATIONS_PER_DEGREE / Units.NATIVE_UNITS_PER_TICK;
		private static final double TURRET_ENCODER_TICKS_PER_DEGREE = Turret.ENCODER_TICKS_PER_DEGREE_SPARK;
		private static final double SHOOTER_ENCODER_TICKS_PER_DEGREE = Shooter.ENCODER_TICKS_PER_DEGREE_SHOOTER;
		private static final double HOOD_ENCODER_TICKS_PER_DEGREE = Hood.ENCODER_TICKS_PER_DEGREE_HOOD;
		private static final double TRANSFER_ENCODER_TICKS_PER_DEGREE = Transfer.ENCODER_TICKS_PER_DEGREE;

		
		public double convertToDefault(double val) {
			if(this == Unit.DEGREE) {
				return val;
			}
			if(this == Unit.ROTATION) {
				return val / ROTATIONS_PER_DEGREE;
			}
			if(this == Unit.RADIAN) {
				return val / RADIANS_PER_DEGREE;
			}
			if(this == MAGNETIC_ENCODER_NATIVE_UNITS) {
				return val / MAGNETIC_ENCODER_NATIVE_UNITS_PER_DEGREE;
			}
			if(this == Unit.MAGNETIC_ENCODER_TICK) {
				return val / MAGNETIC_ENCODER_TICKS_PER_DEGREE;
			}
			if(this == Unit.TURRET_ENCODER_TICK){
				return val / TURRET_ENCODER_TICKS_PER_DEGREE;
			}
			if(this == Unit.SHOOTER_ENCODER_TICK){
				return val / SHOOTER_ENCODER_TICKS_PER_DEGREE;
			}
			if(this == Unit.HOOD_ENCODER_TICK){
				return val / HOOD_ENCODER_TICKS_PER_DEGREE;
			}
			if(this == Unit.TRANSFER_ENCODER_TICK){
				return val / TRANSFER_ENCODER_TICKS_PER_DEGREE;
			}
			return 0;
		}
		
		public double convertFromDefault(double val) {
			if(this == Unit.DEGREE) {
				return val;
			}
			if(this == Unit.ROTATION) {
				return ROTATIONS_PER_DEGREE * val;
			}
			if(this == Unit.RADIAN) {
				return RADIANS_PER_DEGREE * val;
			}
			if(this == MAGNETIC_ENCODER_NATIVE_UNITS) {
				return val * MAGNETIC_ENCODER_NATIVE_UNITS_PER_DEGREE;
			}
			if(this == MAGNETIC_ENCODER_TICK) {
				return val * MAGNETIC_ENCODER_TICKS_PER_DEGREE;
			}
			if(this == TURRET_ENCODER_TICK){
				return val * TURRET_ENCODER_TICKS_PER_DEGREE;
			}
			if(this == SHOOTER_ENCODER_TICK){
				return val * SHOOTER_ENCODER_TICKS_PER_DEGREE;
			}
			if(this == HOOD_ENCODER_TICK){
				return val * HOOD_ENCODER_TICKS_PER_DEGREE;
			}
			if(this == Unit.TRANSFER_ENCODER_TICK){
				return val * TRANSFER_ENCODER_TICKS_PER_DEGREE;
			}
			return 0;
		}
}
	
	public Angle(double val, Unit type) {
		this.val = val;
		this.type = type;
	}
	
	public double get(Unit toType) {
		return GenericUnit.convert(val, type, toType);
	}

	public double getDefault() {
		return get(Unit.defaultUnit);
	}
	
	public Angle sub(Angle angleTwo) {
		return new Angle(this.get(Unit.defaultUnit) - angleTwo.get(Unit.defaultUnit), Unit.defaultUnit);
	}
	
	public Angle add(Angle angleTwo) {
		return new Angle(this.get(Unit.defaultUnit) + angleTwo.get(Unit.defaultUnit), Unit.defaultUnit);
	}
	
	public Angle mul(double x) {
		return new Angle(this.get(Unit.defaultUnit) * x, Unit.defaultUnit);
	}
	
	public double div(Angle angleTwo) {
		return this.get(Unit.defaultUnit) / angleTwo.get(Unit.defaultUnit);
	}
	
	public double cos() {
		return Math.cos(this.get(Unit.RADIAN));
	}
	
	public double sin() {
		return Math.sin(this.get(Unit.RADIAN));
	}
	
	public double tan() {
		return Math.tan(this.get(Unit.RADIAN));
	}
	
	public boolean lessThan(Angle angleTwo) {
		return this.get(Unit.defaultUnit) < angleTwo.get(Unit.defaultUnit);
	}

	public boolean greaterThan(Angle angleTwo) {
		return this.get(Unit.defaultUnit) > angleTwo.get(Unit.defaultUnit);
	}
	
	public Angle negate() {
		return new Angle(-this.get(Unit.defaultUnit), Unit.defaultUnit);
	}
	
	public Angle abs() {
		return new Angle(Math.abs(this.get(Unit.defaultUnit)), Unit.defaultUnit);
	}
	
	public double signum() {
		return Math.signum(this.get(Unit.defaultUnit));
	}
	
	@Override
	public boolean equals(Object angleTwo) {
		if(angleTwo instanceof Angle) {
			return this.get(Unit.defaultUnit) == ((Angle) angleTwo).get(Unit.defaultUnit);
		} else {
			return false;
		}
	}

}

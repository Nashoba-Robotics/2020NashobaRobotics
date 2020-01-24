package edu.nr.lib.units;

import edu.nr.lib.Units;
import edu.nr.robotics.subsystems.drive.Drive;
import edu.nr.robotics.subsystems.indexer.Indexer;


public class Distance {
	
	public static final Distance ZERO = new Distance(0, Unit.defaultUnit);
	private double val;
	private Unit type;
	
	public enum Unit implements GenericUnit {
		FOOT, INCH, METER, MAGNETIC_ENCODER_TICK_DRIVE, MAGNETIC_ENCODER_TICK_HOOD, MAGNETIC_ENCODER_TICK_INDEXER;
		
		public static final Unit defaultUnit = INCH;
		
		/**
		 * For the drive
		 */
		private static final double ENCODER_TICK_DRIVE_PER_INCH = Drive.EFFECTIVE_ENC_TICK_PER_INCH_DRIVE;			
		private static final double FOOT_PER_INCH = 1.0/Units.INCHES_PER_FOOT;
		private static final double METER_PER_INCH = 1.0/Units.INCHES_PER_METER;
		private static final double ENCODER_TICK_INDEXER_PER_INCH = Indexer.ENCODER_TICKS_PER_INCH_BALL_MOVED;
		
		public double convertToDefault(double val) {
			if(this == Unit.defaultUnit) {
				return val;
			}
			if(this == Unit.FOOT) {
				return val / FOOT_PER_INCH;
			}
			if(this == Unit.METER) {
				return val / METER_PER_INCH;
			}
			if(this == Unit.MAGNETIC_ENCODER_TICK_DRIVE) {
				return val / ENCODER_TICK_DRIVE_PER_INCH;
			}
			if(this == Unit.MAGNETIC_ENCODER_TICK_INDEXER){
				return val / ENCODER_TICK_INDEXER_PER_INCH;
			}
			return 0;
		}
		
		public double convertFromDefault(double val) {
			if(this == Unit.defaultUnit) {
				return val;
			}
			if(this == Unit.FOOT) {
				return FOOT_PER_INCH * val;
			}
			if(this == Unit.METER) {
				return METER_PER_INCH * val;
			}
			if(this == Unit.MAGNETIC_ENCODER_TICK_DRIVE) {
				return ENCODER_TICK_DRIVE_PER_INCH * val;
			}
			if(this == Unit.MAGNETIC_ENCODER_TICK_INDEXER){
				return ENCODER_TICK_INDEXER_PER_INCH;
			}
			return 0;
		}
}
	
	public Distance(double val, Unit type) {
		this.val = val;
		this.type = type;
	}
	
	public double get(Unit toType) {
		return type.convert(val, toType);
	}

	public double getDefault() {
		return get(Unit.defaultUnit);
	}
	
	public Distance sub(Distance distanceTwo) {
		return new Distance(this.get(Unit.defaultUnit) - distanceTwo.get(Unit.defaultUnit), Unit.defaultUnit);
	}
	
	public Distance add(Distance distanceTwo) {
		return new Distance(this.get(Unit.defaultUnit) + distanceTwo.get(Unit.defaultUnit), Unit.defaultUnit);
	}
	
	public Distance mul(double x) {
		return new Distance(this.get(Unit.defaultUnit) * x, Unit.defaultUnit);
	}
	
	public boolean lessThan(Distance distanceTwo) {
		return this.get(Unit.defaultUnit) < distanceTwo.get(Unit.defaultUnit);
	}

	public boolean greaterThan(Distance distanceTwo) {
		return this.get(Unit.defaultUnit) > distanceTwo.get(Unit.defaultUnit);
	}
	
	public Distance negate() {
		return new Distance(-this.get(Unit.defaultUnit), Unit.defaultUnit);
	}
	
	public Distance abs() {
		return new Distance(Math.abs(this.get(Unit.defaultUnit)), Unit.defaultUnit);
	}
	
	public double signum() {
		return Math.signum(this.get(Unit.defaultUnit));
	}
	
	@Override
	public boolean equals(Object distanceTwo) {
		if(distanceTwo instanceof Distance) {
			return this.get(Unit.defaultUnit) == ((Distance) distanceTwo).get(Unit.defaultUnit);
		} else {
			return false;
		}
	}

	public double div(Distance distance) {
		return this.get(Unit.defaultUnit) / distance.get(Unit.defaultUnit);
	}

}

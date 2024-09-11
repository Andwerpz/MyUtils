package myutils.math;

public class ComplexNumber {
	public static final int XY = 0;
	public static final int RCIS = 1;

	private double real, imaginary;

	public ComplexNumber() {
		real = 0.0;
		imaginary = 0.0;
	}

	public ComplexNumber(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}

	public ComplexNumber(ComplexNumber c) {
		this.real = c.getRe();
		this.imaginary = c.getIm();
	}

	public void set(ComplexNumber z) {
		this.real = z.real;
		this.imaginary = z.imaginary;
	}

	public void add(ComplexNumber z) {
		set(add(this, z));
	}

	public void subtract(ComplexNumber z) {
		set(subtract(this, z));
	}

	public void multiply(ComplexNumber z) {
		set(multiply(this, z));
	}

	public void divide(ComplexNumber z) {
		set(divide(this, z));
	}

	public static ComplexNumber add(ComplexNumber z1, ComplexNumber z2) {
		return new ComplexNumber(z1.real + z2.real, z1.imaginary + z2.imaginary);
	}

	public static ComplexNumber subtract(ComplexNumber z1, ComplexNumber z2) {
		return new ComplexNumber(z1.real - z2.real, z1.imaginary - z2.imaginary);
	}

	public static ComplexNumber multiply(ComplexNumber z1, ComplexNumber z2) {
		double _real = z1.real * z2.real - z1.imaginary * z2.imaginary;
		double _imaginary = z1.real * z2.imaginary + z1.imaginary * z2.real;
		return new ComplexNumber(_real, _imaginary);
	}

	public static ComplexNumber divide(ComplexNumber z1, ComplexNumber z2) {
		ComplexNumber output = multiply(z1, z2.conjugate());
		double div = Math.pow(z2.mod(), 2);
		return new ComplexNumber(output.real / div, output.imaginary / div);
	}

	public ComplexNumber conjugate() {
		return new ComplexNumber(this.real, -this.imaginary);
	}

	/**
	 * The modulus, magnitude or the absolute value of current complex number.
	 * 
	 * @return
	 */
	public double mod() {
		return Math.sqrt(Math.pow(this.real, 2) + Math.pow(this.imaginary, 2));
	}

	/**
	 * The square of the current complex number.
	 * 
	 * @return 
	 */
	public ComplexNumber square() {
		double _real = this.real * this.real - this.imaginary * this.imaginary;
		double _imaginary = 2 * this.real * this.imaginary;
		return new ComplexNumber(_real, _imaginary);
	}

	@Override
	public String toString() {
		String re = this.real + "";
		String im = "";
		if (this.imaginary < 0)
			im = this.imaginary + "i";
		else
			im = "+" + this.imaginary + "i";
		return re + im;
	}

	public static ComplexNumber exp(ComplexNumber z) {
		double a = z.real;
		double b = z.imaginary;
		double r = Math.exp(a);
		a = r * Math.cos(b);
		b = r * Math.sin(b);
		return new ComplexNumber(a, b);
	}

	public static ComplexNumber pow(ComplexNumber z, int power) {
		ComplexNumber output = new ComplexNumber(z.getRe(), z.getIm());
		for (int i = 1; i < power; i++) {
			double _real = output.real * z.real - output.imaginary * z.imaginary;
			double _imaginary = output.real * z.imaginary + output.imaginary * z.real;
			output = new ComplexNumber(_real, _imaginary);
		}
		return output;
	}

	public static ComplexNumber sin(ComplexNumber z) {
		double x = Math.exp(z.imaginary);
		double x_inv = 1 / x;
		double r = Math.sin(z.real) * (x + x_inv) / 2;
		double i = Math.cos(z.real) * (x - x_inv) / 2;
		return new ComplexNumber(r, i);
	}

	public static ComplexNumber cos(ComplexNumber z) {
		double x = Math.exp(z.imaginary);
		double x_inv = 1 / x;
		double r = Math.cos(z.real) * (x + x_inv) / 2;
		double i = -Math.sin(z.real) * (x - x_inv) / 2;
		return new ComplexNumber(r, i);
	}

	public static ComplexNumber tan(ComplexNumber z) {
		return divide(sin(z), cos(z));
	}

	public static ComplexNumber cot(ComplexNumber z) {
		return divide(new ComplexNumber(1, 0), tan(z));
	}

	public static ComplexNumber sec(ComplexNumber z) {
		return divide(new ComplexNumber(1, 0), cos(z));
	}

	public static ComplexNumber cosec(ComplexNumber z) {
		return divide(new ComplexNumber(1, 0), sin(z));
	}

	public double getRe() {
		return this.real;
	}

	public double getIm() {
		return this.imaginary;
	}

	/**
	 * The argument/phase of the current complex number.
	 * @return
	 */
	public double getArg() {
		return Math.atan2(imaginary, real);
	}

	public static ComplexNumber parseComplex(String s) {
		s = s.replaceAll(" ", "");
		ComplexNumber parsed = null;
		if (s.contains(String.valueOf("+")) || (s.contains(String.valueOf("-")) && s.lastIndexOf('-') > 0)) {
			String re = "";
			String im = "";
			s = s.replaceAll("i", "");
			s = s.replaceAll("I", "");
			if (s.indexOf('+') > 0) {
				re = s.substring(0, s.indexOf('+'));
				im = s.substring(s.indexOf('+') + 1, s.length());
				parsed = new ComplexNumber(Double.parseDouble(re), Double.parseDouble(im));
			}
			else if (s.lastIndexOf('-') > 0) {
				re = s.substring(0, s.lastIndexOf('-'));
				im = s.substring(s.lastIndexOf('-') + 1, s.length());
				parsed = new ComplexNumber(Double.parseDouble(re), -Double.parseDouble(im));
			}
		}
		else {
			// Pure imaginary number
			if (s.endsWith("i") || s.endsWith("I")) {
				s = s.replaceAll("i", "");
				s = s.replaceAll("I", "");
				parsed = new ComplexNumber(0, Double.parseDouble(s));
			}
			// Pure real number
			else {
				parsed = new ComplexNumber(Double.parseDouble(s), 0);
			}
		}
		return parsed;
	}

	@Override
	public final boolean equals(Object z) {
		if (!(z instanceof ComplexNumber))
			return false;
		ComplexNumber a = (ComplexNumber) z;
		return (real == a.real) && (imaginary == a.imaginary);
	}

	public ComplexNumber inverse() {
		return divide(new ComplexNumber(1, 0), this);
	}

	public String format(int format_id) throws IllegalArgumentException {
		String out = "";
		if (format_id == XY)
			out = toString();
		else if (format_id == RCIS) {
			out = mod() + " cis(" + getArg() + ")";
		}
		else {
			throw new IllegalArgumentException("Unknown Complex Number format.");
		}
		return out;
	}
}
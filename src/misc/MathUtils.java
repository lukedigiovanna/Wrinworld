package misc;

public class MathUtils {
	public static int clip(int val, int min, int max) {
		if (val < min)
			return min;
		if (val > max)
			return max;
		return val;
	}
	
	public static double clip(double val, double min, double max) {
		if (val < min) 
			return min;
		if (val > max) 
			return max;
		return val;
	}
	
	public static int min(int val, int min) {
		if (val < min)
			return min;
		return val;
	}
	
	public static int max(int val, int max) {
		if (val > max)
			return max;
		return val;
	}
	
	public static double min(double val, double min) {
		if (val < min)
			return min;
		return val;
	}
	
	public static double max(double val, double max) {
		if (val > max)
			return max;
		return val;
	}

	
	//return a random integer between two numbers (inclusive)
	public static int randomInRange(int min, int max) {
		if (min > max) {
			int temp = min;
			min = max;
			max = temp;
		}
		int dif = max-min+1;
		int ran = (int)(Math.random()*dif+min);
		return ran;
	}
	
	public static double randomInRange(double min, double max) {
		if (min > max) {
			double temp = min;
			min = max;
			max = temp;
		}
		double dif = max-min;
		double ran = Math.random()*dif+min;
		return ran;
	}
	
	public static boolean equals(double num1, double num2) {
		if (Math.abs(num1-num2) < 0.00001)
			return true;
		return false;
	}
	
	public static double distance(double x1, double y1, double x2, double y2) {
		double dx = x1-x2;
		double dy = y1-y2;
		return Math.sqrt(dx*dx+dy*dy);
	}
	
	public static double angle(double x1, double y1, double x2, double y2) {
		double dx = x2-x1;
		double dy = y2-y1;
		double angle = 0;
		if (dx != 0) {
			angle = Math.atan(dy/dx);
			if (dx < 0)
				angle+=Math.PI;
		} else if (dy > 0)
			angle = Math.PI/2;
		else
			angle = -Math.PI/2;
		
		while (angle < 0)
			angle+=Math.PI*2;
		angle%=Math.PI*2;
		return angle;
	}
	
	public static double angle(Position p1, Position p2) {
		if (p1 == null || p2 == null) 
			return 0;
		return angle(p1.x,p1.y,p2.x,p2.y);
	}
	
	public static double angleFromSides(double width, double height) {
		if (width == 0) {
			if (height > 0)
				return Math.PI/2;
			else
				return Math.PI*3/2;
		}
		double radian = Math.tan(height/width);
		if (width < 0)
			radian+=Math.PI;
		while (radian < 0)
			radian+=Math.PI*2;
		radian%=Math.PI*2;
		return radian;
	}
	
	public static double getDegree(double radian) {
		return (radian*180)/(Math.PI);
	}
	
	public static double getRadian(double degree) {
		return (degree/180)*(Math.PI);
	}
	
	public static String getSuffix(int number) {
		if (number >= 11 && number <= 13)
			return "th";
		int lastDigit = number%10;
		switch (lastDigit) {
		case 1:
			return "st";
		case 2:
			return "nd";
		case 3:
			return "rd";
		default:
			return "th";
		}
	}
	
	public static String addCommas(int num) {
		String s = "";
		String numS = num+"";
		while (numS.length() > 3) {
			s = ","+numS.substring(numS.length()-3) + s;
			numS = numS.substring(0, numS.length()-3);
		}
		s = numS + s;
		return s;
	}
	
	public static String romanNumerals(int num) {
		String s = "";
		//1000s (M)
		int thousands = num/1000;
		for (int i = 0; i < thousands; i++)
			s+="M";
		num%=1000;
		//500s (D)
		if (num >= 900)
			s+="CM";
		else if (num >= 500)
			s+="D";
		else if (num >= 400)
			s+="CD";
		num%=500;
		//100s (C)
		if (num < 400) {
			//add the hundreds now
			int hundreds = num/100;
			for (int i = 0; i < hundreds; i++)
				s+="C";
		}
		num%=100;
		if (num >= 90)
			s+="XC";
		else if (num >= 50)
			s+="L";
		else if (num >= 40)
			s+="XL";
		num%=50;
		//100s (C)
		if (num < 40) {
			//add the hundreds now
			int tens = num/10;
			for (int i = 0; i < tens; i++)
				s+="X";
		}
		num%=10;
		if (num >= 9)
			s+="IX";
		else if (num >= 5)
			s+="V";
		else if (num >= 4)
			s+="IV";
		num%=5;
		if (num < 4) {
			//add the hundreds now
			int ones = num;
			for (int i = 0; i < ones; i++)
				s+="I";
		}
		return s;
	}
	
	public static double round(double val, double decimalPlace) {
		//decimalPlace is given as 0.1, 0.001, 0.5
		return (int)(Math.round(val/decimalPlace))/(1/decimalPlace);
	}
	
	public static double floor(double val, double decimalPlace) {
		double factor = 1/decimalPlace;
		return Math.floor(val*factor)/factor;
	}
}

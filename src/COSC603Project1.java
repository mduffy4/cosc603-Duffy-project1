/*
 * 
 */
import java.util.Scanner;

// TODO: Auto-generated Javadoc
/**
 * The Class COSC603Project1.
 */
public class COSC603Project1 {

	/**
	 * The main method.
	 *
	 * @param args            the command line arguments
	 */
	public static void main(String[] args) {
		initialize();
	}

	/**
	 * Initialize.
	 */
	private static void initialize() {

		double dryingFactor = 0;
		double fineFeulMoisture = 99;
		double adjustedDayFuelMoisture = 99;
		double fireLoadRating = 0;
		double diff = 0;
		
		double[] a = createArrayA();
		double[] b = createArrayB();
		double[] c = createArrayC();
		double[] d = createArrayD();

		boolean isSnow = getIsSnow();
		double dryBulbTemperature = getDryBulbTemperature();
		double wetBulbTemperature = getWetBulbTemperature();
		double precip = getPrecip();
		double windSpeed = getWindSpeed();
		double buildUpIndex = getBuildUpIndex();
		int isHerb = getIsHerb();// 1=Cured 2=Transition 3=Green;
		double grassSpreadIndex = getGrassSpreadIndex();
		double timberSpreadIndex = getTimberSpreadIndex();

		if (isSnow) {

			grassSpreadIndex = 0;
			timberSpreadIndex = 0;

			if ((precip - .1) <= 0) {
				printOutput(dryingFactor, fineFeulMoisture, adjustedDayFuelMoisture, fireLoadRating, dryBulbTemperature,
						wetBulbTemperature, isSnow, precip, windSpeed, buildUpIndex, isHerb, grassSpreadIndex,
						timberSpreadIndex);
				return;
			} else {
				buildUpIndex = calculateBuildUpIndex(precip, buildUpIndex);
				if (buildUpIndex < 0) {
					buildUpIndex = 0;
				}
				printOutput(dryingFactor, fineFeulMoisture, adjustedDayFuelMoisture, fireLoadRating, dryBulbTemperature,
						wetBulbTemperature, isSnow, precip, windSpeed, buildUpIndex, isHerb, grassSpreadIndex,
						timberSpreadIndex);
				return;
			}

		} else {

			diff = dryBulbTemperature - wetBulbTemperature;

			fineFeulMoisture = calculateFineFeulMoisture(diff, a, b, c);

			dryingFactor = calculateDryingFactor(fineFeulMoisture, d);

			fineFeulMoisture = calculateFineFeulMoistureHerbStage(fineFeulMoisture, isHerb);

			if ((precip - .1) > 0) {

				buildUpIndex = calculateBuildUpIndex(precip, diff);
				if (buildUpIndex < 0) {
					buildUpIndex = 0;
				}
			}

			buildUpIndex = buildUpIndex + dryingFactor;

			adjustedDayFuelMoisture = calculateAdjustedDayFuelMoisture(fineFeulMoisture, buildUpIndex);

			if (adjustedDayFuelMoisture - 30 >= 0) {
				if (fineFeulMoisture - 30 >= 0) {
					grassSpreadIndex = 1;
					timberSpreadIndex = 1;
					printOutput(dryingFactor, fineFeulMoisture, adjustedDayFuelMoisture, fireLoadRating,
							dryBulbTemperature, wetBulbTemperature, isSnow, precip, windSpeed, buildUpIndex, isHerb,
							grassSpreadIndex, timberSpreadIndex);
					return;
				}
				timberSpreadIndex = 1;
				if (windSpeed - 14 < 0) {
					grassSpreadIndex = calculateGrassAndTimberSpreadIndex(0.01312, 6, windSpeed, fineFeulMoisture);
					if (timberSpreadIndex - 1 <= 0) {
						timberSpreadIndex = 1;
						if (grassSpreadIndex - 1 < 0) {
							grassSpreadIndex = 1;
						}
					}
				} else {
					grassSpreadIndex = calculateGrassAndTimberSpreadIndex(0.00918, 14, windSpeed, fineFeulMoisture);
					if (grassSpreadIndex - 99 <= 0) {
						grassSpreadIndex = 99;
					}
					if (timberSpreadIndex - 99 <= 0) {
						timberSpreadIndex = 99;
					}
				}

			} else {
				if (windSpeed - 14 < 0) {
					timberSpreadIndex = calculateGrassAndTimberSpreadIndex(0.01312, 6, windSpeed,
							adjustedDayFuelMoisture);
					grassSpreadIndex = calculateGrassAndTimberSpreadIndex(0.01312, 6, windSpeed, fineFeulMoisture);
					if (timberSpreadIndex - 1 <= 0) {
						timberSpreadIndex = 1;
						if (grassSpreadIndex - 1 < 0) {
							grassSpreadIndex = 1;
						}
					}
				} else {
					timberSpreadIndex = calculateGrassAndTimberSpreadIndex(0.00918, 14, windSpeed,
							adjustedDayFuelMoisture);
					grassSpreadIndex = calculateGrassAndTimberSpreadIndex(0.00918, 14, windSpeed, fineFeulMoisture);
					if (grassSpreadIndex - 99 <= 0) {
						grassSpreadIndex = 99;
					}
					if (timberSpreadIndex - 99 <= 0) {
						timberSpreadIndex = 99;
					}

				}
			}
			if (timberSpreadIndex < 0) {
				printOutput(dryingFactor, fineFeulMoisture, adjustedDayFuelMoisture, fireLoadRating, dryBulbTemperature,
						wetBulbTemperature, isSnow, precip, windSpeed, buildUpIndex, isHerb, grassSpreadIndex,
						timberSpreadIndex);
				return;
			}
			if (buildUpIndex <= 0) {
				printOutput(dryingFactor, fineFeulMoisture, adjustedDayFuelMoisture, fireLoadRating, dryBulbTemperature,
						wetBulbTemperature, isSnow, precip, windSpeed, buildUpIndex, isHerb, grassSpreadIndex,
						timberSpreadIndex);
				return;
			}

			fireLoadRating = calculateFireLoadRating(timberSpreadIndex, buildUpIndex);

			if (fireLoadRating <= 0) {
				fireLoadRating = 0;
				printOutput(dryingFactor, fineFeulMoisture, adjustedDayFuelMoisture, fireLoadRating, dryBulbTemperature,
						wetBulbTemperature, isSnow, precip, windSpeed, buildUpIndex, isHerb, grassSpreadIndex,
						timberSpreadIndex);
				return;
			}
			fireLoadRating = Math.pow(10, fireLoadRating);
			printOutput(dryingFactor, fineFeulMoisture, adjustedDayFuelMoisture, fireLoadRating, dryBulbTemperature,
					wetBulbTemperature, isSnow, precip, windSpeed, buildUpIndex, isHerb, grassSpreadIndex,
					timberSpreadIndex);
			return;

		}
	}

	private static double[] createArrayC() {
		double[] c = new double[4];
		c[1] = 4.5;
		c[2] = 12.5;
		c[3] = 27.5;
		return c;
	}

	private static double[] createArrayB() {
		double[] b = new double[5];
				b[1] = -30.0;
				b[2] = 19.2;
				b[3] = 13.8;
				b[4] = 22.5;
		return b;
	}

	private static double[] createArrayA() {
		double[] a = new double[5];
				a[1] = -0.185900;
				a[2] = -0.85900;
				a[3] = -0.059660;
				a[4] = -0.077373;
		return a;
	}

	private static double[] createArrayD() {
		double[] d = new double[7];
		d[1] = 16.0;
		d[2] = 10.0;
		d[3] = 7.0;
		d[4] = 5.0;
		d[5] = 4.0;
		d[6] = 3.0;
		return d;
	}

	/**
	 * Gets the dry bulb temperature.
	 *
	 * @return the dry bulb temperature
	 */
	private static double getDryBulbTemperature() {
		return parseStringToDouble(promptUser("Enter the dry bulb temperature:"));
	}

	/**
	 * Gets the wet bulb temperature.
	 *
	 * @return the wet bulb temperature
	 */
	private static double getWetBulbTemperature() {
		return parseStringToDouble(promptUser("Enter the wet bulb temperature:"));
	}

	/**
	 * Gets the wind speed.
	 *
	 * @return the wind speed
	 */
	private static double getWindSpeed() {
		return parseStringToDouble(promptUser("Enter Wind Speed: "));
	}

	/**
	 * Gets the builds the up index.
	 *
	 * @return the builds the up index
	 */
	private static double getBuildUpIndex() {
		return parseStringToDouble(promptUser("Enter Build Up Index: "));
	}

	/**
	 * Gets the checks if is herb.
	 *
	 * @return the checks if is herb
	 */
	private static int getIsHerb() {
		return parseStringToInt(promptUser("Enter Herb Numer; 1=Cured 2=Transition 3=Green: "));
	}

	/**
	 * Gets the grass spread index.
	 *
	 * @return the grass spread index
	 */
	private static double getGrassSpreadIndex() {
		return parseStringToDouble(promptUser("Enter Grass Spread Index: "));
	}

	/**
	 * Gets the timber spread index.
	 *
	 * @return the timber spread index
	 */
	private static double getTimberSpreadIndex() {
		return parseStringToDouble(promptUser("Enter Timber Spead Index: "));
	}

	/**
	 * Gets the checks if is snow.
	 *
	 * @return the checks if is snow
	 */
	private static boolean getIsSnow() {
		return checkAnswer(promptUser("Is there snow? Yes/No"));
	}

	/**
	 * Gets the precip.
	 *
	 * @return the precip
	 */
	private static double getPrecip() {
		return parseStringToDouble(promptUser("Enter Precip:"));
	}

	/**
	 * Calculate build up index.
	 *
	 * @param precip the precip
	 * @param BUO the buo
	 * @return the double
	 */
	private static double calculateBuildUpIndex(double precip, double BUO) {
		return -50 * (Math.log(1 - (Math.exp(-BUO / 50.))) * Math.exp(1.175 * (precip - .1)));
	}

	/**
	 * Calculate fine feul moisture.
	 *
	 * @param diff the diff
	 * @param a the a
	 * @param b the b
	 * @param c the c
	 * @return the double
	 */
	private static double calculateFineFeulMoisture(double diff, double a[], double b[], double c[]) {
		int temp = getTemp(diff, c);

		return b[temp] * Math.exp(a[temp] * diff);
	}

	private static int getTemp(double diff, double[] c) {
		int temp = 0;
		for (int i = 1; i <= 3; i++) {
			if (diff - c[i] <= 0) {
				temp = i;
				break;
			} else {
				temp = 4;
			}
		}
		return temp;
	}

	/**
	 * Calculate fine feul moisture herb stage.
	 *
	 * @param fineFeulMoisture the fine feul moisture
	 * @param isHerb the is herb
	 * @return the double
	 */
	private static double calculateFineFeulMoistureHerbStage(double fineFeulMoisture, int isHerb) {
		fineFeulMoisture = checkFineFeulMoistureNotLessThanZero(fineFeulMoisture);
		return (fineFeulMoisture + isHerb - 1) * 5;
	}

	private static double checkFineFeulMoistureNotLessThanZero(double fineFeulMoisture) {
		if (fineFeulMoisture - .1 < 0) {
			fineFeulMoisture = 0;
		}
		return fineFeulMoisture;
	}

	/**
	 * Calculate drying factor.
	 *
	 * @param fineFeulMoisture the fine feul moisture
	 * @param d the d
	 * @return the double
	 */
	private static double calculateDryingFactor(double fineFeulMoisture, double d[]) {
		for (int i = 1; i <= 6; i++) {
			if (fineFeulMoisture - d[i] > 0) {
				return i - 1;
			}
		}
		return 7;
	}

	/**
	 * Calculate adjusted day fuel moisture.
	 *
	 * @param fineFeulMoisture the fine feul moisture
	 * @param buildUpIndex the build up index
	 * @return the double
	 */
	private static double calculateAdjustedDayFuelMoisture(double fineFeulMoisture, double buildUpIndex) {
		return 0.9 * fineFeulMoisture + 0.5 + 9.5 * Math.exp(-buildUpIndex / 50);
	}

	/**
	 * Calculate grass and timber spread index.
	 *
	 * @param indexBasedOnSpeed the index based on speed
	 * @param x the x
	 * @param windSpeed the wind speed
	 * @param ADFMOrFFM the ADFM or ffm
	 * @return the double
	 */
	private static double calculateGrassAndTimberSpreadIndex(double indexBasedOnSpeed, int x, double windSpeed,
			double ADFMOrFFM) {
		return indexBasedOnSpeed * (windSpeed + x) * Math.pow((33. - ADFMOrFFM), 1.65 - 3);
	}

	/**
	 * Calculate fire load rating.
	 *
	 * @param timberSpreadIndex the timber spread index
	 * @param buildUpIndex the build up index
	 * @return the double
	 */
	private static double calculateFireLoadRating(double timberSpreadIndex, double buildUpIndex) {
		return 1.75 * Math.log10(timberSpreadIndex) + 0.32 * Math.log10(buildUpIndex) - 1.640;
	}

	/**
	 * Check answer.
	 *
	 * @param answer the answer
	 * @return true, if successful
	 */
	private static boolean checkAnswer(String answer) {
		return answer.toUpperCase().equals("Y") || answer.toUpperCase().equals("YES");
	}

	/**
	 * Parses the string to double.
	 *
	 * @param answer the answer
	 * @return the double
	 */
	private static double parseStringToDouble(String answer) {
		return Double.parseDouble(answer);
	}

	/**
	 * Parses the string to int.
	 *
	 * @param answer the answer
	 * @return the int
	 */
	private static int parseStringToInt(String answer) {
		return Integer.parseInt(answer);
	}

	/**
	 * Prompt user.
	 *
	 * @param prompt the prompt
	 * @return the string
	 */
	private static String promptUser(String prompt) {
		String input = "";
		Scanner scan = new Scanner(System.in);

		System.out.println(prompt);
		input = scan.nextLine();
		//scan.close();
		return input;
	}

	/**
	 * Prints the output.
	 *
	 * @param dryingFactor the drying factor
	 * @param fineFeulMoisture the fine feul moisture
	 * @param adjustedDayFuelMoisture the adjusted day fuel moisture
	 * @param fireLoadRating the fire load rating
	 * @param dryBulbTemperature the dry bulb temperature
	 * @param wetBulbTemperature the wet bulb temperature
	 * @param isSnow the is snow
	 * @param precip the precip
	 * @param windSpeed the wind speed
	 * @param buildUpIndex the build up index
	 * @param isHerb the is herb
	 * @param grassSpreadIndex the grass spread index
	 * @param timberSpreadIndex the timber spread index
	 */
	private static void printOutput(double dryingFactor, double fineFeulMoisture, double adjustedDayFuelMoisture,
			double fireLoadRating, double dryBulbTemperature, double wetBulbTemperature, boolean isSnow, double precip,
			double windSpeed, double buildUpIndex, int isHerb, double grassSpreadIndex, double timberSpreadIndex) {

		print("Dry Factor: " + String.valueOf(dryingFactor));
		print("Fine Feul Moisture: " + String.valueOf(fineFeulMoisture));
		print("Adjusted Day Fuel Moisture: " + String.valueOf(adjustedDayFuelMoisture));
		print("Fire Load Rating: " + String.valueOf(fireLoadRating));
		print("Dry Bulb Temperature: " + String.valueOf(dryBulbTemperature));
		print("Wet Bulb Temperature: " + String.valueOf(wetBulbTemperature));
		print("Snow: " + String.valueOf(isSnow));
		print("Precip: " + String.valueOf(precip));
		print("WindSpeed: " + String.valueOf(windSpeed));
		print("Build Up Index: " + String.valueOf(buildUpIndex));
		print("Herb: " + String.valueOf(isHerb));
		print("Grass Spread Index: " + String.valueOf(grassSpreadIndex));
		print("Timber Spread Index: " + String.valueOf(timberSpreadIndex));

	}

	/**
	 * Prints the.
	 *
	 * @param str the str
	 */
	private static void print(String str) {
		System.out.println(str);
	}

}

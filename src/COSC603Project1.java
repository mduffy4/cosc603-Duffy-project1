import java.util.Scanner;

public class COSC603Project1 {

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		initialize();
	}

	private static void initialize() {

		double dryingFactor = 0;
		double fineFeulMoisture = 99;
		double adjustedDayFuelMoisture = 99;
		double fireLoadRating = 0;
		double diff = 0;

		double[] a = new double[5];
		double[] b = new double[5];
		double[] c = new double[4];
		double[] d = new double[7];

		a[1] = -0.185900;
		a[2] = -0.85900;
		a[3] = -0.059660;
		a[4] = -0.077373;
		b[1] = -30.0;
		b[2] = 19.2;
		b[3] = 13.8;
		b[4] = 22.5;
		c[1] = 4.5;
		c[2] = 12.5;
		c[3] = 27.5;
		d[1] = 16.0;
		d[2] = 10.0;
		d[3] = 7.0;
		d[4] = 5.0;
		d[5] = 4.0;
		d[6] = 3.0;

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

	private static double getDryBulbTemperature() {
		return parseStringToDouble(promptUser("Enter the dry bulb temperature:"));
	}

	private static double getWetBulbTemperature() {
		return parseStringToDouble(promptUser("Enter the wet bulb temperature:"));
	}

	private static double getWindSpeed() {
		return parseStringToDouble(promptUser("Enter Wind Speed: "));
	}

	private static double getBuildUpIndex() {
		return parseStringToDouble(promptUser("Enter Build Up Index: "));
	}

	private static int getIsHerb() {
		return parseStringToInt(promptUser("Enter Herb Numer; 1=Cured 2=Transition 3=Green: "));
	}

	private static double getGrassSpreadIndex() {
		return parseStringToDouble(promptUser("Enter Grass Spread Index: "));
	}

	private static double getTimberSpreadIndex() {
		return parseStringToDouble(promptUser("Enter Timber Spead Index: "));
	}

	private static boolean getIsSnow() {
		return checkAnswer(promptUser("Is there snow? Yes/No"));
	}

	private static double getPrecip() {
		return parseStringToDouble(promptUser("Enter Precip:"));
	}

	private static double calculateBuildUpIndex(double precip, double BUO) {
		return -50 * (Math.log(1 - (Math.exp(-BUO / 50.))) * Math.exp(1.175 * (precip - .1)));
	}

	private static double calculateFineFeulMoisture(double diff, double a[], double b[], double c[]) {
		int temp = 0;
		for (int i = 1; i <= 3; i++) {
			if (diff - c[i] <= 0) {
				temp = i;
				break;
			} else {
				temp = 4;
			}
		}

		return b[temp] * Math.exp(a[temp] * diff);
	}

	private static double calculateFineFeulMoistureHerbStage(double fineFeulMoisture, int isHerb) {
		if (fineFeulMoisture - .1 < 0) {
			fineFeulMoisture = 0;
		}
		return (fineFeulMoisture + isHerb - 1) * 5;
	}

	private static double calculateDryingFactor(double fineFeulMoisture, double d[]) {
		for (int i = 1; i <= 6; i++) {
			if (fineFeulMoisture - d[i] > 0) {
				return i - 1;
			}
		}
		return 7;
	}

	private static double calculateAdjustedDayFuelMoisture(double fineFeulMoisture, double buildUpIndex) {
		return 0.9 * fineFeulMoisture + 0.5 + 9.5 * Math.exp(-buildUpIndex / 50);
	}

	private static double calculateGrassAndTimberSpreadIndex(double indexBasedOnSpeed, int x, double windSpeed,
			double ADFMOrFFM) {
		return indexBasedOnSpeed * (windSpeed + x) * Math.pow((33. - ADFMOrFFM), 1.65 - 3);
	}

	private static double calculateFireLoadRating(double timberSpreadIndex, double buildUpIndex) {
		return 1.75 * Math.log10(timberSpreadIndex) + 0.32 * Math.log10(buildUpIndex) - 1.640;
	}

	private static boolean checkAnswer(String answer) {
		return answer.toUpperCase().equals("Y") || answer.toUpperCase().equals("YES");
	}

	private static double parseStringToDouble(String answer) {
		return Double.parseDouble(answer);
	}

	private static int parseStringToInt(String answer) {
		return Integer.parseInt(answer);
	}

	private static String promptUser(String prompt) {
		Scanner scan = new Scanner(System.in);

		System.out.println(prompt);
		return scan.nextLine();
	}

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

	private static void print(String str) {
		System.out.println(str);
	}

}

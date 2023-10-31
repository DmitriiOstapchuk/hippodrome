import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class HorseTest {
    @Test
    public void constructorNullNameTest() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> new Horse(null, 2.8));
        String expectedMessage = "Name cannot be null.";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @ParameterizedTest
    @MethodSource("blankNamesStream")
    public void constructorBlankNameTest(String blankName) {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> new Horse(blankName, 2.8));
        String expectedMessage = "Name cannot be blank.";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @ParameterizedTest
    @MethodSource("notBlankNamesStream")
    public void constructorNotBlankNameTest(String notBlankName) {
        Assertions.assertDoesNotThrow(() -> new Horse(notBlankName, 2.8));
    }

    static Stream<String> blankNamesStream () {
            return Stream.of("", " ", "     ", "\t\n", "\r\f\t\t\t", "\u000B\r\t   \n", " \t\r \n");
    }

    static Stream<String> notBlankNamesStream () {
        return Stream.of("Lucky\t", "Pegasus\r", "\u000BMr. Frost\f", "\t_\n");
    }

    @Test
    public void constructorNegativeSpeedTest () {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> new Horse("Pegasus", -2.8));
        String expectedMessage = "Speed cannot be negative.";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void constructorNegativeDistanceTest () {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> new Horse("Pegasus", 2.8, -150));
        String expectedMessage = "Distance cannot be negative.";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @ParameterizedTest
    @MethodSource("notBlankNamesStream")
    public void getNameTest(String name) {
        Horse horse = new Horse(name, 2.3, 20);
        String expectedName = name;
        String actualName = horse.getName();
        Assertions.assertEquals(expectedName, actualName);
    }

    @Test
    public void getSpeedTest() {
        //speed has random value between 2 and 5
        double expectedSpeed = 2 + Math.random() * 3;
        Horse horse = new Horse("Spirit", expectedSpeed, 94);
        double actualSpeed = horse.getSpeed();
        Assertions.assertEquals(expectedSpeed, actualSpeed);
    }

    @Test
    public void getDistanceTest() {
        // distance is a random value between 0 and 10
        double expectedDistance = Math.random() * 10;
        Horse horse = new Horse("Caesar", 3.7, expectedDistance);
        double actualDistance = horse.getDistance();
        Assertions.assertEquals(expectedDistance, actualDistance);
    }

    @Test
    public void getZeroDistanceTest() {
        Horse horse = new Horse("Napoleon", 5.4);
        double expectedDistance = 0.0;
        double actualDistance = horse.getDistance();
        Assertions.assertEquals(expectedDistance, actualDistance);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.2, 0.5, 0.75, 0.9})
    public void moveTest(double probabilityValue) {
        try (MockedStatic<Horse> horseMock = Mockito.mockStatic(Horse.class)) {
            horseMock.when(() -> Horse.getRandomDouble(0.2, 0.9)).thenReturn(probabilityValue);
            Horse horse = new Horse("Napoleon", 2.8, 15.6);
            horse.move();
            horseMock.verify(() -> Horse.getRandomDouble(0.2, 0.9), Mockito.times(1));
            double expectedDistance  = 15.6 + 2.8 * probabilityValue;
            double actualDistance = horse.getDistance();
            Assertions.assertEquals(expectedDistance, actualDistance);
        }
    }
}

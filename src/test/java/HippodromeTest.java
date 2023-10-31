import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.times;

public class HippodromeTest {
    @Test
    public void constructorNullListTest() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> new Hippodrome(null));
        String expectedMessage = "Horses cannot be null.";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }
    @Test
    public void constructorEmptyListTest() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> new Hippodrome(new ArrayList<Horse>()));
        String expectedMessage = "Horses cannot be empty.";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void getHorsesTest() {
        List<Horse> horsesExpectedList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            String name = "horse" + i;
            //speed is a random value between 2 and 5
            double speed = 2.0 + Math.random() * 3.0;
            horsesExpectedList.add(new Horse(name, speed));
        }
        Hippodrome hippodrome = new Hippodrome(horsesExpectedList);
        List<Horse> horsesActualList = hippodrome.getHorses();
        Assertions.assertEquals(horsesExpectedList, horsesActualList);
    }

    @Test
    public void moveTest() {
        List<Horse> horses = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            horses.add(Mockito.mock(Horse.class));
        }
        Hippodrome hippodrome = new Hippodrome(horses);
        hippodrome.move();
        for (Horse horse: horses) {
            Mockito.verify(horse, times(1)).move();
        }
    }

    @Test
    public void getWinnerTest() {
        Horse whiteHorse = new Horse("White", 2.4, 12);
        Horse brownHorse = new Horse("Brown", 2.7, 12);
        //horse with max distance has random distance between 12 and 13
        Horse greyHorse = new Horse("Grey", 2.9, 12 + Math.random());
        //Set is used for randomizing order of horses in ArrayList
        Set<Horse> horses = Set.of(whiteHorse, brownHorse, greyHorse);
        Hippodrome hippodrome = new Hippodrome(new ArrayList<> (horses));
        Horse expectedHorse = greyHorse;
        Horse actualHorse = hippodrome.getWinner();
        Assertions.assertEquals(expectedHorse, actualHorse);
    }


}

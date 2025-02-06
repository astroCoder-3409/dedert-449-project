package SprintZero;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SprintZeroTest {
    @Test
    @DisplayName("Checks if Binary Form of base 10 number is a palindrome")
    void isBinaryFormPalindrome() {
        assertAll(() -> assertEquals(false, SprintZero.isBinaryFormPalindrome(2)),
                () -> assertEquals(true, SprintZero.isBinaryFormPalindrome(5)),
                () -> assertEquals(false, SprintZero.isBinaryFormPalindrome(8)),
                () -> assertEquals(true, SprintZero.isBinaryFormPalindrome(231)));
    }

    @Test
    @DisplayName("Checks if list of ints is ascending")
    void isAscending() {
        assertAll(() -> assertEquals(false, SprintZero.isAscending(new int[]{1,3,2})),
                () -> assertEquals(true, SprintZero.isAscending(new int[]{1,2,3})),
                () -> assertEquals(false, SprintZero.isAscending(new int[]{1,4,243,10})),
                () -> assertEquals(true, SprintZero.isAscending(new int[]{1,4,10,243})));
    }
}
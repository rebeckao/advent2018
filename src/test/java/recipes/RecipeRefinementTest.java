package recipes;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class RecipeRefinementTest {
    private RecipeRefinement recipeRefinement = new RecipeRefinement();

    @ParameterizedTest
    @CsvSource({
            "9, 5158916779",
            "5, 0124515891",
            "18, 9251071085",
            "2018, 5941429882",
            "909441, 2615161213"
    })
    void nextRecipeScores(int afterNumberOfRecipes, String expected) {
        String actual = recipeRefinement.nextRecipeScores(afterNumberOfRecipes, 10);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource({
            "51589, 9",
            "01245, 5",
            "92510, 18",
            "59414, 2018",
            "909441, 20403320"
    })
    void nextRecipeScoress(String desiredRecipes, int expected) {
        int actual = recipeRefinement.necessaryRecipes(desiredRecipes);
        assertEquals(expected, actual);
    }

}
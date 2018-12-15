package recipes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class RecipeRefinement {
    String nextRecipeScores(int afterNumberOfRecipes, int numberOfRecipesToScore) {
        List<Integer> recipes = new ArrayList<>();
        recipes.add(3);
        recipes.add(7);
        int elf1 = 0;
        int elf2 = 1;

        while (recipes.size() < afterNumberOfRecipes + numberOfRecipesToScore) {
            int combine = recipes.get(elf1) + recipes.get(elf2);
            if (combine >= 10) {
                recipes.add(combine / 10);
                recipes.add(combine % 10);
            } else {
                recipes.add(combine);
            }
            int listLength = recipes.size();
            elf1 = (elf1 + (recipes.get(elf1) + 1)) % listLength;
            elf2 = (elf2 + (recipes.get(elf2) + 1)) % listLength;
//            System.out.println(recipes + "," + elf1 + "," + elf2);
        }

        return recipes.subList(afterNumberOfRecipes, afterNumberOfRecipes + numberOfRecipesToScore).stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    int necessaryRecipes(String desiredRecipes) {
        List<Integer> recipes = new ArrayList<>();
        recipes.add(3);
        recipes.add(7);
        int elf1 = 0;
        int elf2 = 1;
        List<Integer> desired = desiredRecipes.chars()
                .mapToObj(c -> (char) c)
                .map(String::valueOf)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        int desiredSize = desired.size();

        while (true) {
            int combine = recipes.get(elf1) + recipes.get(elf2);
            if (combine >= 10) {
                recipes.add(combine / 10);
                recipes.add(combine % 10);
            } else {
                recipes.add(combine);
            }
            int listLength = recipes.size();
            elf1 = (elf1 + (recipes.get(elf1) + 1)) % listLength;
            elf2 = (elf2 + (recipes.get(elf2) + 1)) % listLength;

            if (listLength > desiredSize) {
                if (recipes.subList(listLength - desiredSize, listLength).equals(desired)) {
                    return listLength - desiredSize;
                } else if (combine >= 10 && recipes.subList(listLength - desiredSize - 1, listLength - 1).equals(desired)) {
                    return listLength - desiredSize - 1;
                }
            }
        }
    }
}

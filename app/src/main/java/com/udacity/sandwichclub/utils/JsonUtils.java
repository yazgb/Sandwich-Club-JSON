package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    /**
     * This method parses JSON from a String and returns a Sandwich object with its details.
     * @param json string with info to be parsed
     * @return Sandwich with details of the type of sandwich
     */
    public static Sandwich parseSandwichJson(String json) {

        try {
            JSONObject sandwichJson = new JSONObject(json);
            JSONObject name = sandwichJson.getJSONObject("name");

            String mainName = name.getString("mainName");
            JSONArray alsoKnownAs = name.getJSONArray("alsoKnownAs");
            String placeOfOrigin = sandwichJson.getString("placeOfOrigin");
            String description = sandwichJson.getString("description");
            String image = sandwichJson.getString("image");
            JSONArray ingredients = sandwichJson.getJSONArray("ingredients");

            Sandwich sandwich = new Sandwich();

            sandwich.setMainName(mainName);
            sandwich.setPlaceOfOrigin(placeOfOrigin);
            sandwich.setDescription(description);
            sandwich.setImage(image);

            int alsoKnowAsLength = alsoKnownAs.length();
            int ingredientsLength = ingredients.length();
            List<String> knownAsList = new ArrayList<String>(alsoKnowAsLength);
            List<String> ingredientsList = new ArrayList<String>(ingredientsLength);

            for (int i = 0, j = 0; i < alsoKnowAsLength || j < ingredientsLength; i++, j++) {

                if (i < alsoKnowAsLength)
                    knownAsList.add(alsoKnownAs.getString(i));

                if (j < ingredientsLength)
                    ingredientsList.add(ingredients.getString(j));
            }

            sandwich.setAlsoKnownAs(knownAsList);
            sandwich.setIngredients((ingredientsList));

            return sandwich;
        }
        catch(JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}

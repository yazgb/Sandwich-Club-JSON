package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private TextView mOrigin;
    private TextView mAlsoKnown;
    private TextView mIngredients;
    private TextView mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        // References to TextView views from activity_detail.xml
        mOrigin = findViewById(R.id.origin_tv);
        mAlsoKnown = findViewById(R.id.also_known_tv);
        mIngredients = findViewById(R.id.ingredients_tv);
        mDescription = findViewById(R.id.description_tv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Called to populate fields in the layout to display details of the Sandwich object.
     *
     * @param sandwich The Sandwich whose info will be displayed in detail.
     */
    private void populateUI(Sandwich sandwich) {

        mOrigin.setText(sandwich.getPlaceOfOrigin());

        // Line separator to help display details
        String lineSeparator = System.getProperty("line.separator");

        // Get list of 'also known as' from the Sandwich object and construct a string to display it
        List<String> knownAs = sandwich.getAlsoKnownAs();
        StringBuilder sbKnownAs = new StringBuilder();
        for(String alias : knownAs) {
            sbKnownAs.append(alias);
            sbKnownAs.append(lineSeparator);
        }
        if(sbKnownAs.length() > 0)
        {
            sbKnownAs.setLength(sbKnownAs.length()-1);
        }
        mAlsoKnown.setText(sbKnownAs);

        // Get list of 'ingredients' from the Sandwich object and construct a string to display them
        List<String> ingredientsList = sandwich.getIngredients();
        StringBuilder sbIngredients = new StringBuilder();
        for(String ingredient : ingredientsList) {
            sbIngredients.append(ingredient);
            sbIngredients.append(lineSeparator);
        }
        if(sbIngredients.length() > 0)
        {
            sbIngredients.setLength(sbIngredients.length()-1);
        }
        mIngredients.setText(sbIngredients);

        mDescription.setText(sandwich.getDescription());
    }
}

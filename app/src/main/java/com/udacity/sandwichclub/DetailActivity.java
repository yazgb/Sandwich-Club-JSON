package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @BindView(R.id.origin_tv) TextView mOrigin;
    @BindView(R.id.also_known_tv) TextView mAlsoKnown;
    @BindView(R.id.ingredients_tv) TextView mIngredients;
    @BindView(R.id.description_tv) TextView mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        ButterKnife.bind(this);

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

        String sentinelMsg = "Message not available";

        mOrigin.setText(sandwich.getPlaceOfOrigin().isEmpty()? sentinelMsg : sandwich.getPlaceOfOrigin());

        // Line separator to help display details
        String lineSeparator = System.getProperty("line.separator");

        // Get list of 'also known as' from the Sandwich object and construct a string to display it
        List<String> knownAs = sandwich.getAlsoKnownAs();
        String sKnownAs = TextUtils.join(lineSeparator, knownAs);
        mAlsoKnown.setText(sKnownAs.isEmpty()? sentinelMsg : sKnownAs);

        // Get list of 'ingredients' from the Sandwich object and construct a string to display them
        List<String> ingredientsList = sandwich.getIngredients();
        String sIngredients = TextUtils.join(lineSeparator, ingredientsList);
        mIngredients.setText(sIngredients.isEmpty()? sentinelMsg : sIngredients);

        mDescription.setText(sandwich.getDescription().isEmpty()? sentinelMsg : sandwich.getDescription());
    }
}

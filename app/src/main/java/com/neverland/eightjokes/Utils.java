package com.neverland.eightjokes;

import android.text.TextUtils;
import android.widget.EditText;

import com.parse.ParseUser;

/**
 * Created by Vasilev on 17.1.2015 г..
 */
public class Utils {

    /**
     * Check if current user exists.
     *
     * @return
     */
    public static boolean isCurrentUserExists() {
        return ParseUser.getCurrentUser() != null;
    }

    /**
     * Check EditText filed and set Error message.
     *
     * @param editText
     * @return
     */
    public static boolean checkEditTextEmptyOrLessThenThree(EditText editText) {

        String enteredText = editText.getText().toString().trim();

        if (TextUtils.isEmpty(enteredText)) {
            editText.setError(editText.getContext().getString(R.string.required_filed_error_message));
        } else if (enteredText.length() <= 3) {
            editText.setError(editText.getContext().getString(R.string.short_text_error_message));
        } else {
            return true;
        }

        return false;
    }

}

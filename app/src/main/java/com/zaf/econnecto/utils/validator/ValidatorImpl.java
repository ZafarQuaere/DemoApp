package com.zaf.econnecto.utils.validator;

import android.util.Patterns;

import java.util.regex.Pattern;

public class ValidatorImpl implements Validator {
    @Override
    public InvalidPassword isPasswordValid(String password) {
        // 8 <= length <= 16
        if (password == null || !isValidLength(password, MIN_CHAR_COUNT_PASSWORD, MAX_CHAR_COUNT_PASSWORD)) {
            return InvalidPassword.INVALID_LENGTH;
        }
        // must contain at least 1 digit
        if (numOfDigits(password) < MIN_DIGITS_COUNT_PASSWORD) {
            return InvalidPassword.MISS_DIGIT;
        }
        // must not contain special character
        if (Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE).matcher(password).find()) {
            return InvalidPassword.HAS_SPECIAL;
        }
        // must start with an alphabetic character
        char s = password.charAt(0);
        if (!Character.isLetter(s)) {
            return InvalidPassword.NOT_START_WITH_ALPHA;
        }
        return null;
    }

    @Override
    public boolean isEmailValid(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public boolean isVINValid(String vin) {
        if (vin == null) {
            return false;
        }
        if (!isLarger(vin.length(), MIN_DIGITS_COUNT_VIN)) {
            return false;
        }
        int num = numOfDigits(vin);
        return isLarger(num, MIN_DIGITS_COUNT_VIN);
    }

    @Override
    public boolean isBatchNumValid(String batchNumber) {
        if (batchNumber == null) {
            return false;
        }
        if (!isLarger(batchNumber.length(), MIN_DIGITS_COUNT_BATCH)) {
            return false;
        }
        int num = numOfDigits(batchNumber);
        return isInRange(num, MIN_DIGITS_COUNT_BATCH, MAX_DIGITS_COUNT_BATCH);
    }

    @Override
    public boolean isFirstNameValid(String firstName) {
        return isValidLength(firstName, 1, MAX_CHAR_COUNT_USERNAME);
    }

    @Override
    public boolean isLastNameValid(String lastName) {
        return isValidLength(lastName, 1, MAX_CHAR_COUNT_LAST_NAME);
    }

    @Override
    public boolean isStreetValid(String street) {
        return isValidLength(street, 1, MAX_CHAR_COUNT_STREET);
    }

    @Override
    public boolean isSuburbValid(String suburb) {
        return isValidLength(suburb, 1, MAX_CHAR_COUNT_SUBURB);
    }

    @Override
    public boolean isPostcodeValid(String postcode) {
        return isValidLength(postcode, 1, MIN_DIGITS_COUNT_POSTCODE)
                && numOfDigits(postcode) == MIN_DIGITS_COUNT_POSTCODE;
    }

    @Override
    public boolean isMobileNumberValid(String mobile) {
        return mobile != null && mobile.length() == REQUIRED_CHAR_COUNT_MOBILE
                && (mobile.startsWith("04") || mobile.startsWith("05"));
    }

    @Override
    public boolean isPhoneNumberValid(String phone) {
        if (phone == null) {
            return false;
        }
        // if more than one character supplied, minimum 6 digits and maximum 13 digits
        // noinspection SimplifiableIfStatement
        if (phone.length() >= 1 && !isInRange(numOfDigits(phone), MIN_DIGITS_COUNT_PHONE, MAX_DIGITS_COUNT_PHONE)) {
            return false;
        }
        return Patterns.PHONE.matcher(phone).matches();
    }

    private int numOfDigits(String text) {
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c >= '0' && c <= '9') {
                count++;
            }
        }
        return count;
    }

    private boolean isValidLength(String text, int minLength, int maxLength) {
        return text != null && isInRange(text.length(), minLength, maxLength);
    }

    private boolean isInRange(int num, int min, int max) {
        return isLarger(num, min) && isSmaller(num, max);
    }

    private boolean isLarger(int num, int lowBounds) {
        return num >= lowBounds;
    }

    private boolean isSmaller(int num, int upBounds) {
        return num <= upBounds;
    }

}

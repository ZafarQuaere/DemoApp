package com.zaf.econnecto.utils.validator;

public interface Validator {

    int MAX_CHAR_COUNT_USERNAME = 40;
    int MAX_CHAR_COUNT_LAST_NAME = 80;
    int MAX_CHAR_COUNT_STREET = 255;
    int MAX_CHAR_COUNT_SUBURB = 40;
    int MIN_DIGITS_COUNT_POSTCODE = 4;
    int REQUIRED_CHAR_COUNT_MOBILE = 10;

    int MIN_DIGITS_COUNT_VIN = 6;
    int MIN_DIGITS_COUNT_BATCH = 7;
    int MAX_DIGITS_COUNT_BATCH = 10;

    int MIN_CHAR_COUNT_PASSWORD = 8;
    int MAX_CHAR_COUNT_PASSWORD = 16;
    int MIN_DIGITS_COUNT_PASSWORD = 1;

    int MIN_DIGITS_COUNT_PHONE = 6;
    int MAX_DIGITS_COUNT_PHONE = 13;

    enum InvalidPassword {
        INVALID_LENGTH,
        MISS_DIGIT,
        HAS_SPECIAL,
        NOT_START_WITH_ALPHA
    }

    /**
     * Function to check if the password is valid. If the password is invalid, it will return one of {@link InvalidPassword} to declare the invalid reason.
     * The function will return null if the password is valid.
     *
     * @param password to check
     * @return null if the given password is valid, or {@link InvalidPassword}
     */
    InvalidPassword isPasswordValid(String password);

    boolean isEmailValid(String email);

    boolean isVINValid(String vin);

    boolean isBatchNumValid(String batchNumber);

    boolean isFirstNameValid(String firstName);

    boolean isLastNameValid(String lastName);

    boolean isStreetValid(String street);

    boolean isSuburbValid(String suburb);

    boolean isPostcodeValid(String postcode);

    boolean isMobileNumberValid(String mobile);

    boolean isPhoneNumberValid(String phone);

}

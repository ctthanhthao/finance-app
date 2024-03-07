package com.home.financial.app.common.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class UserUtils {

    public static boolean isValidEmail(String email) {
        /**
         * The following restrictions are imposed in the email address’ local part by using this regex:
         *
         * - It allows numeric values from 0 to 9.
         * - Both uppercase and lowercase letters from a to z are allowed.
         * - Allowed are underscore “_”, hyphen “-“, and dot “.”
         * - Dot isn’t allowed at the start and end of the local part.
         * - Consecutive dots aren’t allowed.
         * - For the local part, a maximum of 64 characters are allowed.
         *
         * Restrictions for the domain part in this regular expression include:
         * - It allows numeric values from 0 to 9.
         * - We allow both uppercase and lowercase letters from a to z.
         * - Hyphen “-” and dot “.” aren’t allowed at the start and end of the domain part.
         * - No consecutive dots.
         */
        String regEmailPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regEmailPattern)
                .matcher(email)
                .matches();
    }

    public static boolean isValidUserName(String name) {
        if (StringUtils.isEmpty(name))
            return false;
        /**
         * A username is considered valid if all the following constraints are satisfied:
         *
         * The username consists of 6 to 30 characters inclusive. If the username
         * consists of less than 6 or greater than 30 characters, then it is an invalid username.
         * The username can only contain alphanumeric characters and underscores (_). Alphanumeric characters describe the character set consisting of lowercase characters [a – z], uppercase characters [A – Z], and digits [0 – 9].
         * The first character of the username must be an alphabetic character, i.e., either lowercase character
         * [a – z] or uppercase character [A – Z].
         *
         */
        String regNamePattern = "^[A-Za-z]\\w{5,29}$";
        return Pattern.compile(regNamePattern)
                .matcher(name)
                .matches();
    }
}

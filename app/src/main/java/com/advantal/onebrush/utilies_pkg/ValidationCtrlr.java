package com.advantal.onebrush.utilies_pkg;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sonam on 01-03-2022 14:54.
 */
public class ValidationCtrlr {

    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 15;
    public static boolean WEAK, MEDIUM, STRONG, VERY_STRONG;
    static private ValidationCtrlr validationCtrlr;
    private final String lower = ".*[a-z].*";

    /*    private static final String lower = "(\".*[a-z].*\")";
        private static final String upper = "(\".*[A-Z].*\")";
        private static final String number = "(\".*[0-9].*\")";
        private static final String spacialChar = "(?=.*[@#$%^&+=]) ";*/
    private final String upper = ".*[A-Z].*";
    private final String spacialChar = ".*[@#$%^&+=][@#$%^&+=].*";
    private final String whiteSpace = ".*\\\\S+$.*";
    private final String number = ".*[0-9][0-9].*";

    public static ValidationCtrlr getValidatInstance() {
        if (validationCtrlr == null) {
            validationCtrlr = new ValidationCtrlr();
        }
        return validationCtrlr;

    }

    public String checkString(String str) {
        char ch;
        boolean capitalFlag = false;
        boolean lowerCaseFlag = false;
        boolean numberFlag = false;
        boolean specialCaseFlag = false;
        int numCount = 0, specialCount = 0;
        for (int i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            if (Character.isDigit(ch)) {
                numCount++;
                if (numCount >= 2) {
                    numberFlag = true;

                }
            } else if (Character.isUpperCase(ch)) {
                capitalFlag = true;
            } else if (Character.isLowerCase(ch)) {
                lowerCaseFlag = true;
            } else if (Character.isSpaceChar(ch)) {
                specialCount++;
                if (specialCount >= 2) {
                    specialCaseFlag = true;
                }
            }
        }

        if (str.length() == 0) {
            return "";
        } else if ((lowerCaseFlag) && (str.length() == 1)) {
            return "week";//week
        } else if ((lowerCaseFlag) && (str.length() == 2)) {
            return "week";//week
        } else if (lowerCaseFlag && capitalFlag && str.length() == 3) {
            return "week";//week

        } else if (lowerCaseFlag && numberFlag && (str.length() == 4)) {
            return "so-so";//so-so
        } else if ((lowerCaseFlag && capitalFlag) && (str.length() == 4)) {
//                return true;//so-so
            return "so-so";//so-so
        } else if (lowerCaseFlag && capitalFlag && numberFlag && (str.length() == 5)) {
            return "good";//good
        } else if (lowerCaseFlag && capitalFlag && specialCaseFlag && (str.length() == 5)) {
            return "good";//good
        } else if (lowerCaseFlag && capitalFlag && specialCaseFlag && (str.length() == 6)) {
            return "good";//good
        } else if (lowerCaseFlag && capitalFlag && specialCaseFlag && numberFlag && str.length() == 7) {
            return "very good";//very good
        } else if (lowerCaseFlag && capitalFlag && numberFlag && specialCaseFlag && ((str.length() == 8)))// && str.length() <= 12
            return "great";//great


        return "week";
    }

    public boolean checkStringLowerChar(String str) {
        char ch;
        boolean lowerCaseFlag = false;
        for (int i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            if (Character.isLowerCase(ch)) {
                lowerCaseFlag = true;
            }
            if (lowerCaseFlag)
                return true;
        }

        return false;
    }


    public String checkStringForPassword(String pass) {


        boolean NOTEXIT = false;
        if (Pattern.matches("(?=.*[A-Z])(?=.*[a-z])(?=.*\\d){1,6}", pass)) {
//                && (Pattern.matches("^(.*?[a-z]){6,}", pass))
//                || (Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", pass))) {
            Log.e("", "");
            return "week";
        }
        return "";
    }


    public boolean passawardaValid(String pass) {

        if (pass.matches(lower) && pass.matches(upper) && pass.matches(number) && pass.matches(spacialChar) && pass.length() > 12) {
            return true;
        } else if (pass.matches(lower) && pass.matches(upper) && pass.matches(number) && pass.matches(spacialChar) && pass.length() >= 6) {
            return true;
        } else if (pass.matches(lower) && pass.matches(upper) && pass.matches(number) && pass.length() >= 6) {
            return true;

        } else if (pass.matches(lower) && pass.matches(number) && pass.length() >= 6) {
            return true;

        } else return (pass.matches(lower)) || pass.matches(upper) || pass.length() > 6;



      /*  if (Pattern.matches("^(?=.*[a-z])[a-z]{1,4}", pass) || Pattern.matches("^(?=.*[a-z])[a-z]{4,}", pass) || Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])[A-Za-z]{1,6}", pass)) {
        if (Pattern.matches("^(?=.*[a-z])[a-z]{1,4}", pass) || Pattern.matches("^(?=.*[a-z])[a-z]{4,}", pass) ||
                Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])[A-Za-z]{1,6}", pass)) {
            return true;
        }
        if (Pattern.matches("^(?=.*[a-z])(?=.*\\d)[a-z\\d]{7,}", pass) ||
                Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])[A-Za-z]{7,}", pass)) {
            return true;


        }

        if (Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{7,}", pass) || Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[d$@$!%*?&#])[A-Za-z$@$!%*?&#]{7,}", pass) ||
                Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[d$@$!%*?&#])[A-Za-zd$@$!%*?&#]{12,}", pass)) {

            return true;

        }
        if (Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[d$@$!%*?&#])[A-Za-z\\dd$@$!%*?&#]{10,}", pass) || Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[d$@$!%*?&#])[A-Za-z\\dd$@$!%*?&#]{11,}", pass)) {
            return true;


        } else {
            return Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[d$@$!%*?&#])[A-Za-z\\dd$@$!%*?&#]{12,}", pass);
*/
    }


//        return Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])[A-Za-z]{1,6}", pass);


//        pattern3 = Pattern.compile(PASSWORD_PATTERN_small);
//        matcher3 = pattern3.matcher(pass);

//        return false;
//        return matcher3.matches();


    /*
        public static boolean calculate(String password) {
            int score = 0;
            // boolean indicating if password has an upper case
            boolean upper = false;
            // boolean indicating if password has a lower case
            boolean lower = false;
            // boolean indicating if password has at least one digit
            boolean digit = false;
            // boolean indicating if password has a leat one special char
            boolean specialChar = false;

            for (int i = 0; i < password.length(); i++) {
                char c = password.charAt(i);

                if (!specialChar  &&  !Character.isLetterOrDigit(c)) {
                    score++;
                    specialChar = true;
                } else {
                    if (!digit  &&  Character.isDigit(c)) {
                        score++;
                        digit = true;
                    } else {
                        if (!upper || !lower) {
                            if (Character.isUpperCase(c)) {
                                upper = true;
                            } else {
                                lower = true;
                            }

                            if (upper && lower) {
                                score++;
                            }
                        }
                    }
                }
            }

            int length = password.length();

            if (length > MAX_LENGTH) {
                score++;
            } else if (length < MIN_LENGTH) {
                score = 0;
            }

            // return enum following the score
            switch(score) {
                case 0 : return WEAK;
                case 1 : return MEDIUM;
                case 2 : return STRONG;
                case 3 : return VERY_STRONG;
                default:
            }

            return VERY_STRONG;
        }
    */
    public boolean passawardaValid_num(String pass) {
        Pattern pattern1;
        Matcher matcher1;
        final String PASSWORD_PATTERN_num = "^(?=.*[0-9]).{6,}${12,}";


        pattern1 = Pattern.compile(PASSWORD_PATTERN_num);
        matcher1 = pattern1.matcher(pass);


        return matcher1.matches();

    }

    public boolean passawardaValid_symbol(String pass) {
        Pattern pattern2;
        Matcher matcher2;
        final String PASSWORD_PATTERN_sym = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}${12,}";


        pattern2 = Pattern.compile(PASSWORD_PATTERN_sym);
        matcher2 = pattern2.matcher(pass);


        return matcher2.matches();

    }

    public boolean passawardaValid_all_small(String pass) {
        Pattern pattern3;
        Matcher matcher3;
        final String PASSWORD_PATTERN_small = "^(?=.*[a-z]).{6,}${12,}";


        pattern3 = Pattern.compile(PASSWORD_PATTERN_small);
        matcher3 = pattern3.matcher(pass);


        return matcher3.matches();

    }

/*
    public boolean passawardaValid_all_capital(String pass) {
        Pattern pattern4;
        Matcher matcher4;
        final String PASSWORD_PATTERN_capital = "^(?=.*[A-Z]).{6,}${12,}";


        pattern4 = Pattern.compile(PASSWORD_PATTERN_small);
        matcher4 = pattern4.matcher(pass);


        return matcher4.matches();

    }
*/


    public boolean emailValidator(String email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches());
    }

    public String phoneNumValidator(String phone) {
        if (phone.isEmpty())
            return "";
        else
        if (phone.length() < AppConstant.PHONE_NUM_MIN_LIMIT) {
            return "Please enter valid phone number.";
        }
//            else if (phone.length() >= AppConstant.PHONE_NUM_MAX_LIMIT)
//            return "Please enter 12  digite phone number.";
        return "";
    }


}
//else {
//        showErrorMsg(this.getResources().getString(R.string.bad_internet_connection));
//    }
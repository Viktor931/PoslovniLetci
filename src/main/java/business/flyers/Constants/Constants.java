package business.flyers.Constants;

public class Constants {
    public static class Registration {
        public class Error {
            public static final String USERNAME_TAKEN = "Username taken";
            public static final String PASSWORDS_NOT_EQUAL = "Passwords must match";
            public static final String NAME_CAPITAL_LETTER = "Name must start with capital letter";
            public static final String LAST_NAME_CAPITAL_LETTER = "Last name must start with capital letter";
            public static final String PASSWORD_ERRORS = "Password must contain 1 digit, 2 capital and 2 lower case letters, and must be between 5 and 15 characters";
        }
    }
    public static class Pagination {
        public static int ENTRIES_PER_PAGE = 10;
    }
    public static class User {
        public static class Role {
            public static final String USER = "USER";
            public static final String MODERATOR = "MODERATOR";
            public static final String ADMIN = "ADMIN";
            public static final String DATABASEADMIN = "DATABASEADMIN";
        }
    }
}

package util;
import model.UserRole;

public class Session {
    private static UserRole currentRole;

    public static void setRole(UserRole role) {
        currentRole = role;
    }
    public static UserRole getRole() {
        return currentRole;
    }
}

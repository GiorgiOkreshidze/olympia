package dev.local.olympia.dto.auth;

public class PasswordChangeRequest {
    String username;
    String oldPassword;
    String newPassword;

    public PasswordChangeRequest() {}
    public PasswordChangeRequest(String username, String oldPassword, String newPassword) {
        this.username = username;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    // --- Getters ---
    public String getUsername() {
        return username;
    }
    public String getOldPassword() {
        return oldPassword;
    }
    public String getNewPassword() {
        return newPassword;
    }

    // --- Setters ---
    public void setUsername(String username) {
        this.username = username;
    }
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

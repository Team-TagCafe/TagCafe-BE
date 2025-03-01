package com.Minjin.TagCafe.dto;

public class UserDTO {
    private String email;
    private String nickname;

    public UserDTO(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }

    public String getEmail() { return email; }
    public String getNickname() { return nickname; }
}

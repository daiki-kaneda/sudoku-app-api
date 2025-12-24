package com.example.sudoku_app_api.entity;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity<String>{
    @Id
    private String uid;

    @Length(min = 1, message = "名前は必須です。")
    private String name;
    @Email(message = "無効なEmailアドレスです。")
    private String email;

    public static User create(String uid, String name, String email) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("名前は必須です");
        }
        User user = new User();
        user.uid = uid;
        user.name = name;
        user.email = email;
        return user;
    }

    @Override
    public String getId() {
        return uid;
    }
}

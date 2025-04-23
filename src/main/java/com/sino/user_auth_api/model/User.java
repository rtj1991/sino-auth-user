    package com.sino.user_auth_api.model;

    import jakarta.persistence.*;
    import lombok.*;

    import java.io.Serializable;
    import java.util.ArrayList;
    import java.util.List;

    @EqualsAndHashCode(callSuper = true)
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    @Table(name = "app_user")
    public class User extends PersistedObject implements Serializable {

        private String username;
        private String password;
        private String email;

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<LoginHistory> loginHistories = new ArrayList<>();
    }

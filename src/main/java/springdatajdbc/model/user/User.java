package springdatajdbc.model.user;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.Wither;
import org.springframework.data.annotation.Id;

@RequiredArgsConstructor
@Value
@Wither
@Builder
public class User {

    public static User of(String name) {
        return new User(null, name);
    }

    @Id
    private final Integer id;
    private final String name;
}

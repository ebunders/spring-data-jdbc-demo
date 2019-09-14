package springdatajdbc.model.blog;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.Wither;
import org.springframework.data.relational.core.mapping.Table;
import springdatajdbc.model.user.User;

@RequiredArgsConstructor
@Value
@Wither
@Builder
@Table("blogpost_user")
class AuthorRef {

    static AuthorRef of(User user) {
        return new AuthorRef(user.getId());
    }

    final int user;
}

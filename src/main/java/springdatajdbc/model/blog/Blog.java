package springdatajdbc.model.blog;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.Wither;
import org.springframework.data.annotation.Id;
import springdatajdbc.model.user.User;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;

@RequiredArgsConstructor
@Value
@Wither
@Builder
class Blog {

    static Blog of(User owner, String title) {
        checkArgument(nonNull(owner.getId()), "User needs id");
        return Blog.builder().owner(owner.getId()).title(title).build();
    }

    @Id
    private final Integer id;
    private final String title;
    private final List<Blogpost> blogPosts;
    private final int owner;
}

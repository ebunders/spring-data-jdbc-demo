package springdatajdbc.model.blog;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.Wither;
import org.springframework.data.annotation.Id;
import springdatajdbc.model.user.User;

import java.time.LocalDateTime;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static springdatajdbc.util.CollectionUtil.asSet;

@RequiredArgsConstructor
@Value
@Wither
@Builder
class Blogpost {
    static Blogpost of(String title) {
        return Blogpost.builder().title(title).build();
    }

    @Id
    private final Integer id;
    private final String title;
    private final String text;
    private final LocalDateTime posted;
    private final Set<AuthorRef> authors;

    final Blogpost addAuthor(final User user) {
        checkArgument(nonNull(user.getId()), "User needs id");

        return this.getAuthors() == null ?
                this.withAuthors(asSet(new AuthorRef(user.getId()))) :
                this.withAuthors(asSet(this.authors, new AuthorRef(user.getId())));
    }
}

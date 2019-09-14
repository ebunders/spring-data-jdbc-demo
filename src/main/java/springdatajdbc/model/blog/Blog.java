package springdatajdbc.model.blog;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.Wither;
import org.springframework.data.annotation.Id;

import java.util.List;

@RequiredArgsConstructor
@Value
@Wither
@Builder
class Blog {

    @Id
    private final Integer id;
    private final String title;
    private final List<Blogpost> blogPosts;
    private final int owner;
}

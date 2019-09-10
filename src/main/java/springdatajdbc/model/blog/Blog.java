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
public class Blog {

    @Id
    private final Integer id;
    private final String name;
    private final List<BlogPost> blogPosts;
}

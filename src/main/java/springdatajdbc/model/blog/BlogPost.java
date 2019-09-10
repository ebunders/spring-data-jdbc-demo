package springdatajdbc.model.blog;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.Wither;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Value
@Wither
@Builder
public class BlogPost {
    @Id
    private final Integer id;
    private final LocalDateTime posted;
    private final String postName;
}

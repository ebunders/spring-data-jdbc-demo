package springdatajdbc.model.article;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.Wither;
import org.springframework.data.annotation.Id;

@RequiredArgsConstructor
@Value
@Wither
@Builder
public class Paragraph {
    @Id
    private final int id;
    private final String title;
    private final String text;
    private final String imageUrl;
    private final ImagePosition imagePosition;
}

package springdatajdbc.model.article;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang.BooleanUtils.isFalse;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureJdbc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ArticleRepositoryTest {

    @Autowired
    ArticleRepository articleRepository;

    @Test
    public void testAddArticle() {
        final Article article = articleRepository.save(Article.builder().title("an article").build());

        assertThat(article.getId(), is(0));
        assertThat(Stream.of(articleRepository.findAll()).count(), is(1L));

        final Article foundArticle = articleRepository.findById(article.getId()).orElseThrow(() -> new RuntimeException("Article not found"));
        assertThat(foundArticle.getTitle(), is("an article"));
    }

    @Test
    public void testRemoveArticle() {
        final Article article = articleRepository.save(Article.builder().title("an article").build());
        assertThat(Stream.of(articleRepository.findAll()).count(), is(1L));

        articleRepository.delete(article);
        assertThat(articleRepository.findById(article.getId()).isPresent(), is(false));
    }

    @Test
    public void testupdateArticle() {
        final Article a1 = articleRepository.save(Article.builder().title("an article").build());
        final Article a2 = articleRepository.save(a1.withTitle("new title"));

        assertThat(a1.getTitle(), is("an article"));
        assertThat(a2.getTitle(), is("new title"));

        final Article a3 = articleRepository.findById(a2.getId()).orElseThrow(() -> new RuntimeException("article not found"));
        assertThat(a3.getTitle(), is("new title"));
    }

    @Test
    public void testupdateArticleAddParagraph() {
        final Article a1 = articleRepository.save(Article.builder().title("an article").build());
        final Article a2 = articleRepository.save(a1.withParagraphs(asList(
                Paragraph.builder()
                        .title("title")
                        .text("text")
                        .imagePosition(ImagePosition.LEFT)
                        .imageUrl("www.img.com")
                        .build())));
        assertThat(a1.getParagraphs(), is(nullValue()));
        assertThat(a2.getParagraphs().size(), is(1));

        final Paragraph p1 = a2.getParagraphs().get(0);
        validateParagraph(p1);

        final Article a3 = articleRepository.findById(a2.getId()).orElseThrow(() -> new RuntimeException("can not find article"));
        assertThat(a3.getParagraphs().size(), is(1));
        final Paragraph p2 = a3.getParagraphs().get(0);
        validateParagraph(p2);
    }

    @Test
    public void testupdateArticleRemoveParagraph() {
        final Article a1 = articleRepository.save(Article.builder()
                .title("an article")
                .paragraphs(asList(Paragraph.builder()
                        .title("title")
                        .text("text")
                        .imagePosition(ImagePosition.LEFT)
                        .imageUrl("www.img.com")
                        .build()))
                .build());
        final Article a2 = a1.withParagraphs(
                a1.getParagraphs().stream()
                        .filter(p -> isFalse(p.getTitle().equals("title")))
                        .collect(toList()));

        assertThat(Optional.ofNullable(a2.getParagraphs()).orElse(emptyList()), is(empty()));
    }

    @Test
    public void testfindArtcileForParagraph() {
        final Article a1 = articleRepository.save(Article.builder()
                .title("an article")
                .paragraphs(singletonList(Paragraph.builder()
                        .title("title")
                        .text("text")
                        .imagePosition(ImagePosition.LEFT)
                        .imageUrl("www.img.com")
                        .build()))
                .build());

        final int paragraphId = a1.getParagraphs().stream().map(Paragraph::getId).findFirst().orElseThrow(() -> new RuntimeException("no paragraph found"));
        assertThat(paragraphId, is(0));

        Article a2 = articleRepository.findArticleForParagraph(paragraphId).orElseThrow(() -> new RuntimeException("no article found"));
        assertThat(a2.getId(), is(a1.getId()));


    }

    private void validateParagraph(Paragraph p) {
        assertThat(p.getTitle(), is("title"));
        assertThat(p.getText(), is("text"));
        assertThat(p.getImagePosition(), is(ImagePosition.LEFT));
        assertThat(p.getImageUrl(), is("www.img.com"));
    }

}

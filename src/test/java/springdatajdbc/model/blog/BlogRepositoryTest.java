package springdatajdbc.model.blog;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.codepoetics.protonpack.StreamUtils.zipWithIndex;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static org.apache.commons.lang.BooleanUtils.isFalse;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureJdbc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BlogRepositoryTest {

    @Autowired
    BlogRepository blogRepository;

    @Test
    public void testSaveNewBlogSavesBlog() {
        final Blog unsavedBlog = Blog.builder().name("blog").blogPosts(asList(BlogPost.builder().postName("foo").build())).build();
        Blog savedBlog = blogRepository.save(unsavedBlog);

        assertThat(savedBlog.getId(), is(equalTo(0)));
        assertThat(Stream.of(blogRepository.findAll()).count(), is(equalTo(1L)));


        assertThat(blogRepository.findById(0), is(equalTo(Optional.of(
                unsavedBlog.withId(0)
                        .withBlogPosts(zipWithIndex(unsavedBlog.getBlogPosts().stream())
                                .map(pair -> pair.getValue().withId(Math.toIntExact(pair.getIndex())))
                                .collect(toList()))))));
    }

    @Test
    public void testSaveExistingBlogSavesBlog() {
        final Blog blog = blogRepository.save(Blog.builder()
                .name("blog")
                .blogPosts(asList(BlogPost.builder()
                        .postName("foo")
                        .build()))
                .build());

        blogRepository.save(blog.withName("a new name"));
        assertThat(blogRepository.findById(0).map(b -> b.getName().equals("a new name")).orElse(false), is(true));
    }

    @Test
    public void testAddBlogPost() {
        final LocalDateTime now = LocalDateTime.now().truncatedTo(MINUTES);

        final Blog blog = blogRepository.save(Blog.builder()
                .name("blog")
                .blogPosts(asList(BlogPost.builder()
                        .postName("foo")
                        .posted(now.plusMinutes(5))
                        .build()))
                .build());

        blogRepository.save(blog.withBlogPosts(concat(blog.getBlogPosts().stream(), Stream.of(
                BlogPost.builder()
                        .postName("a new post")
                        .posted(now)
                        .build()
        )).collect(toList())));

        assertThat(blogRepository.findById(blog.getId())
                .map(Blog::getBlogPosts)
                .map(bps -> bps.size() == 2
                        && "a new post".equals(bps.get(1).getPostName())
                        && now.equals(bps.get(1).getPosted())
                )
                .orElse(false), is(true));

    }

    @Test
    public void testDeleteBlog() {
        Blog blog = blogRepository.save(Blog.builder()
                .name("blog")
                .blogPosts(asList(BlogPost.builder()
                        .postName("foo")
                        .build()))
                .build());

        blogRepository.deleteById(blog.getId());

        assertThat(blogRepository.findById(blog.getId()), is(empty()));
    }

    @Test
    public void testRemoveBlogPost() {
        Blog blog = blogRepository.save(Blog.builder()
                .name("blog")
                .blogPosts(asList(BlogPost.builder()
                        .postName("foo")
                        .build()))
                .build());

        assertThat(blogRepository.findAll().iterator().hasNext(), is(true));
        assertThat(blog.getId(), is(not(nullValue())));

        blogRepository.delete(blog);

        assertThat(blogRepository.findById(blog.getId()).isPresent(), is(false));
    }

    @Test
    public void testEditBlogPost() {
        Blog blog = blogRepository.save(Blog.builder()
                .name("blog")
                .blogPosts(asList(BlogPost.builder()
                        .postName("foo")
                        .build()))
                .build());

        blogRepository.save(blog.withName("new blog name"));

        assertThat(Stream.of(blogRepository.findAll()).count(), is(1L));
        assertThat(blogRepository.findById(0).map(b -> b.getName().equals("new blog name")).orElse(false), is(true));
    }

    @Test
    public void testRemoveBlog() {
        Blog blog = blogRepository.save(Blog.builder()
                .name("blog")
                .blogPosts(asList(BlogPost.builder()
                        .postName("foo")
                        .build()))
                .build());

        blogRepository.delete(blog);

        assertThat(blogRepository.findAll().iterator().hasNext(), is(false));
    }

    @Test
    public void testFindBlogByBlogPostId() {


        Blog blog = blogRepository.save(Blog.builder()
                .name("blog")
                .blogPosts(asList(
                        BlogPost.builder().postName("foo").build(),
                        BlogPost.builder().postName("bar").build(),
                        BlogPost.builder().postName("baz").build()
                ))
                .build());

        Blog foudBlog = blogRepository.findBlogForBlogPostId(blog.getBlogPosts().get(0).getId()).orElseThrow(() -> new RuntimeException("Blog niet gevonden"));
        assertThat(blog.getName(), is("blog"));
    }

    @Test
    public void testUpdateBlogRemovePost() {
        final Blog blog = blogRepository.save(Blog.builder()
                .name("blog")
                .blogPosts(asList(
                        BlogPost.builder().postName("foo").build(),
                        BlogPost.builder().postName("bar").build(),
                        BlogPost.builder().postName("baz").build()
                ))
                .build());

        blogRepository.save(blog.withBlogPosts(
                blog.getBlogPosts().stream()
                        .filter(post -> isFalse("foo".equals(post.getPostName())))
                        .collect(Collectors.toList())
        ));

        final Blog updatedBlog = blogRepository.findById(blog.getId()).orElseThrow(() -> new RuntimeException("Blog met id " + blog.getId() + " niet gevonden"));

        assertThat(updatedBlog.getBlogPosts().size(), is(2));
    }

    @Test
    public void testUpdateBlogaDDPost() {
        final Blog blog = blogRepository.save(Blog.builder()
                .name("blog")
                .blogPosts(asList(
                        BlogPost.builder().postName("foo").build(),
                        BlogPost.builder().postName("bar").build()
                ))
                .build());

        blogRepository.save(blog.withBlogPosts(
                concat(
                        blog.getBlogPosts().stream(),
                        Stream.of(BlogPost.builder().postName("baz").build()))
                        .collect(toList())
        ));

        final Blog updatedBlog = blogRepository.findById(blog.getId()).orElseThrow(() -> new RuntimeException("Blog met id " + blog.getId() + " niet gevonden"));

        assertThat(updatedBlog.getBlogPosts().size(), is(3));
    }
}

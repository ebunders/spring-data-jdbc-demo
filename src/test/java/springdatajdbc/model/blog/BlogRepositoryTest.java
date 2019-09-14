package springdatajdbc.model.blog;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import springdatajdbc.model.user.User;
import springdatajdbc.model.user.UserRepository;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.temporal.ChronoUnit.MINUTES;
import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.concat;
import static org.apache.commons.lang.BooleanUtils.isFalse;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static springdatajdbc.util.CollectionUtil.asSet;
import static springdatajdbc.util.CollectionUtil.first;
import static springdatajdbc.util.TestUtil.optionalValue;

@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureJdbc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BlogRepositoryTest {

    @Autowired
    BlogRepository blogRepository;

    @Autowired
    UserRepository userRepository;
    private User user;

    @Before
    public void set() {
        user = userRepository.save(User.of("user"));
    }

    @Test
    public void testSaveNewBlogSavesBlog() {
        Blog blog = blogRepository.save(Blog.builder()
                .title("blog")
                .owner(user.getId())
                .blogPosts(asList(Blogpost.of("blogpost").addAuthor(user)))
                .build());

        assertThat(user.getId(), notNullValue());
        assertThat(blog.getId(), notNullValue());
        assertThat(blog.getOwner(), is(user.getId()));
        assertThat(blog.getTitle(), is("blog"));

        assertThat(optionalValue(userRepository.findById(blog.getOwner())), is(user));
        assertThat(optionalValue(userRepository.findById(blog.getOwner())), not(sameInstance(user)));

        assertThat(blog.getBlogPosts().size(), is(1));
        assertThat(first(blog.getBlogPosts()), not(Blogpost.builder().id(0).title("blogpost")));

        assertThat(first(blog.getBlogPosts()).getAuthors(), is(asSet(new AuthorRef(user.getId()))));


    }

    @Test
    public void testSaveExistingBlogSavesBlog() {
        final Blog blog = blogRepository.save(Blog.builder()
                .title("blog")
                .blogPosts(asList(Blogpost.of("foo")))
                .owner(user.getId())
                .build());

        blogRepository.save(blog.withTitle("a new title"));
        assertThat(blogRepository.findById(0).map(b -> b.getTitle().equals("a new title")).orElse(false), is(true));
    }

    @Test
    public void testAddBlogPost() {
        final LocalDateTime now = LocalDateTime.now().truncatedTo(MINUTES);

        final Blog blog = blogRepository.save(Blog.builder()
                .title("blog")
                .owner(user.getId())
                .blogPosts(asList(Blogpost.builder()
                        .title("foo")
                        .posted(now.plusMinutes(5))
                        .build()))
                .build());

        blogRepository.save(blog.withBlogPosts(concat(blog.getBlogPosts().stream(), Stream.of(
                Blogpost.builder()
                        .title("a new post")
                        .posted(now)
                        .build()
        )).collect(toList())));

        assertThat(blogRepository.findById(blog.getId())
                .map(Blog::getBlogPosts)
                .map(posts -> posts.size() == 2
                        && "a new post".equals(posts.get(1).getTitle())
                        && now.equals(posts.get(1).getPosted())
                )
                .orElse(false), is(true));

    }

    @Test
    public void testDeleteBlog() {
        Blog blog = blogRepository.save(Blog.builder()
                .title("blog")
                .owner(user.getId())
                .blogPosts(asList(Blogpost.builder()
                        .title("foo")
                        .build()))
                .build());

        blogRepository.deleteById(blog.getId());

        assertThat(blogRepository.findById(blog.getId()), is(empty()));
    }

    @Test
    public void testRemoveBlogPost() {
        Blog blog = blogRepository.save(Blog.builder()
                .title("blog")
                .owner(user.getId())
                .blogPosts(asList(Blogpost.builder()
                        .title("foo")
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
                .title("blog")
                .owner(user.getId())
                .blogPosts(asList(Blogpost.builder()
                        .title("foo")
                        .build()))
                .build());

        blogRepository.save(blog.withTitle("new blog title"));

        assertThat(Stream.of(blogRepository.findAll()).count(), is(1L));
        assertThat(blogRepository.findById(0).map(b -> b.getTitle().equals("new blog title")).orElse(false), is(true));
    }

    @Test
    public void testRemoveBlog() {
        Blog blog = blogRepository.save(Blog.builder()
                .title("blog")
                .owner(user.getId())
                .blogPosts(asList(Blogpost.builder()
                        .title("foo")
                        .build()))
                .build());

        blogRepository.delete(blog);

        assertThat(blogRepository.findAll().iterator().hasNext(), is(false));
    }

    @Test
    public void testFindBlogByBlogPostId() {


        Blog blog = blogRepository.save(Blog.builder()
                .title("blog")
                .owner(user.getId())
                .blogPosts(asList(
                        Blogpost.builder().title("foo").build(),
                        Blogpost.builder().title("bar").build(),
                        Blogpost.builder().title("baz").build()
                ))
                .build());

        Blog foudBlog = optionalValue(blogRepository.findBlogForBlogPostId((first(blog.getBlogPosts()).getId())));
        assertThat(blog.getTitle(), is("blog"));
    }

    @Test
    public void testUpdateBlogRemovePost() {
        final Blog blog = blogRepository.save(Blog.builder()
                .title("blog")
                .owner(user.getId())
                .blogPosts(asList(
                        Blogpost.builder().title("foo").build(),
                        Blogpost.builder().title("bar").build(),
                        Blogpost.builder().title("baz").build()
                ))
                .build());

        blogRepository.save(blog.withBlogPosts(
                blog.getBlogPosts().stream()
                        .filter(post -> isFalse("foo".equals(post.getTitle())))
                        .collect(Collectors.toList())
        ));

        final Blog updatedBlog = optionalValue(blogRepository.findById(blog.getId()));

        assertThat(updatedBlog.getBlogPosts().size(), is(2));
    }

    @Test
    public void testUpdateBlogaDDPost() {
        final Blog blog = blogRepository.save(Blog.builder()
                .title("blog")
                .owner(user.getId())
                .blogPosts(asList(
                        Blogpost.builder().title("foo").build(),
                        Blogpost.builder().title("bar").build()
                ))
                .build());

        blogRepository.save(blog.withBlogPosts(
                concat(
                        blog.getBlogPosts().stream(),
                        Stream.of(Blogpost.builder().title("baz").build()))
                        .collect(toList())
        ));

        final Blog updatedBlog = optionalValue(blogRepository.findById(blog.getId()));

        assertThat(updatedBlog.getBlogPosts().size(), is(3));
    }

    @Test
    public void testAddAuthors() {
        Blog blog = blogRepository.save(Blog.builder()
                .title("blog")
                .owner(user.getId())
                .blogPosts(asList(Blogpost.of("blogpost").addAuthor(user)))
                .build());

        final User foo = userRepository.save(User.of("foo"));
        final User bar = userRepository.save(User.of("bar"));

        final Blog updatedBlog = blogRepository.save(blog.withBlogPosts(asList(
                first(blog.getBlogPosts()).withAuthors(asSet(first(blog.getBlogPosts()).getAuthors(), AuthorRef.of(foo), AuthorRef.of(bar))))));

        final Blogpost updatedBlogpost = first(updatedBlog.getBlogPosts());

        assertThat(updatedBlogpost.getAuthors().size(), is(3));
        assertThat(updatedBlogpost.getAuthors(), is(asSet(user, foo, bar).stream().map(AuthorRef::of).collect(toSet())));
    }

    @Test
    public void testRemoveAuthors() {
        final User foo = userRepository.save(User.of("foo"));
        final User bar = userRepository.save(User.of("bar"));

        Blog blog = blogRepository.save(Blog.builder()
                .title("blog")
                .owner(user.getId())
                .blogPosts(asList(Blogpost.of("blogpost").withAuthors(asSet(user, foo, bar).stream().map(AuthorRef::of).collect(toSet()))))
                .build());

        assertThat(blog.getBlogPosts().get(0).getAuthors().size(), is(3));

        final Blog updatedBlog = blogRepository.save(blog.withBlogPosts(asList(first(blog.getBlogPosts()).withAuthors(asSet(AuthorRef.of(user))))));

        assertThat(first(updatedBlog.getBlogPosts()).getAuthors().size(), is(1));

    }
}

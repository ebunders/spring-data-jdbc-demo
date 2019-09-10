package springdatajdbc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import springdatajdbc.model.blog.BlogRepository;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureJdbc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SpringDataJdbcTestApplicationTests {

    @Autowired
    private BlogRepository blogRepository;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testInsertBlog() {
//		final Blog a = Blog.builder().name("blog").id(null).build();
//		Blog b = blogRepository.save(a);
//		Blog u = b.withId(10);
//		assertThat(a.getId(), is(nullValue()));
//		assertThat(b.getId(), is(notNullValue()));
//		assertThat(u.getId(), is(notNullValue()));
//
//		assertNotSame(u, b);
//
//		List<Blog> blogs = asList(blogRepository.findAll());
//		assertThat(blogs.size(), is(equalTo(1)));
//		assertThat(blogs.get(0).getId(), is(equalTo(0)));
//		assertThat(blogs.get(0).getName(), is(equalTo("blog")));

//        BlogPost bp1 = BlogPost.builder().posted(LocalDateTime.now()).postName("foo").build();
//        BlogPost bp2 = BlogPost.builder().posted(LocalDateTime.now()).postName("bar").build();
//
//        final Blog a = Blog.builder().name("blog").blogPosts(asList(bp1, bp2)).build();
//
//
//        Blog blog = blogRepository.save(a);
//        assertThat(blog.getId(), is(notNullValue()));
//        assertThat(blog.getBlogPosts().size(), is(equalTo(2)));
//
//        assertThat(blog.getBlogPosts().stream()
//                .map(BlogPost::getPostName).collect(toList()), is(equalTo(asList(bp1, bp2)
//                .stream().map(BlogPost::getPostName).collect(toList()))));


    }

}

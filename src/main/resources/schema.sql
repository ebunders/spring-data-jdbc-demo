-- Blog
create table if not exists blog
(
    id   integer identity primary key,
    name varchar(100)
);
create table if not exists blog_post
(
    id        integer identity primary key,
    blog      integer references blog (id),
    blog_key  integer,
    post_name varchar(100),
    posted    timestamp
);

-- Article
create table if not exists article
(
    id    integer identity primary key,
    title varchar(100)
);

create table if not exists paragraph
(
    id             integer identity primary key,
    title          varchar(100),
    text           varchar(3000),
    image_url      varchar(1000),
    image_position varchar(100),
    article        integer references article (id),
    article_key    integer
);

-- User

create table if not exists user
(
    id   integer identity primary key,
    name varchar(200)
);

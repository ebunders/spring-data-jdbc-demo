-- User

create table if not exists user
(
    id   integer identity primary key,
    name varchar(200)
);


-- Blog
create table if not exists blog
(
    id    integer identity primary key,
    title varchar(100) not null,
    owner integer      not null,
    foreign key (owner) references user (id)
);
create table if not exists blogpost
(
    id       integer identity primary key,
    blog     integer references blog (id),
    blog_key integer,
    title    varchar(100) not null,
    text     longvarchar,
    posted   timestamp
);

create table if not exists blogpost_user
(
    user     integer,
    blogpost integer,
    primary key (user, blogpost)
);


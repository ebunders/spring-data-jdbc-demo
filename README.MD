Spring Data JDBC
"keep it Simple!"
- domain drive design
- supports immutable data
- no caching
- no lazy loading
- no proxies
- no deferred flushing	

### Referenties

presentatie author:
https://www.youtube.com/watch?v=AnIouYdwxo0

Project pagina: 
https://spring.io/projects/spring-data-jdbc

reference documentation:
https://docs.spring.io/spring-data/jdbc/docs/1.0.9.RELEASE/reference/html/

git repo:
https://github.com/spring-projects/spring-data-jdbc


spring data jdbc & mybatis:
https://stackoverflow.com/questions/56273556/how-is-spring-data-jdbc-complimenting-mybatis


Relaties binnen aggregaties (one-to-many):
https://www.youtube.com/watch?v=ccxBXDAPdmo

Relaties tussen aggregaties (many-to-many):
https://www.youtube.com/watch?v=5rqlqon8xko&t=1s

-=Kotlin=-

Kotling Spring Data JDBC example:
https://stonesoupprogramming.com/2017/06/29/kotlin-spring-jdbc-template/

meer referenties:
referenties:
- https://docs.spring.io/spring-data/jdbc/docs/1.0.9.RELEASE/reference/html/
- https://spring.io/blog/2018/09/24/spring-data-jdbc-references-and-aggregates
- https://spring.io/projects/spring-data-jdbc
- https://www.youtube.com/watch?v=ccxBXDAPdmo
### Entity aggregaties
-- todo

### Relaties

## Een op meer mapping
Het uitgangspunt is dat het hier om relaties binnen een entity aggregatie gaat. 

	Set
		Zorg dat de tabel van de 'meer' entity een kolom heeft met de naam van de 'een' entity met als type integer references {een}(id)
			tabel blog_post:
				blog   			integer references blog(id)

	Lis
		Zoals hierboven, maar er komt in de 'meer' eentity nog een kolom bij voor de plek in de list:
			tabel blog_post:
				blog   			integer references blog(id)
				blog_key 		integer
        vraag: Hoe zit het met 'bags' (ongesorteerd, niet uniek)

	Map
		Zoals hierboven, maar de blog_key kolom moet nu een text kolom worden:
			tabel blog_post:
				blog   			integer references blog(id)
				blog_key 		varchar(100)
		En de in blog wordt blogPosts een Map<String, BlogPost>

		Er is nog een tweede manier om maps te doen: Je kunt de Map declaratie annoteren met:
			@Column(keyColumn = "{veld-in-meer-die-je-als-key-wilt-gebruiken}") bijv: blog_title
			Let op: het moet de database column naam zijn, en niet de entity veld naam.

## Meer op meer mapping
Het uitgangspunt is dat het hier om relaties tussen verschillende entity aggregaties gaat, en de 'target' is dan
ook altijd de 'root' van een andere entity aggregatie.

Omdat het om een referentie gaat naar een andere aggregatie bestaat de relatie uit een id. Verder is de relatie altijd
'owned' door een van de kanten (bijv. een auto `heeft` vier wielen), en dit bepaald de richting van de realatie. Relaties 
zijn *niet* twee-richting. 
Om zo'n relatie uit te drukken heb je twee dingen nodig: een koppeltabel, en een class. In het geval van de relatie
tussen bijv. een 'book' en een 'author' (ervan uitgaand dat de ownership bij het boek ligt):
tabel: book_author:
```
create table if not exists book_author(
    boek     integer,
    author integer,
    primary key (user, blogpost)
)
```
En dan hebben we een class nodig:
```
@Value
@Table("book_author")
class AuthorRef {
    final int user;
}
```
en in de book class voeg je een veld toe van type AuthorRef 

## relatie classes en tabel name
Spring data jdbc gaat ervan uit dat entity classes en tabellen dezelfde naam hebben waarbij camelcase wordt omgezet
naar een scheiding met _ tekens. Voor classes kun je dat aanpassen met @Table, maar of en hoe je dat voor kolommen
kunt doen is mij nog niet helemaal duidelijk. 


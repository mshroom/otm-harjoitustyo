# Ohjelmistotekniikan menetelmät -harjoitustyö

Harjoitustyön aiheena on sovellus, johon käyttäjät voivat kirjata tietoa liikuntasuorituksistaan, asettaa itselleen tavoitteita ja seurata niiden toteutumista.

## Dokumentaatio

[Vaatimusmäärittely / Software requirements specification](https://github.com/mshroom/otm-harjoitustyo/blob/master/dokumentointi/Software_requirements_specification.md)

[Arkkitehtuurikuvaus / Software architecture](https://github.com/mshroom/otm-harjoitustyo/blob/master/dokumentointi/architecture.md)

[Työaikakirjanpito / Time-tracking](https://github.com/mshroom/otm-harjoitustyo/blob/master/dokumentointi/time-tracking.md)

## Komentorivitoiminnot

### Testaus

Testit suoritetaan komennolla

```
mvn test
```

Testikattavuusraportti luodaan komennolla

```
mvn jacoco:report
```

Kattavuusraporttia voi tarkastella avaamalla selaimella tiedosto _target/site/jacoco/index.html_

### Suoritettavan jarin generointi

Komento

```
mvn package
```

generoi hakemistoon _target_ suoritettavan jar-tiedoston _Sportbook-1.0-SNAPSHOT.jar_

### JavaDoc

JavaDoc generoidaan komennolla

```
mvn javadoc:javadoc
```

JavaDocia voi tarkastella avaamalla selaimella tiedosto _target/site/apidocs/index.html_


### Checkstyle

Tiedostoon [checkstyle.xml](https://github.com/mshroom/otm-harjoitustyo/blob/master/Sportbook/checkstyle.xml) määrittelemät tarkistukset suoritetaan komennolla

```
 mvn jxr:jxr checkstyle:checkstyle
```

Mahdolliset virheilmoitukset selviävät avaamalla selaimella tiedosto _target/site/checkstyle.html_

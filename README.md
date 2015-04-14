BetDevFitNesse
==============

[FitNesse](https://github.com/unclebob/fitnesse) Exec fixture in [slim format](http://www.fitnesse.org/FitNesse.UserGuide.WritingAcceptanceTests.SliM). Allows running Runtime.exec as a fixture. This project is licensed under [MIT](LICENSE).

#### Installation

* This module must be in the [FitNesse classpath](http://www.fitnesse.org/FitNesse.FullReferenceGuide.UserGuide.WritingAcceptanceTests.ClassPath).

```xml
<dependency>
  <groupId>net.betterdeveloper</groupId>
  <artifactId>BetDevFitNesse</artifactId>
  <version>0.0.1</version>
</dependency>
```

####  Sample:
```
|import|
|net.betterdeveloper.fitnesse| 
 
|library|
|exec fixture|

| script |
| $lsResult= | exec | ls |
| check| last exec return code | 0 |
| check | exec | ls -la | =~/fitnesse-standalone.jar/ |
| check| last exec return code | 0 |
| check |  exec | ls |=~/FitNesse/ |
| check| last exec return code | 0 |
```

BetDevFitNesse
==============

[FitNesse](https://github.com/unclebob/fitnesse) Fixtures.

 * Exec fixture in [slim format](http://www.fitnesse.org/FitNesse.UserGuide.WritingAcceptanceTests.SliM).
   Allows running Runtime.exec as a fixture. This project is licensed under [MIT](LICENSE).

 * [AutoIt](https://www.autoitscript.com/site/autoit/) fixture in [slim format](http://www.fitnesse.org/FitNesse.UserGuide.WritingAcceptanceTests.SliM).
   Allows you to control desktop apps via the GUI. This project is licensed under [MIT](LICENSE).

#### Installation

* This module must be in the [FitNesse classpath](http://www.fitnesse.org/FitNesse.FullReferenceGuide.UserGuide.WritingAcceptanceTests.ClassPath).

```xml
<dependency>
  <groupId>net.betterdeveloper</groupId>
  <artifactId>BetDevFitNesse</artifactId>
  <version>0.0.1</version>
</dependency>
```

* For the AutoIt fixture, you must make sure that the ActiveX DLL is registered first. Run this as Administrator:

```
regsvr32 lib/AutoItX3_x64.dll
```

Also, make sure the DLLs in our lib directory are in your lib directory in your FitNesse install. We configure
[JACOB](http://sourceforge.net/projects/jacob-project/) with the DLL path, as described in [this totorial](http://www.joecolantonio.com/2014/07/02/selenium-autoit-how-to-automate-non-browser-based-functionality/).
Therefore the same ./lib/*.dll path has to be valid when you launch FitNesse.

You will also need to manually copy our lib/*.jar files to where you have your other JARs that you feed to FitNesse's classpath.

####  Exec Sample:
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

####  AutoIt Sample:
```
!define TEST_SYSTEM {slim} 
!path ./dependencies/*

|import|
|net.betterdeveloper.fitnesse|

|library |
|Auto It Fixture |

| script|
| $oldDelay= | set win wait delay to | 200|
| $pid= | start app | calc.exe |
| wait for window ready | Calculator |
| click control |[ID:133]|
| click control |[ID:93]| 
| click control |[ID:133]|
| click control |[ID:121]|
```

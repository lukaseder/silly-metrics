silly-metrics
=============

Silly metrics is a bunch of scripts that provide you with silly and mostly useless metrics about your Java project.

KeywordCounter
--------------

Run 

```
$ java silly.metrics.KeywordCounter /path/to/java/project
```

... and observe the number of Java keywords used in your project. For instance, running this on http://www.jooq.org:

```
Silly metrics: Keywords
=======================
Enter a path (C:\Users\Lukas\workspace\silly-metrics):
C:\Users\Lukas\git\jOOQ\jOOQ


Keyword      Count
public       8127
return       6801
final        6608
import       5938
static       3903
new          3110
extends      2111
int          1822
throws       1756
void         1707
if           1661
this         1464
private      1347
class        1239
case         841
else         839
package      711
boolean      506
throw        495
for          421
long         404
true         384
byte         345
interface    337
false        332
protected    293
super        265
break        200
try          149
switch       146
implements   139
catch        127
default      112
instanceof   107
char         96
short        91
abstract     54
double       43
transient    42
finally      34
float        34
enum         25
while        23
continue     12
synchronized 8
volatile     6
do           1
```

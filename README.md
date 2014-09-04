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

2013                2014                   
--------------------------------------
Keyword      Count  Keyword      Count     
public       8127   public       9379      
return       6801   return       8079      
final        6608   final        7561      
import       5938   import       7232      
static       3903   static       5154      
new          3110   new          3915      
extends      2111   extends      2884      
int          1822   int          2132      
throws       1756   if           1985      
void         1707   throws       1898      
if           1661   void         1834      
this         1464   this         1803      
private      1347   private      1605      
class        1239   class        1437      
case         841    case         1225      
else         839    else         940       
package      711    package      842       
boolean      506    boolean      623       
throw        495    throw        553       
for          421    for          469       
long         404    long         456       
true         384    true         439       
byte         345    interface    407       
interface    337    byte         397       
false        332    false        396       
protected    293    break        357       
super        265    protected    328       
break        200    super        328       
try          149    switch       197       
switch       146    try          193       
implements   139    catch        167       
catch        127    implements   162       
default      112    default      156       
instanceof   107    instanceof   156       
char         96     char         122       
short        91     short        93        
abstract     54     finally      54        
double       43     abstract     50        
transient    42     transient    45        
finally      34     double       44        
float        34     float        35        
enum         25     while        35        
while        23     enum         31        
continue     12     continue     13        
synchronized 8      synchronized 10        
volatile     6      do           6         
do           1      volatile     5         
```

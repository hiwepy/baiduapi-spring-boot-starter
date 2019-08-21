#Berkeley DB Java Edition使用说明

#一、简介
Berkeley DB Java Edition (JE)是一个完全用JAVA写的，它适合于管理海量的，简单的数据。
1、能够高效率的处理1到1百万条记录，制约JE数据库的往往是硬件系统,而不是JE本身。
2、多线程支持，JE使用超时的方式来处理线程间的死琐问题。
3、Database都采用简单的key/value对应的形式。
4、事务支持。
5、 允许创建二级库。这样我们就可以方便的使用一级key,二级key来访问我们的数据。
6、支持RAM缓冲，这样就能减少频繁的IO操作。
7、支持日志。
8、数据备份和恢复。
9、游标支持。

#二、获取JE
JE下载地址：
http://www.oracle.com/technology/software/products/berkeley-db/je/index.html
解开包后 把JE_HOME/lib/je-<version>.jar 中的jar文件添加到你的环境变量中就可以使用je了。
相关帮助文档可以参考 JE_HOME/docs/index.html
源代码见JE_HOME/src/*.*

#三、JE常见的异常

DatabaseNotFoundException 当没有找到指定的数据库的时候会返回这个异常
DeadlockException 线程间死锁异常
RunRecoveryException 回收异常，当发生此异常的时候，你必须得重新打开环境变量。

#四、关于日志文件必须了解的六项

JE的日志文件跟其他的数据库的日志文件不太一样，跟C版的DBD也是有区别的
1、JE的日志文件只能APPEND，第一个日志文件名是 00000000.jdb，当他增长到一定大小的时候(默认是10M)，开始写第二个日志文件00000001.jdb，已此类推。
2、跟C版本有所不同，JE的数据日志和事务日志是放在一起的，而不是分开放的。
3、E cleaner负责清扫没用到的磁盘空间，删除后，或者更新后新的记录会追加进来，而原有的记录空间就不在使用了，cleaner负责清理不用的空间。
4、清理并不是立即进行的，当你关闭你的数据库环境后，通过调用一个cleaner方法来清理。
5、清理也不是只动执行的，需要你自己手动调用cleaner 方法来定时清理的。
6、 日志文件的删除仅发生在检查点之后。cleaner准备出哪些log 文件需要被删除，当检查点过后，删掉一些不在被使用的文件。每写20M的日志文件就执行一次检查点，默认下。

#五、创建数据库环境

JE要求在任何DATABASE操作前，要先打开数据库环境，就像我们要使用数据库的话必须得先建立连接一样。你可以通过数据库环境来创建和打开database，或者更改database名称和删除database.
可以通过Environments对象来打开环境，打开环境的时候设置的目录必须是已经存在的目录，否则会出错误。默认情况下，如果指定的database不存在则不会自动创建一个新的detabase,但可以通过设置setAllowCreate来改变这一情况。
...
更多参见：http://blog.csdn.net/muyannian/article/details/1723971


#Maven依赖
=======================================================

<!-- https://mvnrepository.com/artifact/com.sleepycat/je -->
<dependency>
    <groupId>com.sleepycat</groupId>
    <artifactId>je</artifactId>
    <version>5.0.84</version>
</dependency>


https://blog.csdn.net/woaigaolaoshi/article/details/51181165
   
    

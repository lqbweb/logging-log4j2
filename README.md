This is a quick adaptation of log4j 2.6 to run on JDK6.

This is unmaintained, and I do not recommend to use it (although, so far, it works for me).

Many JUnits have been removed due to the amount of work of adapting them to run on java 6.

Also, I have only tested it with slf4j and some abstract actions and conditions (like IfFileName, and IfLastModified), and a rolling appender.

I have replace the use of Java NIO2, with apache commons io 2.5. Also, I have downgraded Kafka, to a lower version that can also run on Java6.

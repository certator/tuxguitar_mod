cd "."

SET "JAVA=java"

SET "JAVA_LIBRARY_PATH=%JAVA_LIBRARY_PATH%;lib\"

SET "JAVA_CLASSPATH=%JAVA_CLASSPATH%;lib\tuxguitar.jar"
SET "JAVA_CLASSPATH=%JAVA_CLASSPATH%;lib\tuxguitar-lib.jar"
SET "JAVA_CLASSPATH=%JAVA_CLASSPATH%;lib\tuxguitar-gm-utils.jar"
SET "JAVA_CLASSPATH=%JAVA_CLASSPATH%;lib\swt.jar"
SET "JAVA_CLASSPATH=%JAVA_CLASSPATH%;lib\itext-pdf.jar"
SET "JAVA_CLASSPATH=%JAVA_CLASSPATH%;lib\itext-xmlworker.jar"
SET "JAVA_CLASSPATH=%JAVA_CLASSPATH%;share\"
SET "JAVA_CLASSPATH=%JAVA_CLASSPATH%;dist\"

SET "TG_MAIN_CLASS=org.herac.tuxguitar.app.TGMain"

%JAVA% -agentlib:jdwp=transport=dt_socket,address=localhost:8000 -cp %JAVA_CLASSPATH% -Djava.library.path=%JAVA_LIBRARY_PATH% %TG_MAIN_CLASS% %1 %2 %3 %4 %5 %6 %7 %8 %9 %10

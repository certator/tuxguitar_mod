TuxGuitar 1.3-SNAPSHOT
======================

[TuxGuitar](http://tuxguitar.herac.com.ar/) is a multitrack tablature editor and player written in Java [SWT](http://www.eclipse.org/swt/).

This branch is a full rework of the build process with [Maven](http://http://maven.apache.org/), which is not especially a good idea for JNI modules :-D Anyway, help to improve it is welcome.

Right now, only tested with Ubuntu.

Build Java
----------

This command build all pure Java code and plugins

    $ mvn clean package

Build JNI plugins
-----------------

Another profile allow to build all JNI plugins available for Linux.
Before that, it is needed to install all expected headers and lib.

    $ sudo apt-get install libfluidsynth-dev oss4-dev libjack-dev libasound2-dev

Then...

    $ mvn clean package -Plinux-jni

If you dont have all this dependencies, you can use the next command.

    $ mvn clean package -Plinux-jni --fail-never

Execute
-------

To lautch the application you can use this command.

    $ cd TuxGuitar
    $ mvn exec:exec

License
-------
TuxGuitar is released under the [GNU Lesser General Public License 2.1](http://www.gnu.org/licenses/lgpl-2.1.html) or any later version.

Requirements
------------
Java Runtime Environment 1.6.X or later installed in your system.

Todo
----

* Portability
    * Fix plugins loading in a portable way (at the moment it use links inside ./TuxGuitar/share/plugins, but maybe we also work around classpath)
    * Portable compilation of JNI module
        * Use third party jar module instead of JNI when it is possible
    * Improve tuxguitar-swt description
    * Merge Mac OS integration to Maven
* Allow to exec the appli from the main parent pom
* Target to package dist into a ZIP
* Check if it is possible to compile with Linux JNI plugins for other platform
* Use pom name/description/version to provide java plugin name/description/version?


cd %~dp0
cd javaprolog
DEL dir *.class

javac -cp gnuprologjava-0.2.6.jar;json-simple-1.1.1.jar;. Shrdlite.java

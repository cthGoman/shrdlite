
cd %~dp0javaprolog
DEL evaluation.txt
echo on
SET /a i=0

:loop
IF %i%==100 GOTO END
java -cp gnuprologjava-0.2.6.jar;json-simple-1.1.1.jar;. Shrdlite < ../examples/complex.json >> evaluation.txt

SET /a i=%i%+1
GOTO LOOP

:end

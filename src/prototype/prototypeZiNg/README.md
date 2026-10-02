## Steps To Create Prototype
1. Compile the code
2. run command `mkdir build`
3. run command `mkdir dist`
4. run build class command `javac -d build src/prototype/prototypeZiNg/*.java`
5. run build jar command `jar cfe dist/prototypeZiNg.jar prototypeZiNg.MainZiWei -C build .`
6. run your jar `java -jar dist/prototypeZiNg.jar` on Intellij terminal 
7. Or inside the Intellij, execute with click (`prototypeZiNg.jar`) 

Please note that my prototype is build on Java SDK 21.0.10



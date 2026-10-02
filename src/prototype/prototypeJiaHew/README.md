## Steps To Create Prototype

1. Compile the code
2. Then run command `mkdir build` to store the compiled file when we build
3. Then run command `mkdir dist` to store the JAR file
4. Then run `javac -d build src/prototype/prototypeJiaHew/*.java `
5. Then run `jar cfe dist/prototypeJiaHew.jar prototype.prototypeJiaHew.MainJiaJing -C build .`
6. Then run `java -jar dist/prototypeJiaHew.jar`

Extra notes:
Please note that my prototype is build on Java SDK 21.0.2 and environment was setup on IntelliJ IDEA
So, please use Java SDK 21 to run my compiled file.

Another option is that you can click on (`prototypeJiaHew.jar`), which will immediately run my compiled JAR file.

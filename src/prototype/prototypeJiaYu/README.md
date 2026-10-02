## Steps To Create Prototype
1. Compile the code
2. run command `mkdir build` to store compiled file when you build
3. run command `mkdir dist` to store jar file when you compiled into a jar file
4. Above is to create two directory to store our build class and jar
5. run build class command `javac -d <output_folder> <source_files>`
   - `<output_folder>`: the folder to store your compiled/build class
   - `<source_files>`: the java files that you want to build. Generally to be `<path>/*.java` to build all Java files in that folder
6. run build jar command `jar cfe <jar_output_path> <main_class_path> -C <output_folder> .`
    - `<jar_output_path>`: the folder to store your jar executable
    - `<main_class_path>`: the main class path of your compiled main class in your `<output_folder>` in the build command
    - `<output_folder>`: the `<output_folder>` used in your build command
    - `.`: refers to include all the compiled class to include in the jar file
7. jar refers to executable of Java
8. run your jar `java -jar <jar_path>.jar`

Example: to recreate and run the jar file for classes contained in prototypeJiaYu
1. run build class command `javac -d build src/prototype/prototypeJiaYu/*.java`
2. the build class will now contain in `build/prototype/prototypeJiaYu`
3. run build jar command `jar cfe dist/prototypeJiaYu.jar prototype.prototypeJiaYu.MainJiaMing -C build .`
4. a new jar will be created in `dist` directory
5. run the jar using command `java -jar dist/prototypeJiaYu.jar` or click on (`prototypeJiaYu.jar`)
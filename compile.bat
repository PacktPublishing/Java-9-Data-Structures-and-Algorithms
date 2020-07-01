javac -d mods --module-source-path src src\com.example.build\module-info.java src\com.example.build\com\example\build\Builder.java 
java --module-path mods -m com.example.build/com.example.build.Builder

# Grafter IntelliJ Plugin

The goal of this plugin is to provide syntax support for Grafter macros in IntelliJ

## Testing

The `src/test` directory contains a file that will not compile without a functioning version of the plugin installed.

## Building

Build the plugin with sbt:

```
sbt package-plugin
```

This will create a plugin zip file in the `target` directory.
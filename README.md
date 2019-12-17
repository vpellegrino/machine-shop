# Machine Shop
Frameworkless layered application, written in Java 8, for managing a machine shop.

## Project structure
It's quite simple:
- **machine-shop**: parent module where the build order is declared.
- **machine-shop-domain**: domain entities and their relationship.
- **machine-shop-persistence**: persistence layer, used to store application status.
- **machine-shop-cli**: command-line-interface to interact with the domain.

## How to build
In order to build the project, [Maven](https://maven.apache.org/docs/3.3.9/release-notes.html) is required.

From root folder:
```
mvn clean package
```
Build will be launched, and for each module, before executing the packaging, all tests will be executed.

In the `/machine-shop-cli/target` folder, a fat JAR (`machine-shop-cli.jar`) is available for the execution.

## Considerations
- Business logic is mostly present in the *domain* module. The user can interact with the application by using the **machine-shop-cli**'s terminal.
This gives you the possibility to change the *UI* layer from the current CLI to a REST Controller Interface, or a GUI (e.g. realized in Swing).
- The persistence is confined to a dedicated module and a generic interface exposes all methods that can be used.
Currently, the state is maintained in memory, but the implementation can be easily switched to anything else (serialized file, DB, etc.).
- In order to realize an interactive shell, the library [Text IO](https://github.com/beryx/text-io) is used. 
Several design patterns have been leveraged for making the code as clean and simple, as possible.

## Assumptions
- When printing all repair orders to be processed, in the daily program, repair order that are pending from previous days are still present.
- Multiple planned repairs with the same name are possible.
- There are constraints on input coming from the user. For example, license plate must be from 5 to 7 chars (this may not be true for certain countries).

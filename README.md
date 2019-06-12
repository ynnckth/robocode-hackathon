# Robocode Challenge

![Teser](doc/robocode.png)

The goal of this repository is to have a very fast setup for a RoboCode hacking challenge in Intellij.

## Steps required to start

1. Check out the repository
2. Make sure, that the Project SDK is configured correctly in Intellij under `File > Project Structure` (tested with Java 8)
3. Start "Robocode" from the run configurations
4. In the Robocode window go to `Options > Preferences > Development Options`
   and set the correct paths to the directories as in the picture below. Usually you need to remove all entries and re-add them:
    ![DevelopmentOptions](doc/DevelopmentOptionsConfig.png)
    ```
    ./dist/robots
    ./out/production/RoboCodeChallenge
    ```
5. Restart Robocode from IntelliJ
6. You are now ready!

## Further links and documentation}
- Official side with further Reading [https://robocode.sourceforge.io](https://robocode.sourceforge.io)
- Documentation [https://robocode.sourceforge.io/docs/robocode/](https://robocode.sourceforge.io/docs/robocode/)
# merge-cpd-pmd-report-maven-plugin

A maven plugin, it merges cpd.xml(duplicate code report) into pmd.xml(pmd report), so that sonarqube can display cpd report.

Sample usage:

```xml
    <reporting>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-pmd-plugin</artifactId>
                <version>3.13.0</version>
                <configuration>
                    <rulesets>
                        <ruleset>${project.basedir}/src/main/resources/pmd.xml</ruleset>
                    </rulesets>
                    <minimumTokens>30</minimumTokens>
                    <ignoreLiterals>true</ignoreLiterals>
                </configuration>
            </plugin>
            <plugin>
                <groupId>com.github.meixuesong</groupId>
                <artifactId>merge-cpd-pmd-report-maven-plugin</artifactId>
                <version>1.0</version>
            </plugin>
        </plugins>
    </reporting>
```    

Sonar:

```
mvn clean verify site com.github.meixuesong:merge-cpd-pmd-report-maven-plugin:1.0:merge sonar:sonar
```

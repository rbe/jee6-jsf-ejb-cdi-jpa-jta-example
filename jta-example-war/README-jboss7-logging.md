# Logging

Mapped Diagnostic Context (MDC) 

## JBoss

Change format pattern by including properties put into MDC map using %X{key}: "[%X{user} %X{task}]".

    <profile>
        <subsystem xmlns="urn:jboss:domain:logging:1.1">
            <console-handler name="CONSOLE">
                <level name="INFO"/>
                <formatter>
                    <pattern-formatter pattern="%d{HH:mm:ss,SSS} %-5p [%c] (%t) [%X{user} %X{task}] - %s%E%n"/>
                </formatter>
            </console-handler>
            [...]
        </subsystem>
    </profile>

Add own category:

    /subsystem=logging/file-handler=example:add(level=TRACE, file={"relative-to"=>"jboss.server.log.dir", "path"=>"example.log"}, append=false, autoflush=true)
    /subsystem=logging/logger=eu.artofcoding.example:add(use-parent-handlers=false,handlers=["example"],level="TRACE")

    <profile>
        <subsystem xmlns="urn:jboss:domain:logging:1.1">
            <file-handler name="example" autoflush="true">
                <level name="TRACE"/>
                <formatter>
                    <pattern-formatter pattern="%d{HH:mm:ss,SSS} %-5p [%c] (%t) [%X{user} %X{task}] - %s%E%n"/>
                </formatter>
                <file relative-to="jboss.server.log.dir" path="example.log"/>
                <append value="false"/>
            </file-handler>
            <logger category="eu.artofcoding.example" use-parent-handlers="false">
                <level name="TRACE"/>
                <handlers>
                    <handler name="example"/>
                </handlers>
            </logger>
        </subsystem>
    </profile>

Change logging level at runtime through CLI:

    /subsystem=logging/console-handler=CONSOLE:write-attribute(name=level,value=TRACE)
    /subsystem=logging/periodic-rotating-file-handler=FILE:write-attribute(name=level,value=TRACE)
    /subsystem=logging/logger=eu.artofcoding.example:write-attribute(name=level,value=TRACE)

Example:

    jboss-as-7.1.1.Final $ bin/jboss-cli.sh 
    You are disconnected at the moment. Type 'connect' to connect to the server or 'help' for the list of supported commands.
    [disconnected /] connect
    [standalone@localhost:9999 /] /subsystem=logging/console-handler=CONSOLE:write-attribute(name=level,value=DEBUG)
    {"outcome" => "success"}

### Resources

* https://docs.jboss.org/author/display/AS71/How+To

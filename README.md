# UoY-DSK-Tool
The University of York Data Source Key building block which allows system administrators to alter user, course and enrolment DSK, row status and availability via the GUI.

## Version 2.5.3
Fixed [Version 2.5.2 throwing error in OpenJDK Instance](https://github.com/OSCELOT/UoY-DSK-Tool/issues/2) issue reported by [hcrites54](https://github.com/hcrites54).

Add the javax.annotation-api lib to fix the **javax.annotation.Resource** issue with OpenJDK 11
```
compile group: 'javax.annotation', name: 'javax.annotation-api', version: '1.3.2'
```

## Version 2.5.2
Fixed [Organization Status Update Throws Error, no Logging](https://github.com/OSCELOT/UoY-DSK-Tool/issues/1) issue reported by [hcrites54](https://github.com/hcrites54).


## Version 2.5.1
This release has resolved the following issue on SaaS (3600.x). Only tested on a 3600.x DVM running with OpenJDK 11. The xml-apis-1.0.b2.jar has been excluded in this version.

```
java.lang.SecurityException: Sealing violation loading javax.xml.parsers.FactoryConfigurationError : 
Package javax.xml.parsers is sealed.
```

## Version 2.5.0
This release should hopefully have fixed the enrolment issue on SaaS. Only tested on the Bb learn 9.1 Q2 2018 (3400.0.0) DVM.

Added database tables to store change logs.

```
SELECT
    *
FROM
    york_dsk_change_log l
    JOIN york_dsk_change_message m ON l.york_dsk_change_message_pk1 = m.pk1
WHERE
    trunc(m.dtcreated) = trunc(current_date)
ORDER BY m.dtcreated;
```

## Version 2.4.0
This release has fixed the paging issue. The source has been recompiled with Spring v5.0.6.RELEASE and JDK 1.8.

Added columns sorting feature.

An update comment box is added.

![Alt text](york-dsk-2.4.0-change-comment.png?raw=true "screenshot")

Result receipt is updated to show change logs.

![Alt text](york-dsk-2.4.0-result-log.png?raw=true "screenshot")

Change logs are written to Tomcat stdout-stderr log.

![Alt text](york-dsk-2.4.0-stdout-stderr.png?raw=true "screenshot")

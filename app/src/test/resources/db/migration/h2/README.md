h2 flyway script
================

Custom flyway script for kompatibilitet med H2 database 2.x+

Mode=Oracle; tilpasninger:

* `VARCHAR2(24 CHAR)` må være `VARCHAR2(24)` eller `VARCHAR2(24 CHARACTERS)`
* Ingen inline constraints under oppretting av tabell

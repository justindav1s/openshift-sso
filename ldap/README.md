# LDAP Integration

https://access.redhat.com/documentation/en-us/red_hat_enterprise_linux/7/html/system-level_authentication_guide/openldap

https://www.certdepot.net/rhel7-configure-ldap-directory-service-user-connection/

https://www.itzgeek.com/how-tos/linux/centos-how-tos/step-step-openldap-server-configuration-centos-7-rhel-7.html

### Install RPMs

yum -y install openldap compat-openldap openldap-clients openldap-servers openldap-servers-sql openldap-devel

### Create password hash 

slappasswd -h {SSHA} -s ```<password>```

### Setup basic config

ldapmodify -Y EXTERNAL  -H ldapi:/// -f db.ldif

### Restrict monitor access

ldapmodify -Y EXTERNAL  -H ldapi:/// -f monitor.ldif

### Setup schemas

Copy the sample database configuration file to /var/lib/ldap and update the file permissions.

cp /usr/share/openldap-servers/DB_CONFIG.example /var/lib/ldap/DB_CONFIG

chown ldap:ldap /var/lib/ldap/*

Add the cosine and nis LDAP schemas.

ldapadd -Y EXTERNAL -H ldapi:/// -f /etc/openldap/schema/cosine.ldif

ldapadd -Y EXTERNAL -H ldapi:/// -f /etc/openldap/schema/nis.ldif 

ldapadd -Y EXTERNAL -H ldapi:/// -f /etc/openldap/schema/inetorgperson.ldif

### Setup groups and users

ldapadd -x -W -D "cn=ldapadm,dc=datr,dc=eu" -f base.ldif

### Delete entries up the tree

ldapdelete -w ```<password>``` -D "cn=ldapadm,dc=datr,dc=eu" "uid=justindav1s,ou=people,dc=datr,dc=eu"

ldapdelete -w ```<password>``` -D "cn=ldapadm,dc=datr,dc=eu" "cn=Marc Chambers,ou=people,dc=datr,dc=eu"

ldapdelete -w ```<password>``` -D "cn=ldapadm,dc=datr,dc=eu" "cn=June Rossi,ou=people,dc=datr,dc=eu"

ldapdelete -w ```<password>``` -D "cn=ldapadm,dc=datr,dc=eu" "uid=superman,ou=people,dc=datr,dc=eu"

ldapdelete -w ```<password>``` -D "cn=ldapadm,dc=datr,dc=eu" "uid=justindav1s,ou=people,dc=datr,dc=eu"

ldapdelete -w ```<password>``` -D "cn=ldapadm,dc=datr,dc=eu" "cn=Robert Wong,ou=people,dc=datr,dc=eu"

ldapdelete -w ```<password>``` -D "cn=ldapadm,dc=datr,dc=eu" "ou=people, dc=datr,dc=eu"

ldapdelete -w ```<password>``` -D "cn=ldapadm,dc=datr,dc=eu" "ou=groups,dc=datr,dc=eu"

ldapdelete -w ```<password>``` -D "cn=ldapadm,dc=datr,dc=eu" "dc=datr,dc=eu"

### Search Directory

ldapsearch  -w ```<password>``` -D "cn=ldapadm,dc=datr,dc=eu" -b "dc=datr,dc=eu" "cn=*"

ldapsearch  -w ```<password>``` -D "cn=ldapadm,dc=datr,dc=eu" -b "dc=datr,dc=eu" "cn=j*"

ldapsearch  -w ```<password>``` -D "cn=ldapadm,dc=datr,dc=eu" -b "dc=datr,dc=eu" "roomNumber=220"



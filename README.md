# Generic Implementation of a Spring Authorization Server

# User
hpy@user.com
# Password
p@ssworD1234 = {bcrypt}$2a$10$8w22Ivp3S1g.RBdArAP09.hdrC3BVnC0Adez93gLQiFAnSTsV8Y6G

# Keytool Command to Generate a JKS Keystore
`cd resources/jks`
```
keytool -genkeypair \
        -alias jwtkey \
        -keyalg RSA \
        -keysize 2048 \
        -validity 3650 \
        -storetype JKS \
        -keystore keystore.jks \
        -storepass password \
        -keypass password \
        -dname "CN=auth, OU=Dev, O=SpringShop, L=Tallinn, C=EE"
```
Path will be: `resources/jks/keystore.jks`
Classpath will be: `classpath:jks/keystore.jks`
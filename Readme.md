# Quarkus Learning Projekt


https://quarkus.io/quarkus-workshops/super-heroes/mac/spine.html



# Quarkus native build without Graal VM


Without automatic image creation:
```bash
quarkus build --native --no-tests -Dquarkus.native.remote-container-build=true
```

With automatic image creation:
```bash
quarkus build --native --no-tests -Dquarkus.native.remote-container-build=true -Dquarkus.container-image.build=true
```
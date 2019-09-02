# Optimizacija izvajanja mikrostoritev z uporabo GraalVM
Zahteve:
- Ustrezno vzpostavljeno GraalVM okolje
- Inštalirano orodje Maven
- Inštaliran docker

Programska koda vsebuje gostujoče programske jezike, zato je potrebno v GraalVM predhodno naložiti R z ukazom:
`gu install R`

Pred prvim zagonom je potrebno namestiti zahtevane javanske knjižnjice in docker vsebnik s podatkovno bazo. V ta namen se požene skripta `init.sh`

Za prevjanje aplikacije in generiranje native imagea je potrebno pognati `build.sh`, za zagon pa `run.sh`.

Podatke, ki so samodejno vnešeni v podatkovno bazo, lahko najdete v datoteki `init-db.sh`, ki se nahaja v `resources` podmapi modula `entities`.

Relevantne končne točke:
- [Izdelki - CORS](https://localhost:8080/v1/products)
- [Nakupi - CORS](https://localhost:8080/v1/purchases)
- [Nagrade - CORS](https://localhost:8080/v1/rewards)
- [Prevzemi - CORS](https://localhost:8080/v1/claims)
- [Nakupi - Graf](https://localhost:8080/v1/purchases/graph)
- [Prevzemi - Graf](https://localhost:8080/v1/claims/graph)

Primer telesa zahtevka za dodajanje prevzema nagrade:
```
{
    "reward": {
        "id": 3,
        "rewardName": "3h F.U.N. Room voucher",
        "price": 20
    },
    "amount": 1
}
```

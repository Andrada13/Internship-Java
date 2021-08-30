# Gestiune produse din magazin

Aplicatia reprezinta un sistem simplist de gestiune al unor produse dintr-un magazin, informatiile fiind preluate dintr-un fisier json primit ca argument din linie de comanda.

## Continut

Aplicatia contine 13 comenzi ce vor fi introduse de la tastatura in consola, iar raspunsurile acestora se vor afisa tot in consola.
Contine 4 clase : "Product" ce reprezinta produsele existente in stoc, "User" ce reprezinta clientii, "Printer" ce este responsabila de modul de afisare a comenzilor (consola sau fisier) si "Command" ce contine implementarea comezilor.

Initial, aplicatia a fost implementata folosind obiecte de tip JSONObject, dar ulterior a fost optimizata datorita definirii celor doua clase, "Product" si "User" pentru a facilita accesarea directa a campurilor existente in fisierul JSON.


## Comenzi


* Functia "createCategoriesList()" retine lista categoriilor, aceasta fiind apelata in functia main, pentru a se initializa mereu cand se reluaza aplicatia.

```java
command.createCategoriesList();
```

* In functia "printCategories()" am folosit tipul HashSet care este mai eficient decat varianta initiala ArrayList.

```java
HashSet<Object> list = new HashSet<>();
		for (Product product : productsArr) {
			list.add(product.getCategory());

		}
```

* In functia "removeProduct()" am folosit un iterator pentru a avea acces la metode precum "remove()".

```java
for (Iterator<Product> iterator = productsArr.iterator(); iterator.hasNext();) {
			Product product = iterator.next();

}
```

* Functia getJSONObject() creeaza si returneaza JSONObject-ul facut din Product, respectiv User, avand rol in crearea unui fisier JSON identic cu cel initial.


```java
public JSONObject getJSONObject() {
		JSONObject newObj = new JSONObject();
		newObj.put("category", this.category);
		newObj.put("name", this.name);
		newObj.put("quantity", this.quantity);
		newObj.put("price", this.price);
		newObj.put("maxQuantity", this.maxQuantity);

		return newObj;
	}

    public JSONObject getJSONObject() {
		JSONObject newObj = new JSONObject();
		newObj.put("username", this.username);
		newObj.put("balance", this.balance);

		return newObj;
	}
```




